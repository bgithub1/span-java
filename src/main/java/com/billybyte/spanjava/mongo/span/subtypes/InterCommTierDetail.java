package com.billybyte.spanjava.mongo.span.subtypes;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class InterCommTierDetail implements MongoDoc {

	private final Integer tierNumber;
	private final String startConMonth;
	private final String endConMonth;
	private final String startDay;
	private final String endDay;
	private final String shortOptMinCharge;
	
	public InterCommTierDetail(Integer tierNumber, String startConMonth,
			String endConMonth, String startDay, String endDay,
			String shortOptMinCharge) {
		super();
		this.tierNumber = tierNumber;
		this.startConMonth = startConMonth;
		this.endConMonth = endConMonth;
		this.startDay = startDay;
		this.endDay = endDay;
		this.shortOptMinCharge = shortOptMinCharge;
	}

	public Integer getTierNumber() {
		return tierNumber;
	}

	public String getStartConMonth() {
		return startConMonth;
	}

	public String getEndConMonth() {
		return endConMonth;
	}

	public String getStartDay() {
		return startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public String getShortOptMinCharge() {
		return shortOptMinCharge;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("tierNumber", tierNumber);
		doc.put("startConMonth", startConMonth);
		doc.put("endConMonth", endConMonth);
		doc.put("startDay", startDay);
		doc.put("endDay", endDay);
		doc.put("shortOptMinCharge", shortOptMinCharge);
		return doc;
	}
	
	
	
}
