/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience;

import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistrationResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.EntityId;
import java.util.ArrayList;
import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.DiscoveryMarshaller;
import java.util.List;
import javax.xml.bind.JAXBException;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.RegisterStoreAccess;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.RegisterResultFilter;

/**
 * Resource which has only one representation.
 */
public class Resource04_IndividualAttributeDomain extends ServerResource {

    @Get
    public Representation getIndvAttrDomain() throws JAXBException {
        
        String acceptType = "";
        int size = getClientInfo().getAcceptedMediaTypes().size();
        if (size > 0) {
            acceptType = getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().getSubType();
        }

        String eIdString = (String) getRequest().getAttributes().get("EntityID");
        String attrDomain = (String) getRequest().getAttributes().get("attributeDomainName");
        
        System.out.println("CO 4: GET ATTRIBUTE DOMAIN OF INDIVIDUAL CONTEXT ENTITY: eId = '" + eIdString + "'" + ", attribute domain = '" + attrDomain + "'");
        
        EntityId eId = new EntityId();        
        eId.setId(eIdString);
        ArrayList<String> attrList = new ArrayList<>();

        //RETRIEVE USING ATTRIBUTE DOMAIN        
        DiscoveryContextAvailabilityResponse discContResp = new DiscoveryContextAvailabilityResponse();
        StatusCode sc = new StatusCode(200, "OK", "result");
        try {
            List<RegisterContextRequest> result = RegisterStoreAccess.getRegByEntityID(eId, attrList);            
            RegisterResultFilter regFilter = new RegisterResultFilter();            
            ContextRegistrationResponse crr;
            crr = regFilter.getContRegHasEntityIdAttrDomain(result, eIdString, attrDomain);
            List<ContextRegistrationResponse> crrl = new ArrayList<>();
            try{
            crr.getContextRegistration().getEntityId().size();
            crrl.add(crr);            
            }catch (NullPointerException npe){
                System.out.println("no entities found");
            
            }
            discContResp.getContextRegistrationResponse().addAll(crrl);
            discContResp = regFilter.removeSharedAttrDomain(discContResp, attrDomain);
            try {
                int contRegRespSize = discContResp.getContextRegistrationResponse().size();
                if (contRegRespSize < 1) {
                    sc = new StatusCode(404, "Context Element Not Found", "Result");
                }
            } catch (NullPointerException npe) {
                sc = new StatusCode(404, "Context Element Not Found", "result");
            }
        } catch (Exception e) {
            System.out.println("Internal Error: "+e.getMessage());
            sc = new StatusCode(500, "Internal Error", "result");
        }
        
        DiscoveryMarshaller dcam = new DiscoveryMarshaller();
        discContResp.setErrorCode(sc);
        String discRespMsg = "";
        discRespMsg = dcam.marshallResponse(discContResp);
        System.out.println("Response To Send: \n" + discRespMsg);
        StringRepresentation result = new StringRepresentation(discRespMsg);
        result.setMediaType(MediaType.APPLICATION_XML);
        return result;

    }
}
