/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource04_AvailabilitySubscriptionUpdate;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource05_AvailabilitySubscriptionDeletion;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.UnsubscribeContextAvailabilityRequest;

/**
 * Resource which has only one representation.
 */
public class Resource10_AvailabilitySubscription extends ServerResource {

    @Put
    public Representation subscribeToDescription(Representation entity) throws ResourceException, IOException, JAXBException {

        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }        
        String subsId = (String) getRequest().getAttributes().get("subscriptionId"); 
        System.out.println("CO 10: PUT AVAILABILITY SUBSCRIPTION: subscriptionId = '" + subsId + "'");
        
        Resource04_AvailabilitySubscriptionUpdate ras = new Resource04_AvailabilitySubscriptionUpdate();        
        Representation result = ras.subscribeDescription(entity, acceptType);
        return result;
    }

    @Delete
    public Representation removeDescription() throws JAXBException, ResourceException, IOException {
        
        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }
        else acceptType = MediaType.APPLICATION_JSON.getSubType();

        String subsId = (String) getRequest().getAttributes().get("subscriptionID");
        System.out.println("CO 10: DELETE AVAILABILITY SUBSCRIPTION: subscriptionId = '" + subsId + "'");
        
        UnsubscribeContextAvailabilityRequest ucar = new UnsubscribeContextAvailabilityRequest();
        ucar.setSubscriptionId(subsId);        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        InputStream stream = new ByteArrayInputStream(objectMapper.writeValueAsBytes(ucar));

        Resource05_AvailabilitySubscriptionDeletion ras = new Resource05_AvailabilitySubscriptionDeletion();
        StringRepresentation result = ras.unsubscribeJsonHandler(stream, acceptType);
        return result;

    }

}
