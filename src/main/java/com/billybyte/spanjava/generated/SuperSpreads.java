//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.24 at 08:06:09 AM EDT 
//


package com.billybyte.spanjava.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}sSpread" maxOccurs="unbounded"/>
 *         &lt;element ref="{}dSpread" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sSpread",
    "dSpread"
})
@XmlRootElement(name = "superSpreads")
public class SuperSpreads {

    @XmlElement(required = true)
    protected List<SSpread> sSpread;
    @XmlElement(required = true)
    protected List<DSpread> dSpread;

    /**
     * Gets the value of the sSpread property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sSpread property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSSpread().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SSpread }
     * 
     * 
     */
    public List<SSpread> getSSpread() {
        if (sSpread == null) {
            sSpread = new ArrayList<SSpread>();
        }
        return this.sSpread;
    }

    /**
     * Gets the value of the dSpread property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dSpread property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDSpread().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DSpread }
     * 
     * 
     */
    public List<DSpread> getDSpread() {
        if (dSpread == null) {
            dSpread = new ArrayList<DSpread>();
        }
        return this.dSpread;
    }

}
