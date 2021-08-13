package dk.nobelium.inmobile.boundary;

import dk.nobelium.inmobile.entity.SendMessagesResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("V2")
@RegisterRestClient
public interface InMobileService {

    @POST
    @Path("SendMessages")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    SendMessagesResponse sendMessages(String body);

}
