/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience;

import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistrationResponseList;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistrationResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.EntityId;
import java.util.ArrayList;
import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.DiscoveryMarshaller;
import java.util.List;
import javax.servlet.ServletContext;
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
    public Representation getIADomain() throws JAXBException {

//        ServletContext context = (ServletContext) getContext().getServerDispatcher().getContext().getAttributes().get("org.restlet.ext.servlet.ServletContext");

        EntityId eId = new EntityId();
        String eIdString = (String) getRequest().getAttributes().get("EntityID");
        eId.setId(eIdString);
//        ArrayList<EntityId> discEidList = new ArrayList<EntityId>();
//        discEidList.add(eId);
        ArrayList<String> attrList = new ArrayList<String>();
        String attrDomain = (String) getRequest().getAttributes().get("attributeDomainName");
        
        System.out.println("CO 4: GET ATTRIBUTE DOMAIN OF INDIVIDUAL CONTEXT ENTITY: eId = '" + eIdString + "'" + ", attribute domain = '" + attrDomain + "'");

        StatusCode sc = new StatusCode(200, "OK", "result");

        //RETRIEVE USING ATTRIBUTE DOMAIN
        DiscoveryMarshaller dcam = new DiscoveryMarshaller();
        DiscoveryContextAvailabilityResponse discContResp = new DiscoveryContextAvailabilityResponse();
        try {
            //RegisterStoreAccess regStore = new RegisterStoreAccess(context);
            RegisterResultFilter regFilter = new RegisterResultFilter();
            //regStore.openDb4o();
            List<RegisterContextRequest> result = RegisterStoreAccess.getRegByEntityID(eId, attrList);
            List<ContextRegistrationResponse> crrl = new ArrayList<>();
            ContextRegistrationResponse crr;
            crr = regFilter.getContRegHasEntityIdAttrDomain(result, eIdString, attrDomain);
            try{
            crr.getContextRegistration().getEntityId().size();
            crrl.add(crr);            
            }catch (NullPointerException npe){
                System.out.println("no entities found");
            
            }
//            discContResp.setContextRegistrationResponseList(crrl);
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

        discContResp.setErrorCode(sc);
        String discRespMsg = "";
        discRespMsg = dcam.marshallResponse(discContResp);
        System.out.println("Response To Send: \n" + discRespMsg);
        StringRepresentation result = new StringRepresentation(discRespMsg);
        result.setMediaType(MediaType.APPLICATION_XML);
        return result;

    }
}
