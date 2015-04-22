package com.billybyte.spanjava.mongo;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanContractId;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SpanHistSettleDoc implements MongoDoc {

	private final SpanContractId contractId;
	private final String YYYYMMDD;
	private final String settle;
	
	public SpanHistSettleDoc(SpanContractId contractId, String YYYMMDD, String settle) {
		super();
		this.contractId = contractId;
		YYYYMMDD = YYYMMDD;
		this.settle = settle;
	}

	public SpanContractId getContractId() {
		return contractId;
	}

	public String getYYYYMMDD() {
		return YYYYMMDD;
	}

	public String getSettle() {
		return settle;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("contractId", contractId.getDBObject());
		doc.put("YYYYMMDD", YYYYMMDD);
		doc.put("settle", settle);
		return doc;
	}

	
	
}
