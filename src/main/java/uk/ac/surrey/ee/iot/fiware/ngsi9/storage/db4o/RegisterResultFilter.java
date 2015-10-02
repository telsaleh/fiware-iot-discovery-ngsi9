/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.storage.db4o;

import java.util.ArrayList;
import java.util.List;

import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistration;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistrationResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.ContextRegistrationResponseList;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityResponse;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.EntityId;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.NotifyContextAvailabilityRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;

/**
 *
 * @author te0003
 */
public class RegisterResultFilter {

    public RegisterResultFilter() {
    }

    public ContextRegistrationResponse getContRegHasEntityId(
            List<RegisterContextRequest> result, String eId) {
        ContextRegistrationResponse crr = new ContextRegistrationResponse();

        int resultListSize = result.size();
        for (int i = 0; i < resultListSize; i++) {

            int contRegListSize = result.get(i).getContextRegistration().size();
            for (int j = 0; j < contRegListSize; j++) {

                boolean eidFound = false;
                int entityIdListSize = result.get(i).getContextRegistration()
                        .get(j).getEntityId().size();
                for (int k = 0; k < entityIdListSize; k++) {

                    String eIdCheck = result.get(i)
                            .getContextRegistration().get(j)
                            .getEntityId().get(k).getId();
                    if (eIdCheck.equals(eId)) {
                        eidFound = true;
                        break;
                    }
                }
                if (eidFound) {
                    ContextRegistration regContReg = result.get(i)
                            .getContextRegistration().get(j);

                    crr.setContextRegistration(regContReg);
                    return crr;
                }
            }
        }
        return crr;
    }

    //ContextRegistrationResponse elements that match with the attributeList
    public void getCrrContainsAttr(
            List<RegisterContextRequest> result, ArrayList<String> attr, List<ContextRegistrationResponse> crrl) {

        int resultListSize = result.size();
        
        for (int i = 0; i < resultListSize; i++) {

            int contRegListSize = result.get(i).getContextRegistration().size();
            for (int j = 0; j < contRegListSize; j++) {

                boolean attributeFound = false;
                int attributeListSize = result.get(i)
                        .getContextRegistration()
                        .get(j)
                        .getContextRegistrationAttribute().size();
                for (int k = 0; k < attributeListSize; k++) {

                    String attributeCheck = result.get(i)
                            
                            .getContextRegistration().get(j)
                            
                            .getContextRegistrationAttribute().get(k).getName();

                    //if (attributeCheck.equals(attr)) {
                    if (attr.contains(attributeCheck)) {
                        attributeFound = true;
                        break;
                    }
                }
                if (attributeFound || (attr.size() < 1)) {
                    ContextRegistrationResponse crr = new ContextRegistrationResponse();
                	
                    ContextRegistration regCr = result.get(i)
                            
                            .getContextRegistration().get(j);

                    crr.setContextRegistration(regCr);
                	
                    crrl.add(crr);
                }

            }
        }

    }
    
    //returns ContextRegistrationResponse for Standard Operation
    public ContextRegistrationResponse getCrrContainsEIdAttr(
            List<RegisterContextRequest> result, EntityId eId, ArrayList<String> attr) {

        ContextRegistrationResponse crr = new ContextRegistrationResponse();

        int resultListSize = result.size();
        
        for (int i = 0; i < resultListSize; i++) {

            int contRegListSize = result.get(i).getContextRegistration().size();
            for (int j = 0; j < contRegListSize; j++) {

                boolean eIdFound = false;
                int entityIdListSize = result.get(i)
                        .getContextRegistration()
                        .get(j).getEntityId().size();
                for (int k = 0; k < entityIdListSize; k++) {

                	String eIdCheck = result.get(i)
                                                        .getContextRegistration().get(j)
                            .getEntityId().get(k).getId();
                    if (eIdCheck.equals(eId.getId())) {
                        eIdFound = true;
                        break;
                    }
                }

                boolean attributeFound = false;
                int attributeListSize = result.get(i)
                        .getContextRegistration()
                        .get(j)
                        .getContextRegistrationAttribute().size();
                for (int k = 0; k < attributeListSize; k++) {

                    String attributeCheck = result.get(i)
                            
                            .getContextRegistration().get(j)
                            
                            .getContextRegistrationAttribute().get(k).getName();

                    //if (attributeCheck.equals(attr)) {
                    if (attr.contains(attributeCheck)) {
                        attributeFound = true;
                        break;
                    }
                }
                if ((eIdFound && attributeFound) || (eIdFound && attr.size() < 1)) {
                    ContextRegistration regCr = result.get(i)
                            
                            .getContextRegistration().get(j);

                    crr.setContextRegistration(regCr);
                	
                    return crr;
                }

            }
        }
        return crr;

    }

