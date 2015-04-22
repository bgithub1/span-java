package com.billybyte.spanjava.mongo.expanded.subdocs;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.RiskArrayParamBlock;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class RiskArrayParamBlockDoc extends RiskArrayParamBlock implements MongoDoc {

	public RiskArrayParamBlockDoc(String commProdCode, String conType, String riskArrValDecLoc, String riskArrValDecSign) {
		super(commProdCode.trim(), conType.trim(), riskArrValDecLoc, riskArrValDecSign);
	}

	public RiskArrayParamBlockDoc(RiskArrayParamBlock rec) {
		super(rec.getCommProdCode().trim(), rec.getProdType().trim(), rec.getRiskArrValDecLoc(), rec.getRiskArrValDecSign());
	}
	
	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("commProdCode", this.getCommProdCode());
		doc.put("conType", this.getProdType());
		doc.put("riskArrValDecLoc", this.getRiskArrValDecLoc());
		doc.put("riskArrValDecSign", this.getRiskArrValDecSign());
		return doc;
	}

}
