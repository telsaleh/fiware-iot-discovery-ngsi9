/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.DiscoveryMarshaller;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistrationResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.EntityId;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.OperationScope;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.Value;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.RegisterResultFilter;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.RegisterStoreAccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Resource which has only one representation.
 */
public class Resource02_Discovery extends ServerResource {

    @Post
    public Representation handlePost(Representation entity) throws ResourceException, IOException {

        //get context path
//        ServletContext sc = (ServletContext) getContext().getServerDispatcher().getContext().getAttributes().get("org.restlet.ext.servlet.ServletContext");
        String acceptType = "";
        int atSize = 0;
        try{
        atSize = getClientInfo().getAcceptedMediaTypes().size();
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }
        if (atSize > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }
        Representation rt = discoverDescription(entity, acceptType);
        return rt;
    }

    public Representation discoverDescription(Representation entity, String acceptType) throws ResourceException, IOException {

        //get NGSI discovery request
        InputStream discoveryReq = new ByteArrayInputStream(entity.getText().getBytes());

        StringRepresentation discRespMsg ;//= null;
        String contentType = entity.getMediaType().getSubType();
        System.out.println("Content-Type is: " + contentType);
        System.out.println("Accept: " + acceptType);
        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON.getSubType())) {
            //if request payload is JSON
            discRespMsg = discoveryJsonHandler(discoveryReq, acceptType);
        } else {
            //request payload is XML
            discRespMsg = discoveryXmlHandler(discoveryReq, acceptType);
        }

