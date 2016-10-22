//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.25 at 06:50:50 PM BST 
//


package uk.ac.surrey.ee.iot.fiware.ngsi9.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "NotifyContextAvailabilityRequest")
public class NotifyContextAvailabilityRequest{

//    @XmlElement(required = true)
    protected String subscriptionId;
//    @XmlElementWrapper(name="contextRegistrationResponseList")
    @JsonProperty("contextRegistrationResponses")
//   protected ContextRegistrationResponseList contextRegistrationResponseList;
    protected List<ContextRegistrationResponse> contextRegistrationResponse;
    protected StatusCode errorCode;

    /**
     * Gets the value of the subscriptionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * Sets the value of the subscriptionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubscriptionId(String value) {
        this.subscriptionId = value;
    }

    /**
     * Gets the value of the contextRegistrationResponseList property.
     * 
     * @return
     *     possible object is
     *     {@link ContextRegistrationResponseList }
     *     
     */
//    public ContextRegistrationResponseList getContextRegistrationResponseList() {
//        return contextRegistrationResponseList;
//    }

    /**
     * Sets the value of the contextRegistrationResponseList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContextRegistrationResponseList }
     *     
     */
//    public void setContextRegistrationResponseList(ContextRegistrationResponseList value) {
//        this.contextRegistrationResponseList = value;
//    }
    
    public List<ContextRegistrationResponse> getContextRegistrationResponse() {
        if (contextRegistrationResponse == null) {
            contextRegistrationResponse = new ArrayList<>();
        }
        return this.contextRegistrationResponse;
    }

    /**
     * Gets the value of the errorCode property.
     * 
     * @return
     *     possible object is
     *     {@link StatusCode }
     *     
     */
    public StatusCode getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusCode }
     *     
     */
    public void setErrorCode(StatusCode value) {
        this.errorCode = value;
    }

}
