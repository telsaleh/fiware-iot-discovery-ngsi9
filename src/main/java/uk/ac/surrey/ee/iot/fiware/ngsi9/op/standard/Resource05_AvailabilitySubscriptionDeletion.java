/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.UnsubscribeContextAvailabilityResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.UnsubscribeContextAvailabilityRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.RandomStringUtils;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.UnsubscribeMarshaller;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.UpdateContextAvailabilitySubscriptionRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.UpdateContextAvailabilitySubscriptionResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.StorageStartup;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.SubscriptionStoreAccess;

public class Resource05_AvailabilitySubscriptionDeletion extends ServerResource {
    
    @Post //("json:xml")
    public Representation handlePost(Representation entity) throws ResourceException, IOException, JAXBException {

        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }
        Representation rt = unsubscribeToDescription(entity, acceptType);
        return rt;
    }
    
    public Representation unsubscribeToDescription(Representation entity, String acceptType) throws ResourceException, IOException, JAXBException {

        //read NGSI description
        InputStream description = new ByteArrayInputStream(entity.getText().getBytes());
        String contentType = entity.getMediaType().getSubType();
//        System.out.println("Content-Type is: " + contentType);
//        System.out.println("Accept: " + acceptType);

        //update subscription
        StringRepresentation updSubRespMsg = null;
        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON.getSubType())) {
            //if request payload is JSON
            updSubRespMsg = unsubscribeJsonHandler(description, acceptType);
        } else {
            //request payload is XML
            updSubRespMsg = unsubscribeXmlHandler(description, acceptType);
        }

//        System.out.println("Respose To Send: \n" + regRespMsg.getText() + "\n");
        return updSubRespMsg;
    }
    
    public StringRepresentation unsubscribeJsonHandler(InputStream description, String acceptType) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        UnsubscribeContextAvailabilityRequest unsubReq;
        UnsubscribeContextAvailabilityResponse unsubResp;
        String subRespMsg = "";
        StringRepresentation regRespSr = new StringRepresentation("");

        Reader jsonReader = new InputStreamReader(description);
        unsubReq = gson.fromJson(jsonReader, UnsubscribeContextAvailabilityRequest.class);
//        System.out.println("Duration is: "+regReq.getDuration());
//        System.out.println("getContextRegistrationList is: "+regReq.getContextRegistration());
        unsubResp = unsubscribeContextPojo(unsubReq);

        if (acceptType.equalsIgnoreCase(MediaType.APPLICATION_XML.getSubType())) {
            UnsubscribeMarshaller subMar = new UnsubscribeMarshaller();
            try {
                subRespMsg = subMar.marshallResponse(unsubResp);
                regRespSr = new StringRepresentation(subRespMsg, MediaType.APPLICATION_XML);
            } catch (JAXBException ex) {
                Logger.getLogger(Resource05_AvailabilitySubscriptionDeletion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            subRespMsg = gson.toJson(unsubResp);
            regRespSr = new StringRepresentation(subRespMsg, MediaType.APPLICATION_JSON);
        }

        return regRespSr;
    }
    
    public StringRepresentation unsubscribeXmlHandler(InputStream description, String acceptType) throws ResourceException, IOException, JAXBException {

        //instantiate registration marshaller/unmarshaller, request, and response
        UnsubscribeMarshaller subMar = new UnsubscribeMarshaller();
        UnsubscribeContextAvailabilityRequest subReq;
        UnsubscribeContextAvailabilityResponse subResp = new UnsubscribeContextAvailabilityResponse();

        //set status code to default
        StatusCode sc = new StatusCode(200, "OK", "Stored");
        subResp.setStatusCode(sc);
        String subRespString = "";
        StringRepresentation subRespMsg = new StringRepresentation("");

        //unmarshall XML request
        try {
            subReq = subMar.unmarshallRequest(description);
            System.out.println("Marshalled XML Request: \n" + subMar.marshallRequest(subReq));
        } catch (JAXBException | java.lang.ClassCastException je) {
            //je.printStackTrace();
            System.out.println(je.getLocalizedMessage());
            //Error with XML structure, return message
            Logger.getLogger(Resource04_AvailabilitySubscriptionUpdate.class.getName()).log(Level.SEVERE, null, je);
            sc = new StatusCode(400, "Bad Request", "Error in XML structure");
            subResp.setStatusCode(sc);

            try {
                if (acceptType.equals(MediaType.APPLICATION_JSON.getSubType())) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    subRespString = gson.toJson(subResp);
                    subRespMsg = new StringRepresentation(subRespString, MediaType.APPLICATION_JSON);

                } else {
                    subRespString = subMar.marshallResponse(subResp);
                    subRespMsg = new StringRepresentation(subRespString, MediaType.APPLICATION_XML);
                }
            } catch (JAXBException ex2) {
                Logger.getLogger(Resource05_AvailabilitySubscriptionDeletion.class.getName()).log(Level.SEVERE, null, ex2);
            }

            return subRespMsg;

        }

        subResp = unsubscribeContextPojo(subReq);

        //marshal response message
        try {
            if (acceptType.equals(MediaType.APPLICATION_JSON.getSubType())) {
                Gson gson = new Gson();
                subRespString = gson.toJson(subResp);
                subRespMsg = new StringRepresentation(subRespString, MediaType.APPLICATION_JSON);
            } else {
                subRespString = subMar.marshallResponse(subResp);
                subRespMsg = new StringRepresentation(subRespString, MediaType.APPLICATION_XML);
            }
        } catch (JAXBException ex2) {
            Logger.getLogger(Resource05_AvailabilitySubscriptionDeletion.class.getName()).log(Level.SEVERE, null, ex2);
        }
        return subRespMsg;
    }
    
    public UnsubscribeContextAvailabilityResponse unsubscribeContextPojo(UnsubscribeContextAvailabilityRequest req) {

        //set status code to default
        StatusCode sc = new StatusCode(200, "OK", "Stored");

        //instantiate registration response
        UnsubscribeContextAvailabilityResponse subResp = new UnsubscribeContextAvailabilityResponse();

        //check if update is required
        String subId = req.getSubscriptionId();
        //does description have a registration ID that has the prefix "UniS_"?
        try {
            if (subId.startsWith("UniS_")) {
                //attempt to delete stored registration with this ID.
                boolean deleted = SubscriptionStoreAccess.deleteSubscription(subId);
                if (!deleted) {                    
                    // no registration with this ID found
                    sc = new StatusCode(404, "Resource not Found", "No Context Subscription with ID: " + subId);
                    subResp.setStatusCode(sc);
                }
            }
        } catch (NullPointerException npe) {
            //npe.printStackTrace();
            System.out.println("Subscription ID not provided");
            sc = new StatusCode(400, "Bad Request", "No Subscription ID provided");
            subResp.setStatusCode(sc);
        }
        
        //set Subscription ID
        subResp.setSubscriptionId(req.getSubscriptionId());        
        return subResp;
    }