    public ContextRegistrationResponse getContRegHasEntityIdAttrDomain(
            List<RegisterContextRequest> result, String eId, String attr) {

        ContextRegistrationResponse crr = new ContextRegistrationResponse();

        int resultListSize = result.size();
        for (int i = 0; i < resultListSize; i++) {

            int contRegListSize = result.get(i)
                    .getContextRegistration().size();
            for (int j = 0; j < contRegListSize; j++) {

                boolean eIdFound = false;
                int entityIdListSize = result.get(i)
                        .getContextRegistration()
                        .get(j).getEntityId().size();
                for (int k = 0; k < entityIdListSize; k++) {

                    String eIdCheck = result.get(i)
                            
                            .getContextRegistration().get(j)
                            .getEntityId().get(k).getId();
                    if (eIdCheck.equals(eId)) {
                        eIdFound = true;
                        break;
                    }
                }

                boolean attrDomainFound = false;
                int attributeListSize = result.get(i)
                        .getContextRegistration()
                        .get(j)
                        .getContextRegistrationAttribute().size();
                for (int k = 0; k < attributeListSize; k++) {

                    String attrDomainCheck = result.get(i)
                            
                            .getContextRegistration().get(j)
                            
                            .getContextRegistrationAttribute().get(k).getName();
                    boolean isAttrDomain = result.get(i)
                            
                            .getContextRegistration().get(j)
                            
                            .getContextRegistrationAttribute().get(k)
                            .isIsDomain();
                    if (attrDomainCheck.equals(attr) && isAttrDomain) {
                        attrDomainFound = true;
                        break;
                    }
                }
                if (eIdFound && attrDomainFound) {
                    ContextRegistration regCr = result.get(i)
                            
                            .getContextRegistration().get(j);
                    crr.setContextRegistration(regCr);
                }
            }
        }
        return crr;
    }

//    public ContextRegistrationResponseList getContRegHasEntityIdAttrDomain(
//            List<RegisterContextRequest> result, String eId, String attr) {
//
//        ContextRegistrationResponseList crrl = new ContextRegistrationResponseList();
//
//        int resultListSize = result.size();
//        for (int i = 0; i < resultListSize; i++) {
//
//            int contRegListSize = result.get(i)
//                    .getContextRegistration().size();
//            for (int j = 0; j < contRegListSize; j++) {
//
//                boolean eIdFound = false;
//                int entityIdListSize = result.get(i)
//                        .getContextRegistration()
//                        .get(j).getEntityId().size();
//                for (int k = 0; k < entityIdListSize; k++) {
//
//                    String eIdCheck = result.get(i)
//                            
//                            .getContextRegistration().get(j)
//                            .getEntityId().get(k).getId();
//                    if (eIdCheck.equals(eId)) {
//                        eIdFound = true;
//                        break;
//                    }
//                }
//
//                boolean attrDomainFound = false;
//                int attributeListSize = result.get(i)
//                        .getContextRegistration()
//                        .get(j)
//                        .getContextRegistrationAttribute().size();
//                for (int k = 0; k < attributeListSize; k++) {
//
//                    String attrDomainCheck = result.get(i)
//                            
//                            .getContextRegistration().get(j)
//                            
//                            .getContextRegistrationAttribute().get(k).getName();
//                    boolean isAttrDomain = result.get(i)
//                            
//                            .getContextRegistration().get(j)
//                            
//                            .getContextRegistrationAttribute().get(k)
//                            .isIsDomain();
//                    if (attrDomainCheck.equals(attr) && isAttrDomain) {
//                        attrDomainFound = true;
//                        break;
//                    }
//                }
//                if (eIdFound && attrDomainFound) {
//                    ContextRegistration regCr = result.get(i)
//                            
//                            .getContextRegistration().get(j);
//                    ContextRegistrationResponse crr = new ContextRegistrationResponse();
//                    crr.setContextRegistration(regCr);
//                    crrl.getContextRegistrationResponse().add(crr);
//                    // return crrl;
//                }
//            }
//        }
//        return crrl;
//    }
    public ContextRegistrationResponseList getContRegHasEntityType(
            List<RegisterContextRequest> result, String eIdType) {
        ContextRegistrationResponseList crrl = new ContextRegistrationResponseList();

        int resultListSize = result.size();
        for (int i = 0; i < resultListSize; i++) {

            int contRegListSize = result.get(i)
                    .getContextRegistration().size();
            for (int j = 0; j < contRegListSize; j++) {

                boolean eidTypeFound = false;
                int entityIdListSize = result.get(i)
                        .getContextRegistration()
                        .get(j).getEntityId().size();
                for (int k = 0; k < entityIdListSize; k++) {

                    String eIdTypeCheck = result.get(i)
                            
                            .getContextRegistration().get(j)
                            .getEntityId().get(k).getType();
                    if (eIdTypeCheck.equals(eIdType)) {
                        eidTypeFound = true;
                        break;
                    }
                }

                if (eidTypeFound) {
                    ContextRegistration regCr = result.get(i)
                            
                            .getContextRegistration().get(j);
                    ContextRegistrationResponse crr = new ContextRegistrationResponse();
                    crr.setContextRegistration(regCr);
                    crrl.getContextRegistrationResponse().add(crr);
                    // return crrl;
                }
            }
        }
        return crrl;
    }

