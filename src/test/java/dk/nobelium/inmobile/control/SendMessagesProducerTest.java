package dk.nobelium.inmobile.control;

import dk.nobelium.inmobile.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.time.LocalDateTime;
import java.util.List;

class SendMessagesProducerTest {

    @Test
    public void build_simple_noMessageId_noUrlCallback_test() throws ParserConfigurationException, TransformerException {

        String generatedXml = SendMessagesProducer.buildOptimizedMessageObjectAsXmlString(
                List.of(
                        new SmsMessage(
                                null,
                                false,
                                "4511111111",
                                "Text msg 1",
                                "SenderA",
                                SmsEncoding.UTF_8,
                                null,
                                null
                                , null,
                                true)
                ),
                "apiKey",
                null
        );

        //language=XML
        String expectedXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <request source="Nobelium ApS, Client for InMobile SMS Gateway">
                    <authentication apikey="apiKey"/>
                    <data>
                        <message>
                            <sendername>SenderA</sendername>
                            <text encoding="utf-8" flash="false"><![CDATA[Text msg 1]]></text>
                            <recipients>
                                <msisdn>4511111111</msisdn>
                            </recipients>
                        </message>
                    </data>
                </request>
                """;

        Assertions.assertEquals(expectedXml, generatedXml);

    }

    @Test
    public void build_simple_withRespectBlacklistingToFalse_test() throws ParserConfigurationException, TransformerException {

        String generatedXml = SendMessagesProducer.buildOptimizedMessageObjectAsXmlString(
                List.of(
                        new SmsMessage(
                                null,
                                false,
                                "4511111111",
                                "Text msg 1",
                                "SenderA",
                                SmsEncoding.UTF_8,
                                null,
                                null
                                , null,
                                false)
                ),
                "apiKey",
                null
        );

        //language=XML
        String expectedXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <request source="Nobelium ApS, Client for InMobile SMS Gateway">
                    <authentication apikey="apiKey"/>
                    <data>
                        <message>
                            <sendername>SenderA</sendername>
                            <text encoding="utf-8" flash="false"><![CDATA[Text msg 1]]></text>
                            <respectblacklist>false</respectblacklist>
                            <recipients>
                                <msisdn>4511111111</msisdn>
                            </recipients>
                        </message>
                    </data>
                </request>
                """;

        Assertions.assertEquals(expectedXml, generatedXml);

    }

    @Test
    public void build_simple_withMessageId_callback_sendTime_Test() throws ParserConfigurationException, TransformerException {

        String generatedXml = SendMessagesProducer.buildOptimizedMessageObjectAsXmlString(
                List.of(
                        new SmsMessage(
                                "MessageId123",
                                false,
                                "4511111111",
                                "Text msg 1",
                                "SenderA",
                                SmsEncoding.UTF_8,
                                null,
                                LocalDateTime.of(2001, 2, 3, 4, 5, 6)
                                , null,
                                true)
                ),
                "apiKey",
                "http://someUrl.callback.inmobile.dk/unittest/messagestatus"
        );

        //language=XML
        String expectedXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <request source="Nobelium ApS, Client for InMobile SMS Gateway">
                    <authentication apikey="apiKey"/>
                    <data>
                        <statuscallbackurl>http://someUrl.callback.inmobile.dk/unittest/messagestatus</statuscallbackurl>
                        <message>
                            <sendername>SenderA</sendername>
                            <text encoding="utf-8" flash="false"><![CDATA[Text msg 1]]></text>
                            <sendtime>2001-02-03 04:05:06</sendtime>
                            <recipients>
                                <msisdn id="MessageId123">4511111111</msisdn>
                            </recipients>
                        </message>
                    </data>
                </request>
                """;

        Assertions.assertEquals(expectedXml, generatedXml);
    }

    @Test
    public void Build_Overcharged_Test() throws ParserConfigurationException, TransformerException {

        String generatedXml = SendMessagesProducer.buildOptimizedMessageObjectAsXmlString(
                List.of(
                        new SmsMessage(
                                "MessageId123",
                                false,
                                "4511111111",
                                "Text msg 1",
                                "SenderA",
                                SmsEncoding.UTF_8,
                                new OverchargeInfo(2500, "45", "1245", OverChargeType.MOBILE_PAYMENT, "Some description"),
                                null
                                , null,
                                true)
                ),
                "apiKey",
                "http://someUrl.callback.inmobile.dk/unittest/messagestatus"
        );

        //language=XML
        String expectedXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <request source="Nobelium ApS, Client for InMobile SMS Gateway">
                    <authentication apikey="apiKey"/>
                    <data>
                        <statuscallbackurl>http://someUrl.callback.inmobile.dk/unittest/messagestatus</statuscallbackurl>
                        <message>
                            <sendername>SenderA</sendername>
                            <text encoding="utf-8" flash="false"><![CDATA[Text msg 1]]></text>
                            <overchargeinfo countrycode="45" invoicedescription="Some description" price="2500" shortcode="1245" type="3"/>
                            <recipients>
                                <msisdn id="MessageId123">4511111111</msisdn>
                            </recipients>
                        </message>
                    </data>
                </request>
                """;

        Assertions.assertEquals(expectedXml, generatedXml);

    }

