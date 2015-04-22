package com.billybyte.spanjava.mongo.span;

import java.util.List;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class FlatInterCommSpreadDoc implements MongoDoc {

	private final String combCommCode;
	private final List<Integer> spreadPriorityList;
	
	public FlatInterCommSpreadDoc(String combCommCode,
			List<Integer> spreadPriorityList) {
		super();
		this.combCommCode = combCommCode;
		this.spreadPriorityList = spreadPriorityList;
	}
	
	public String getCombCommCode() {
		return combCommCode;
	}
	
	public List<Integer> getSpreadPriorityList() {
		return spreadPriorityList;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("combCommCode", combCommCode);
		doc.put("spreadPriorityList", spreadPriorityList);
		return doc;
	}
	
	public String toString() {
		return this.getDBObject().toString();
	}
}
