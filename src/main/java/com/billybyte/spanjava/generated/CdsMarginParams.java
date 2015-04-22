//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.24 at 08:06:09 AM EDT 
//


package com.billybyte.spanjava.generated;

import java.math.BigDecimal;
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
 *         &lt;element ref="{}cdsIGHYThreshold"/>
 *         &lt;element ref="{}cdsIGCompressionShockPct"/>
 *         &lt;element ref="{}cdsHYCompressionShockPct"/>
 *         &lt;element ref="{}cdsIGSingleMinMarginPct"/>
 *         &lt;element ref="{}cdsHYSingleMinMarginPct"/>
 *         &lt;element ref="{}cdsIGIndexMinMarginPct"/>
 *         &lt;element ref="{}cdsHYIndexMinMarginPct"/>
 *         &lt;element ref="{}cdsIGCurveShockPct"/>
 *         &lt;element ref="{}cdsHYCurveShockPct"/>
 *         &lt;element ref="{}cdsIGLiqMarginIntercept"/>
 *         &lt;element ref="{}cdsHYLiqMarginIntercept"/>
 *         &lt;element ref="{}cdsIGLiqMarginCoefficient"/>
 *         &lt;element ref="{}cdsHYLiqMarginCoefficient"/>
 *         &lt;element ref="{}cdsIGLiqMarginExponent"/>
 *         &lt;element ref="{}cdsHYLiqMarginExponent"/>
 *         &lt;element ref="{}cdsIGLiqMarginScale"/>
 *         &lt;element ref="{}cdsHYLiqMarginScale"/>
 *         &lt;element ref="{}cdsIGLiqMarginBidAsk"/>
 *         &lt;element ref="{}cdsHYLiqMarginBidAsk"/>
 *         &lt;element ref="{}cdsIGLiqMarginDST"/>
 *         &lt;element ref="{}cdsHYLiqMarginDST"/>
 *         &lt;element ref="{}cdsIGLiqMarginOTRPV01"/>
 *         &lt;element ref="{}cdsHYLiqMarginOTRPV01"/>
 *         &lt;element ref="{}cdsDeviationPct"/>
 *         &lt;element ref="{}sectorDef" maxOccurs="unbounded"/>
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
    "cdsIGHYThreshold",
    "cdsIGCompressionShockPct",
    "cdsHYCompressionShockPct",
    "cdsIGSingleMinMarginPct",
    "cdsHYSingleMinMarginPct",
    "cdsIGIndexMinMarginPct",
    "cdsHYIndexMinMarginPct",
    "cdsIGCurveShockPct",
    "cdsHYCurveShockPct",
    "cdsIGLiqMarginIntercept",
    "cdsHYLiqMarginIntercept",
    "cdsIGLiqMarginCoefficient",
    "cdsHYLiqMarginCoefficient",
    "cdsIGLiqMarginExponent",
    "cdsHYLiqMarginExponent",
    "cdsIGLiqMarginScale",
    "cdsHYLiqMarginScale",
    "cdsIGLiqMarginBidAsk",
    "cdsHYLiqMarginBidAsk",
    "cdsIGLiqMarginDST",
    "cdsHYLiqMarginDST",
    "cdsIGLiqMarginOTRPV01",
    "cdsHYLiqMarginOTRPV01",
    "cdsDeviationPct",
    "sectorDef"
})
@XmlRootElement(name = "cdsMarginParams")
public class CdsMarginParams {

    @XmlElement(required = true)
    protected BigDecimal cdsIGHYThreshold;
    @XmlElement(required = true)
    protected BigDecimal cdsIGCompressionShockPct;
    @XmlElement(required = true)
    protected BigDecimal cdsHYCompressionShockPct;
    @XmlElement(required = true)
    protected BigDecimal cdsIGSingleMinMarginPct;
    @XmlElement(required = true)
    protected BigDecimal cdsHYSingleMinMarginPct;
    @XmlElement(required = true)
    protected BigDecimal cdsIGIndexMinMarginPct;
    @XmlElement(required = true)
    protected BigDecimal cdsHYIndexMinMarginPct;
    @XmlElement(required = true)
    protected BigDecimal cdsIGCurveShockPct;
    @XmlElement(required = true)
    protected BigDecimal cdsHYCurveShockPct;
    @XmlElement(required = true)
    protected BigDecimal cdsIGLiqMarginIntercept;
    @XmlElement(required = true)
    protected BigDecimal cdsHYLiqMarginIntercept;
    @XmlElement(required = true)
    protected BigDecimal cdsIGLiqMarginCoefficient;
    @XmlElement(required = true)
    protected BigDecimal cdsHYLiqMarginCoefficient;
    @XmlElement(required = true)
    protected BigDecimal cdsIGLiqMarginExponent;
    @XmlElement(required = true)
    protected BigDecimal cdsHYLiqMarginExponent;
    @XmlElement(required = true)
    protected BigDecimal cdsIGLiqMarginScale;
    @XmlElement(required = true)
    protected BigDecimal cdsHYLiqMarginScale;
    @XmlElement(required = true)
    protected BigDecimal cdsIGLiqMarginBidAsk;
    @XmlElement(required = true)
    protected BigDecimal cdsHYLiqMarginBidAsk;
    @XmlElement(required = true)
    protected BigDecimal cdsIGLiqMarginDST;
    @XmlElement(required = true)
    protected BigDecimal cdsHYLiqMarginDST;
    @XmlElement(required = true)
    protected BigDecimal cdsIGLiqMarginOTRPV01;
    @XmlElement(required = true)
    protected BigDecimal cdsHYLiqMarginOTRPV01;
    @XmlElement(required = true)
    protected BigDecimal cdsDeviationPct;
    @XmlElement(required = true)
    protected List<SectorDef> sectorDef;

