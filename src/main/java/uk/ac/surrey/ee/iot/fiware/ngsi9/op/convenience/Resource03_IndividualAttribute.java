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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.DiscoveryMarshaller;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource01_ContextRegistration;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource02_Discovery;

/**
 * Resource which has only one representation.
 */
public class Resource03_IndividualAttribute extends ServerResource {

    @Get
    public Representation getIndvAttrRequest() throws JAXBException {
        
        
        EntityId eId = new EntityId();
        String eIdString = (String) getRequest().getAttributes().get("EntityID");
        eId.setId(eIdString);
        ArrayList<EntityId> discEidList = new ArrayList<>();
        discEidList.add(eId);
        String attribute = (String) getRequest().getAttributes().get("attributeName");
        ArrayList<String> discAttrList = new ArrayList<>();
        ArrayList<OperationScope> opScopeList = new ArrayList<>();
        discAttrList.add(attribute);
        opScopeList.add(new OperationScope("nothing","nothing"));
        
        System.out.println("CO 3: GET ATTRIBUTE OF INDIVIDUAL CONTEXT ENTITY: eId = '" + eIdString + "'" + ", attribute = '" + attribute + "'");
        
        Resource02_Discovery resDisc = new Resource02_Discovery();
        DiscoveryContextAvailabilityResponse discResp = resDisc.discoverContext(discEidList, discAttrList, opScopeList);
        
        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }

        StringRepresentation discRespSr = new StringRepresentation("");
        String discRespMsg = "";

        if (acceptType.equalsIgnoreCase(MediaType.APPLICATION_JSON.getSubType())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            discRespMsg = gson.toJson(discResp);
            discRespSr = new StringRepresentation(discRespMsg, MediaType.APPLICATION_JSON);
        } else {
            DiscoveryMarshaller discMar = new DiscoveryMarshaller();
            try {
                discRespMsg = discMar.marshallResponse(discResp);
                discRespSr = new StringRepresentation(discRespMsg, MediaType.APPLICATION_XML);
            } catch (JAXBException ex) {
                Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

//        System.out.println("Response To Send: \n" + discRespMsg);

        return discRespSr;
    }
    
}
