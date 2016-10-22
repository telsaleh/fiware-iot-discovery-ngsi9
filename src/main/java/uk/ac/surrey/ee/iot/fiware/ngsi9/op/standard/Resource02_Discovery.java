/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.*;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.RegisterResultFilter;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.RegisterStoreAccess;

/**
 * Resource which has only one representation.
 */
public class Resource02_Discovery extends ServerResource {

    @Post
    public Representation handlePost(Representation entity) throws ResourceException, IOException {

        String acceptType = "";
        int atSize = 0;
        try {
            atSize = getClientInfo().getAcceptedMediaTypes().size();
        } catch (NullPointerException npe) {
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

        StringRepresentation discRespMsg;//= null;
//        String contentType = entity.getMediaType().getSubType();
//        System.out.println("Content-Type is: " + contentType);
//        System.out.println("Accept: " + acceptType);
//        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON.getSubType())) {
            //if request payload is JSON
            discRespMsg = discoveryJsonHandler(discoveryReq, acceptType);
//        } else {
//            discRespMsg = discoveryJsonHandler(discoveryReq, acceptType);
//        }

//        System.out.println("Respose To Send: \n" + discRespMsg.getText() + "\n");
        return discRespMsg;
    }

    public StringRepresentation discoveryJsonHandler(InputStream description, String acceptType) {

        DiscoveryContextAvailabilityRequest discReq = new DiscoveryContextAvailabilityRequest();//= new DiscoveryContextAvailabilityRequest();
        DiscoveryContextAvailabilityResponse discResp;//= new DiscoveryContextAvailabilityResponse();
        String discRespMsg = "";//= "";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            //        Reader jsonReader = new InputStreamReader(description);
            discReq = objectMapper.readValue(description, DiscoveryContextAvailabilityRequest.class);
        } catch (IOException ex) {
            Logger.getLogger(Resource02_Discovery.class.getName()).log(Level.SEVERE, null, ex);
        }
        discResp = parseDiscoveryRequest(discReq);

        StringRepresentation discRespSr = new StringRepresentation("");

//            System.out.println("HELLO");
        try {
            discRespMsg = objectMapper.writeValueAsString(discResp);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Resource02_Discovery.class.getName()).log(Level.SEVERE, null, ex);
        }
        discRespSr = new StringRepresentation(discRespMsg, MediaType.APPLICATION_JSON);

