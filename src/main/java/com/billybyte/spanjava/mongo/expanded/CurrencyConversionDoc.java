package com.billybyte.spanjava.mongo.expanded;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CurrencyConversionDoc implements MongoDoc {

	private final String fromCurrISO;
	private final String fromCurrByte;
	private final String toCurrISO;
	private final String toCurrByte;
	private final String convMult;
	
	public CurrencyConversionDoc(String fromCurrISO, String fromCurrByte,
			String toCurrISO, String toCurrByte, String convMult) {
		super();
		this.fromCurrISO = fromCurrISO;
		this.fromCurrByte = fromCurrByte;
		this.toCurrISO = toCurrISO;
		this.toCurrByte = toCurrByte;
		this.convMult = convMult;
	}

	public String getFromCurrISO() {
		return fromCurrISO;
	}

	public String getFromCurrByte() {
		return fromCurrByte;
	}

	public String getToCurrISO() {
		return toCurrISO;
	}

	public String getToCurrByte() {
		return toCurrByte;
	}

	public String getConvMult() {
		return convMult;
	}

	public String toString() {
		return this.getDBObject().toString();
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("fromCurrISO", fromCurrISO);
		doc.put("fromCurrByte", fromCurrByte);
		doc.put("toCurrISO", toCurrISO);
		doc.put("toCurrByte", toCurrByte);
		doc.put("convMult", convMult);
		return doc;
	}
	
}
