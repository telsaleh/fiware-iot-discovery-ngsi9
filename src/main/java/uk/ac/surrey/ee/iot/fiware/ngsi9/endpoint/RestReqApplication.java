/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.endpoint;

import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource04_AvailabilitySubscriptionUpdate;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource02_Discovery;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource05_AvailabilitySubscriptionDeletion;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource01_ContextRegistration;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience.Resource03_IndividualAttribute;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience.Resource10_AvailabilitySubscription;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience.Resource01_Individual;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience.Resource07_TypeAttribute;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.convenience.Resource05_Type;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router; 
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource03_AvailabilitySubscription;

public class RestReqApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of HelloWorldResource.
        Router router = new Router(getContext());
                
        //index page
        router.attachDefault(indexResource.class);
        
        //sanity check
        router.attach("/sanityCheck", SanityCheckResource.class);
        
        // Standard NGSI-9 Operation Resources
        
        router.attach("/registerContext", Resource01_ContextRegistration.class);
        router.attach("/discoverContextAvailability", Resource02_Discovery.class);
        router.attach("/subscribeContextAvailability", Resource03_AvailabilitySubscription.class);
        router.attach("/updateContextAvailabilitySubscription", Resource04_AvailabilitySubscriptionUpdate.class);
        router.attach("/unsubscribeContextAvailability", Resource05_AvailabilitySubscriptionDeletion.class);
        
        // Convenience Operation Resources
        
        router.attach("/contextEntities/{EntityID}", Resource01_Individual.class);
        router.attach("/contextEntities/{EntityID}/attributes", Resource01_Individual.class); //same as previous       
        router.attach("/contextEntities/{EntityID}/attributes/{attributeName}", Resource03_IndividualAttribute.class);
//        router.attach("/contextEntities/{EntityID}/attributeDomains/{attributeDomainName}", Resource04_IndividualAttributeDomain.class);
        router.attach("/contextEntityTypes/{typeName}", Resource05_Type.class);
        router.attach("/contextEntityTypes/{typeName}/attributes", Resource05_Type.class); //same as previous
        router.attach("/contextEntityTypes/{typeName}/attributes/{attributeName}", Resource07_TypeAttribute.class);
//        router.attach("/contextEntityTypes/{typeName}/attributeDomains/{attributeDomainName}", Resource08_TypeAttributeDomain.class);                
        router.attach("/contextAvailabilitySubscriptions", Resource03_AvailabilitySubscription.class);        
        router.attach("/contextAvailabilitySubscriptions/{subscriptionID}", Resource10_AvailabilitySubscription.class);

        //testing
        //router.attach("/testUnitStart", Resource_testUnitDb.class);

        return router;
    }

}
