/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.notify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistration;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistrationResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.NotifyContextAvailabilityRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.SubscribeContextAvailabilityRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource02_Discovery;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.SubscriptionStoreAccess;

/**
 *
 * @author te0003
 */
public class NotifierAtRegistration implements Runnable {

    public RegisterContextRequest regReq;

    public NotifierAtRegistration(RegisterContextRequest regRequest) {

        this.regReq = regRequest;
    }

    @Override
    public void run() {

        List<SubscribeContextAvailabilityRequest> subReq = SubscriptionStoreAccess.matchRegToSubs(regReq);

        int subReqListSize = subReq.size();

        List<ContextRegistration> cr = regReq.getContextRegistration();

        //iterate through each subscription
        for (int i = 0; i < subReqListSize; i++) {

            NotifyContextAvailabilityRequest ncar = new NotifyContextAvailabilityRequest();
            List<ContextRegistrationResponse> crrl = new ArrayList<>();

            int crListSize = cr.size();
            //iterate through each context registration
            for (int j = 0; j < crListSize; j++) {
                ContextRegistrationResponse crr = new ContextRegistrationResponse();
                crr.setContextRegistration(cr.get(j));
                crrl.add(crr);
            }
            ncar.getContextRegistrationResponse().addAll(crrl);
            ncar.setSubscriptionId(subReq.get(i).getSubscriptionId());
            //serialize request

            String notifMsg = null;
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            try {
                notifMsg = objectMapper.writeValueAsString(ncar);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(Resource02_Discovery.class.getName()).log(Level.SEVERE, null, ex);
            }            

            //notify subscriber
            String callbackUrl = subReq.get(i).getReference();
            ClientResource ngsiClient = new ClientResource(callbackUrl);
            ngsiClient.accept(MediaType.APPLICATION_JSON);
            String payload = notifMsg;
            try {
                ngsiClient.post(payload).write(System.out); //Response is not handled for now
            } catch (IOException ex) {
                Logger.getLogger(NotifierAtRegistration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
