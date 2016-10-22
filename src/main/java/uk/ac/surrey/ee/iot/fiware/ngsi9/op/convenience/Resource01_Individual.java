/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.EntityId;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.*;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource01_ContextRegistration;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource02_Discovery;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityRequest;

/**
 * Resource which has only one representation.
 */
public class Resource01_Individual extends ServerResource {

    @Get
    public Representation handleGet() throws JAXBException {
        
        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }

        //get entity ID from URL
        String eIdString = (String) getRequest().getAttributes().get("EntityID");
        System.out.println("CO_1B: GET INDIVIDUAL CONTEXT ENTITY: " + eIdString);
        
        DiscoveryContextAvailabilityRequest dcar = new DiscoveryContextAvailabilityRequest();
        EntityId eId = new EntityId();
        eId.setId(eIdString);
        dcar.getEntityId().add(eId);
        
        Resource02_Discovery resDisc = new Resource02_Discovery();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        InputStream stream = null;
        try {
            stream = new ByteArrayInputStream(objectMapper.writeValueAsBytes(dcar)); //.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Resource01_Individual.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringRepresentation discRespSr = resDisc.discoveryJsonHandler(stream, acceptType);        

        return discRespSr;
    }

    @Post
    public Representation handlePost(Representation entity) throws ResourceException, IOException, JAXBException {

        //get registerContextRequest
        Resource01_ContextRegistration regConHandler = new Resource01_ContextRegistration();

        String eIdString = (String) getRequest().getAttributes().get("EntityID");
        System.out.println("CO 1A: REGISTER INDIVIDUAL CONTEXT ENTITY: eId = '" + "'" + eIdString);

        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }

        //register context provider
        Representation result = regConHandler.registerDescription(entity, acceptType);
        System.out.println(result);
        return result;
    }

}