        return discRespSr;
    }

    public DiscoveryContextAvailabilityResponse parseDiscoveryRequest(DiscoveryContextAvailabilityRequest discReq) {

        //get query parameters
        //entityId        
        ArrayList<EntityId> discEIdList = new ArrayList<>();
        int discEntityIdListSize = 0;
        try {
            discEntityIdListSize = discReq.getEntityId().size();
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
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
        try {
            discAttrListSize = discReq.getAttribute().size();
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
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
        try {
            opScopeListSize = discReq.getRestriction().getOperationScope().size();
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
        }
        for (int i = 0; i < opScopeListSize; i++) {
            OperationScope opScope = discReq.getRestriction().getOperationScope().get(i);
            if (!opScope.getScopeType().isEmpty()) {
                opScopeList.add(opScope);
            }
        }

        DiscoveryContextAvailabilityResponse discRespMsg;//= new DiscoveryContextAvailabilityResponse();
        //process discovery request
        discRespMsg = discoverContext(discEIdList, discAttrList, opScopeList);
        return discRespMsg;

    }

    public DiscoveryContextAvailabilityResponse discoverContext(ArrayList<EntityId> entityIdList, ArrayList<String> attributeList, ArrayList<OperationScope> opScopeList) {

        //create response message
        DiscoveryContextAvailabilityResponse dcaResponse = new DiscoveryContextAvailabilityResponse();

        //set status code to default
        StatusCode sc = new StatusCode(200, "OK", "Result");

        //instantiate registration filter
        RegisterResultFilter regFilter = new RegisterResultFilter();

        try {
            //create context registration response and response list            
            ContextRegistrationResponse crr;
            List<ContextRegistrationResponse> crrl = new ArrayList<>();

            for (EntityId entityId : entityIdList) {
                try {
                    //...discovery request is for context provider
                    //retreive using IDs
                    List<RegisterContextRequest> eIdResults = RegisterStoreAccess.getRegByEntityID(entityId, attributeList);
                    //retrieve using attributes
                    crr = regFilter.getCrrContainsEIdAttr(eIdResults, entityId, attributeList);
                    crr = regFilter.removeSharedEntityID(crr, entityIdList);
                    //add retrieve response to response list
                    crrl.add(crr);
                } catch (NullPointerException npe) {
                    System.out.println("get context entity error: " + npe.getMessage());
                }
                if (opScopeList.size() > 0) {
                    for (OperationScope opScope : opScopeList) {
                        if (opScope.getScopeType().equalsIgnoreCase("IncludeAssociations")) {

                            crrl = includeAssociations(crrl, opScope, entityId, attributeList);
                        }
                        if (opScope.getScopeType().equalsIgnoreCase("FIWARE::Location")) {

                            crrl = geolocate(crrl, opScope, entityId, attributeList);
                        } else if (opScope.getScopeType().equalsIgnoreCase("Search")) {

                            crrl = probabilisticSearch(crrl, opScope, entityId, attributeList);
                        }
                    }
                }
            }
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

        ObjectMapper objectMapper = new ObjectMapper();

        //instantiate registration filter
        RegisterResultFilter regFilter = new RegisterResultFilter();
        ContextRegistrationResponse crr = new ContextRegistrationResponse();

        //retrieve using entity ID, attribute, and association
        String scopeValue = (String) opScope.getScopeValue();
        List<RegisterContextRequest> assocResults;
        assocResults = RegisterStoreAccess.getAssociations(discEId, scopeValue, discAttrList);
        EntityId entityAssocId = new EntityId();
        for (RegisterContextRequest assocResult : assocResults) {

            int regMetaSize = assocResult.getContextRegistration().size();
            for (int i = 0; i < regMetaSize; i++) {

                int contMetaSize = assocResult.getContextRegistration().size();
                for (int j = 0; j < contMetaSize; j++) {

//                    Association association = (Association) assocResult.getContextRegistration().get(i).getContextMetadata().get(j).getValue();
                    LinkedHashMap assocLHashMap = (LinkedHashMap) assocResult.getContextRegistration().get(i).getContextMetadata().get(j).getValue();
                    Association association = (Association) objectMapper.convertValue(assocLHashMap, Association.class);

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

    private List<ContextRegistrationResponse> geolocate(List<ContextRegistrationResponse> crrl, OperationScope opScope, EntityId entityId, ArrayList<String> attributeList) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        ObjectMapper objectMapper = new ObjectMapper();

        //instantiate registration filter
        RegisterResultFilter regFilter = new RegisterResultFilter();
        ContextRegistrationResponse crr = new ContextRegistrationResponse();

        LinkedHashMap shapeHMap = (LinkedHashMap) opScope.getScopeValue();
        Shape ngsiShape = objectMapper.convertValue(shapeHMap, Shape.class);
        final GeometryFactory gf = new GeometryFactory();
        Polygon polygon = ngsiShape.getPolygon();
        if (!(polygon == null)) {
            final ArrayList<Coordinate> points = new ArrayList<>();
            int polygonPoints = polygon.getVertices().size();
            for (int i = 0; i < polygonPoints; i++) {
                int lat = Integer.getInteger(polygon.getVertices().get(i).getLatitude());
                int lon = Integer.getInteger(polygon.getVertices().get(i).getLongitude());                
                points.add(new Coordinate(lat, lon));
            }
            Geometry shape = gf.createPolygon(new LinearRing(new CoordinateArraySequence(points.toArray(
                    new Coordinate[points.size()])), gf), null);
            
        }

        return crrl;
    }

    private List<ContextRegistrationResponse> probabilisticSearch(List<ContextRegistrationResponse> crrl, OperationScope opScope, EntityId entityId, ArrayList<String> attributeList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
