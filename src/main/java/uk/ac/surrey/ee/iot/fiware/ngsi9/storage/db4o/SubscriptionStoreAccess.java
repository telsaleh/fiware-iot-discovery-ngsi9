package uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o;

import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.SubscribeContextAvailabilityRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.UpdateContextAvailabilitySubscriptionRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.EntityId;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistrationAttribute;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import java.io.File;
import java.util.ArrayList;

public class SubscriptionStoreAccess {

    public static ObjectContainer db;
    public static String DB4OFILENAME = "";

    public SubscriptionStoreAccess() {
    }

//    public SubscriptionStoreAccess(ServletContext context) {
//
//        DB4OFILENAME = context.getRealPath("/repository/ngsiSubRepo.db4o");
//        //user.home
//
//    }
    public static void openDb4o(String repoPath) {

//        DB4OFILENAME = context.getRealPath("/WEB-INF/repository/ngsiSubRepo.db4o");
        DB4OFILENAME = repoPath;
        System.out.println(DB4OFILENAME);
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().activationDepth(15); // set activation depth
        configuration.common().objectClass(RegisterContextRequest.class).cascadeOnDelete(true);
        db = Db4oEmbedded.openFile(configuration, DB4OFILENAME);
    }

    public static void closeDb4o() {
        db.close();

    }

    public static void deleteDb() {

        new File(DB4OFILENAME).delete();
        System.out.println("db deleted: " + DB4OFILENAME);
    }

    public static void storeSubscription(SubscribeContextAvailabilityRequest subsReq) {

        //openDb4o();
        try {
            db.store(subsReq);
        } finally {
//            db.close();
            db.commit();
            System.out.println("Stored SubscribeContextAvailabilityRequest: " + subsReq.getSubscriptionId());
        }
    }

    public static boolean deleteSubscription(final String subsID) {
        List<SubscribeContextAvailabilityRequest> results;
        //final String updateSubId = rcrNew.getSubscriptionId();
        results = db.query(new Predicate<SubscribeContextAvailabilityRequest>() {
            public boolean match(SubscribeContextAvailabilityRequest req) {
                String storedRegID = req.getSubscriptionId();
                return storedRegID.equals(subsID);
            }
        });
        if (results.size() > 0) {
            SubscribeContextAvailabilityRequest found = results.get(0);
            System.out.println("Deleting SubscribeContextAvailabilityRequest");
            //found.getReference();
            db.delete(found);
            db.commit();
            return true;
        }
        return false;
    }

    public static boolean updateSubscription(final UpdateContextAvailabilitySubscriptionRequest updSubReq) {
        List<SubscribeContextAvailabilityRequest> results;
        //final String updateSubId = rcrNew.getSubscriptionId();
        results = db.query(new Predicate<SubscribeContextAvailabilityRequest>() {
            public boolean match(SubscribeContextAvailabilityRequest req) {
                String storedRegID = req.getSubscriptionId();
                return storedRegID.equals(updSubReq.getSubscriptionId());
            }
        });
        if (results.size() > 0) {
            SubscribeContextAvailabilityRequest subReqFound = results.get(0);
            System.out.println("Updating SubscribeContextAvailabilityRequest");

            subReqFound.getEntityId().clear();
            subReqFound.getEntityId().addAll(updSubReq.getEntityId());
            subReqFound.getAttribute().clear();
            subReqFound.getAttribute().addAll(updSubReq.getAttribute());
            subReqFound.setRestriction(updSubReq.getRestriction());
            subReqFound.setDuration(updSubReq.getDuration());

            try {
                db.store(subReqFound);
                db.commit();
            } finally {
                System.out.println("Stored SubscribeContextAvailabilityRequest: " + subReqFound.getSubscriptionId());
            }
            return true;
        }
        return false;
    }

