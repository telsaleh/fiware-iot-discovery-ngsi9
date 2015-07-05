/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.surrey.ee.iot.fiware.ngsi9.pojo;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
//import javax.xml.bind.annotation.adapters.

/**
 *
 * @author te0003
 */
//@XmlSeeAlso(value = {Association.class})
//@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Value")
public class Value{ 
  
    @SerializedName("source")
    protected EntityId sourceEntityId;
    @SerializedName("target")
    protected EntityId targetEntityId;
    @SerializedName("attributeAssociations")
    @XmlElementWrapper(name="attributeAssociationList")
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
