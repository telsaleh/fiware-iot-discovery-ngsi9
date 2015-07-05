/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.notify;

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
import javax.xml.bind.JAXBException;
import org.restlet.resource.ClientResource;
import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.NotifyMarshaller;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.SubscriptionStoreAccess;

/**
 *
 * @author te0003
 */
public class AvailabilityNotifier implements Runnable{
    
    public RegisterContextRequest regReq;
    
    public AvailabilityNotifier(RegisterContextRequest regRequest){
        
        this.regReq=regRequest;
    }

    @Override
    public void run() {

        List<SubscribeContextAvailabilityRequest> subReq = SubscriptionStoreAccess.matchRegReqToSubsStore(regReq);
        
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
            //marshal request
            NotifyMarshaller nm = new NotifyMarshaller();

            String notifMsg = null;
            try {
                notifMsg = nm.marshallRequest(ncar);
            } catch (JAXBException ex) {
                Logger.getLogger(AvailabilityNotifier.class.getName()).log(Level.SEVERE, null, ex);
            }

            //notify subscriber
            String callbackUrl = subReq.get(i).getReference();
            ClientResource ngsiClient = new ClientResource(callbackUrl);
            String payload = notifMsg;
            try {
                ngsiClient.post(payload).write(System.out); //Response is not handled for now
            } catch (IOException ex) {
                Logger.getLogger(AvailabilityNotifier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
