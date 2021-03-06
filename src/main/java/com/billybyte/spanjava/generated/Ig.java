//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.24 at 08:06:09 AM EDT 
//


package com.billybyte.spanjava.generated;

import java.math.BigDecimal;
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
 *         &lt;element ref="{}dst"/>
 *         &lt;element ref="{}dstExponent"/>
 *         &lt;element ref="{}dstFloor"/>
 *         &lt;element ref="{}capPct"/>
 *         &lt;element ref="{}bidAskFloor"/>
 *         &lt;element ref="{}concCoeficient"/>
 *         &lt;element ref="{}concExponent"/>
 *         &lt;element ref="{}concIntercept"/>
 *         &lt;element ref="{}concScale"/>
 *         &lt;element ref="{}curveShockPct"/>
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
    "dst",
    "dstExponent",
    "dstFloor",
    "capPct",
    "bidAskFloor",
    "concCoeficient",
    "concExponent",
    "concIntercept",
    "concScale",
    "curveShockPct"
})
@XmlRootElement(name = "ig")
public class Ig {

    @XmlElement(required = true)
    protected BigDecimal dst;
    @XmlElement(required = true)
    protected BigDecimal dstExponent;
    @XmlElement(required = true)
    protected BigDecimal dstFloor;
    @XmlElement(required = true)
    protected BigDecimal capPct;
    @XmlElement(required = true)
    protected BigDecimal bidAskFloor;
    @XmlElement(required = true)
    protected BigDecimal concCoeficient;
    @XmlElement(required = true)
    protected BigDecimal concExponent;
    @XmlElement(required = true)
    protected BigDecimal concIntercept;
    @XmlElement(required = true)
    protected BigDecimal concScale;
    @XmlElement(required = true)
    protected BigDecimal curveShockPct;

    /**
     * Gets the value of the dst property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDst() {
        return dst;
    }

    /**
     * Sets the value of the dst property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDst(BigDecimal value) {
        this.dst = value;
    }

    /**
     * Gets the value of the dstExponent property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDstExponent() {
        return dstExponent;
    }

    /**
     * Sets the value of the dstExponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDstExponent(BigDecimal value) {
        this.dstExponent = value;
    }

    /**
     * Gets the value of the dstFloor property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDstFloor() {
        return dstFloor;
    }

    /**
     * Sets the value of the dstFloor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDstFloor(BigDecimal value) {
        this.dstFloor = value;
    }

    /**
     * Gets the value of the capPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCapPct() {
        return capPct;
    }

    /**
     * Sets the value of the capPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCapPct(BigDecimal value) {
        this.capPct = value;
    }

    /**
     * Gets the value of the bidAskFloor property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBidAskFloor() {
        return bidAskFloor;
    }

    /**
     * Sets the value of the bidAskFloor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBidAskFloor(BigDecimal value) {
        this.bidAskFloor = value;
    }

    /**
     * Gets the value of the concCoeficient property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConcCoeficient() {
        return concCoeficient;
    }

    /**
     * Sets the value of the concCoeficient property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConcCoeficient(BigDecimal value) {
        this.concCoeficient = value;
    }

    /**
     * Gets the value of the concExponent property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConcExponent() {
        return concExponent;
    }

    /**
     * Sets the value of the concExponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConcExponent(BigDecimal value) {
        this.concExponent = value;
    }

    /**
     * Gets the value of the concIntercept property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConcIntercept() {
        return concIntercept;
    }

    /**
     * Sets the value of the concIntercept property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConcIntercept(BigDecimal value) {
        this.concIntercept = value;
    }

    /**
     * Gets the value of the concScale property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConcScale() {
        return concScale;
    }

    /**
     * Sets the value of the concScale property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConcScale(BigDecimal value) {
        this.concScale = value;
    }

    /**
     * Gets the value of the curveShockPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCurveShockPct() {
        return curveShockPct;
    }

    /**
     * Sets the value of the curveShockPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCurveShockPct(BigDecimal value) {
        this.curveShockPct = value;
    }

}
