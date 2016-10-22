/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistrationResponseList;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Resource07_TypeAttribute extends ServerResource {

    @Get
    public Representation getTypeAttribute() {

        //ServletContext context = (ServletContext) getContext().getServerDispatcher().getContext().getAttributes().get("org.restlet.ext.servlet.ServletContext");
        String eType = (String) getRequest().getAttributes().get("typeName");
        String attribute = (String) getRequest().getAttributes().get("attributeName");

        System.out.println("CO 7: GET CONTEXT ENTITY TYPE: eId Type = '" + eType + "'" + ", attribute = '" + attribute + "'");

        StatusCode sc = new StatusCode(200, "OK", "Result");
        DiscoveryContextAvailabilityResponse discResp = new DiscoveryContextAvailabilityResponse();

        try {
            RegisterResultFilter regFilter = new RegisterResultFilter();
            List<RegisterContextRequest> result = RegisterStoreAccess.getRegByEntityType(eType);
            ContextRegistrationResponseList crrl = new ContextRegistrationResponseList();
            crrl = regFilter.getContRegHasEntityTypeAttr(result, eType, attribute);
            discResp.getContextRegistrationResponse().addAll(crrl.getContextRegistrationResponse());
            discResp = regFilter.removeSharedEntityType(discResp, eType);
            try {
                int contRegRespSize = discResp.getContextRegistrationResponse().size();
                if (contRegRespSize < 1) {
                    sc = new StatusCode(404, "Context Element Not Found", "Result");
                }
            } catch (NullPointerException npe) {
                sc = new StatusCode(404, "Context Element Not Found", "result");
            }
        } catch (Exception e) {
            sc = new StatusCode(500, "Internal Error", "result");
        }
        discResp.setErrorCode(sc);

        StringRepresentation discRespSr = new StringRepresentation("");
        String discRespMsg = "";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            discRespMsg = objectMapper.writeValueAsString(discResp);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Resource07_TypeAttribute.class.getName()).log(Level.SEVERE, null, ex);
        }
        discRespSr = new StringRepresentation(discRespMsg, MediaType.APPLICATION_JSON);
        return discRespSr;
    }

}
