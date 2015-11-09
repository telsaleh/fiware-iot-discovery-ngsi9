package uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.Association;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistrationAttribute;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.EntityId;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.Value;

public class RegisterStoreAccess {

    public static ObjectContainer db;
    public static String DB4OFILENAME = "";

    public RegisterStoreAccess() {

        DB4OFILENAME = System.getProperty("user.dir") + "/WEB-INF/repository/ngsiRegRepo.db4o";
        //user.home
    }

//    public static void RegisterStoreAccess(ServletContext context) {
//    DB4OFILENAME = context.getRealPath("/repository/ngsiRegRepo.db4o");
//        
//    }
    public static void openDb4o(String repoPath) {

//        DB4OFILENAME = context.getRealPath("/WEB-INF/repository/ngsiRegRepo.db4o");
        DB4OFILENAME = repoPath;
        System.out.println(DB4OFILENAME);
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().activationDepth(20); // set activation depth
        configuration.common().objectClass(RegisterContextRequest.class).cascadeOnDelete(true);
        db = Db4oEmbedded.openFile(configuration, DB4OFILENAME);        
    }

    public static void closeDb4o() {
        try {
            db.close();
        } catch (NullPointerException npe) {
            System.out.println("db already closed: " + DB4OFILENAME);
        }

    }

    public static void deleteDb() {

        new File(DB4OFILENAME).delete();
        System.out.println("db deleted: " + DB4OFILENAME);
    }

    public synchronized static void storeRegistration(RegisterContextRequest regreq) {

        //openDb4o();
        try {
            db.store(regreq);
        } finally {
            db.commit();
            //db.close();
            System.out.println("RegisterContextRequest Stored: " + regreq.getRegistrationId()); 
        }
    }