    public ContextRegistrationResponseList getContRegHasEntityTypeAttr(
            List<RegisterContextRequest> result, String eIdType, String attr) {
        ContextRegistrationResponseList crrl = new ContextRegistrationResponseList();

        int resultListSize = result.size();
        for (int i = 0; i < resultListSize; i++) {

            int contRegListSize = result.get(i)
                    .getContextRegistration().size();
            for (int j = 0; j < contRegListSize; j++) {

                boolean eidTypeFound = false;
                int entityIdListSize = result.get(i)
                        .getContextRegistration()
                        .get(j).getEntityId().size();
                for (int k = 0; k < entityIdListSize; k++) {

                    String eIdTypeCheck = result.get(i)
                            
                            .getContextRegistration().get(j)
                            .getEntityId().get(k).getType();
                    if (eIdTypeCheck.equals(eIdType)) {
                        eidTypeFound = true;
                        break;
                    }
                }

                boolean attributeFound = false;
                int attributeListSize = result.get(i)
                        .getContextRegistration()
                        .get(j)
                        .getContextRegistrationAttribute().size();
                for (int k = 0; k < attributeListSize; k++) {

                    String attributeCheck = result.get(i)
                            
                            .getContextRegistration().get(j)
                            
                            .getContextRegistrationAttribute().get(k).getName();
                    if (attributeCheck.equals(attr)) {
                        attributeFound = true;
                        break;
                    }
                }
                if (eidTypeFound && attributeFound) {
                    ContextRegistration regCr = result.get(i)
                            
                            .getContextRegistration().get(j);
                    ContextRegistrationResponse crr = new ContextRegistrationResponse();
                    crr.setContextRegistration(regCr);
                    crrl.getContextRegistrationResponse().add(crr);
                    // return crrl;
                }
            }
        }
        return crrl;
    }

    public ContextRegistrationResponseList getContRegContainsETypeAttrDomain(
            List<RegisterContextRequest> result, String eIdType,
            String attrDomain) {
        ContextRegistrationResponseList crrl = new ContextRegistrationResponseList();

        int resultListSize = result.size();
        for (int i = 0; i < resultListSize; i++) {

            int contRegListSize = result.get(i)
                    .getContextRegistration().size();
            for (int j = 0; j < contRegListSize; j++) {

                boolean eidTypeFound = false;
                int entityIdListSize = result.get(i)
                        .getContextRegistration()
                        .get(j).getEntityId().size();
                for (int k = 0; k < entityIdListSize; k++) {

                    String eIdTypeCheck = result.get(i)
                            
                            .getContextRegistration().get(j)
                            .getEntityId().get(k).getType();
                    if (eIdTypeCheck.equals(eIdType)) {
                        eidTypeFound = true;
                        break;
                    }
                }

                boolean attributeFound = false;
                int attributeListSize = result.get(i)
                        .getContextRegistration()
                        .get(j)
                        .getContextRegistrationAttribute().size();

                for (int k = 0; k < attributeListSize; k++) {

                    String attributeCheck = result.get(i)
                            
                            .getContextRegistration().get(j)
                            
                            .getContextRegistrationAttribute().get(k).getName();
                    boolean isAttrDomain = result.get(i)
                            
                            .getContextRegistration().get(j)
                            
                            .getContextRegistrationAttribute().get(k)
                            .isIsDomain();
                    if (attributeCheck.equals(attrDomain) && isAttrDomain) {
                        attributeFound = true;
                        break;
                    }
                }
                if (eidTypeFound && attributeFound) {
                    ContextRegistration regCr = result.get(i)
                            
                            .getContextRegistration().get(j);
                    ContextRegistrationResponse crr = new ContextRegistrationResponse();
                    crr.setContextRegistration(regCr);
                    crrl.getContextRegistrationResponse().add(crr);
                }
            }
        }
        return crrl;
    }

