package com.billybyte.spanjava.mongo.span.subtypes;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class InterMonthSpreadDetail implements MongoDoc {

	private final Integer spreadPriority;
	private final Integer numOfLegs;
	private final String chargeRate;
	private final List<InterMonthSpreadLeg> spreadLegList;
	
	public InterMonthSpreadDetail(Integer spreadPriority, Integer numOfLegs,
			String chargeRate, List<InterMonthSpreadLeg> spreadLegList) {
		super();
		this.spreadPriority = spreadPriority;
		this.numOfLegs = numOfLegs;
		this.chargeRate = chargeRate;
		this.spreadLegList = spreadLegList;
	}

	public Integer getSpreadPriority() {
		return spreadPriority;
	}

	public Integer getNumOfLegs() {
		return numOfLegs;
	}

	public String getChargeRate() {
		return chargeRate;
	}

	public List<InterMonthSpreadLeg> getSpreadLegList() {
		return spreadLegList;
	}

	public String toString() {
		return this.getDBObject().toString();
	}
	
	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("spreadPriority", spreadPriority);
		doc.put("numOfLegs", numOfLegs);
		doc.put("chargeRate", chargeRate);
		List<DBObject> legList = new ArrayList<DBObject>();
		for(InterMonthSpreadLeg leg:spreadLegList) {
			legList.add(leg.getDBObject());
		}
		doc.put("spreadLegList", legList);
		return doc;
	}
	
	

}