//    public Representation unsubscribeToDescription(Representation entity) throws ResourceException, IOException, JAXBException {
//
//        InputStream description = new ByteArrayInputStream(entity.getText().getBytes());
//        ServletContext context = StorageStartup.servletContext;
//        UnsubscribeMarshaller subMar = new UnsubscribeMarshaller();
//        UnsubscribeContextAvailabilityRequest req = new UnsubscribeContextAvailabilityRequest();
//        String respMsg = "";
//
//        //unmarshall XML request
//        try {
//            req = subMar.unmarshallRequest(description);
//            System.out.println("RECEIEVED REQUEST: \n" + subMar.marshallRequest(req));
//            respMsg = unsubscribeToContext(context, req.getSubscriptionId());
//        } catch (JAXBException | java.lang.ClassCastException je) {
//            //Error with XML structure
//            System.out.println(je.getMessage());
//            UnsubscribeContextAvailabilityResponse unSubResp = new UnsubscribeContextAvailabilityResponse();
//            StatusCode sc = new StatusCode(400, "Bad Request", "Error in XML structure");
//            unSubResp.setStatusCode(sc);
//            respMsg = subMar.marshallResponse(unSubResp);
//
//        }
//        //pass subscription ID for subscription deletion
//        System.out.println("RESPONSE TO SEND: \n" + respMsg);
//        StringRepresentation result = new StringRepresentation(respMsg);
//        result.setMediaType(MediaType.APPLICATION_XML);
//        return result;
//    }
//
//    public String unsubscribeToContext(ServletContext context, String subId) throws ResourceException, IOException, JAXBException {
//
//        UnsubscribeMarshaller subMar = new UnsubscribeMarshaller();
//        UnsubscribeContextAvailabilityRequest req = new UnsubscribeContextAvailabilityRequest();
//        UnsubscribeContextAvailabilityResponse unSubResp = new UnsubscribeContextAvailabilityResponse();
//        StatusCode sc = new StatusCode(200, "OK", "Deleted");
//
//        boolean deleted = false;
//        try {
//
//            if (subId.startsWith("UniS_")) {
//                //SubscriptionStoreAccess ss = new SubscriptionStoreAccess(context);
//                //ss.openDb4o();
//                deleted = SubscriptionStoreAccess.deleteSubscription(subId);
////                ss.closeDb4o();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            sc = new StatusCode(500, "Internal Error", "Result");
//            req.setSubscriptionId("");
//        }
//
//        if (!deleted) {
//            sc = new StatusCode(404, "Subscription Not Found", "Result");
//        }
//
//        unSubResp.setStatusCode(sc);
//        unSubResp.setSubscriptionId(subId);
//        String regRespMsg = "";
//        regRespMsg = subMar.marshallResponse(unSubResp);
//        return regRespMsg;
//    }
}
