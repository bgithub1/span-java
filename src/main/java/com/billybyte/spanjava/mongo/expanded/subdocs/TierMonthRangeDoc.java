package com.billybyte.spanjava.mongo.expanded.subdocs;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.TierMonthRange;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TierMonthRangeDoc extends TierMonthRange implements MongoDoc {

	public TierMonthRangeDoc(String tierNumber, String startMonth, String endMonth) {
		super(tierNumber, startMonth, endMonth);
	}
	
	public TierMonthRangeDoc(TierMonthRange rec) {
		super(rec.getTierNumber(), rec.getStartMonth(), rec.getEndMonth());
	}
	
	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("tierNumber", this.getTierNumber());
		doc.put("startMonth", this.getStartMonth());
		doc.put("endMonth", this.getEndMonth());
		return doc;
	}

}
