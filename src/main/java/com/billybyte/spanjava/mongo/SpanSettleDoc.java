package com.billybyte.spanjava.mongo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.billybyte.marketdata.SettlementDataImmute;
import com.billybyte.mongo.MongoDoc;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SpanSettleDoc implements MongoDoc {
	
	private final String _id;
	private final String settlePrice;
	
	public SpanSettleDoc(String _id, String settlePrice) {
		super();
		this._id = _id;
		this.settlePrice = settlePrice;
	}
	
	public String get_id() {
		return _id;
	}
	
	public String getSettlePrice() {
		return settlePrice;
	}

	public String toString() {
		return this.getDBObject().toString();
	}
	
	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("_id", _id);
		doc.put("settlePrice", settlePrice);
		return doc;
	}
	
	public  static  Map<String, SettlementDataImmute> getSettlementDataInterfaceMapFromMongoDocs(
			DBCursor cur,Long yyyyMmDd){
		Gson gson = new Gson();
		
		Map<String, SettlementDataImmute> ret = new HashMap<String, SettlementDataImmute>();
		try {
			while(cur.hasNext()) {
				DBObject docObj = cur.next();
				SpanSettleDoc doc = gson.fromJson(docObj.toString(), SpanSettleDoc.class);
				String shortName = doc.get_id();
				BigDecimal price = new BigDecimal(doc.getSettlePrice());
				
				SettlementDataImmute settle = 
						new SettlementDataImmute(shortName, price, 1, yyyyMmDd);
				ret.put(doc.get_id(),settle );
			}
		} finally {
			cur.close();
		}
		
		return ret;
		
	}
	
	

}
