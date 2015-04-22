package com.billybyte.spanjava.mongo.expanded.subdocs;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.TierDayWeekRange;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TierDayWeekRangeDoc extends TierDayWeekRange implements MongoDoc {

	public TierDayWeekRangeDoc(String startDayWeekCode, String endDayWeekCode) {
		super(startDayWeekCode, endDayWeekCode);
	}
	
	public TierDayWeekRangeDoc(TierDayWeekRange rec) {
		super(rec.getStartDayWeekCode(), rec.getEndDayWeekCode());
	}
	
	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("startDayWeekCode", this.getStartDayWeekCode());
		doc.put("endDayWeekCode", this.getEndDayWeekCode());
		return doc;
	}

}
