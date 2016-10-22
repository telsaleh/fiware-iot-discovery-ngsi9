/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import uk.ac.surrey.ee.iot.fiware.ngsi9.semantic.*;
import static com.hp.hpl.jena.assembler.JA.OntModel;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.time.Instant;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import uk.ac.surrey.ee.iot.fiware.ngsi9.op.standard.Resource01_ContextRegistration;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.StatusCode;

/**
 *
 * @author te0003
 */
public class SemanticConverter {

//    public String ONT_URL = "http://iot.ee.surrey.ac.uk/fiware/ontologies/iot-lite.owl#";
//    public String ONT_URL = "http://www.surrey.ac.uk/ccsr/ontologies/ServiceModel.owl#";
    public String ONT_URL = "http://purl.oclc.org/NET/UNIS/iot-lite/iot-lite#";
    
    protected String ONT_FILE = "web/ontologies/iot-lite.ttl";// web/IoTA-Models/ResourceModel.owl";
    protected String NGSI_FILE = "web\\examples\\registerContextRequest.xml";
    
    public void createJenaModel(RegisterContextRequest rcr) {
        
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        Model entityOnt = FileManager.get().loadModel(ONT_FILE);
        ontModel.addSubModel(entityOnt);
        ontModel.setNsPrefixes(entityOnt.getNsPrefixMap());
//        ontModel.loadImports();
        
        ExtendedIterator<OntProperty> iter = ontModel.listAllOntProperties();
        while (iter.hasNext()) {
            OntProperty ontProp = iter.next();
            System.out.println(ontProp.getLocalName());
//            if (formParams.containsKey(ontProp.getLocalName())) {
//                regIndividual.addProperty(ontProp, ontModel.getcreateTypedLiteral(formParams.get(ontProp.getLocalName())[0]));
//            }
        }

//         OntClass regClass = ontModel.getOntClass(ONT_URL + "iotReg");
//         OntClass entClass = ontModel.createOntClass(ONT_URL + "entity");
//        OntClass regClass = (OntClass) ontModel.createOntResource(OntClass.class, null,ONT_URL+"Registration" );
//        OntClass regClass = (OntClass) ontModel.createClass(ONT_URL + "Registration");
//        OntClass entityClass = (OntClass) ontModel.createClass(ONT_URL + "Entity");
        OntClass entityGroup = (OntClass) ontModel.getOntClass(ONT_URL + "EntityGroup");
        OntClass entity = (OntClass) ontModel.getOntClass(ONT_URL + "Entity");
        OntClass attribute = (OntClass) ontModel.getOntClass(ONT_URL + "Attribute");
        OntClass metadata = (OntClass) ontModel.getOntClass(ONT_URL + "Metadata");
        OntClass location = (OntClass) ontModel.getOntClass(entityOnt.getNsPrefixURI("geo") + "Point");

//         System.out.println("Class type is: " + regClass.getLocalName());
        String ngsiValue = "";
        ngsiValue = rcr.getRegistrationId();
        Individual entityGroupIndiv = ontModel.createIndividual(ONT_URL + ngsiValue, entityGroup);
        entityGroupIndiv.setPropertyValue(ontModel.getProperty(ONT_URL + "registrationId"), ontModel.createLiteral(ngsiValue));
        ngsiValue = rcr.getTimestamp().toString();
        entityGroupIndiv.setPropertyValue(ontModel.getProperty(ONT_URL + "regTimeStamp"), ontModel.createLiteral(ngsiValue));
        ngsiValue = rcr.getDuration();
        entityGroupIndiv.setPropertyValue(ontModel.getProperty(ONT_URL + "duration"), ontModel.createLiteral(ngsiValue));
        
        ngsiValue = rcr.getContextRegistration().get(0).getEntityId().get(0).getId();
        Individual entity1 = ontModel.createIndividual(ONT_URL + ngsiValue, entity);
        entityGroupIndiv.setPropertyValue(ontModel.getProperty(ONT_URL + "hasEntity"), entity1);
        ngsiValue = rcr.getContextRegistration().get(0).getEntityId().get(0).getId();
        entity1.setPropertyValue(ontModel.getProperty(ONT_URL + "id"), ontModel.createLiteral(ngsiValue));        
        ngsiValue = rcr.getContextRegistration().get(0).getEntityId().get(0).getType();
        entity1.setPropertyValue(ontModel.getProperty(ONT_URL + "type"), ontModel.createLiteral(ngsiValue));        
        
        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getName();
        Individual attribute1 = ontModel.createIndividual(ONT_URL + ngsiValue, attribute);
        entity1.setPropertyValue(ontModel.getProperty(ONT_URL + "hasAttribute"), attribute1);
        
        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getType();
        attribute1.setPropertyValue(ontModel.getProperty(ONT_URL + "type"), ontModel.createLiteral(ngsiValue));
        
        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getContextMetadata().get(0).getName();
        Individual metadata1 = ontModel.createIndividual(ONT_URL + ngsiValue, metadata);
        attribute1.setPropertyValue(ontModel.getProperty(ONT_URL + "hasMetadata"), metadata1);
        
        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getContextMetadata().get(0).getType();
        metadata1.setPropertyValue(ontModel.getProperty(ONT_URL + "type"), ontModel.createLiteral(ngsiValue));
        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getContextMetadata().get(0).getValue().toString();
        metadata1.setPropertyValue(ontModel.getProperty(ONT_URL + "value"), ontModel.createLiteral(ngsiValue));
        
        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getContextMetadata().get(1).getName();
        Individual metadata2 = ontModel.createIndividual(ONT_URL + ngsiValue, metadata);
        attribute1.addProperty(ontModel.getProperty(ONT_URL + "hasMetadata"), metadata2);
        
        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getContextMetadata().get(1).getType();
        metadata2.setPropertyValue(ontModel.getProperty(ONT_URL + "type"), ontModel.createLiteral(ngsiValue));
        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getContextMetadata().get(1).getValue().toString();
        metadata2.setPropertyValue(ontModel.getProperty(ONT_URL + "value"), ontModel.createLiteral(ngsiValue));
        
//        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getContextMetadata().get(2).getName();
//        Individual metadata3 = ontModel.createIndividual(ONT_URL + ngsiValue, metadata);
//        attribute1.addProperty(ontModel.getProperty(ONT_URL + "hasMetadata"), metadata3);
//        
//        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getContextMetadata().get(2).getType();
//        metadata3.setPropertyValue(ontModel.getProperty(ONT_URL + "type"), ontModel.createLiteral(ngsiValue));
//        ngsiValue = rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(0).getContextMetadata().get(2).getValue().toString();
//        metadata3.setPropertyValue(ontModel.getProperty(ONT_URL + "value"), ontModel.createLiteral(ngsiValue));
        
        
        
//        System.out.println("has propertry \"expiry\":"+entityIndiv.hasProperty(ontModel.getProperty(ONT_URL, "expiry")));

        ontModel.write(System.out, "TURTLE");
//        ontModel.write(System.out, "RDF/XML");
//          ontModel.write(System.out, "JSON-LD");

    }
    