    public static boolean checkRegIdUsed(final String idGenerated) {

        //openDb4o();
        List<RegisterContextRequest> results;
        //final String updateRegId = rcrNew.getRegistrationId();
        results = db.query(new Predicate<RegisterContextRequest>() {
            public boolean match(RegisterContextRequest req) {
                String storedRegID = req.getRegistrationId();
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

    public static boolean deleteRegistration(final RegisterContextRequest rcrNew) {

        List<RegisterContextRequest> results;
        final String updateRegId = rcrNew.getRegistrationId();

        results = db.query(new Predicate<RegisterContextRequest>() {
            public boolean match(RegisterContextRequest req) {
                String storedRegID = req.getRegistrationId();
                if (storedRegID.equals(updateRegId)) {
                    return true;
                }
                return false;
            }
        });

        if (results.size() > 0) {
            RegisterContextRequest found = results.get(0);
            System.out.println("Deleting registration");
            db.delete(found);
            db.commit();
            return true;
        }
        return false;
    }

    public static ObjectSet<RegisterContextRequest> getAllRegistrations() {

        ObjectSet<RegisterContextRequest> result = db
                .queryByExample(RegisterContextRequest.class);
        // listResult(result);
        return result;
    }

    public static List<RegisterContextRequest> getAssociations(final EntityId eID, final String scopeValueType, final ArrayList<String> discAttrList) {

        List<RegisterContextRequest> results;

        results = db.query(new Predicate<RegisterContextRequest>() {
            public boolean match(RegisterContextRequest req) {

                final int contRegListSize = req.getContextRegistration().size();
                //System.out.println("contRegListSize: " + contRegListSize);
                for (int i = 0; i < contRegListSize; i++) {

                    final int contMetadataListSize = req.getContextRegistration().get(i).getContextMetadata().size();
                    //System.out.println("entityidListSize: " + entityIdListSize);

                    if (contMetadataListSize < 1) {
                        return false;
                    }

                    for (int j = 0; j < contMetadataListSize; j++) {

                        String scopeValueEId = "";
                        ArrayList<String> scopeValueAttr = new ArrayList<>();
                        Value association = (Value) req.getContextRegistration().get(i)
                                .getContextMetadata().get(j).getValue();
                        int attrListSize = association.getAttributeAssociation().size();

                        //check if operation scope is "Association"
                        String scopeField = req.getContextRegistration().get(i)
                                .getContextMetadata().get(j).getType();

                        if (scopeField.equalsIgnoreCase("Association")) {

                            if (scopeValueType.equalsIgnoreCase("SOURCES")) {

                                scopeValueEId = association./*getEntityAssociation().*/getTargetEntityId().getId();
//                                System.out.println("target eid is: " + scopeValueEId);
                                for (int k = 0; k < attrListSize; k++) {
                                    String scopeValueAttrString = association.getAttributeAssociation().get(k).getTargetAttribute();
                                    scopeValueAttr.add(scopeValueAttrString);
                                }

                            } else if (scopeValueType.equalsIgnoreCase("TARGETS")) {

                                scopeValueEId = association./*getEntityAssociation().*/getSourceEntityId().getId();
//                                System.out.println("source eid is: " + scopeValueEId);
                                for (int k = 0; k < attrListSize; k++) {
                                    String scopeValueAttrString = association.getAttributeAssociation().get(k).getSourceAttribute();

                                    scopeValueAttr.add(scopeValueAttrString);
                                }
                            }
//System.out.println("eid is: "+eID.getId());
//System.out.println("scope eid is: "+scopeValueEId);
                            if (scopeValueEId.equalsIgnoreCase(eID.getId())) {
//                                  System.out.println("eid is: "+eID.getId());
                                //int scopeValueAttrSize = scopeValueAttr.size();
                                int discAttrListSize = discAttrList.size();
                                if (discAttrListSize > 0) {
                                    for (int m = 0; m < discAttrListSize; m++) {

                                        if (scopeValueAttr.contains(discAttrList.get(m))) {
                                            return true;
                                        }
                                    }

                                } else {
                                    return true;  //no attributes in request                              
                                }
                            } else {
                                return false;
                            }
                        }
                    }
                }
                return false;
            }
        });

        return results;

    }

    public static List<RegisterContextRequest> getRegByEntityID(
            final EntityId reqEntityId, final ArrayList<String> discAttrList) {

        List<RegisterContextRequest> results;
        
        String reqEId = reqEntityId.getId();
        System.out.println("regId is: "+ reqEId);
        StringBuffer wildcard = new StringBuffer(".*");
        
        if (reqEId.contentEquals(wildcard)&&reqEntityId.isIsPattern())
        {
            System.out.println("\".*\" specified");
            return getRegByEntityType(reqEntityId.getType());
        }        
        System.out.println("\".*\" NOT specified");

        results = db.query(new Predicate<RegisterContextRequest>() {
            public boolean match(RegisterContextRequest req) {

                final int contRegListSize = req
                        .getContextRegistration().size();

                for (int i = 0; i < contRegListSize; i++) {

                    final int reqEIdListSize = req
                            .getContextRegistration().get(i)
                            .getEntityId().size();

                    for (int j = 0; j < reqEIdListSize; j++) {

                        String storedId = req
                                .getContextRegistration().get(i)
                                .getEntityId().get(j).getId();

                        String storedIdType = req
                                .getContextRegistration().get(i)
                                .getEntityId().get(j).getType();

//                        String reqEId = reqEntityId.getId();
                        boolean reqIsPattern;// = false;
                        String reqIdType = "";

                        try {
                            if (reqEntityId.getType() != null) {
                                reqIdType = reqEntityId.getType();
                            }
                        } catch (NullPointerException npe) {
                            System.out.println("'type' field missing in request");
                        }
                        if (!reqIdType.isEmpty()) {

                            if (!reqIdType.equals(storedIdType)) {
                                continue;
                            }
                        }

                        try {
                            reqIsPattern = reqEntityId.isIsPattern();
                        } catch (NullPointerException npe) {
                            System.out.println("'isPattern' field missing in request");
                            reqIsPattern = false;
                        }

                        List<ContextRegistrationAttribute> contRegAttrlist = req
                                .getContextRegistration().get(i).getContextRegistrationAttribute();

                        boolean attrMatch = checkAttrMatch(contRegAttrlist, discAttrList);

                        if (reqIsPattern) {
                            if (reqEId.contains("*")) {
                                if (reqEId.startsWith("*")) {
                                    String[] suffix = reqEId.split("\\*");
//                                       System.out.println("ends with: "+suffix[1]);
//                                       System.out.println("stored: "+storedId);
                                    if ((storedId.endsWith(suffix[1]) && reqIdType.isEmpty()) || (storedId.endsWith(suffix[1]) && storedIdType.equals(reqEntityId.getType()))) {
                                        System.out.println("'isPattern'");
                                        return true;
                                    }
                                } else if (reqEId.endsWith(".*")) {
                                    String[] suffix = reqEId.split("\\.*");
                                    //System.out.println(suffix[0]);
                                    if ((storedId.startsWith(suffix[0]) && reqIdType.isEmpty()) || (storedId.startsWith(suffix[0]) && storedIdType.equals(reqEntityId.getType()))) {
                                        return true;
                                    }
                                }
                            }
                        } else {
                            if (reqIdType.isEmpty() && storedId.equals(reqEntityId.getId())) {
                                return true;
                            } else if (storedIdType.equals(reqEntityId.getType()) && storedId.equals(reqEntityId.getId())) {
                                return true;
                            }
                        }//if not regIsPattern
                    }
                }//loop until true or end
                return false;
            }
        });
        return results;
    }

    static public boolean checkAttrMatch(List<ContextRegistrationAttribute> cral, ArrayList<String> discAttrList) {

        int attributeListSize = cral.size();

        //if no attributes in list
        if (attributeListSize < 1) {
            return true;
        }

        for (int k = 0; k < attributeListSize; k++) {

            String attributeCheck = cral.get(k).getName();

            if (discAttrList.contains(attributeCheck)) {
                return true;
            }

        }

        return false;

    }

    static public List<RegisterContextRequest> getRegByEntityType(
            final String entityType) {

        System.out.println("get by type: "+ entityType);
        List<RegisterContextRequest> results;

        results = db.query(new Predicate<RegisterContextRequest>() {
            public boolean match(RegisterContextRequest req) {

                final int contRegListSize = req
                        .getContextRegistration().size();
//                System.out.println("contRegListSize: " + contRegListSize);
                for (int i = 0; i < contRegListSize; i++) {

                    final int entityIdListSize = req
                            .getContextRegistration().get(i)
                            .getEntityId().size();
//                    System.out.println("entityidListSize: " + entityIdListSize);

                    for (int j = 0; j < entityIdListSize; j++) {

                        String storedEType = req
                                .getContextRegistration().get(i)
                                .getEntityId().get(j)
                                .getType();

                        //return storedEType.equals(entityType);
                        if (storedEType.equals(entityType)) {
                            return true;
                        }
                    }
                }//contRegList loop
                return false;
            }
        });
        System.out.println("size of \"type\" result:" + results.size());
        return results;
    }

    public static void listResult(List<RegisterContextRequest> result) {
        System.out.println(result.size());
        for (int i = 1; i <= result.size(); i++) {
            System.out.println(result.get(i)
                    .getContextRegistration().get(i - 1)
                    .getEntityId().get(i - 1).getId());
        }
    }
}
