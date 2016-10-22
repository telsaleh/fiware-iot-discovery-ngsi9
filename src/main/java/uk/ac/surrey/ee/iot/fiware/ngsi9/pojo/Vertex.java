/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.surrey.ee.iot.fiware.ngsi9.pojo;

/**
 *
 * @author te0003
 */
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"latitude",
"longitude"
})
public class Vertex {

@JsonProperty("latitude")
private String latitude;
@JsonProperty("longitude")
private String longitude;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The latitude
*/
@JsonProperty("latitude")
public String getLatitude() {
return latitude;
}

/**
* 
* @param latitude
* The latitude
*/
@JsonProperty("latitude")
public void setLatitude(String latitude) {
this.latitude = latitude;
}

/**
* 
* @return
* The longitude
*/
@JsonProperty("longitude")
public String getLongitude() {
return longitude;
}

/**
* 
* @param longitude
* The longitude
*/
@JsonProperty("longitude")
public void setLongitude(String longitude) {
this.longitude = longitude;
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