    public static void main(String[] args) throws JAXBException {
        
        SemanticConverter ri = new SemanticConverter();
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
         RegisterContextRequest rcr =  new RegisterContextRequest();
        try {
            rcr = objectMapper.readValue(ri.NGSI_FILE, RegisterContextRequest.class);
        } catch (Exception e) {
            
        }
//        RegisterContextRequest rcr = rm.unmarshallRequest(ri.NGSI_FILE);
        rcr.setTimestamp(Instant.now());
        ri.createJenaModel(rcr);

//        //hMap.put("class", "OnDeviceResource"); 
////        hMap.put("class", new String[]{"ResourceService"});
//        hMap.put("class", new String[]{"VirtualEntity"});
////        hMap.put("hasID", new String[]{"Resource_1"});
//        hMap.put("hasID", new String[]{"VirtualEntity_1"});
//        hMap.put("hasAttributeType", new String[]{"http://purl.oclc.org/NET/ssnx/qu/quantity#temperature"});     
////        hMap.put("isHostedOn", "PloggBoard_49_BA_01_light");
////        hMap.put("hasType", "Sensor");
////        hMap.put("hasName", "lightsensor49_BA_01");
////        hMap.put("isExposedThroughService", "http://www.surrey.ac.uk/ccsr/ontologies/ServiceModel.owl#49_BA_01_light_sensingService");
////        hMap.put("hasTag", "light sensor 49,1st,BA,office");
//        hMap.put("hasLatitude", new String[]{"51.243455"});
////        hMap.put("hasGlobalLocation", "http://www.geonames.org/2647793/");
////        hMap.put("hasResourceID", "Resource_53_BA_power_sensor");
////        hMap.put("hasLocalLocation", "http://www.surrey.ac.uk/ccsr/ontologies/LocationModel.owl#U49");
//        hMap.put("hasAltitude", new String[]{""});
//        hMap.put("hasLongitude", new String[]{"-0.588088"});
////        hMap.put("hasTimeOffset", "20");
//
//        //ri.ONT_URL = "http://www.surrey.ac.uk/ccsr/ontologies/ServiceModel.owl#";
//        //ri.ONT_URL = "http://www.surrey.ac.uk/ccsr/ontologies/VirtualEntityModel.owl#";
//        //ri.ONT_FILE = "web/IoTA-Models/VirtualEntityModel.owl";
//        ri.createJenaModel(hMap);
    }
    
}
