package com.billybyte.spanjava.mongo.span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.span.subtypes.InterCommSpreadLeg;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class InterCommSpreadInfo implements MongoDoc {

	private final String commGrpCode;
	private final Integer spreadPriority;
	private final BigDecimal spreadCreditRate;
	private final String spreadMethodCode;
	private final String creditCalcMethod;
	private final String spreadGroupFlag;
	private final String ratePerLegFlag;
	private final String regStatusCode;
	private final List<InterCommSpreadLeg> interCommSpreadLegList;
	
	public InterCommSpreadInfo(String commGrpCode, Integer spreadPriority,
			BigDecimal spreadCreditRate, String spreadMethodCode,
			String creditCalcMethod, String spreadGroupFlag,
			String ratePerLegFlag, String regStatusCode,
			List<InterCommSpreadLeg> interCommSpreadLegList) {
		super();
		this.commGrpCode = commGrpCode;
		this.spreadPriority = spreadPriority;
		this.spreadCreditRate = spreadCreditRate;
		this.spreadMethodCode = spreadMethodCode;
		this.creditCalcMethod = creditCalcMethod;
		this.spreadGroupFlag = spreadGroupFlag;
		this.ratePerLegFlag = ratePerLegFlag;
		this.regStatusCode = regStatusCode;
		this.interCommSpreadLegList = interCommSpreadLegList;
	}

	public String getCommGrpCode() {
		return commGrpCode;
	}

	public Integer getSpreadPriority() {
		return spreadPriority;
	}

	public BigDecimal getSpreadCreditRate() {
		return spreadCreditRate;
	}

	public String getSpreadMethodCode() {
		return spreadMethodCode;
	}

	public String getCreditCalcMethod() {
		return creditCalcMethod;
	}

	public String getSpreadGroupFlag() {
		return spreadGroupFlag;
	}

	public String getRatePerLegFlag() {
		return ratePerLegFlag;
	}

	public String getRegStatusCode() {
		return regStatusCode;
	}

	public List<InterCommSpreadLeg> getInterCommSpreadLegList() {
		return interCommSpreadLegList;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("commGrpCode", commGrpCode);
		doc.put("spreadPriority", spreadPriority);
		doc.put("spreadCreditRate", spreadCreditRate.toString());
		doc.put("spreadMethodCode", spreadMethodCode);
		doc.put("creditCalcMethod", creditCalcMethod);
		doc.put("spreadGroupFlag", spreadGroupFlag);
		doc.put("ratePerLegFlag", ratePerLegFlag);
		doc.put("regStatusCode", regStatusCode);
		List<DBObject> legList = new ArrayList<DBObject>();
		for(InterCommSpreadLeg leg:interCommSpreadLegList){
			legList.add(leg.getDBObject());
		}
		doc.put("interCommSpreadLegList", legList);
		return doc;
	}
	
	public String toString() {
		return this.getDBObject().toString();
	}

}