//        System.out.println("Respose To Send: \n" + discRespMsg.getText() + "\n");
        return discRespMsg;
    }

    public StringRepresentation discoveryJsonHandler(InputStream description, String acceptType) {

        DiscoveryContextAvailabilityRequest discReq ;//= new DiscoveryContextAvailabilityRequest();
        DiscoveryContextAvailabilityResponse discResp ;//= new DiscoveryContextAvailabilityResponse();
        String discRespMsg ;//= "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        StringRepresentation discRespSr = new StringRepresentation("");

        Reader jsonReader = new InputStreamReader(description);
        discReq = gson.fromJson(jsonReader, DiscoveryContextAvailabilityRequest.class);
        discResp = parseDiscoveryRequest(discReq);

        if (acceptType.equalsIgnoreCase(MediaType.APPLICATION_XML.getSubType())) {
            DiscoveryMarshaller discMar = new DiscoveryMarshaller();
            try {
                discRespMsg = discMar.marshallResponse(discResp);
                discRespSr = new StringRepresentation(discRespMsg, MediaType.APPLICATION_XML);
            } catch (JAXBException ex) {
                Logger.getLogger(Resource01_ContextRegistration.class.getName()).log(Level.SEVERE, null, ex);
//                StatusCode sc = new StatusCode(500, "Internal Error", "Cannot serialize response object");
            }
        } else {
            discRespMsg = gson.toJson(discResp);
            discRespSr = new StringRepresentation(discRespMsg, MediaType.APPLICATION_JSON);
        }

        return discRespSr;

    }

    public StringRepresentation discoveryXmlHandler(InputStream discoveryReq, String acceptType) throws ResourceException, IOException {
        //unmarshall request
        DiscoveryMarshaller discMar = new DiscoveryMarshaller();
        DiscoveryContextAvailabilityRequest discReq ;//= new DiscoveryContextAvailabilityRequest();
        DiscoveryContextAvailabilityResponse discResp = new DiscoveryContextAvailabilityResponse();
        String discRespMsg = "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            discReq = discMar.unmarshallRequest(discoveryReq);
//            System.out.println("Received Request: \n" + discMar.marshallRequest(discReq));
            discResp = parseDiscoveryRequest(discReq);
            if (acceptType.equalsIgnoreCase(MediaType.APPLICATION_JSON.getSubType())) {
                discRespMsg = gson.toJson(discResp);
            } else {
                discRespMsg = discMar.marshallResponse(discResp);
            }

        } catch (JAXBException | java.lang.ClassCastException ex) {
            //request structure invalid
            Logger.getLogger(Resource02_Discovery.class.getName()).log(Level.SEVERE, null, ex);
            StatusCode statusCode = new StatusCode(400, "Bad Request", "Error in XML structure");
            discResp.setErrorCode(statusCode);
            try {
                discRespMsg = discMar.marshallResponse(discResp);
            } catch (JAXBException ex2) {
                Logger.getLogger(Resource02_Discovery.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
        StringRepresentation result = new StringRepresentation(discRespMsg);
        result.setMediaType(MediaType.APPLICATION_XML);

        return result;
    }

    public DiscoveryContextAvailabilityResponse parseDiscoveryRequest(DiscoveryContextAvailabilityRequest discReq) {

        //get query parameters
        //entityId        
        ArrayList<EntityId> discEIdList = new ArrayList<>();
        int discEntityIdListSize = 0;
        try{
        discEntityIdListSize = discReq.getEntityId().size();
        }catch (NullPointerException npe){
        npe.printStackTrace();
        }        
        for (int i = 0; i < discEntityIdListSize; i++) {
            EntityId eId = discReq.getEntityId().get(i);
            if (!eId.getId().isEmpty()) {
                discEIdList.add(eId);
            }
        }

        //attributes
        ArrayList<String> discAttrList = new ArrayList<>();
        int discAttrListSize = 0;
        try{
        discAttrListSize = discReq.getAttribute().size();
        }catch (NullPointerException npe){
        npe.printStackTrace();
        }
        for (int i = 0; i < discAttrListSize; i++) {
            String discAttrId = discReq.getAttribute().get(i);
            if (!discAttrId.isEmpty()) {
                discAttrList.add(discAttrId);
            }
        }

        //operation scopes
        ArrayList<OperationScope> opScopeList = new ArrayList<>();
        int opScopeListSize = 0;
        try{
        opScopeListSize = discReq.getRestriction().getOperationScope().size();
        }catch (NullPointerException npe){
        npe.printStackTrace();
        }
        for (int i = 0; i < opScopeListSize; i++) {
            OperationScope opScope = discReq.getRestriction().getOperationScope().get(i);
            if (!opScope.getScopeType().isEmpty()) {
                opScopeList.add(opScope);
            }
        }

        DiscoveryContextAvailabilityResponse discRespMsg ;//= new DiscoveryContextAvailabilityResponse();
        //process discovery request
        discRespMsg = discoverContext(discEIdList, discAttrList, opScopeList);
        return discRespMsg;

    }

    public DiscoveryContextAvailabilityResponse discoverContext(ArrayList<EntityId> entityIdList, ArrayList<String> attributeList, ArrayList<OperationScope> opScopeList) {

        //instatiate response message
        DiscoveryContextAvailabilityResponse dcaResponse = new DiscoveryContextAvailabilityResponse();

        //set status code to default
        StatusCode sc = new StatusCode(200, "OK", "Result");

        //instantiate registration filter
        RegisterResultFilter regFilter = new RegisterResultFilter();

        try {
            //create context registration response and response list            
            ContextRegistrationResponse crr ;//= new ContextRegistrationResponse();
            List<ContextRegistrationResponse> crrl = new ArrayList<>();
            
            List<String> entityTypes = new ArrayList<String>();
            
            for (EntityId entityId : entityIdList) {
                try {
              
                    //...discovery request is for context provider
                    //retreive using IDs
                    List<RegisterContextRequest> eIdResults = RegisterStoreAccess.getRegByEntityID(entityId, attributeList);
                    //retrieve using attributes
                    
                    if(!entityId.getId().contentEquals(".*")){
	                    crr = regFilter.getCrrContainsEIdAttr(eIdResults, entityId, attributeList);
	                    
	                  
	            		crr = regFilter.removeSharedEntityID(crr, entityIdList);
	            		
	                    //add retrieve response to response list
	                    //crr.getContextRegistration().getEntityIdList();
	                    crrl.add(crr);
                    }
                    else{
                    	
                    	regFilter.getCrrContainsAttr(eIdResults, attributeList, crrl);
                    	
                    	//add type found to the list
                    	entityTypes.add(entityId.getType());
                    	
                    	regFilter.removeSharedEntityType(crrl, entityTypes);
                    }
                    

                    
                } catch (NullPointerException npe) {
                    System.out.println("get context entity error: " + npe.getMessage());
                }
                if (opScopeList.size() > 0) {
                    for (OperationScope opScope : opScopeList) {
                        if (opScope.getScopeType().equalsIgnoreCase("IncludeAssociations")) {

                            crrl = includeAssociations(crrl, opScope, entityId, attributeList);
                        }
                    }
                }
            }
//            dcaResponse.setContextRegistrationResponseList(crrl);
            dcaResponse.getContextRegistrationResponse().addAll(crrl);

            try {
                int crrSize = dcaResponse.getContextRegistrationResponse().size();
                if (crrSize < 1) {
                    sc = new StatusCode(404, "Context Element Not Found", "");
                }
            } catch (NullPointerException npe) {
                //set 404 if no entity is found  
                sc = new StatusCode(404, "Context Element Not Found", "");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sc = new StatusCode(500, "Internal Error", e.getLocalizedMessage());
            dcaResponse.setErrorCode(sc);
            return dcaResponse;
        }
        dcaResponse.setErrorCode(sc);
        return dcaResponse;
    }

    public List<ContextRegistrationResponse> includeAssociations(List<ContextRegistrationResponse> crrl, OperationScope opScope, EntityId discEId, ArrayList<String> discAttrList) {

        //instantiate registration filter
        RegisterResultFilter regFilter = new RegisterResultFilter();

        ContextRegistrationResponse crr = new ContextRegistrationResponse();

        //retrieve using entity ID, attribute, and association
        String scopeValue = opScope.getScopeValue();
        List<RegisterContextRequest> assocResults;//= new ArrayList<>();
        assocResults = RegisterStoreAccess.getAssociations(discEId, scopeValue, discAttrList);
        EntityId entityAssocId = new EntityId();
        for (RegisterContextRequest assocResult : assocResults) {
            
            int regMetaSize = assocResult.getContextRegistration().size();
            for (int i = 0; i < regMetaSize; i++) {
                
                int contMetaSize = assocResult.getContextRegistration().size();
                for (int j = 0; j < contMetaSize; j++) {
                    Value association = (Value) assocResult.getContextRegistration().get(i).getContextMetadata().get(j).getValue();
                    if (scopeValue.equalsIgnoreCase("SOURCES")) {                        //if field specifies "SOURCES", then get the sourceEntityId                        
                        //entityAssocId = assocResult.getContextRegistration().get(i).getContextMetadata().get(j).getValue().getEntityAssociation().getSourceEntityId();
                        entityAssocId = association./*getEntityAssociation().*/getSourceEntityId(); 
                        System.out.println("Source ID is: " + entityAssocId.getId());
                    } else if (scopeValue.equalsIgnoreCase("TARGETS")) {
                        //if field specifies "TARGETS", then get the targetEntityId
                        //entityAssocId = assocResult.getContextRegistration().get(i).getContextMetadata().get(j).getValue().getEntityAssociation().getTargetEntityId();
                        entityAssocId = association./*getEntityAssociation().*/getTargetEntityId();
                        System.out.println("Target ID is: " + entityAssocId.getId());
                    }
                    crr.setContextRegistration(assocResult.getContextRegistration().get(0));
                    crrl.add(crr);
                    //get associated context entity
                    List<RegisterContextRequest> assocIdResults = RegisterStoreAccess.getRegByEntityID(entityAssocId, discAttrList);
                    crr = regFilter.getContRegHasEntityId(assocIdResults, entityAssocId.getId());
                    crrl.add(crr);
                } //iterate through context metadatas
            } //iterate through registration metadatas
        }

        return crrl;
    }//end includeAssociations

}
