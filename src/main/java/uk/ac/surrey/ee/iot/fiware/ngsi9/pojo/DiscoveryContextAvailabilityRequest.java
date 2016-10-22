package uk.ac.surrey.ee.iot.fiware.ngsi9.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class DiscoveryContextAvailabilityRequest{

    @JsonProperty("entities")
    protected List<EntityId> entityId;
    @JsonProperty("attributes")
    protected List<String> attribute;
    protected Restriction restriction;

    /**
     * Gets the value of the entityIdList property. 
     * 
     * @return
     *     possible object is
     *     {@link EntityIdList }
     *     
     */
     public List<EntityId> getEntityId() {
        if (entityId == null) {
            entityId = new ArrayList<>();
        }
        return this.entityId;
    }

    /**
     * Sets the value of the entityIdList property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityIdList }
     *     
     */


    /**
     * Gets the value of the attributeList property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeList }
     *     
     */
    public List<String> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<>();
        }
        return this.attribute;
    }

    /**
     * Sets the value of the attributeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeList }
     *     
     */
    

    /**
     * Gets the value of the restriction property.
     * 
     * @return
     *     possible object is
     *     {@link Restriction }
     *     
     */
    public Restriction getRestriction() {
        return restriction;
    }

    /**
     * Sets the value of the restriction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Restriction }
     *     
     */
    public void setRestriction(Restriction value) {
        this.restriction = value;
    }

}
