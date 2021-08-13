package dk.nobelium.inmobile.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

class SendMessagesResponseTest {

    // Tests the deserialization of the recipient object using JAXB annotations.
    @Test
    public void testRecipientDeserialize() throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance((MsisdnAndMessageId.class));

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        String xmlObject = """
                <recipient msisdn="4511223344" id="Message1" />
                """;

        MsisdnAndMessageId msisdnAndMessageId = (MsisdnAndMessageId) unmarshaller.unmarshal(new StringReader(xmlObject));

        Assertions.assertEquals("4511223344", msisdnAndMessageId.getMsisdn());
        Assertions.assertEquals("Message1", msisdnAndMessageId.getMessageId());

    }

    // Tests the deserialization of the complete reply using JAXB annotations.
    @Test
    public void test() throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance((SendMessagesResponse.class));

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        String xmlResponse = """
                <reply>
                	<recipient msisdn="4511223344" id="Message1" />
                	<recipient msisdn="4544332211" id="Message2" />
                	<recipient msisdn="4588888888" id="13cab0f4-0e4f-44cf-8f84-a9eb435f36a4" />
                </reply>
                """;

        SendMessagesResponse sendMessagesResponse = (SendMessagesResponse) unmarshaller.unmarshal(new StringReader(xmlResponse));

        Assertions.assertNotNull(sendMessagesResponse);

        Assertions.assertEquals(3, sendMessagesResponse.getMessageIds().size());

        MsisdnAndMessageId o1 = sendMessagesResponse.getMessageIds().get(0);
        Assertions.assertEquals("4511223344", o1.getMsisdn());
        Assertions.assertEquals("Message1", o1.getMessageId());

        MsisdnAndMessageId o2 = sendMessagesResponse.getMessageIds().get(1);
        Assertions.assertEquals("4544332211", o2.getMsisdn());
        Assertions.assertEquals("Message2", o2.getMessageId());

        MsisdnAndMessageId o3 = sendMessagesResponse.getMessageIds().get(2);
        Assertions.assertEquals("4588888888", o3.getMsisdn());
        Assertions.assertEquals("13cab0f4-0e4f-44cf-8f84-a9eb435f36a4", o3.getMessageId());
    }

}