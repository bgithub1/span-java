package com.billybyte.spanjava.mongo.span;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DeliveryChargeDoc implements MongoDoc {

	private final String combCommCode;
	private final String delivChargeMethodCode;
	private final String shortOptMinCharge;
	private final String perfAdjMember;
	private final String perfAdjHedger;
	private final String perfAdjSpec;
	private final String shortOptCalcMethod;
	
	public DeliveryChargeDoc(String combCommCode, String delivChargeMethodCode,
			String shortOptMinCharge, String perfAdjMember,
			String perfAdjHedger, String perfAdjSpec, String shortOptCalcMethod) {
		super();
		this.combCommCode = combCommCode;
		this.delivChargeMethodCode = delivChargeMethodCode;
		this.shortOptMinCharge = shortOptMinCharge;
		this.perfAdjMember = perfAdjMember;
		this.perfAdjHedger = perfAdjHedger;
		this.perfAdjSpec = perfAdjSpec;
		this.shortOptCalcMethod = shortOptCalcMethod;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public String getDelivChargeMethodCode() {
		return delivChargeMethodCode;
	}

	public String getShortOptMinCharge() {
		return shortOptMinCharge;
	}

	public String getPerfAdjMember() {
		return perfAdjMember;
	}

	public String getPerfAdjHedger() {
		return perfAdjHedger;
	}

	public String getPerfAdjSpec() {
		return perfAdjSpec;
	}

	public String getShortOptCalcMethod() {
		return shortOptCalcMethod;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("combCommCode", combCommCode);
		doc.put("delivChargeMethodCode", delivChargeMethodCode);
		doc.put("shortOptMinCharge", shortOptMinCharge);
		doc.put("perfAdjMember", perfAdjMember);
		doc.put("perfAdjHedger", perfAdjHedger);
		doc.put("perfAdjSpec", perfAdjSpec);
		doc.put("shortOptCalcMethod", shortOptCalcMethod);
		return doc;
	}
	
}
