//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.24 at 08:06:09 AM EDT 
//


package com.billybyte.spanjava.generated;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 *         &lt;element ref="{}tm"/>
 *         &lt;element ref="{}val"/>
 *         &lt;element ref="{}offset"/>
 *         &lt;element ref="{}logret" minOccurs="0"/>
 *         &lt;element ref="{}isValAnchor"/>
 *         &lt;element ref="{}isHvarAnchor"/>
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
    "tm",
    "val",
    "offset",
    "logret",
    "isValAnchor",
    "isHvarAnchor"
})
@XmlRootElement(name = "tenor")
public class Tenor {

    @XmlElement(required = true)
    protected BigDecimal tm;
    @XmlElement(required = true)
    protected BigDecimal val;
    @XmlElement(required = true)
    protected BigInteger offset;
    protected BigDecimal logret;
    @XmlElement(required = true)
    protected BigInteger isValAnchor;
    @XmlElement(required = true)
    protected BigInteger isHvarAnchor;

    /**
     * Gets the value of the tm property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTm() {
        return tm;
    }

    /**
     * Sets the value of the tm property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTm(BigDecimal value) {
        this.tm = value;
    }

    /**
     * Gets the value of the val property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVal() {
        return val;
    }

    /**
     * Sets the value of the val property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVal(BigDecimal value) {
        this.val = value;
    }

    /**
     * Gets the value of the offset property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOffset(BigInteger value) {
        this.offset = value;
    }

    /**
     * Gets the value of the logret property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLogret() {
        return logret;
    }

    /**
     * Sets the value of the logret property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLogret(BigDecimal value) {
        this.logret = value;
    }

    /**
     * Gets the value of the isValAnchor property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIsValAnchor() {
        return isValAnchor;
    }

    /**
     * Sets the value of the isValAnchor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIsValAnchor(BigInteger value) {
        this.isValAnchor = value;
    }

    /**
     * Gets the value of the isHvarAnchor property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIsHvarAnchor() {
        return isHvarAnchor;
    }

    /**
     * Sets the value of the isHvarAnchor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIsHvarAnchor(BigInteger value) {
        this.isHvarAnchor = value;
    }

}