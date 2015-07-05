/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard;

import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextResponse;
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
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.RandomStringUtils;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.RegisterMarshaller;
import uk.ac.surrey.ee.iot.fiware.ngsi9.notify.AvailabilityNotifier;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.RegisterStoreAccess;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.StorageStartup;

/**
 * Resource which has only one representation.
 */
public class Resource01_ContextRegistration extends ServerResource {

    @Post //("json:xml")
    public Representation handlePost(Representation entity) throws ResourceException, IOException, JAXBException {

//        ServletContext context = (ServletContext) getContext().getServerDispatcher().getContext().getAttributes().get("org.restlet.ext.servlet.ServletContext");
//        ServletContext context = StorageStartup.servletContext;
        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }
        Representation rt = registerDescription(entity, acceptType);
        return rt;
    }

    public Representation registerDescription(Representation entity, String acceptType) throws ResourceException, IOException, JAXBException {

        //read NGSI description
        InputStream description = new ByteArrayInputStream(entity.getText().getBytes());
        String contentType = entity.getMediaType().getSubType();
//        System.out.println("Content-Type is: " + contentType);
//        System.out.println("Accept: " + acceptType);

        //register description
        StringRepresentation regRespMsg = null;
        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON.getSubType())) {
            //if request payload is JSON
            regRespMsg = registerJsonHandler(description, acceptType);
        } else {
            //request payload is XML
            regRespMsg = registerXmlHandler(description, acceptType);
        }

//        System.out.println("Respose To Send: \n" + regRespMsg.getText() + "\n");
        return regRespMsg;
    }

    public StringRepresentation registerJsonHandler(InputStream description, String acceptType) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RegisterContextRequest regReq;
        RegisterContextResponse regResp;
        String regRespMsg = "";
        StringRepresentation regRespSr = new StringRepresentation("");

        Reader jsonReader = new InputStreamReader(description);
        regReq = gson.fromJson(jsonReader, RegisterContextRequest.class);
//        System.out.println("Duration is: "+regReq.getDuration());
//        System.out.println("getContextRegistrationList is: "+regReq.getContextRegistration());
        regResp = registerContextPojo(regReq);

        if (acceptType.equalsIgnoreCase(MediaType.APPLICATION_XML.getSubType())) {
            RegisterMarshaller regMar = new RegisterMarshaller();
            try {
                regRespMsg = regMar.marshallResponse(regResp);
                regRespSr = new StringRepresentation(regRespMsg, MediaType.APPLICATION_XML);
            } catch (JAXBException ex) {
                Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            regRespMsg = gson.toJson(regResp);
            regRespSr = new StringRepresentation(regRespMsg, MediaType.APPLICATION_JSON);
        }

        return regRespSr;
    }

    public StringRepresentation registerXmlHandler(InputStream description, String acceptType) throws ResourceException, IOException, JAXBException {

        //instantiate registration marshaller/unmarshaller, request, and response
        RegisterMarshaller regMar = new RegisterMarshaller();
        RegisterContextRequest regReq;
        RegisterContextResponse regResp = new RegisterContextResponse();

        //set status code to default
        StatusCode sc = new StatusCode(200, "OK", "Stored");
        regResp.setErrorCode(sc);
        String regRespString = "";
        StringRepresentation regRespMsg = new StringRepresentation("");

        //unmarshall XML request
        try {
            regReq = regMar.unmarshallRequest(description);
            System.out.println("Marshalled XML Request: \n" + regMar.marshallRequest(regReq));
        } catch (JAXBException | java.lang.ClassCastException je) {
            //je.printStackTrace();
            System.out.println(je.getLocalizedMessage());
            //Error with XML structure, return message
            Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, je);
            sc = new StatusCode(400, "Bad Request", "Error in XML structure");
            regResp.setErrorCode(sc);

            try {
                if (acceptType.equals(MediaType.APPLICATION_JSON.getSubType())) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    regRespString = gson.toJson(regResp);
                    regRespMsg = new StringRepresentation(regRespString, MediaType.APPLICATION_JSON);

                } else {
                    regRespString = regMar.marshallResponse(regResp);
                    regRespMsg = new StringRepresentation(regRespString, MediaType.APPLICATION_XML);
                }
            } catch (JAXBException ex2) {
                Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex2);
            }

            return regRespMsg;

        }

        regResp = registerContextPojo(regReq);

        //marshal response message
        try {
            if (acceptType.equals(MediaType.APPLICATION_JSON.getSubType())) {
                Gson gson = new Gson();
                regRespString = gson.toJson(regResp);
                regRespMsg = new StringRepresentation(regRespString, MediaType.APPLICATION_JSON);
            } else {
                regRespString = regMar.marshallResponse(regResp);
                regRespMsg = new StringRepresentation(regRespString, MediaType.APPLICATION_XML);
            }
        } catch (JAXBException ex2) {
            Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex2);
        }
        return regRespMsg;
    }

    public RegisterContextResponse registerContextPojo(RegisterContextRequest req) {

        //set status code to default
        StatusCode sc = new StatusCode(200, "OK", "Stored");

        //instantiate registration response
        RegisterContextResponse regResp = new RegisterContextResponse();

        //check if update is required
        boolean doUpdate = false;
        String regId = req.getRegistrationId();
        //does description have a registration ID that has the prefix "UniS_"?
        try {
            if (regId.startsWith("UniS_")) {
                //attempt to delete stored registration with this ID.
                boolean deleted = RegisterStoreAccess.deleteRegistration(req);
                if (deleted) {
                    //perform update
                    doUpdate = true;
                } else {
                    // no registration with this ID found
                    sc = new StatusCode(404, "Resource not Found", "No Context Registration with ID: " + regId);
                    regResp.setErrorCode(sc);
                    return regResp;
                }
            }
        } catch (NullPointerException npe) {
            //npe.printStackTrace();
            System.out.println("Registration ID not provided");
        }
        // store/update registration
        try {
            if (!doUpdate) {
                // create new registration ID
                boolean regIdUsed = false;
                do {
                    //check if generated ID is already used
                    String idGenerated = "UniS_" + RandomStringUtils.randomAlphanumeric(10);
                    req.setRegistrationId(idGenerated);
                    regIdUsed = RegisterStoreAccess.checkRegIdUsed(idGenerated);
                } while (regIdUsed);
            }
            //store registration
            req.setTimestamp(Instant.now());
            RegisterStoreAccess.storeRegistration(req);
            regResp.setDuration(req.getDuration());
        } catch (Exception e) {
            //internal error with storage
            System.out.println("Internal Error: " + e.getLocalizedMessage());
            sc = new StatusCode(500, "Internal Error", e.getLocalizedMessage());
            req.setRegistrationId("");
        }
        regResp.setErrorCode(sc);
        //set registration ID
        regResp.setRegistrationId(req.getRegistrationId());

        //notify Subscribers
        try {
            Thread notifySubs = new Thread(new AvailabilityNotifier(req));
            notifySubs.start();
        } catch (NullPointerException npe) {
            System.out.println("Matched Subscriptions: " + npe.getMessage());
        } finally {
            //subStore.closeDb4o();
        }
        return regResp;
    }

}
