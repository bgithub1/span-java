package com.billybyte.spanjava.mongo.span.subtypes;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TierRange implements MongoDoc {

	private final String tierNumber;
	private final String startConMonth;
	private final String endConMonth;
	private final String startDay;
	private final String endDay;
	
	public TierRange(String tierNumber, String startConMonth,
			String endConMonth, String startDay, String endDay) {
		super();
		this.tierNumber = tierNumber;
		this.startConMonth = startConMonth;
		this.endConMonth = endConMonth;
		this.startDay = startDay;
		this.endDay = endDay;
	}

	public String getTierNumber() {
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

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("tierNumber", tierNumber);
		doc.put("startConMonth", startConMonth);
		doc.put("endConMonth", endConMonth);
		doc.put("startDay", startDay);
		doc.put("endDay", endDay);
		return doc;
	}
	
	public String toString() {
		return this.getDBObject().toString();
	}
	
}
