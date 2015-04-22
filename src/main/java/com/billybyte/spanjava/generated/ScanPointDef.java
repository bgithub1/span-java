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
 *         &lt;element ref="{}point"/>
 *         &lt;element ref="{}priceScanDef"/>
 *         &lt;element ref="{}volScanDef"/>
 *         &lt;element ref="{}weight"/>
 *         &lt;element ref="{}pairedPoint"/>
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
    "point",
    "priceScanDef",
    "volScanDef",
    "weight",
    "pairedPoint"
})
@XmlRootElement(name = "scanPointDef")
public class ScanPointDef {

    @XmlElement(required = true)
    protected BigInteger point;
    @XmlElement(required = true)
    protected PriceScanDef priceScanDef;
    @XmlElement(required = true)
    protected VolScanDef volScanDef;
    @XmlElement(required = true)
    protected BigDecimal weight;
    @XmlElement(required = true)
    protected BigInteger pairedPoint;

    /**
     * Gets the value of the point property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPoint() {
        return point;
    }

    /**
     * Sets the value of the point property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPoint(BigInteger value) {
        this.point = value;
    }

    /**
     * Gets the value of the priceScanDef property.
     * 
     * @return
     *     possible object is
     *     {@link PriceScanDef }
     *     
     */
    public PriceScanDef getPriceScanDef() {
        return priceScanDef;
    }

    /**
     * Sets the value of the priceScanDef property.
     * 
     * @param value
     *     allowed object is
     *     {@link PriceScanDef }
     *     
     */
    public void setPriceScanDef(PriceScanDef value) {
        this.priceScanDef = value;
    }

    /**
     * Gets the value of the volScanDef property.
     * 
     * @return
     *     possible object is
     *     {@link VolScanDef }
     *     
     */
    public VolScanDef getVolScanDef() {
        return volScanDef;
    }

    /**
     * Sets the value of the volScanDef property.
     * 
     * @param value
     *     allowed object is
     *     {@link VolScanDef }
     *     
     */
    public void setVolScanDef(VolScanDef value) {
        this.volScanDef = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWeight(BigDecimal value) {
        this.weight = value;
    }

    /**
     * Gets the value of the pairedPoint property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPairedPoint() {
        return pairedPoint;
    }

    /**
     * Sets the value of the pairedPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPairedPoint(BigInteger value) {
        this.pairedPoint = value;
    }

}
