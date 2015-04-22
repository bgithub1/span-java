package com.billybyte.spanjava.mongo.expanded;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ExchangeDoc implements MongoDoc {

	private final String exchAcro;
	private final String exchCode;
	
	public ExchangeDoc(String exchAcro, String exchCode) {
		super();
		this.exchAcro = exchAcro;
		this.exchCode = exchCode;
	}

	public String getExchAcro() {
		return exchAcro;
	}

	public String getExchCode() {
		return exchCode;
	}

	public String toString() {
		return this.getDBObject().toString();
	}
	
	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("exchAcro", exchAcro);
		doc.put("exchCode", exchCode);
		return doc;
	}
	
	
	
	
}
