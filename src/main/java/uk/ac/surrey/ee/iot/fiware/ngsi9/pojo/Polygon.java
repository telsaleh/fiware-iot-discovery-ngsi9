package uk.ac.surrey.ee.iot.fiware.ngsi9.pojo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
//@Generated("org.jsonschema2pojo")
//@JsonPropertyOrder({"vertices"})
//@JsonRootName ("polygon")
public class Polygon {

@JsonProperty("vertices")
private List<Vertex> vertices = new ArrayList<Vertex>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The vertices
*/
@JsonProperty("vertices")
public List<Vertex> getVertices() {
return vertices;
}

/**
* 
* @param vertices
* The vertices
*/
@JsonProperty("vertices")
public void setVertices(List<Vertex> vertices) {
this.vertices = vertices;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}

//public class Polygon {
//    
//    @XmlElement(required = true)
//    @SerializedName("latitudes")
//    protected String latitudes;
//    @XmlElement(required = true)
//    @SerializedName("longitudes")
//    protected String longitudes;
//
//    public Polygon(String latitudes, String longitudes) {
//        this.latitudes = latitudes;
//        this.longitudes = longitudes;
//    }
//
//    public String getLatitudes() {
//        return latitudes;
//    }
//
//    public void setLatitudes(String latitudes) {
//        this.latitudes = latitudes;
//    }
//
//    public String getLongitudes() {
//        return longitudes;
//    }
//
//    public void setLongitudes(String longitudes) {
//        this.longitudes = longitudes;
//    }
//}
