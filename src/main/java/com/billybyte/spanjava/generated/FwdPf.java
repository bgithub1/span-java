//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.24 at 08:06:09 AM EDT 
//


package com.billybyte.spanjava.generated;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{}pfId"/>
 *         &lt;element ref="{}pfCode"/>
 *         &lt;element ref="{}group" minOccurs="0"/>
 *         &lt;element ref="{}alias" maxOccurs="unbounded"/>
 *         &lt;element ref="{}name"/>
 *         &lt;element ref="{}currency"/>
 *         &lt;element ref="{}cvf"/>
 *         &lt;element ref="{}eqPosFactor" minOccurs="0"/>
 *         &lt;element ref="{}notionAmt"/>
 *         &lt;element ref="{}priceDl"/>
 *         &lt;element ref="{}priceFmt"/>
 *         &lt;element ref="{}valueMeth"/>
 *         &lt;element ref="{}priceMeth"/>
 *         &lt;element ref="{}setlMeth"/>
 *         &lt;element ref="{}undPf"/>
 *         &lt;element ref="{}fwd" maxOccurs="unbounded" minOccurs="0"/>
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
    "pfId",
    "pfCode",
    "group",
    "alias",
    "name",
    "currency",
    "cvf",
    "eqPosFactor",
    "notionAmt",
    "priceDl",
    "priceFmt",
    "valueMeth",
    "priceMeth",
    "setlMeth",
    "undPf",
    "fwd"
})
@XmlRootElement(name = "fwdPf")
public class FwdPf {

    @XmlElement(required = true)
    protected BigInteger pfId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String pfCode;
    protected Group group;
    @XmlElement(required = true)
    protected List<Alias> alias;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String currency;
    @XmlElement(required = true)
    protected BigDecimal cvf;
    protected BigDecimal eqPosFactor;
    @XmlElement(required = true)
    protected BigDecimal notionAmt;
    @XmlElement(required = true)
    protected BigInteger priceDl;
    @XmlElement(required = true)
    protected String priceFmt;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String valueMeth;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String priceMeth;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String setlMeth;
    @XmlElement(required = true)
    protected UndPf undPf;
    protected List<Fwd> fwd;

    /**
     * Gets the value of the pfId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPfId() {
        return pfId;
    }

    /**
     * Sets the value of the pfId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPfId(BigInteger value) {
        this.pfId = value;
    }

    /**
     * Gets the value of the pfCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPfCode() {
        return pfCode;
    }

    /**
     * Sets the value of the pfCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPfCode(String value) {
        this.pfCode = value;
    }

    /**
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link Group }
     *     
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Sets the value of the group property.
     * 
     * @param value
     *     allowed object is
     *     {@link Group }
     *     
     */
    public void setGroup(Group value) {
        this.group = value;
    }

    /**
     * Gets the value of the alias property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alias property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlias().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Alias }
     * 
     * 
     */
    public List<Alias> getAlias() {
        if (alias == null) {
            alias = new ArrayList<Alias>();
        }
        return this.alias;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the cvf property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCvf() {
        return cvf;
    }

    /**
     * Sets the value of the cvf property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCvf(BigDecimal value) {
        this.cvf = value;
    }

    /**
     * Gets the value of the eqPosFactor property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEqPosFactor() {
        return eqPosFactor;
    }

    /**
     * Sets the value of the eqPosFactor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEqPosFactor(BigDecimal value) {
        this.eqPosFactor = value;
    }

    /**
     * Gets the value of the notionAmt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNotionAmt() {
        return notionAmt;
    }

    /**
     * Sets the value of the notionAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNotionAmt(BigDecimal value) {
        this.notionAmt = value;
    }

    /**
     * Gets the value of the priceDl property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPriceDl() {
        return priceDl;
    }

    /**
     * Sets the value of the priceDl property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPriceDl(BigInteger value) {
        this.priceDl = value;
    }

    /**
     * Gets the value of the priceFmt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriceFmt() {
        return priceFmt;
    }

    /**
     * Sets the value of the priceFmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriceFmt(String value) {
        this.priceFmt = value;
    }

    /**
     * Gets the value of the valueMeth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueMeth() {
        return valueMeth;
    }

    /**
     * Sets the value of the valueMeth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueMeth(String value) {
        this.valueMeth = value;
    }

    /**
     * Gets the value of the priceMeth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriceMeth() {
        return priceMeth;
    }

    /**
     * Sets the value of the priceMeth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriceMeth(String value) {
        this.priceMeth = value;
    }

    /**
     * Gets the value of the setlMeth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetlMeth() {
        return setlMeth;
    }

    /**
     * Sets the value of the setlMeth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetlMeth(String value) {
        this.setlMeth = value;
    }

    /**
     * Gets the value of the undPf property.
     * 
     * @return
     *     possible object is
     *     {@link UndPf }
     *     
     */
    public UndPf getUndPf() {
        return undPf;
    }

    /**
     * Sets the value of the undPf property.
     * 
     * @param value
     *     allowed object is
     *     {@link UndPf }
     *     
     */
    public void setUndPf(UndPf value) {
        this.undPf = value;
    }

    /**
     * Gets the value of the fwd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fwd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFwd().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Fwd }
     * 
     * 
     */
    public List<Fwd> getFwd() {
        if (fwd == null) {
            fwd = new ArrayList<Fwd>();
        }
        return this.fwd;
    }

}