    @Test
    public void build_refundMessages_test() throws ParserConfigurationException, TransformerException {

        RefundMessage refund1 = new RefundMessage("RefundId1", "Refund & < > text 1", "MessageId123");
        RefundMessage refund2 = new RefundMessage("RefundId2", "Refund text 2", null);

        String generatedXml = SendMessagesProducer.buildOptimizedMessageObjectAsXmlString(
                List.of(refund1, refund2),
                "apiKey",
                "http://someUrl.callback.inmobile.dk/unittest/messagestatus"
        );

        //language=XML
        String expectedXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <request source="Nobelium ApS, Client for InMobile SMS Gateway">
                    <authentication apikey="apiKey"/>
                    <data>
                        <statuscallbackurl>http://someUrl.callback.inmobile.dk/unittest/messagestatus</statuscallbackurl>
                        <refundmessage messageid="MessageId123" messageidtorefund="RefundId1">
                            <text><![CDATA[Refund & < > text 1]]></text>
                        </refundmessage>
                        <refundmessage messageidtorefund="RefundId2">
                            <text><![CDATA[Refund text 2]]></text>
                        </refundmessage>
                    </data>
                </request>
                """;

        Assertions.assertEquals(expectedXml, generatedXml);

    }

    @Test
    public void Build_MultipleMessages_Test() throws ParserConfigurationException, TransformerException {
        String generatedXml = SendMessagesProducer.buildOptimizedMessageObjectAsXmlString(
                List.of(
                        new SmsMessage(
                                "MessageId123",
                                false,
                                "4511111111",
                                "Text msg 1",
                                "SenderA",
                                SmsEncoding.UTF_8,
                                null,
                                LocalDateTime.of(2001, 2, 3, 4, 5, 6),
                                null,
                                true),
                        new SmsMessage(
                                null,
                                true,
                                "4522222222",
                                "Text msg 2",
                                "SenderB",
                                SmsEncoding.GSM_7,
                                null,
                                null,
                                null,
                                true

                        )
                ),
                "apiKey",
                "http://someUrl.callback.inmobile.dk/unittest/messagestatus"
        );

        //language=XML
        String expectedXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <request source="Nobelium ApS, Client for InMobile SMS Gateway">
                    <authentication apikey="apiKey"/>
                    <data>
                        <statuscallbackurl>http://someUrl.callback.inmobile.dk/unittest/messagestatus</statuscallbackurl>
                        <message>
                            <sendername>SenderA</sendername>
                            <text encoding="utf-8" flash="false"><![CDATA[Text msg 1]]></text>
                            <sendtime>2001-02-03 04:05:06</sendtime>
                            <recipients>
                                <msisdn id="MessageId123">4511111111</msisdn>
                            </recipients>
                        </message>
                        <message>
                            <sendername>SenderB</sendername>
                            <text encoding="gsm7" flash="true"><![CDATA[Text msg 2]]></text>
                            <recipients>
                                <msisdn>4522222222</msisdn>
                            </recipients>
                        </message>
                    </data>
                </request>
                """;

        Assertions.assertEquals(expectedXml, generatedXml);
    }
}