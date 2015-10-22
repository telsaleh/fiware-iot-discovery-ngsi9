/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o;

import com.db4o.query.Predicate;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.SubscribeContextAvailabilityRequest;

/**
 *
 * @author te0003
 */
public class ExpirationScanner implements Runnable {

    @Override
    public void run() {

        long scanInterval = 5;
        boolean scanned = false;

        do {
            LocalTime lt = LocalTime.now();
            if (lt.isAfter(LocalTime.MIDNIGHT) && lt.isBefore(LocalTime.MIDNIGHT.plusSeconds(scanInterval))) {
                if (!scanned){
                deleteExpiredRegistrations();
                scanned = true;
                }                
            } else {
                scanned = false;
            }
        } while (!PeriodicExpirationScanner.shutdownCall);
    }

    public boolean deleteExpiredRegistrations() {

        List<RegisterContextRequest> results;

        results = RegisterStoreAccess.db.query(new Predicate<RegisterContextRequest>() {
            @Override
            public boolean match(RegisterContextRequest req) {
                Instant timestamp = req.getTimestamp();
                Duration duration = Duration.parse(req.getDuration());
                Instant expiry = timestamp.plus(duration);
                Instant now = Instant.now();
                boolean expired = now.isAfter(expiry);
                //if timestamp + duration isAfter time now
                return expired;
            }
        });
        if (results.size() > 0) {
            for (RegisterContextRequest found : results) {
                System.out.println("Deleting expired registration");
                RegisterStoreAccess.db.delete(found);
                RegisterStoreAccess.db.commit();
            }

            return true;
        }
        return false;
    }

    public boolean deleteExpiredSubscriptions() {

        List<SubscribeContextAvailabilityRequest> results;

        results = SubscriptionStoreAccess.db.query(new Predicate<SubscribeContextAvailabilityRequest>() {
            @Override
            public boolean match(SubscribeContextAvailabilityRequest req) {
                Instant timestamp = req.getTimestamp();
                Duration duration = Duration.parse(req.getDuration());
                Instant expiry = timestamp.plus(duration);
                Instant now = Instant.now();
                boolean expired = now.isAfter(expiry);
                //if timestamp + duration isAfter time now
                return expired;
            }
        });

        if (results.size() > 0) {
            for (SubscribeContextAvailabilityRequest found : results) {
                System.out.println("Deleting expired registration");
                SubscriptionStoreAccess.db.delete(found);
                SubscriptionStoreAccess.db.commit();
            }
            return true;
        }
        return false;
    }

}
