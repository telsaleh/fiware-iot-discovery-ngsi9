/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import javax.xml.bind.JAXBException;
//import uk.ac.surrey.ee.iot.fiware.ngsi9.marshall.RegisterMarshaller;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;

/**
 *
 * @author te0003
 */
public class NgsiToSemantic {

    public NgsiToSemantic() {
    }

    public String ONT_URI = "http://purl.oclc.org/NET/UNIS/iot-lite#";
//    protected String ONT_FILE = "web/ontologies/iot-lite.ttl";
//    protected String ONT_FILE = "file:///C:/Users/te0003/Documents/NetBeansProjects/OntologyEvaluator/src/main/webapp/ontologies/iot-lite.ttl";
    protected String ONT_FILE = "file:///C:/Users/te0003/Documents/NetBeansProjects/OntologyEvaluator/src/main/webapp/ontologies/iot-lite.jsonld";
//    protected String ONT_FILE = "file:///C:/Users/te0003/Documents/NetBeansProjects/OntologyEvaluator/src/main/webapp/ontologies/geojson.jsonld";
//    protected String SPARQL_QUERY1 = "C:/Users/te0003/Documents/NetBeansProjects/OntologyEvaluator/src/main/webapp/query/sensors_declared.rq";
//    protected String ONT_FILE = "file:///C:/Users/te0003/Documents/NetBeansProjects/OntologyEvaluator/src/main/webapp/ontologies/newjson.jsonld";

    public void createJenaModel(RegisterContextRequest rcr) {

        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        Model iotLiteOnt = FileManager.get().loadModel(ONT_FILE);
        ontModel.add(iotLiteOnt);
//        ontModel.read("http://purl.oclc.org/NET/UNIS/fiware/iot-lite");
//        ontModel.setNsPrefixes(iotLiteOnt.getNsPrefixMap());
//        ontModel.loadImports();
        ontModel.setStrictMode(true);

        System.out.println("\nModel Classes");
        System.out.println("--------------------------------------------------");
        ExtendedIterator<OntClass> iter1 = ontModel.listClasses();
        while (iter1.hasNext()) {
            OntClass ontClass = iter1.next();
            System.out.println(ontClass.getURI());

            for (ExtendedIterator<OntClass> subs = ontClass.listSubClasses(); subs.hasNext();) {
                OntClass subClass = subs.next();
                System.out.println("\t" + subClass.getURI());

                for (ExtendedIterator<OntProperty> props = subClass.listDeclaredProperties(); props.hasNext();) {
                    OntProperty declared = props.next();
                    System.out.println("\t\t" + declared);
                }
            }
        }
//        System.out.println("--------------------------------------------------");
//        System.out.println("\nAll Model Properties");
//        System.out.println("--------------------------------------------------");
//        ExtendedIterator<OntProperty> iter2 = ontModel.listAllOntProperties();
//        while (iter2.hasNext()) {
//            OntProperty ontProp = iter2.next();
//            System.out.println(ontProp.getURI());
//        }
        System.out.println("--------------------------------------------------");
        System.out.println("\nAll Model Object Properties");
        System.out.println("--------------------------------------------------");
        ExtendedIterator<ObjectProperty> iter6 = ontModel.listObjectProperties();
        while (iter6.hasNext()) {
            OntProperty ontProp = iter6.next();
            System.out.println(ontProp.getURI());
        }
        System.out.println("--------------------------------------------------");
        System.out.println("\nAll Model Datatype Properties");
        System.out.println("--------------------------------------------------");
        ExtendedIterator<DatatypeProperty> iter4 = ontModel.listDatatypeProperties();
        while (iter4.hasNext()) {
            OntProperty ontProp = iter4.next();
            System.out.println(ontProp.getURI());
        }
        System.out.println("--------------------------------------------------");
        System.out.println("\nAll Model Annotation Properties");
        System.out.println("--------------------------------------------------");
        ExtendedIterator<AnnotationProperty> iter5 = ontModel.listAnnotationProperties();
        while (iter5.hasNext()) {
            OntProperty ontProp = iter5.next();
            System.out.println(ontProp.getURI());
        }
        System.out.println("--------------------------------------------------");//        
        System.out.println("\nAll Model Functional Properties");
        System.out.println("--------------------------------------------------");
        ExtendedIterator<FunctionalProperty> iter3 = ontModel.listFunctionalProperties();
        while (iter3.hasNext()) {
            OntProperty ontProp = iter3.next();
            System.out.println(ontProp.getURI());
        }
        System.out.println("--------------------------------------------------");

//        ontModel.writeAll(System.out, "JSON-LD");
        ontModel.writeAll(System.out, "TURTLE");

    }

    public static void main(String[] args) throws JAXBException {

        NgsiToSemantic ri = new NgsiToSemantic();
//        RegisterMarshaller rm = new RegisterMarshaller();
//        RegisterContextRequest rcr = rm.unmarshallRequest(ri.NGSI_FILE);
//        rcr.setTimestamp(Instant.now());
//        ri.createJenaModel(rcr);
    }

}