    public static List<SubscribeContextAvailabilityRequest> matchRegReqToSubsStore(final RegisterContextRequest regReq) {

        List<SubscribeContextAvailabilityRequest> results;

        ArrayList<EntityId> regEIdList = new ArrayList<EntityId>();
        ArrayList<ContextRegistrationAttribute> regAttrList = new ArrayList<ContextRegistrationAttribute>();

        final int contRegListSize = regReq
                .getContextRegistration()
                .size();
        //System.out.println("contRegListSize: " + contRegListSize);
        for (int i = 0; i < contRegListSize; i++) {

            //get Entities
            int entityIdListSize = regReq
                    .getContextRegistration()
                    .get(i)
                    
                    .getEntityId()
                    .size();

            //System.out.println("entityidListSize: " + entityIdListSize);
            for (int j = 0; j < entityIdListSize; j++) {

                regEIdList.add(regReq
                        .getContextRegistration()
                        .get(i)
                        
                        .getEntityId()
                        .get(j));
            }

            //get attributes
            int attributeListSize = regReq
                    .getContextRegistration()
                    .get(i)
                    
                    .getContextRegistrationAttribute()
                    .size();
            for (int k = 0; k < attributeListSize; k++) {
                regAttrList.add(regReq
                        .getContextRegistration()
                        .get(i)
                        
                        .getContextRegistrationAttribute()
                        .get(k));
            }
        }

        final ArrayList<EntityId> regEIdListQuery = regEIdList;
        final ArrayList<ContextRegistrationAttribute> regAttrListQuery = regAttrList;

        //openDb4o();
        //perform query
        results = db.query(new Predicate<SubscribeContextAvailabilityRequest>() {
            public boolean match(SubscribeContextAvailabilityRequest subReq) {

                int subEIdListSize = subReq.getEntityId().size();
                int subAttrListSize = subReq.getAttribute().size();

                boolean eIdMatch = false;
                boolean attrMatch = false;

                for (int i = 0; i < subEIdListSize; i++) {

                    String subEIdType = subReq.getEntityId().get(i).getType();
                    boolean subEIdIsPattern = subReq.getEntityId().get(i).isIsPattern();
                    String subEId = subReq.getEntityId().get(i).getId();

                    for (int j = 0; j < regEIdListQuery.size(); j++) {

                        if (regEIdListQuery.get(j).getType().equals(subEIdType)) {
                            if (regEIdListQuery.get(j).isIsPattern().equals(subEIdIsPattern)) {
                                if (subEIdIsPattern) {
                                    if (regEIdListQuery.get(j).getId().contains(subEId)) {
                                        eIdMatch = true;
                                        break;
                                    }
                                } else {
                                    if (regEIdListQuery.get(j).getId().equals(subEId)) {
                                        eIdMatch = true;
                                        break;
                                    }
                                }
                            }
                        }
                    } //end j loop

                }
                //get attribute!!
                for (int k = 0; k < subAttrListSize; k++) {
                    String attr = subReq.getAttribute().get(k);
                    for (int m = 0; m < regAttrListQuery.size(); m++) {
                        if (regAttrListQuery.get(k).getName().equals(attr)) {
                            attrMatch = true;
                        }

                    }

                }//end k loop

                if (eIdMatch && attrMatch) {
                    return true;
                } else {
                    System.out.println("No subscription match found");
                    return false;
                }
            }
        });

        return results;

    }//matchSubscriptions()
    
    
    public static boolean checkSubIdUsed(final String idGenerated) {

        //openDb4o();
        List<SubscribeContextAvailabilityRequest> results;
        //final String updateRegId = rcrNew.getRegistrationId();
        results = db.query(new Predicate<SubscribeContextAvailabilityRequest>() {
            public boolean match(SubscribeContextAvailabilityRequest req) {
                String storedRegID = req.getSubscriptionId();
                if (storedRegID.equals(idGenerated)) {
                    return true;
                }
                return false;
            }
        });

        if (results.size() > 0) {
            // closeDb4o();
            return true;
        }
        //closeDb4o();
        return false;
    }

    //    public List<RegisterContextRequest> matchSubReqToRegStore(ServletContext context, final SubscribeContextAvailabilityRequest subsReq) {
//
//        List<RegisterContextRequest> results;
//
//        ArrayList<EntityId> subsEIdList = new ArrayList<EntityId>();
//        ArrayList<String> subsAttrList = new ArrayList<String>();
//
//        //get Entities
//        int entityIdListSize = subsReq.getEntityId().size();
//
//        //System.out.println("entityidListSize: " + entityIdListSize);
//        for (int j = 0; j < entityIdListSize; j++) {
//
//            subsEIdList.add(subsReq
//                    .getEntityId()
//                    .get(j));
//        }
//
//        //get attributes
////        subsAttrList.add(subsReq.getAttributeList());
//        int attributeListSize = subsReq.getAttributeList()
//                .getAttribute()
//                .size();
//        for (int k = 0; k < attributeListSize; k++) {
//            subsAttrList.add(subsReq.getAttributeList().getAttribute().get(k));
//        }
//
//        RegisterStoreAccess regStore = new RegisterStoreAccess(context);
//
//        for (int i = 0; i < entityIdListSize; i++) {
//
//            List<RegisterContextRequest> regResults = regStore.getRegByEntityID(subsEIdList.get(i));
//
//            ContextRegistrationResponseList crrl = new ContextRegistrationResponseList();
//            ContextRegistrationResponse crr = new ContextRegistrationResponse();
//            NotifyContextAvailabilityRequest ncar = new NotifyContextAvailabilityRequest();
//
//            crr = crr = regStore.getContRegRespContainsEIdAttribute(regResults, subsEIdList.get(i), subsAttrList);
//            
//            try {
//                            crr.getContextRegistration();
//                            crrl.getContextRegistrationResponse().add(crr);
//                            ncar.setContextRegistrationResponseList(crrl);
//                            ncar = regStore.removeSharedEntityID(ncar, subsEIdList);
//                        } catch (Exception e) {
//                            System.out.println(e.getMessage());
//                        }
//
//        }
//
//    }
}//class
