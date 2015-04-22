package com.billybyte.spanjava.mongo.span.subtypes;

import java.math.BigDecimal;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class InterCommSpreadLeg implements MongoDoc {

	private final String exchAcro;
	private final String reqForScanFlag;
	private final String combCommCode;
	private final BigDecimal deltaSpreadRatio;
	private final String spreadSide;
	private final Integer tierNumber;
	private final String creditRate;
	
	public InterCommSpreadLeg(String exchAcro, String reqForScanFlag,
			String combCommCode, BigDecimal deltaSpreadRatio, String spreadSide, Integer tierNumber, String creditRate) {
		super();
		this.exchAcro = exchAcro;
		this.reqForScanFlag = reqForScanFlag;
		this.combCommCode = combCommCode.trim();
		this.deltaSpreadRatio = deltaSpreadRatio;
		this.spreadSide = spreadSide;
		this.tierNumber = tierNumber;
		this.creditRate = creditRate;
	}

	public String getExchAcro() {
		return exchAcro;
	}

	public String getReqForScanFlag() {
		return reqForScanFlag;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public BigDecimal getDeltaSpreadRatio() {
		return deltaSpreadRatio;
	}

	public String getSpreadSide() {
		return spreadSide;
	}
	
	public Integer getTierNumber() {
		return tierNumber;
	}

	public String getCreditRate() {
		return creditRate;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("exchAcro", exchAcro);
		doc.put("reqForScanFlag", reqForScanFlag);
		doc.put("combCommCode", combCommCode);
		doc.put("deltaSpreadRatio", deltaSpreadRatio.toString());
		doc.put("spreadSide", spreadSide);
		doc.put("tierNumber", tierNumber);
		doc.put("creditRate", creditRate);
		return doc;
	}
	
	public String toString() {
		return this.getDBObject().toString();
	}

}
