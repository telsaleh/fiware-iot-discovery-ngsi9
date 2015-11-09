/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard;

import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.RandomStringUtils;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.SubscribeMarshaller;
import uk.ac.surrey.ee.iot.fiware.ngsi9.notify.NotifierAtRegistration;
import uk.ac.surrey.ee.iot.fiware.ngsi9.notify.NotifierAtSubscription;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.SubscribeContextAvailabilityRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.SubscribeContextAvailabilityResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.SubscriptionStoreAccess;

/**
 * Resource which has only one representation.
 */
public class Resource03_AvailabilitySubscription extends ServerResource {

    @Post //("json:xml")
    public Representation handlePost(Representation entity) throws ResourceException, IOException, JAXBException {

        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }
        Representation rt = subscribeDescription(entity, acceptType);
        return rt;
    }

    public Representation subscribeDescription(Representation entity, String acceptType) throws ResourceException, IOException, JAXBException {

        //read NGSI description
        InputStream description = new ByteArrayInputStream(entity.getText().getBytes());
        String contentType = entity.getMediaType().getSubType();
//        System.out.println("Content-Type is: " + contentType);
//        System.out.println("Accept: " + acceptType);

        //register description
        StringRepresentation subRespMsg = null;
        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON.getSubType())) {
            //if request payload is JSON
            subRespMsg = subscribeJsonHandler(description, acceptType);
        } else {
            //request payload is XML
            subRespMsg = subscribeXmlHandler(description, acceptType);
        }

//        System.out.println("Respose To Send: \n" + regRespMsg.getText() + "\n");
        return subRespMsg;
    }

    public StringRepresentation subscribeJsonHandler(InputStream description, String acceptType) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SubscribeContextAvailabilityRequest subReq;
        SubscribeContextAvailabilityResponse subResp;
        String subRespMsg = "";
        StringRepresentation regRespSr = new StringRepresentation("");

        Reader jsonReader = new InputStreamReader(description);
        subReq = gson.fromJson(jsonReader, SubscribeContextAvailabilityRequest.class);
//        System.out.println("Duration is: "+regReq.getDuration());
//        System.out.println("getContextRegistrationList is: "+regReq.getContextRegistration());
        subResp = subscribeContextPojo(subReq);

        if (acceptType.equalsIgnoreCase(MediaType.APPLICATION_XML.getSubType())) {
            SubscribeMarshaller subMar = new SubscribeMarshaller();
            try {
                subRespMsg = subMar.marshallResponse(subResp);
                regRespSr = new StringRepresentation(subRespMsg, MediaType.APPLICATION_XML);
            } catch (JAXBException ex) {
                Logger.getLogger(Resource03_AvailabilitySubscription.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            subRespMsg = gson.toJson(subResp);
            regRespSr = new StringRepresentation(subRespMsg, MediaType.APPLICATION_JSON);
        }

        return regRespSr;
    }

    public StringRepresentation subscribeXmlHandler(InputStream description, String acceptType) throws ResourceException, IOException, JAXBException {

        //instantiate registration marshaller/unmarshaller, request, and response
        SubscribeMarshaller subMar = new SubscribeMarshaller();
        SubscribeContextAvailabilityRequest subReq;
        SubscribeContextAvailabilityResponse subResp = new SubscribeContextAvailabilityResponse();

        //set status code to default
        StatusCode sc = new StatusCode(200, "OK", "Stored");
        subResp.setErrorCode(sc);
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
            Logger.getLogger(Resource03_AvailabilitySubscription.class.getName()).log(Level.SEVERE, null, je);
            sc = new StatusCode(400, "Bad Request", "Error in XML structure");
            subResp.setErrorCode(sc);

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
                Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex2);
            }

            return subRespMsg;

        }

        subResp = subscribeContextPojo(subReq);

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
            Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex2);
        }
        return subRespMsg;
    }

    public SubscribeContextAvailabilityResponse subscribeContextPojo(SubscribeContextAvailabilityRequest req) {

        //set status code to default
        StatusCode sc = new StatusCode(200, "OK", "Stored");

        //instantiate registration response
        SubscribeContextAvailabilityResponse subResp = new SubscribeContextAvailabilityResponse();

        //check if update is required
        boolean doUpdate = false;
        String subId = req.getSubscriptionId();
        //does description have a registration ID that has the prefix "UniS_"?
        try {
            if (subId.startsWith("UniS_")) {
                //attempt to delete stored registration with this ID.
                boolean deleted = SubscriptionStoreAccess.deleteSubscription(subId);
                if (deleted) {
                    //perform update
                    doUpdate = true;
                } else {
                    // no registration with this ID found
                    sc = new StatusCode(404, "Resource not Found", "No Context Subscription with ID: " + subId);
                    subResp.setErrorCode(sc);
                    return subResp;
                }
            }
        } catch (NullPointerException npe) {
            //npe.printStackTrace();
            System.out.println("Subscription ID not provided");
        }
        // store/update registration
        try {
            if (!doUpdate) {
                // create new subscription ID
                boolean sub = false;
                do {
                    //check if generated ID is already used
                    String idGenerated = "UniS_" + RandomStringUtils.randomAlphanumeric(10);
                    req.setSubscriptionId(idGenerated);
                    sub = SubscriptionStoreAccess.checkSubIdUsed(idGenerated);
                } while (sub);
            }
            //store registration
            req.setTimestamp(Instant.now());
            SubscriptionStoreAccess.storeSubscription(req);
            subResp.setDuration(req.getDuration());
        } catch (Exception e) {
            //internal error with storage
            System.out.println("Internal Error: " + e.getLocalizedMessage());
            sc = new StatusCode(500, "Internal Error", e.getLocalizedMessage());
            req.setSubscriptionId("");
        }
        subResp.setErrorCode(sc);
        //set Subscription ID
        subResp.setSubscriptionId(req.getSubscriptionId());
        
        try {
            Thread notifySubs = new Thread(new NotifierAtSubscription(req));
            notifySubs.start();
        } catch (NullPointerException npe) {
            System.out.println("Notification error: " + npe.getMessage());
        } finally {
        }
        
        return subResp;
    }

}
