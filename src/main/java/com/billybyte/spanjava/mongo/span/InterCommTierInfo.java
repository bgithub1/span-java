package com.billybyte.spanjava.mongo.span;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.span.subtypes.InterCommTierDetail;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class InterCommTierInfo implements MongoDoc {

	private final String combCommCode;
	private final String spreadMethodCode;
	private final String priceRiskCalcMethod;
	private final List<InterCommTierDetail> interCommTierList;
	
	public InterCommTierInfo(String combCommCode, String spreadMethodCode,
			String priceRiskCalcMethod,
			List<InterCommTierDetail> interCommTierList) {
		super();
		this.combCommCode = combCommCode;
		this.spreadMethodCode = spreadMethodCode;
		this.priceRiskCalcMethod = priceRiskCalcMethod;
		this.interCommTierList = interCommTierList;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public String getSpreadMethodCode() {
		return spreadMethodCode;
	}

	public String getPriceRiskCalcMethod() {
		return priceRiskCalcMethod;
	}

	public List<InterCommTierDetail> getInterCommTierList() {
		return interCommTierList;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("combCommCode",combCommCode);
		doc.put("spreadMethodCode",spreadMethodCode);
		doc.put("priceRiskCalcMethod",priceRiskCalcMethod);
		List<DBObject> tierList = new ArrayList<DBObject>();
		for(InterCommTierDetail detail:interCommTierList) {
			tierList.add(detail.getDBObject());
		}
		doc.put("interCommTierList",tierList);
		return doc;
	}
	
	
	
}