    /**
     * Gets the value of the cdsIGHYThreshold property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGHYThreshold() {
        return cdsIGHYThreshold;
    }

    /**
     * Sets the value of the cdsIGHYThreshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGHYThreshold(BigDecimal value) {
        this.cdsIGHYThreshold = value;
    }

    /**
     * Gets the value of the cdsIGCompressionShockPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGCompressionShockPct() {
        return cdsIGCompressionShockPct;
    }

    /**
     * Sets the value of the cdsIGCompressionShockPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGCompressionShockPct(BigDecimal value) {
        this.cdsIGCompressionShockPct = value;
    }

    /**
     * Gets the value of the cdsHYCompressionShockPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYCompressionShockPct() {
        return cdsHYCompressionShockPct;
    }

    /**
     * Sets the value of the cdsHYCompressionShockPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYCompressionShockPct(BigDecimal value) {
        this.cdsHYCompressionShockPct = value;
    }

    /**
     * Gets the value of the cdsIGSingleMinMarginPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGSingleMinMarginPct() {
        return cdsIGSingleMinMarginPct;
    }

    /**
     * Sets the value of the cdsIGSingleMinMarginPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGSingleMinMarginPct(BigDecimal value) {
        this.cdsIGSingleMinMarginPct = value;
    }

    /**
     * Gets the value of the cdsHYSingleMinMarginPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYSingleMinMarginPct() {
        return cdsHYSingleMinMarginPct;
    }

    /**
     * Sets the value of the cdsHYSingleMinMarginPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYSingleMinMarginPct(BigDecimal value) {
        this.cdsHYSingleMinMarginPct = value;
    }

    /**
     * Gets the value of the cdsIGIndexMinMarginPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGIndexMinMarginPct() {
        return cdsIGIndexMinMarginPct;
    }

    /**
     * Sets the value of the cdsIGIndexMinMarginPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGIndexMinMarginPct(BigDecimal value) {
        this.cdsIGIndexMinMarginPct = value;
    }

    /**
     * Gets the value of the cdsHYIndexMinMarginPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYIndexMinMarginPct() {
        return cdsHYIndexMinMarginPct;
    }

    /**
     * Sets the value of the cdsHYIndexMinMarginPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYIndexMinMarginPct(BigDecimal value) {
        this.cdsHYIndexMinMarginPct = value;
    }

    /**
     * Gets the value of the cdsIGCurveShockPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGCurveShockPct() {
        return cdsIGCurveShockPct;
    }

    /**
     * Sets the value of the cdsIGCurveShockPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGCurveShockPct(BigDecimal value) {
        this.cdsIGCurveShockPct = value;
    }

    /**
     * Gets the value of the cdsHYCurveShockPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYCurveShockPct() {
        return cdsHYCurveShockPct;
    }

    /**
     * Sets the value of the cdsHYCurveShockPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYCurveShockPct(BigDecimal value) {
        this.cdsHYCurveShockPct = value;
    }

    /**
     * Gets the value of the cdsIGLiqMarginIntercept property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGLiqMarginIntercept() {
        return cdsIGLiqMarginIntercept;
    }

    /**
     * Sets the value of the cdsIGLiqMarginIntercept property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGLiqMarginIntercept(BigDecimal value) {
        this.cdsIGLiqMarginIntercept = value;
    }

    /**
     * Gets the value of the cdsHYLiqMarginIntercept property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYLiqMarginIntercept() {
        return cdsHYLiqMarginIntercept;
    }

    /**
     * Sets the value of the cdsHYLiqMarginIntercept property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYLiqMarginIntercept(BigDecimal value) {
        this.cdsHYLiqMarginIntercept = value;
    }

    /**
     * Gets the value of the cdsIGLiqMarginCoefficient property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGLiqMarginCoefficient() {
        return cdsIGLiqMarginCoefficient;
    }

    /**
     * Sets the value of the cdsIGLiqMarginCoefficient property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGLiqMarginCoefficient(BigDecimal value) {
        this.cdsIGLiqMarginCoefficient = value;
    }

    /**
     * Gets the value of the cdsHYLiqMarginCoefficient property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYLiqMarginCoefficient() {
        return cdsHYLiqMarginCoefficient;
    }

    /**
     * Sets the value of the cdsHYLiqMarginCoefficient property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYLiqMarginCoefficient(BigDecimal value) {
        this.cdsHYLiqMarginCoefficient = value;
    }

    /**
     * Gets the value of the cdsIGLiqMarginExponent property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGLiqMarginExponent() {
        return cdsIGLiqMarginExponent;
    }

    /**
     * Sets the value of the cdsIGLiqMarginExponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGLiqMarginExponent(BigDecimal value) {
        this.cdsIGLiqMarginExponent = value;
    }

    /**
     * Gets the value of the cdsHYLiqMarginExponent property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYLiqMarginExponent() {
        return cdsHYLiqMarginExponent;
    }

    /**
     * Sets the value of the cdsHYLiqMarginExponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYLiqMarginExponent(BigDecimal value) {
        this.cdsHYLiqMarginExponent = value;
    }

    /**
     * Gets the value of the cdsIGLiqMarginScale property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGLiqMarginScale() {
        return cdsIGLiqMarginScale;
    }

    /**
     * Sets the value of the cdsIGLiqMarginScale property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGLiqMarginScale(BigDecimal value) {
        this.cdsIGLiqMarginScale = value;
    }

    /**
     * Gets the value of the cdsHYLiqMarginScale property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYLiqMarginScale() {
        return cdsHYLiqMarginScale;
    }

    /**
     * Sets the value of the cdsHYLiqMarginScale property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYLiqMarginScale(BigDecimal value) {
        this.cdsHYLiqMarginScale = value;
    }

    /**
     * Gets the value of the cdsIGLiqMarginBidAsk property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGLiqMarginBidAsk() {
        return cdsIGLiqMarginBidAsk;
    }

    /**
     * Sets the value of the cdsIGLiqMarginBidAsk property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGLiqMarginBidAsk(BigDecimal value) {
        this.cdsIGLiqMarginBidAsk = value;
    }

    /**
     * Gets the value of the cdsHYLiqMarginBidAsk property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYLiqMarginBidAsk() {
        return cdsHYLiqMarginBidAsk;
    }

    /**
     * Sets the value of the cdsHYLiqMarginBidAsk property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYLiqMarginBidAsk(BigDecimal value) {
        this.cdsHYLiqMarginBidAsk = value;
    }

    /**
     * Gets the value of the cdsIGLiqMarginDST property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGLiqMarginDST() {
        return cdsIGLiqMarginDST;
    }

    /**
     * Sets the value of the cdsIGLiqMarginDST property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGLiqMarginDST(BigDecimal value) {
        this.cdsIGLiqMarginDST = value;
    }

    /**
     * Gets the value of the cdsHYLiqMarginDST property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYLiqMarginDST() {
        return cdsHYLiqMarginDST;
    }

    /**
     * Sets the value of the cdsHYLiqMarginDST property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYLiqMarginDST(BigDecimal value) {
        this.cdsHYLiqMarginDST = value;
    }

    /**
     * Gets the value of the cdsIGLiqMarginOTRPV01 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsIGLiqMarginOTRPV01() {
        return cdsIGLiqMarginOTRPV01;
    }

    /**
     * Sets the value of the cdsIGLiqMarginOTRPV01 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsIGLiqMarginOTRPV01(BigDecimal value) {
        this.cdsIGLiqMarginOTRPV01 = value;
    }

    /**
     * Gets the value of the cdsHYLiqMarginOTRPV01 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsHYLiqMarginOTRPV01() {
        return cdsHYLiqMarginOTRPV01;
    }

    /**
     * Sets the value of the cdsHYLiqMarginOTRPV01 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsHYLiqMarginOTRPV01(BigDecimal value) {
        this.cdsHYLiqMarginOTRPV01 = value;
    }

    /**
     * Gets the value of the cdsDeviationPct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCdsDeviationPct() {
        return cdsDeviationPct;
    }

    /**
     * Sets the value of the cdsDeviationPct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCdsDeviationPct(BigDecimal value) {
        this.cdsDeviationPct = value;
    }

    /**
     * Gets the value of the sectorDef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sectorDef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSectorDef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SectorDef }
     * 
     * 
     */
    public List<SectorDef> getSectorDef() {
        if (sectorDef == null) {
            sectorDef = new ArrayList<SectorDef>();
        }
        return this.sectorDef;
    }

}
