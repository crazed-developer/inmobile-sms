# inmobile-sms

This client is pretty much a 1:1 Java port of the official .NET client from inMobile. Found here: https://github.com/inMobile/inMobile-.NET-API-Client

However it does not implement the full client as provided by inMobile. 

The official client has these modules:
 * SendMessages **(Implemented)**
 * GetMessageStatuses
 * CancelMessages
 * StatisticsSummary

Contributions to finalize are accepted. But not needed by my use case as is.

This Java library will get you flying in minutes, and have a plesant API. It also includes a Microprofile Rest Client interface. That works right off the bat in a microprofile context like for example Quarkus, Wildfly and Payara.

The included Rest Client, are as short as shown here
```java
@Path("V2")
@RegisterRestClient
public interface InMobileService {

    @POST
    @Path("SendMessages")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    SendMessagesResponse sendMessages(String body);

}
```

You also need to configure your runtime with endpoint information:

for Quarkus that means adding this to your application.properties file
```
dk.nobelium.inmobile.boundary.InMobileService/mp-rest/url=https://mm.inmobile.dk/Api
dk.nobelium.inmobile.boundary.InMobileService/mp-rest/scope=javax.inject.Singleton
```

And enables you to have your controller method as simple as this:

```java
@Dependent
public class InMobileController {

    // Injecting the rest client declared above, the framework generates the client code.
    @Inject
    @RestClient
    InMobileService smsClient;

    // The api key provided from inMobile are injected here
    @ConfigProperty(name = "dk.inmobile.apikey")
    String apiKey;

    // And the call back url, for recieving confirmations are defined here
    // This functionality are not included in this library, but you can 
    // provide the url and implement the endpoint your self.
    @ConfigProperty(name = "dk.inmobile.callback.url")
    String callbackUrl;

    public SendMessagesResponse sendSmsMessages(List<ISmsMessage> messages) {

        try {
            String xml = SendMessagesProducer.buildOptimizedMessageObjectAsXmlString(
                    messages,
                    apiKey,
                    callbackUrl
            );

            return smsClient.sendMessages(xml);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }
}
```

Inject this controller anywhere you wish to send an sms message, and just call the sendSmsMessages method with a list of messages.

So how to construct those messages, that's simple too.

```java
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
```

How to use then ?

Well you have to clone then build + install and add the following maven coordinates:

basically do this:
```console
>https://github.com/crazed-developer/inmobile-sms.git
>mvn install
```

Import in your project pom.xml file:

```xml
    <dependency>
      <groupId>dk.nobelium</groupId>
      <artifactId>inmobile-sms</artifactId>
      <version>1.0.7-SNAPSHOT</version>
    </dependency>
```

The library ARE NOT available on Maven Central

