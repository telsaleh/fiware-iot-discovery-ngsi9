/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard;

import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.UnsubscribeContextAvailabilityResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.UnsubscribeContextAvailabilityRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.UnsubscribeMarshaller;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.StorageStartup;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.SubscriptionStoreAccess;

public class Resource05_AvailabilitySubscriptionDeletion extends ServerResource {

    @Post
    public Representation unsubscribeToDescription(Representation entity) throws ResourceException, IOException, JAXBException {

        InputStream description = new ByteArrayInputStream(entity.getText().getBytes());
//        ServletContext context = (ServletContext) getContext().getServerDispatcher().getContext().getAttributes().get("org.restlet.ext.servlet.ServletContext");
        ServletContext context = StorageStartup.servletContext;
        UnsubscribeMarshaller subMar = new UnsubscribeMarshaller();
        UnsubscribeContextAvailabilityRequest req = new UnsubscribeContextAvailabilityRequest();
        String respMsg = "";

        //unmarshall XML request
        try {
            req = subMar.unmarshallRequest(description);
            System.out.println("RECEIEVED REQUEST: \n" + subMar.marshallRequest(req));
            respMsg = unsubscribeToContext(context, req.getSubscriptionId());
        } catch (JAXBException | java.lang.ClassCastException je) {
            //Error with XML structure
            System.out.println(je.getMessage());
            UnsubscribeContextAvailabilityResponse unSubResp = new UnsubscribeContextAvailabilityResponse();
            StatusCode sc = new StatusCode(400, "Bad Request", "Error in XML structure");
            unSubResp.setStatusCode(sc);
            respMsg = subMar.marshallResponse(unSubResp);

        }
        //pass subscription ID for subscription deletion
        System.out.println("RESPONSE TO SEND: \n" + respMsg);
        StringRepresentation result = new StringRepresentation(respMsg);
        result.setMediaType(MediaType.APPLICATION_XML);
        return result;
    }

    public String unsubscribeToContext(ServletContext context, String subId) throws ResourceException, IOException, JAXBException {

        UnsubscribeMarshaller subMar = new UnsubscribeMarshaller();
        UnsubscribeContextAvailabilityRequest req = new UnsubscribeContextAvailabilityRequest();
        UnsubscribeContextAvailabilityResponse unSubResp = new UnsubscribeContextAvailabilityResponse();
        StatusCode sc = new StatusCode(200, "OK", "Deleted");

        boolean deleted = false;
        try {

            if (subId.startsWith("UniS_")) {
                //SubscriptionStoreAccess ss = new SubscriptionStoreAccess(context);
                //ss.openDb4o();
                deleted = SubscriptionStoreAccess.deleteSubscription(subId);
//                ss.closeDb4o();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sc = new StatusCode(500, "Internal Error", "Result");
            req.setSubscriptionId("");
        }

        if (!deleted) {
            sc = new StatusCode(404, "Subscription Not Found", "Result");
        }

        unSubResp.setStatusCode(sc);
        unSubResp.setSubscriptionId(subId);
        String regRespMsg = "";
        regRespMsg = subMar.marshallResponse(unSubResp);
        return regRespMsg;
    }
}
