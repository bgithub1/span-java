//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.24 at 08:06:09 AM EDT 
//


package com.billybyte.spanjava.generated;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{}r"/>
 *         &lt;element ref="{}isCust"/>
 *         &lt;element ref="{}acctType"/>
 *         &lt;element ref="{}isM"/>
 *         &lt;element ref="{}pbc"/>
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
    "r",
    "isCust",
    "acctType",
    "isM",
    "pbc"
})
@XmlRootElement(name = "pbRateDef")
public class PbRateDef {

    @XmlElement(required = true)
    protected BigInteger r;
    @XmlElement(required = true)
    protected BigInteger isCust;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String acctType;
    @XmlElement(required = true)
    protected BigInteger isM;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String pbc;

    /**
     * Gets the value of the r property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getR() {
        return r;
    }

    /**
     * Sets the value of the r property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setR(BigInteger value) {
        this.r = value;
    }

    /**
     * Gets the value of the isCust property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIsCust() {
        return isCust;
    }

    /**
     * Sets the value of the isCust property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIsCust(BigInteger value) {
        this.isCust = value;
    }

    /**
     * Gets the value of the acctType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcctType() {
        return acctType;
    }

    /**
     * Sets the value of the acctType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcctType(String value) {
        this.acctType = value;
    }

    /**
     * Gets the value of the isM property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIsM() {
        return isM;
    }

    /**
     * Sets the value of the isM property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIsM(BigInteger value) {
        this.isM = value;
    }

    /**
     * Gets the value of the pbc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPbc() {
        return pbc;
    }

    /**
     * Sets the value of the pbc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPbc(String value) {
        this.pbc = value;
    }

}
