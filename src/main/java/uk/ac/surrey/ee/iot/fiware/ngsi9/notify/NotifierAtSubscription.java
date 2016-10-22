/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.notify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.NotifyContextAvailabilityRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.SubscribeContextAvailabilityRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource02_Discovery;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityResponse;

/**
 *
 * @author te0003
 */
public class NotifierAtSubscription implements Runnable {

    public SubscribeContextAvailabilityRequest subsReq;

    public NotifierAtSubscription(SubscribeContextAvailabilityRequest subsRequest) {

        this.subsReq = subsRequest;
    }

    @Override
    public void run() {

        DiscoveryContextAvailabilityRequest discReq = new DiscoveryContextAvailabilityRequest();
        discReq.getEntityId().addAll(subsReq.getEntityId());
        discReq.getAttribute().addAll(subsReq.getAttribute());
        discReq.getRestriction().setAttributeExpression(subsReq.getRestriction().getAttributeExpression());
        discReq.getRestriction().getOperationScope().addAll(subsReq.getRestriction().getOperationScope());

        Resource02_Discovery discRes = new Resource02_Discovery();
        DiscoveryContextAvailabilityResponse dcar = discRes.parseDiscoveryRequest(discReq);

        NotifyContextAvailabilityRequest ncar = new NotifyContextAvailabilityRequest();
        ncar.getContextRegistrationResponse().addAll(dcar.getContextRegistrationResponse());

        ncar.setSubscriptionId(subsReq.getSubscriptionId());
        //marshal request
//            NotifyMarshaller nm = new NotifyMarshaller();

        String notifMsg = null;
//            try {
//                notifMsg = nm.marshallRequest(ncar);
//            } catch (JAXBException ex) {
//                Logger.getLogger(NotifierAtSubscription.class.getName()).log(Level.SEVERE, null, ex);
//            }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            notifMsg = objectMapper.writeValueAsString(ncar);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Resource02_Discovery.class.getName()).log(Level.SEVERE, null, ex);
        }

        //notify subscriber
        String callbackUrl = subsReq.getReference();
        ClientResource ngsiClient = new ClientResource(callbackUrl);
        ngsiClient.accept(MediaType.APPLICATION_JSON);
        String payload = notifMsg;
        try {
            ngsiClient.post(payload).write(System.out); //Response is not handled for now
        } catch (IOException ex) {
            Logger.getLogger(NotifierAtSubscription.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
