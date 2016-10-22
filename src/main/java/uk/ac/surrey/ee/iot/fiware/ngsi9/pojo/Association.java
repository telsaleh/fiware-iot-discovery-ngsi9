/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.surrey.ee.iot.fiware.ngsi9.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author te0003
 */
//@XmlSeeAlso(value = {Association.class})
//@XmlTransient
//@XmlRootElement(name = "contextMetadata")
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "Value")
public class Association{ 
  
    @JsonProperty ("source")
    protected EntityId sourceEntityId;
    @JsonProperty ("target")
    protected EntityId targetEntityId;
    @JsonProperty ("associations")
    protected List<AttributeAssociation> attributeAssociation;

//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }

    public EntityId getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(EntityId sourceEntityId) {
        this.sourceEntityId = sourceEntityId;
    }

    public EntityId getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(EntityId targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public List<AttributeAssociation> getAttributeAssociation() {
        return attributeAssociation;
    }

    public void setAttributeAssociation(List<AttributeAssociation> attributeAssociation) {
        this.attributeAssociation = attributeAssociation;
    }

    
}
