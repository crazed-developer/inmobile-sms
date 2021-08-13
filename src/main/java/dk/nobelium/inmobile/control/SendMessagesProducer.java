package dk.nobelium.inmobile.control;

import dk.nobelium.inmobile.entity.ISmsMessage;
import dk.nobelium.inmobile.entity.OverchargeInfo;
import dk.nobelium.inmobile.entity.RefundMessage;
import dk.nobelium.inmobile.entity.SmsMessage;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SendMessagesProducer {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String buildOptimizedMessageObjectAsXmlString(
            List<ISmsMessage> messages,
            String apiKey,
            String messageStatusCallbackUrl
    ) throws ParserConfigurationException, TransformerException {

        Document document = buildOptimizedMessageObjectAsDocument(
                messages,
                apiKey,
                messageStatusCallbackUrl
        );

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);

        StringWriter stringWriter = new StringWriter();

        StreamResult result = new StreamResult(stringWriter);
        transformer.transform(source, result);

        return stringWriter.toString();

    }

    public static Document buildOptimizedMessageObjectAsDocument(
            List<ISmsMessage> messages,
            String apiKey,
            String messageStatusCallbackUrl
    ) throws ParserConfigurationException, TransformerException {


        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        Element rootElement = document.createElement("request");
        document.appendChild(rootElement);

        Attr sourceAttr = document.createAttribute("source");
        sourceAttr.setValue("Nobelium ApS, Client for InMobile SMS Gateway");
        rootElement.setAttributeNode(sourceAttr);

        Element authentication = document.createElement("authentication");
        rootElement.appendChild(authentication);
        Attr apiKeyAttr = document.createAttribute("apikey");
        apiKeyAttr.setValue(apiKey);
        authentication.setAttributeNode(apiKeyAttr);

        Element dataNode = document.createElement("data");
        rootElement.appendChild(dataNode);

        if (messageStatusCallbackUrl != null && !messageStatusCallbackUrl.isBlank()) {
            Element statusCallBackNode = document.createElement("statuscallbackurl");
            statusCallBackNode.setTextContent(messageStatusCallbackUrl);
            dataNode.appendChild(statusCallBackNode);
        }

        // Collect identical messages in a map, by object/message hashcode
        Map<Integer, List<SmsMessage>> groupedSmsMessages = messages.stream()
                .filter(m -> m instanceof SmsMessage)
                .map(s -> (SmsMessage) s)
                .collect(
                        Collectors.groupingBy(SmsMessage::commonHashCode)
                );

        Set<Map.Entry<Integer, List<SmsMessage>>> entries = groupedSmsMessages.entrySet();
        for (Map.Entry<Integer, List<SmsMessage>> entry : entries) {
            Element smsMessageContext = createSmsMessageContext(entry.getValue(), document);

            if (smsMessageContext != null) {
                dataNode.appendChild(smsMessageContext);
            }

        }


        // Collect the refund messages in a plain list
        List<RefundMessage> refundMessages = messages.stream()
                .filter(r -> r instanceof RefundMessage)
                .map(r -> (RefundMessage) r)
                .collect(Collectors.toList());

        for (RefundMessage refundMessage : refundMessages) {

            Element refundMessageElement = document.createElement("refundmessage");

            if (refundMessage.getMessageId() != null && !refundMessage.getMessageId().isBlank()) {
                Attr refundMessageIdAttr = document.createAttribute("messageid");
                refundMessageIdAttr.setValue(refundMessage.getMessageId());
                refundMessageElement.setAttributeNode(refundMessageIdAttr);
            }

            Attr messageIdToRefundAttr = document.createAttribute("messageidtorefund");
            messageIdToRefundAttr.setValue(refundMessage.getMessageIdToRefund());
            refundMessageElement.setAttributeNode(messageIdToRefundAttr);

            Element textElement = document.createElement("text");
            CDATASection refundMessageCdata = document.createCDATASection(refundMessage.getMessageText());
            textElement.appendChild(refundMessageCdata);
            refundMessageElement.appendChild(textElement);


            dataNode.appendChild(refundMessageElement);

        }


        return document;


    }

    private static Element createSmsMessageContext(List<SmsMessage> commonMessages, Document document) {


        Optional<SmsMessage> first = commonMessages.stream().findFirst();

        if (first.isPresent()) {

            // This will be the message content, all other messages in list will only be added to the recipients node.
            SmsMessage templateMessage = first.get();

            Element messageNode = document.createElement("message");

            String senderName = templateMessage.getSenderName();
            String messageText = templateMessage.getText();
            String encodingString = "gsm7";
            boolean isFlash = templateMessage.isFlash();

            if (templateMessage.getEncoding() != null) {
                encodingString = templateMessage.getEncoding().value;
            }

            // Sender name tag
            Element senderNameNode = document.createElement("sendername");
            senderNameNode.setTextContent(senderName);
            messageNode.appendChild(senderNameNode);

            // Text tag
            Element textNode = document.createElement("text");
            CDATASection cdataSection = document.createCDATASection(messageText);
            textNode.appendChild(cdataSection);
            messageNode.appendChild(textNode);

            Attr encodingAttr = document.createAttribute("encoding");
            encodingAttr.setValue(encodingString);
            textNode.setAttributeNode(encodingAttr);

            Attr flashAttr = document.createAttribute("flash");
            flashAttr.setValue(isFlash ? "true" : "false");
            textNode.setAttributeNode(flashAttr);

            // Send date time (For scheduled delivery)
            if (templateMessage.getSendTime() != null) {
                Element sendTimeElement = document.createElement("sendtime");
                sendTimeElement.setTextContent(templateMessage.getSendTime().format(DATE_TIME_FORMATTER));
                messageNode.appendChild(sendTimeElement);
            }

            // Expire in seconds, if set. if absent wont expire.
            if (templateMessage.getExpireIn() != null) {
                Element expirenode = document.createElement("expireinseconds");
                expirenode.setTextContent(
                        Long.toString(
                                templateMessage.getExpireIn().getSeconds()
                        )
                );

                messageNode.appendChild(expirenode);

            }

            if (!templateMessage.isRespectBlacklist()) {
                // Respect blacklist tag
                Element respectBlacklistNode = document.createElement("respectblacklist");
                respectBlacklistNode.setTextContent("false");
                messageNode.appendChild(respectBlacklistNode);
            }


            // Overcharge tag
            if (templateMessage.getOverChargeInfo() != null) {

                OverchargeInfo value = templateMessage.getOverChargeInfo();

                Element overchargeNode = document.createElement("overchargeinfo");

                Attr countryCodeAttr = document.createAttribute("countrycode");
                countryCodeAttr.setValue(value.getCountryCode());
                overchargeNode.setAttributeNode(countryCodeAttr);

                Attr shortCodeAttr = document.createAttribute("shortcode");
                shortCodeAttr.setValue(value.getShortCodeNumber());
                overchargeNode.setAttributeNode(shortCodeAttr);

                Attr priceAttr = document.createAttribute("price");
                priceAttr.setValue(Integer.toString(value.getPrice()));
                overchargeNode.setAttributeNode(priceAttr);

                Attr typeAttr = document.createAttribute("type");
                typeAttr.setValue(
                        Integer.toString(
                                value.getType().value
                        )
                );
                overchargeNode.setAttributeNode(typeAttr);

                Attr invoiceDescriptionAttr = document.createAttribute("invoicedescription");
                invoiceDescriptionAttr.setValue(value.getInvoiceDescription());
                overchargeNode.setAttributeNode(invoiceDescriptionAttr);

                messageNode.appendChild(overchargeNode);

            }

            // Add recipients node
            Element recipientsNode = document.createElement("recipients");
            messageNode.appendChild(recipientsNode);

            for (SmsMessage commonMessage : commonMessages) {

                Element msisdnNode = document.createElement("msisdn");
                msisdnNode.setTextContent(commonMessage.getMsisdn());
                recipientsNode.appendChild(msisdnNode);

                if (commonMessage.getMessageId() != null && !commonMessage.getMessageId().isBlank()) {
                    Attr messageIdAttr = document.createAttribute("id");
                    messageIdAttr.setValue(commonMessage.getMessageId());
                    msisdnNode.setAttributeNode(messageIdAttr);
                }
            }

            return messageNode;
        } else {
            return null;
        }


    }
}
