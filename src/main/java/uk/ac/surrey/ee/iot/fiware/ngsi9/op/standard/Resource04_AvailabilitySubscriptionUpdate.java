/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.UpdateContextAvailabilitySubscriptionRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.UpdateContextAvailabilitySubscriptionResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.SubscriptionStoreAccess;

/**
 * Resource which has only one representation.
 */
public class Resource04_AvailabilitySubscriptionUpdate extends ServerResource {

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

        //update subscription
        StringRepresentation updSubRespMsg = null;
//        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON.getSubType())) {
            //if request payload is JSON
            updSubRespMsg = subscribeJsonHandler(description, acceptType);
//        } else {
//            //request payload is XML
//            updSubRespMsg = subscribeXmlHandler(description, acceptType);
//        }

//        System.out.println("Respose To Send: \n" + regRespMsg.getText() + "\n");
        return updSubRespMsg;
    }

    public StringRepresentation subscribeJsonHandler(InputStream description, String acceptType) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        UpdateContextAvailabilitySubscriptionRequest subReq = null;
        UpdateContextAvailabilitySubscriptionResponse subResp;
        String subRespMsg = "";
        StringRepresentation regRespSr = new StringRepresentation("");

        try {
            //        Reader jsonReader = new InputStreamReader(description);
            subReq = objectMapper.readValue(description, UpdateContextAvailabilitySubscriptionRequest.class);
        } catch (IOException ex) {
            Logger.getLogger(Resource04_AvailabilitySubscriptionUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Duration is: "+regReq.getDuration());
//        System.out.println("getContextRegistrationList is: "+regReq.getContextRegistration());
        subResp = subscribeContextPojo(subReq);

//        if (acceptType.equalsIgnoreCase(MediaType.APPLICATION_XML.getSubType())) {
//            UpdateSubsMarshaller subMar = new UpdateSubsMarshaller();
//            try {
//                subRespMsg = subMar.marshallResponse(subResp);
//                regRespSr = new StringRepresentation(subRespMsg, MediaType.APPLICATION_XML);
//            } catch (JAXBException ex) {
//                Logger.getLogger(Resource04_AvailabilitySubscriptionUpdate.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
            try {
                subRespMsg = objectMapper.writeValueAsString(subResp);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(Resource04_AvailabilitySubscriptionUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
            regRespSr = new StringRepresentation(subRespMsg, MediaType.APPLICATION_JSON);
//        }

        return regRespSr;
    }

//    public StringRepresentation subscribeXmlHandler(InputStream description, String acceptType) throws ResourceException, IOException, JAXBException {
//
//        //instantiate registration marshaller/unmarshaller, request, and response
//        UpdateSubsMarshaller subMar = new UpdateSubsMarshaller();
//        UpdateContextAvailabilitySubscriptionRequest subReq;
//        UpdateContextAvailabilitySubscriptionResponse subResp = new UpdateContextAvailabilitySubscriptionResponse();
//
//        //set status code to default
//        StatusCode sc = new StatusCode(200, "OK", "Stored");
//        subResp.setErrorCode(sc);
//        String subRespString = "";
//        StringRepresentation subRespMsg = new StringRepresentation("");
//
//        //unmarshall XML request
//        try {
//            subReq = subMar.unmarshallRequest(description);
//            System.out.println("Marshalled XML Request: \n" + subMar.marshallRequest(subReq));
//        } catch (JAXBException | java.lang.ClassCastException je) {
//            //je.printStackTrace();
//            System.out.println(je.getLocalizedMessage());
//            //Error with XML structure, return message
//            Logger.getLogger(Resource04_AvailabilitySubscriptionUpdate.class.getName()).log(Level.SEVERE, null, je);
//            sc = new StatusCode(400, "Bad Request", "Error in XML structure");
//            subResp.setErrorCode(sc);
//
//            try {
//                if (acceptType.equals(MediaType.APPLICATION_JSON.getSubType())) {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//                    subRespString = objectMapper.writeValueAsString(subResp);
//                    subRespMsg = new StringRepresentation(subRespString, MediaType.APPLICATION_JSON);
//                } else {
//                    subRespString = subMar.marshallResponse(subResp);
//                    subRespMsg = new StringRepresentation(subRespString, MediaType.APPLICATION_XML);
//                }
//            } catch (JAXBException ex2) {
//                Logger.getLogger(Resource04_AvailabilitySubscriptionUpdate.class.getName()).log(Level.SEVERE, null, ex2);
//            }
//
//            return subRespMsg;
//
//        }
//
//        subResp = subscribeContextPojo(subReq);
//
//        //marshal response message
//        try {
//            if (acceptType.equals(MediaType.APPLICATION_JSON.getSubType())) {
//                ObjectMapper objectMapper = new ObjectMapper();
//                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//                subRespString = objectMapper.writeValueAsString(subResp);
//                subRespMsg = new StringRepresentation(subRespString, MediaType.APPLICATION_JSON);
//            } else {
//                subRespString = subMar.marshallResponse(subResp);
//                subRespMsg = new StringRepresentation(subRespString, MediaType.APPLICATION_XML);
//            }
//        } catch (JAXBException ex2) {
//            Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex2);
//        }
//        return subRespMsg;
//    }

    public UpdateContextAvailabilitySubscriptionResponse subscribeContextPojo(UpdateContextAvailabilitySubscriptionRequest req) {

        //set status code to default
        StatusCode sc = new StatusCode(200, "OK", "Stored");

        //instantiate registration response
        UpdateContextAvailabilitySubscriptionResponse subResp = new UpdateContextAvailabilitySubscriptionResponse();

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
            //update registration
            SubscriptionStoreAccess.updateSubscription(req);
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

        return subResp;
    }

}
