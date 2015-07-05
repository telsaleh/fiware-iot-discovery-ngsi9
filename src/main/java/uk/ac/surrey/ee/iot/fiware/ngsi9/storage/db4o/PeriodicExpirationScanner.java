/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author te0003
 */
public class PeriodicExpirationScanner implements ServletContextListener {
    
    Thread expirationScanner ;//= new Thread(new ExpirationScanner());
    protected static volatile boolean shutdownCall = false;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        expirationScanner = new Thread(new ExpirationScanner());
        expirationScanner.start();
        
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
        shutdownCall = true;
        try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            //expirationScanner.join(10000);
            expirationScanner.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(PeriodicExpirationScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
