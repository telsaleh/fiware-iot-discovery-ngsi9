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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource02_Discovery;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityRequest;

/**
 * Resource which has only one representation.
 */
public class Resource03_IndividualAttribute extends ServerResource {

    @Get
    public Representation getIndvAttrRequest() throws JAXBException {
        
        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }
        
        String eIdString = (String) getRequest().getAttributes().get("EntityID");
        String attribute = (String) getRequest().getAttributes().get("attributeName");
        
        System.out.println("CO 3: GET ATTRIBUTE OF INDIVIDUAL CONTEXT ENTITY: eId = '" + eIdString + "'" + ", attribute = '" + attribute + "'");
        
        DiscoveryContextAvailabilityRequest dcar = new DiscoveryContextAvailabilityRequest();
        
        EntityId eId = new EntityId();
        eId.setId(eIdString);
        dcar.getEntityId().add(eId);        
        dcar.getAttribute().add(attribute);
        
        Resource02_Discovery resDisc = new Resource02_Discovery();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        InputStream stream = null;
        try {
            stream = new ByteArrayInputStream(objectMapper.writeValueAsBytes(dcar)); //.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Resource03_IndividualAttribute.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringRepresentation discRespSr = resDisc.discoveryJsonHandler(stream, acceptType);

        return discRespSr;
    }
    
}
