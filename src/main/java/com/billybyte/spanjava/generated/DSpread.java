//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.24 at 08:06:09 AM EDT 
//


package com.billybyte.spanjava.generated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}dSpread"/>
 *           &lt;element ref="{}rate"/>
 *           &lt;element ref="{}spread"/>
 *           &lt;element ref="{}chargeMeth"/>
 *           &lt;element ref="{}tLeg"/>
 *         &lt;/choice>
 *         &lt;element ref="{}pLeg" maxOccurs="unbounded" minOccurs="0"/>
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
    "dSpreadOrRateOrSpread",
    "pLeg"
})
@XmlRootElement(name = "dSpread")
public class DSpread {

    @XmlElementRefs({
        @XmlElementRef(name = "spread", type = JAXBElement.class),
        @XmlElementRef(name = "rate", type = Rate.class),
        @XmlElementRef(name = "chargeMeth", type = JAXBElement.class),
        @XmlElementRef(name = "tLeg", type = TLeg.class),
        @XmlElementRef(name = "dSpread", type = DSpread.class)
    })
    protected List<Object> dSpreadOrRateOrSpread;
    protected List<PLeg> pLeg;

    /**
     * Gets the value of the dSpreadOrRateOrSpread property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dSpreadOrRateOrSpread property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDSpreadOrRateOrSpread().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     * {@link TLeg }
     * {@link DSpread }
     * {@link Rate }
     * 
     * 
     */
    public List<Object> getDSpreadOrRateOrSpread() {
        if (dSpreadOrRateOrSpread == null) {
            dSpreadOrRateOrSpread = new ArrayList<Object>();
        }
        return this.dSpreadOrRateOrSpread;
    }

    /**
     * Gets the value of the pLeg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pLeg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPLeg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PLeg }
     * 
     * 
     */
    public List<PLeg> getPLeg() {
        if (pLeg == null) {
            pLeg = new ArrayList<PLeg>();
        }
        return this.pLeg;
    }

}
