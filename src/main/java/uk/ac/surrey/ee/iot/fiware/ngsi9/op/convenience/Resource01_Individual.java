/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience;

import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.OperationScope;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.EntityId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.*;
import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.DiscoveryMarshaller;
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        InputStream stream = new ByteArrayInputStream(gson.toJson(dcar).getBytes(StandardCharsets.UTF_8));        
        StringRepresentation discRespSr = resDisc.discoveryJsonHandler(stream, acceptType);        
       
//        ArrayList<EntityId> discEidList = new ArrayList<>();
//        //these 2 arrays are left empty
//        ArrayList<String> discAttrList = new ArrayList<>();
//        ArrayList<OperationScope> opScopeList = new ArrayList<>();
//        //add the entity ID to the array list
//        discEidList.add(eId);
//        opScopeList.add(new OperationScope("nothing", "nothing"));
       

        //use discover method to retreive description
        
//        String discRespMsg = resDisc.discoverContext(discEidList, discAttrList, opScopeList);
//        DiscoveryContextAvailabilityResponse discResp = resDisc.discoverContext(discEidList, discAttrList, opScopeList);
//
//        StringRepresentation discRespSr = new StringRepresentation("");
//        String discRespMsg = "";
//
//        if (acceptType.equalsIgnoreCase(MediaType.APPLICATION_JSON.getSubType())) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            discRespMsg = gson.toJson(discResp);
//            discRespSr = new StringRepresentation(discRespMsg, MediaType.APPLICATION_JSON);
//        } else {
//            DiscoveryMarshaller discMar = new DiscoveryMarshaller();
//            try {
//                discRespMsg = discMar.marshallResponse(discResp);
//                discRespSr = new StringRepresentation(discRespMsg, MediaType.APPLICATION_XML);
//            } catch (JAXBException ex) {
//                Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

//        System.out.println("Response To Send: \n" + discRespMsg);

        return discRespSr;
    }

    @Post
    public Representation handlePost(Representation entity) throws ResourceException, IOException, JAXBException {

        //get registerContextRequest
//        InputStream description = new ByteArrayInputStream(entity.getText().getBytes());
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
