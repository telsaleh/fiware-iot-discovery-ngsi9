/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.Association;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.Association;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.DiscoveryContextAvailabilityRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.Polygon;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.Shape;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.Vertex;

/**
 *
 * @author te0003
 */
public class JacksonNsgiDiscover {

//    @JsonIgnoreProperties(ignoreUnknown = true)
//    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
//    @JsonSubTypes({
//        @JsonSubTypes.Type(value = Dog.class, name = "Dog"),
//
//        @JsonSubTypes.Type(value = Cat.class, name = "Cat")}
//    )

    protected static String NGSI_FILE = "C:\\Users\\te0003\\Documents\\NetBeansProjects\\Ngsi9Server_r4_2\\src\\main\\webapp\\binding\\json\\examples\\discover\\ngsi-discover-example-location.json";

    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);       

        String ngsiRcr = new String(Files.readAllBytes(Paths.get(NGSI_FILE)));

        DiscoveryContextAvailabilityRequest dcar = objectMapper.readValue(ngsiRcr, DiscoveryContextAvailabilityRequest.class);
        
//        System.out.println(objectMapper.writeValueAsString(dcar));
        System.out.println(dcar.getRestriction().getOperationScope().get(1).getScopeValue());
                
        LinkedHashMap shapeHMap = (LinkedHashMap) dcar.getRestriction().getOperationScope().get(1).getScopeValue();
//        Association assocObject =  objectMapper.convertValue(shapeHMap, Association.class);
//        System.out.println(assocObject.getAttributeAssociation().get(0).getSourceAttribute());
        
        
        Shape shape = objectMapper.convertValue(shapeHMap, Shape.class);
        System.out.println("Deserialized Class: " + shape.getClass().getSimpleName());
        System.out.println("VALUE: " + shape.getPolygon().getVertices().get(2).getLatitude());
        System.out.println("VALUE: " + shape.getCircle());
        if(!(shape.getCircle()==null))
            System.out.println("This is null");        
       
    Polygon polygon = shape.getPolygon();
    int vertexSize = polygon.getVertices().size();
    Coordinate[] coords = new Coordinate[vertexSize];
    
    final ArrayList<Coordinate> points = new ArrayList<>(); 
    for (int i =0; i<vertexSize; i++){
    Vertex vertex = polygon.getVertices().get(i); 
    points.add(new Coordinate(Double.valueOf(vertex.getLatitude()), Double.valueOf(vertex.getLongitude())));
    coords[i]= new Coordinate(Double.valueOf(vertex.getLatitude()), Double.valueOf(vertex.getLongitude()));
    }
    points.add(new Coordinate(Double.valueOf(polygon.getVertices().get(0).getLatitude()), Double.valueOf(polygon.getVertices().get(0).getLongitude())));
    
    final GeometryFactory gf = new GeometryFactory();
    
    final Coordinate target = new Coordinate(49, -0.6);
    final Point point = gf.createPoint(target);    
    
    Geometry shapeGm = gf.createPolygon(new LinearRing(new CoordinateArraySequence(points.toArray(new Coordinate[points.size()])), gf), null);    
//    Geometry shapeGm = gf.createPolygon(coords);    
    System.out.println(point.within(shapeGm));
    
        
//        System.out.println(rcr.getContextRegistration().get(0).getContextMetadata().get(0).getValue().getClass().getCanonicalName());
        
//        String assocJson = objectMapper.writeValueAsString(association);
//        Value assocObject =  objectMapper.readValue(objectMapper.writeValueAsString(association), Value.class);
//        System.out.println(association.values().toString());
//        System.out.println(assocJson);

    }

}