    public ContextRegistrationResponse removeSharedEntityID(
            ContextRegistrationResponse contRegResp, ArrayList<EntityId> discEIdList) {

        int discEIdListSize = discEIdList.size();
        ArrayList<String> eIdStringList = new ArrayList<String>();
        for (int i = 0; i < discEIdListSize; i++) {
            eIdStringList.add(discEIdList.get(i).getId());
        }
        int entityIdListSize = contRegResp.getContextRegistration().getEntityId()
                .size();

        //iterate through Entity IDs
        for (int k = 0; k < entityIdListSize; k++) {
            String eIdCheck = contRegResp
                    .getContextRegistration()
                    .getEntityId().get(k).getId();

            if (!eIdStringList.contains(eIdCheck)) {
                //remove Entity ID
                contRegResp.getContextRegistration()
                        .getEntityId().remove(k);
                entityIdListSize--;
                k--;
            }
        }
        return contRegResp;
    }

//    public DiscoveryContextAvailabilityResponse removeSharedEntityID(
//            DiscoveryContextAvailabilityResponse discContResp, ArrayList<EntityId> discEIdList) {
//
//        int discEIdListSize = discEIdList.size();
//        ArrayList<String> eIdStringList = new ArrayList<String>();
//
//        for (int i = 0; i < discEIdListSize; i++) {
//            eIdStringList.add(discEIdList.get(i).getId());
//        }
//
//        int contRegRespListSize = discContResp
//                
//                .getContextRegistrationResponse().size();
//
//        //iterate through contextRegistrationResponseLists
//        for (int j = 0; j < contRegRespListSize; j++) {
//
//            int entityIdListSize = discContResp
//                    
//                    .getContextRegistrationResponse().get(j)
//                    .getContextRegistration().getEntityId()
//                    .size();
//
//            //iterate through Entity IDs
//            for (int k = 0; k < entityIdListSize; k++) {
//
//                String eIdCheck = discContResp
//                        
//                        .getContextRegistrationResponse().get(j)
//                        .getContextRegistration()
//                        .getEntityId().get(k).getId();
//
//                if (!eIdStringList.contains(eIdCheck)) {
//                    //remove Entity ID
//                    discContResp
//                            .getContextRegistrationResponse().get(j)
//                            .getContextRegistration()
//                            .getEntityId().remove(k);
//                    entityIdListSize--;
//                    k--;
//                }
//
//            }
//        }
//        return discContResp;
//    }
    public NotifyContextAvailabilityRequest removeSharedEntityID(
            NotifyContextAvailabilityRequest notifyContResp, ArrayList<EntityId> discEIdList) {

        int discEIdListSize = discEIdList.size();
        ArrayList<String> eIdStringList = new ArrayList<String>();

        for (int i = 0; i < discEIdListSize; i++) {
            eIdStringList.add(discEIdList.get(i).getId());
        }

        int contRegRespListSize = notifyContResp
                
                .getContextRegistrationResponse().size();

        //iterate through contextRegistrationResponseLists
        for (int j = 0; j < contRegRespListSize; j++) {

            int entityIdListSize = notifyContResp
                    
                    .getContextRegistrationResponse().get(j)
                    .getContextRegistration().getEntityId()
                    .size();

            //iterate through Entity IDs
            for (int k = 0; k < entityIdListSize; k++) {

                String eIdCheck = notifyContResp
                        
                        .getContextRegistrationResponse().get(j)
                        .getContextRegistration()
                        .getEntityId().get(k).getId();

                if (!eIdStringList.contains(eIdCheck)) {
                    //remove Entity ID
                    notifyContResp
                            .getContextRegistrationResponse().get(j)
                            .getContextRegistration()
                            .getEntityId().remove(k);
                    entityIdListSize--;
                    k--;
                }

            }
        }
        return notifyContResp;
    }

