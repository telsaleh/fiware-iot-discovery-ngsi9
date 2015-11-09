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
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource02_Discovery;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o.SubscriptionStoreAccess;

/**
 *
 * @author te0003
 */
public class NotifierAtSubscription implements Runnable{
    
    public SubscribeContextAvailabilityRequest subsReq;
    
    public NotifierAtSubscription(SubscribeContextAvailabilityRequest subsRequest){
        
        this.subsReq=subsRequest;
    }

    @Override
    public void run() {
        
        DiscoveryContextAvailabilityRequest discReq  = new DiscoveryContextAvailabilityRequest();
        discReq.getEntityId().addAll(subsReq.getEntityId());
        discReq.getAttribute().addAll(subsReq.getAttribute());
        discReq.getRestriction().setAttributeExpression(subsReq.getRestriction().getAttributeExpression());
        discReq.getRestriction().getOperationScope().addAll(subsReq.getRestriction().getOperationScope());
        
        Resource02_Discovery discRes = new Resource02_Discovery();        
        DiscoveryContextAvailabilityResponse dcar = discRes.parseDiscoveryRequest(discReq);
        
        NotifyContextAvailabilityRequest ncar =  new NotifyContextAvailabilityRequest();
        ncar.getContextRegistrationResponse().addAll(dcar.getContextRegistrationResponse());
       
            ncar.setSubscriptionId(subsReq.getSubscriptionId());
            //marshal request
            NotifyMarshaller nm = new NotifyMarshaller();

            String notifMsg = null;
            try {
                notifMsg = nm.marshallRequest(ncar);
            } catch (JAXBException ex) {
                Logger.getLogger(NotifierAtSubscription.class.getName()).log(Level.SEVERE, null, ex);
            }

            //notify subscriber
            String callbackUrl = subsReq.getReference();
            ClientResource ngsiClient = new ClientResource(callbackUrl);
            String payload = notifMsg;
            try {
                ngsiClient.post(payload).write(System.out); //Response is not handled for now
            } catch (IOException ex) {
                Logger.getLogger(NotifierAtSubscription.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

