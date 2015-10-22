/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
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

/**
 * Resource which has only one representation.
 */
public class Resource10_AvailabilitySubscription extends ServerResource {

    @Put
    public Representation subscribeToDescription(Representation entity) throws ResourceException, IOException, JAXBException {

//        InputStream description = new ByteArrayInputStream(entity.getText().getBytes());
        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }
        
        Resource04_AvailabilitySubscriptionUpdate ras = new Resource04_AvailabilitySubscriptionUpdate();
        
        String subsId = (String) getRequest().getAttributes().get("subscriptionId"); 
        
//        ServletContext context = (ServletContext) getContext().getServerDispatcher().getContext().getAttributes().get("org.restlet.ext.servlet.ServletContext");
        
        System.out.println("CO 10: PUT AVAILABILITY SUBSCRIPTION: subscriptionId = '" + subsId + "'");
        
        Representation result = ras.subscribeDescription(entity, acceptType);
        return result;
    }

    @Delete
    public Representation removeDescription() throws JAXBException, ResourceException, IOException {

        String subsId = (String) getRequest().getAttributes().get("subscriptionID");
        Resource05_AvailabilitySubscriptionDeletion ras = new Resource05_AvailabilitySubscriptionDeletion();
        
        System.out.println("CO 10: DELETE AVAILABILITY SUBSCRIPTION: subscriptionId = '" + subsId + "'");

        ServletContext context = (ServletContext) getContext().getServerDispatcher().getContext().getAttributes().get("org.restlet.ext.servlet.ServletContext");

        String respMsg = ras.unsubscribeToContext(context, subsId);

        StringRepresentation result = new StringRepresentation(respMsg);
        result.setMediaType(MediaType.APPLICATION_XML);
        return result;

    }

}
