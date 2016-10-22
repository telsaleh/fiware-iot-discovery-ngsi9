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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.Association;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.RegisterContextRequest;
import uk.ac.surrey.ee.iot.fiware.ngsi9.pojo.Association;

/**
 *
 * @author te0003
 */
public class JacksonNsgiRegister {

//    @JsonIgnoreProperties(ignoreUnknown = true)
//    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
//    @JsonSubTypes({
//        @JsonSubTypes.Type(value = Dog.class, name = "Dog"),
//
//        @JsonSubTypes.Type(value = Cat.class, name = "Cat")}
//    )

    protected static String NGSI_FILE = "C:\\Users\\te0003\\Documents\\NetBeansProjects\\Ngsi9Server_r4_2\\src\\main\\webapp\\binding\\json\\examples\\register\\RegisterContextRequest-example.json";

    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String ngsiRcr = new String(Files.readAllBytes(Paths.get(NGSI_FILE)));

        RegisterContextRequest rcr = objectMapper.readValue(ngsiRcr, RegisterContextRequest.class);
        
        System.out.println(objectMapper.writeValueAsString(rcr));
                
        LinkedHashMap association = (LinkedHashMap) rcr.getContextRegistration().get(0).getContextMetadata().get(0).getValue();
        Association assocObject =  objectMapper.convertValue(association, Association.class);
        System.out.println(assocObject.getAttributeAssociation().get(0).getSourceAttribute());
        
        rcr.getContextRegistration().get(0).getContextRegistrationAttribute().get(1).getContextMetadata().get(0);
        
//        System.out.println(rcr.getContextRegistration().get(0).getContextMetadata().get(0).getValue().getClass().getCanonicalName());
        
//        String assocJson = objectMapper.writeValueAsString(association);
//        Value assocObject =  objectMapper.readValue(objectMapper.writeValueAsString(association), Value.class);
//        System.out.println(association.values().toString());
//        System.out.println(assocJson);

    }

}