    public DiscoveryContextAvailabilityResponse removeSharedEntityType(
            DiscoveryContextAvailabilityResponse discContResp, String eType) {
        // TODO Auto-generated method stub

        int contRegRespListSize = discContResp
                
                .getContextRegistrationResponse().size();
        for (int j = 0; j < contRegRespListSize; j++) {

            int entityIdListSize = discContResp
                    
                    .getContextRegistrationResponse().get(j)
                    .getContextRegistration().getEntityId()
                    .size();
            for (int k = 0; k < entityIdListSize; k++) {

                String eTypeCheck = discContResp
                        
                        .getContextRegistrationResponse().get(j)
                        .getContextRegistration()
                        .getEntityId().get(k).getType();
                if (!eTypeCheck.equals(eType)) {
                    discContResp
                            .getContextRegistrationResponse().get(j)
                            .getContextRegistration()
                            .getEntityId().remove(k);
                    entityIdListSize--;
                    k--;
                }
            }
        }
        return discContResp;
    }
    
    //remove EntityId that doesn't match the list of types eTypes
    public void removeSharedEntityType(
    		List<ContextRegistrationResponse> crrl, List<String> eTypes) {
        // TODO Auto-generated method stub

        int contRegRespListSize = crrl.size();
        for (int j = 0; j < contRegRespListSize; j++) {

            int entityIdListSize = crrl.get(j)
                    .getContextRegistration().getEntityId()
                    .size();
            for (int k = 0; k < entityIdListSize; k++) {

                String eTypeCheck = crrl.get(j)
                        .getContextRegistration()
                        .getEntityId().get(k).getType();
                if (!eTypes.contains(eTypeCheck)) {
                	crrl.get(j)
                            .getContextRegistration()
                            .getEntityId().remove(k);
                    entityIdListSize--;
                    k--;
                }
            }
        }
    }

    public DiscoveryContextAvailabilityResponse removeSharedAttribute(
            DiscoveryContextAvailabilityResponse discContResp, ArrayList<String> discAttributeList) {

        int contRegRespListSize = discContResp
                
                .getContextRegistrationResponse().size();
        for (int j = 0; j < contRegRespListSize; j++) {

            int attributeListSize = discContResp
                    
                    .getContextRegistrationResponse().get(j)
                    .getContextRegistration()
                    
                    .getContextRegistrationAttribute().size();

            for (int k = 0; k < attributeListSize; k++) {

                String attributeCheck = discContResp
                        
                        .getContextRegistrationResponse().get(j)
                        .getContextRegistration()
                        .getContextRegistrationAttribute()
                        .get(k).getName();
//                if (!attributeCheck.equals(attribute)) {
                if (!discAttributeList.contains(attributeCheck)) {
                    //does attribute array contain this attribute from result
                    discContResp
                            .getContextRegistrationResponse().get(j)
                            .getContextRegistration()
                            .getContextRegistrationAttribute().remove(k);
                    attributeListSize--;
                    k--;
                }
            }
        }
        return discContResp;
    }

    public DiscoveryContextAvailabilityResponse removeSharedAttrDomain(
            DiscoveryContextAvailabilityResponse discContResp, String attribute) {

        int contRegRespListSize = discContResp
                
                .getContextRegistrationResponse().size();
        for (int j = 0; j < contRegRespListSize; j++) {

            int attrDomainListSize = discContResp
                    
                    .getContextRegistrationResponse().get(j)
                    .getContextRegistration()
                    
                    .getContextRegistrationAttribute().size();

            for (int k = 0; k < attrDomainListSize; k++) {

                String attributeCheck = discContResp
                        
                        .getContextRegistrationResponse().get(j)
                        .getContextRegistration().getContextRegistrationAttribute()
                        .get(k).getName();
                boolean attrDomainCheck = discContResp
                        
                        .getContextRegistrationResponse().get(j)
                        .getContextRegistration().getContextRegistrationAttribute()
                        .get(k).isIsDomain();
                if (!(attributeCheck.equals(attribute) && attrDomainCheck)) {
                    discContResp
                            .getContextRegistrationResponse().get(j)
                            .getContextRegistration()
                            .getEntityId().remove(k);
                    attrDomainListSize--;
                    k--;
                }
            }
        }
        return discContResp;
    }

}
