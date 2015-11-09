/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.endpoint.coap;

import org.eclipse.californium.core.CoapServer;
import java.io.File;
import java.util.concurrent.Executors;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author te0003
 */
public class CoapServerContextListener implements ServletContextListener {

    public CoapServer server;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // start the thread

        System.out.println("intializing CoAP server");
        System.out.println("context-path: " + sce.getServletContext().getContextPath());
        System.out.println("real-path: " + sce.getServletContext().getRealPath(File.separator));

        server = new CoapServer(5700);//deafult is 5683
        server.setExecutor(Executors.newScheduledThreadPool(4));

        //test
//        server.add(new HelloWorldResource("hello"));

        //Standard Operations        
        server.add(new CoapR01_ContextRegistration("ngsi9/registerContext"));
        server.add(new CoapR02_Discovery("ngsi9/discoverContextAvailability"));
        server.add(new CoapR03_AvailabilitySubscription("ngsi9/subscribeContextAvailability"));
        server.add(new CoapR04_AvailabilitySubscriptionUpdate("ngsi9/updateContextAvailabilitySubscription"));
        server.add(new CoapR05_AvailabilitySubscriptionDeletion("ngsi9/unsubscribeContextAvailability"));

        //Convenience Operations
//        server.add(new CoapRC01_Individual("ngsi9/contextEntities/{EntityID}"));       
        
        server.start();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // stop the thread

        server.stop();
        System.out.println("Context Listener for CoAP server Destroyed");

    }
}
