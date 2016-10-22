package uk.ac.surrey.ee.iot.fiware.ngsi9.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;
import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContextMetadata")
@XmlRootElement(name = "contextMetadata")
@JsonRootName ("contextMetadata")
//@XmlSeeAlso (value={ContextMetadataString.class,ContextMetadataObject.class})
public class ContextMetadataAssociation extends ContextMetadata {

    @XmlElement(name = "name", required = true)
    protected String name;
    @XmlElement(name = "type", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String type;
    @XmlElement(name="value", required = true)
    protected Association value;

    @Override
    public Association getValue() {
        System.out.println("hi getValue");
        
        return value;
    }

    public void setValue(Association value) {
        
         this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    @Override public String toString() {
        return "ContextMetadata{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
