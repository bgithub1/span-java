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
 *         &lt;element ref="{}r"/>
 *         &lt;element ref="{}priceScan"/>
 *         &lt;element ref="{}priceScanPct" minOccurs="0"/>
 *         &lt;element ref="{}volScanPct" minOccurs="0"/>
 *         &lt;element ref="{}volScan" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{}priceScanDown"/>
 *           &lt;element ref="{}volScanDown"/>
 *         &lt;/sequence>
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
    "priceScan",
    "priceScanPct",
    "volScanPct",
    "volScan",
    "priceScanDown",
    "volScanDown"
})
@XmlRootElement(name = "scanRate")
public class ScanRate {

    @XmlElement(required = true)
    protected BigInteger r;
    @XmlElement(required = true)
    protected BigDecimal priceScan;
    protected BigDecimal priceScanPct;
    protected BigDecimal volScanPct;
    protected BigDecimal volScan;
    protected BigDecimal priceScanDown;
    protected BigDecimal volScanDown;

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
     * Gets the value of the priceScan property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPriceScan() {
        return priceScan;
    }

    /**
     * Sets the value of the priceScan property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPriceScan(BigDecimal value) {
        this.priceScan = value;
    }

    /**
     * Gets the value of the priceScanPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPriceScanPct() {
        return priceScanPct;
    }

    /**
     * Sets the value of the priceScanPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPriceScanPct(BigDecimal value) {
        this.priceScanPct = value;
    }

    /**
     * Gets the value of the volScanPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVolScanPct() {
        return volScanPct;
    }

    /**
     * Sets the value of the volScanPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVolScanPct(BigDecimal value) {
        this.volScanPct = value;
    }

    /**
     * Gets the value of the volScan property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVolScan() {
        return volScan;
    }

    /**
     * Sets the value of the volScan property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVolScan(BigDecimal value) {
        this.volScan = value;
    }

    /**
     * Gets the value of the priceScanDown property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPriceScanDown() {
        return priceScanDown;
    }

    /**
     * Sets the value of the priceScanDown property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPriceScanDown(BigDecimal value) {
        this.priceScanDown = value;
    }

    /**
     * Gets the value of the volScanDown property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVolScanDown() {
        return volScanDown;
    }

    /**
     * Sets the value of the volScanDown property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVolScanDown(BigDecimal value) {
        this.volScanDown = value;
    }

}
