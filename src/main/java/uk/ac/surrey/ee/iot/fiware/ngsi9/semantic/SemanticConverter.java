/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.semantic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import java.util.Map;
import javax.xml.bind.JAXBException;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;

/**
 *
 * @author te0003
 */
public class SemanticConverter {

    public String ONT_URL = "http://iot.ee.surrey.ac.uk/fiware/ontologies/iot-lite.owl#";  //"http://www.surrey.ac.uk/ccsr/ontologies/ResourceModel.owl";
//    public String ONT_URL = "http://www.surrey.ac.uk/ccsr/ontologies/ServiceModel.owl#";
    protected String ONT_FILE = "web/ontologies/iot-lite.ttl";// web/IoTA-Models/ResourceModel.owl";
    protected String NGSI_FILE = "web\\examples\\registerContextRequest.xml";

    public void createJenaModel(RegisterContextRequest rcr) {

        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        Model entityOnt = FileManager.get().loadModel(ONT_FILE);
        ontModel.addSubModel(entityOnt);
        ontModel.setNsPrefixes(entityOnt.getNsPrefixMap());

//         OntClass regClass = ontModel.getOntClass(ONT_URL + "iotReg");
//         OntClass entClass = ontModel.createOntClass(ONT_URL + "entity");
//        OntClass regClass = (OntClass) ontModel.createOntResource(OntClass.class, null,ONT_URL+"Registration" );
//        OntClass regClass = (OntClass) ontModel.createClass(ONT_URL + "Registration");
//        OntClass entityClass = (OntClass) ontModel.createClass(ONT_URL + "Entity");
        OntClass entityClass = (OntClass) ontModel.getOntClass(ONT_URL + "Entity");

//         System.out.println("Class type is: " + regClass.getLocalName());
//         System.out.println(rcr.getRegistrationId());
        Individual regIndividual = ontModel.createIndividual(ONT_URL + "roomSensor13CII01", entityClass);
        
        System.out.println("has propertry \"expiry\":"+regIndividual.hasProperty(ontModel.getProperty(ONT_URL, "expiry")));
//        Property p = ontModel.createProperty(ONT_URL, "hasRegistrationId");
//        regIndividual.addProperty(p, "");
        regIndividual.setPropertyValue(ontModel.getProperty(ONT_URL, "registrationId"), ontModel.createLiteral(rcr.getRegistrationId()));
//        p = ontModel.createProperty(ONT_URL, "hasDuration");
//        regIndividual.addProperty(p, "");
        regIndividual.setPropertyValue(ontModel.getProperty(ONT_URL, "expiry"), ontModel.createLiteral(rcr.getDuration()));
        System.out.println("has propertry \"expiry\":"+regIndividual.hasProperty(ontModel.getProperty(ONT_URL, "expiry")));

        ExtendedIterator<OntProperty> iter = ontModel.listAllOntProperties();
        while (iter.hasNext()) {
            OntProperty ontProp = iter.next();
            System.out.println(ontProp.getLocalName());
//            if (formParams.containsKey(ontProp.getLocalName())) {
//                regIndividual.addProperty(ontProp, ontModel.getcreateTypedLiteral(formParams.get(ontProp.getLocalName())[0]));
//            }
        }
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
