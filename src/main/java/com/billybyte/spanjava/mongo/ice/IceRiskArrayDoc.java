package com.billybyte.spanjava.mongo.ice;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class IceRiskArrayDoc implements MongoDoc {

	private final String recordType;
	private final String strikePrice;
	private final String contractType;
	private final String lotSize;
	private final String settlementPrice;
	private final String compositeDelta;
	private final String[] lossArray;
	
	public IceRiskArrayDoc(String recordType, String strikePrice,
			String contractType, String lotSize, String settlementPrice,
			String compositeDelta, String[] lossArray) {
		super();
		this.recordType = recordType;
		this.strikePrice = strikePrice;
		this.contractType = contractType;
		this.lotSize = lotSize;
		this.settlementPrice = settlementPrice;
		this.compositeDelta = compositeDelta;
		this.lossArray = lossArray;
	}

	public String getRecordType() {
		return recordType;
	}

	public String getStrikePrice() {
		return strikePrice;
	}

	public String getContractType() {
		return contractType;
	}

	public String getLotSize() {
		return lotSize;
	}

	public String getSettlementPrice() {
		return settlementPrice;
	}

	public String getCompositeDelta() {
		return compositeDelta;
	}

	public String[] getLossArray() {
		return lossArray;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("recordType", recordType);
		doc.put("strikePrice", strikePrice);
		doc.put("contractType", contractType);
		doc.put("lotSize", lotSize);
		doc.put("settlementPrice", settlementPrice);
		doc.put("compositeDelta", compositeDelta);
		doc.put("lossArray", lossArray);
		return doc;
	}
	
	public String toString() {
		return this.getDBObject().toString();
	}
	
}
