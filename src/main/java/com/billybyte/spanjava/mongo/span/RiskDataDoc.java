package com.billybyte.spanjava.mongo.span;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class RiskDataDoc implements MongoDoc {

//	private final String exchAcro;
//	private final String prodCode;
//	private final String prodTypeCode;
	private final SpanProdId prodId;
	private final String futConMonth;
	private final String futConDay;
	private final String optConMonth;
	private final String optConDay;
	private final String baseVol;
	private final String intRate;
	private final String divYield;
	private final String timeToExp;
	private final String pricingModel;

	public RiskDataDoc(String exchAcro, String prodCode, String prodTypeCode, String futConMonth,
			String futConDay, String optConMonth, String optConDay,
			String baseVol, String intRate,
			String divYield, String timeToExp, String pricingModel) {
		super();
//		this.exchAcro = exchAcro;
//		this.prodCode = prodCode;
//		this.prodTypeCode = prodTypeCode;
		this.prodId = new SpanProdId(exchAcro, prodCode, prodTypeCode);	
		this.futConMonth = futConMonth;
		this.futConDay = futConDay;
		this.optConMonth = optConMonth;
		this.optConDay = optConDay;
		this.baseVol = baseVol;
		this.intRate = intRate;
		this.divYield = divYield;
		this.timeToExp = timeToExp;
		this.pricingModel = pricingModel;
	}

	public SpanProdId getProdId() {
		return prodId;
	}

	public String getFutConMonth() {
		return futConMonth;
	}

	public String getFutConDay() {
		return futConDay;
	}

	public String getOptConMonth() {
		return optConMonth;
	}

	public String getOptConDay() {
		return optConDay;
	}

	public String getBaseVol() {
		return baseVol;
	}

	public String getIntRate() {
		return intRate;
	}

	public String getDivYield() {
		return divYield;
	}

	public String getTimeToExp() {
		return timeToExp;
	}

	public String getPricingModel() {
		return pricingModel;
	}

	public String toString() {
		return this.getDBObject().toString();
	}
	
	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
//		doc.put("exchAcro", exchAcro);
//		doc.put("prodCode", prodCode);
//		doc.put("prodTypeCode", prodTypeCode);
		doc.put("prodId", prodId.getDBObject());
		doc.put("futConMonth", futConMonth);
		doc.put("futConDay", futConDay);
		doc.put("optConMonth", optConMonth);
		doc.put("optConDay", optConDay);
		doc.put("baseVol", baseVol);
		doc.put("intRate", intRate);
		doc.put("divYield", divYield);
		doc.put("timeToExp", timeToExp);
		doc.put("pricingModel", pricingModel);
		return doc;
	}
	
}
