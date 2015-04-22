package com.billybyte.spanjava.mongo.span;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanContractId;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class RiskArrayDoc implements MongoDoc {

	private final SpanContractId contractId;
	private final String undCommProdCode;
	private final String optRightCode;
	private final String futConMonth;
	private final String futConDay;
	private final String optConMonth;
	private final String optConDay;
	private final String optStrike;
	private final String settle;
	private final String delta;
	private final String impliedVol;
	private final String[] spanArray;

	public RiskArrayDoc(String exchAcro, String prodCode, String prodTypeCode, String undCommProdCode,
			String optRightCode, String futConMonth, String futConDay,
			String optConMonth, String optConDay, String optStrike,
			String settle, String delta, String impliedVol, String[] spanArray) {
		super();
		this.undCommProdCode = undCommProdCode;
		this.optRightCode = optRightCode;
		this.futConMonth = futConMonth;
		this.futConDay = futConDay;
		this.optConMonth = optConMonth;
		this.optConDay = optConDay;
		this.optStrike = optStrike;
		this.settle = settle;
		this.delta = delta;
		this.impliedVol = impliedVol;
		this.spanArray = spanArray;
		SpanContractId id;
		if(optRightCode==null || optRightCode.trim().length()==0) {
			id = new SpanContractId(exchAcro, prodCode, prodTypeCode, futConMonth, futConDay, null, null, null, null);
		} else {
			id = new SpanContractId(exchAcro, prodCode, prodTypeCode, futConMonth, 
					futConDay, optConMonth, optConDay, optRightCode,optStrike);
		}
		this.contractId = id;
	}

	public SpanContractId getContractId() {
		return contractId;
	}

	public String getUndCommProdCode() {
		return undCommProdCode;
	}

	public String getOptRightCode() {
		return optRightCode;
	}

	public String getFutConMonth() {
		return futConMonth;
	}

	public String getFutConDay() {
		return futConDay;
	}

	public String getOptConMonth() {
		return optConMonth;
	}

	public String getOptConDay() {
		return optConDay;
	}

	public String getOptStrike() {
		return optStrike;
	}

	public String getSettle() {
		return settle;
	}
	
	public String getDelta() {
		return delta;
	}

	public String getImpliedVol() {
		return impliedVol;
	}

	public String[] getSpanArray() {
		return spanArray;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("contractId", contractId.getDBObject());
		doc.put("undCommProdCode", undCommProdCode);
		doc.put("optRightCode", optRightCode);
		doc.put("futConMonth", futConMonth);
		doc.put("futConDay", futConDay);
		doc.put("optConMonth", optConMonth);
		doc.put("optConDay", optConDay);
		doc.put("optStrike", optStrike);
		doc.put("settle", settle);
		doc.put("delta", delta);
		doc.put("impliedVol", impliedVol);
		doc.put("spanArray", spanArray);
		return doc;
	}

	public String toString() {
		return this.getDBObject().toString();
	}
	
}
