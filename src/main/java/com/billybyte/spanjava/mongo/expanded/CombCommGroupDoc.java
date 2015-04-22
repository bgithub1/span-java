package com.billybyte.spanjava.mongo.expanded;

import java.util.List;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CombCommGroupDoc implements MongoDoc {

	private final String combCommGroupCode;
	private final List<String> combCommCodeList;
	
	public CombCommGroupDoc(String combCommGroupCode,
			List<String> combCommCodeList) {
		super();
		this.combCommGroupCode = combCommGroupCode;
		this.combCommCodeList = combCommCodeList;
	}

	public String getCombCommGroupCode() {
		return combCommGroupCode;
	}

	public List<String> getCombCommCodeList() {
		return combCommCodeList;
	}

	public String toString() {
		return this.getDBObject().toString();
	}
	
	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("combCommGroupCode", combCommGroupCode);
		doc.put("combCommCodeList", combCommCodeList);
		return doc;
	}
	
}
