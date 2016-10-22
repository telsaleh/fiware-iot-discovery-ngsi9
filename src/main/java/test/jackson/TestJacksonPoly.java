/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author te0003
 */
public class TestJacksonPoly {

    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();

        Animal myDog = new Dog("ruffus","english shepherd");

        Animal myCat = new Cat("goya", "mice");

        try {
            String dogJson = objectMapper.writeValueAsString(myDog);

            System.out.println(dogJson);

            Animal deserializedDog = objectMapper.readValue(dogJson, Animal.class);

            System.out.println("Deserialized dogJson Class: " + deserializedDog.getClass().getSimpleName());

            String catJson = objectMapper.writeValueAsString(myCat);

            Animal deseriliazedCat = objectMapper.readValue(catJson, Animal.class);

            System.out.println("Deserialized catJson Class: " + deseriliazedCat.getClass().getSimpleName());



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
