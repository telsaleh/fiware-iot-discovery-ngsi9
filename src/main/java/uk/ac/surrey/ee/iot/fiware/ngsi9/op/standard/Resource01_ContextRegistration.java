/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import uk.ac.surrey.ee.iot.fiware.ngsi9.notify.NotifierAtRegistration;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.RegisterStoreAccess;

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

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

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
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            RegisterContextResponse regResp = new RegisterContextResponse();
            regResp.setErrorCode(new StatusCode(404, "Bad Request", "JSON Content-Type not specified in header"));
            regRespMsg = new StringRepresentation(objectMapper.writeValueAsString(regResp), MediaType.APPLICATION_JSON);
        }
//        System.out.println("Respose To Send: \n" + regRespMsg.getText() + "\n");
        return regRespMsg;
    }

    public StringRepresentation registerJsonHandler(InputStream description, String acceptType) {

//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
         ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        RegisterContextRequest regReq = new RegisterContextRequest();
        RegisterContextResponse regResp = new RegisterContextResponse();
        String regRespMsg = "";
        StringRepresentation regRespSr ;//= new StringRepresentation("");

//        Reader jsonReader = new InputStreamReader(description);
        try {
            regReq = objectMapper.readValue(description, RegisterContextRequest.class);
        } catch (Exception e) {
            System.out.println("Bad Request: " + e.getLocalizedMessage());
            regResp.setErrorCode(new StatusCode(404, "Bad Request, Error in body", e.getLocalizedMessage()));
             try {
                 regRespMsg = objectMapper.writeValueAsString(regResp);
             } catch (JsonProcessingException ex) {
                 Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex);
             }
            return new StringRepresentation(regRespMsg, MediaType.APPLICATION_JSON);
        }

        regResp = registerContextPojo(regReq);

        
             try {
                 regRespMsg = objectMapper.writeValueAsString(regResp);
             } catch (JsonProcessingException ex) {
                 Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex);
             }
            regRespSr = new StringRepresentation(regRespMsg, MediaType.APPLICATION_JSON);       

        return regRespSr;
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
            Thread notifySubs = new Thread(new NotifierAtRegistration(req));
            notifySubs.start();
        } catch (NullPointerException npe) {
            System.out.println("Matched Subscriptions: " + npe.getMessage());
        } finally {
        }
        return regResp;
    }

}
