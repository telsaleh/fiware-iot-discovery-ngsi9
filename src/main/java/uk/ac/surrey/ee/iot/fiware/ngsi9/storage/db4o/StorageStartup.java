/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author te0003
 */
public class StorageStartup implements ServletContextListener {
    
    public static ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        servletContext = sce.getServletContext();

        Path repoPath = Paths.get(sce.getServletContext().getInitParameter("repository_path"));

        String finalRepoPath = "";

        if (Files.exists(repoPath, LinkOption.NOFOLLOW_LINKS)
                && Files.isDirectory(repoPath, LinkOption.NOFOLLOW_LINKS)
                && Files.isReadable(repoPath)
                && Files.isWritable(repoPath)) {
            finalRepoPath = repoPath.toString();
            System.out.println("repository_path in web.xml is valid");
        } else {
//            finalRepoPath = System.getProperty("user.home"+"/ngsi9_repository");
            System.out.println("repository_path in web.xml not workable");            
            finalRepoPath = sce.getServletContext().getRealPath("/WEB-INF/repository");
            System.out.println("repository_path will be instead: "+finalRepoPath);
        }

        System.out.println("Opening Db4o database for Registration");
        RegisterStoreAccess.openDb4o(finalRepoPath+"/ngsiRegRepo.db4o");

        System.out.println("Opening Db4o database for Subscription");
        SubscriptionStoreAccess.openDb4o(finalRepoPath+"/ngsiSubRepo.db4o");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Closing Db4o database for Registration");
        RegisterStoreAccess.closeDb4o();

        System.out.println("Closing Db4o database for Subscription");
        SubscriptionStoreAccess.closeDb4o();
    }
}
