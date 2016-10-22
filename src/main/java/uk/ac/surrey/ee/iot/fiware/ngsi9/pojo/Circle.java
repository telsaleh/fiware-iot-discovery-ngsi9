/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author te0003
 */

public class Circle {
    
    @XmlElement(required = true)
    @JsonProperty("point")
    protected String point;
    @XmlElement(required = true)
    @JsonProperty("radius")
    protected String radius;

    public Circle(String point, String radius) {
        this.point = point;
        this.radius = radius;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

   
    
    
    
    
}
