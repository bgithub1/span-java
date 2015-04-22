package com.billybyte.spanjava.mongo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.billybyte.mongo.MongoDoc;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SpanImpVolDoc implements MongoDoc {

	private final String _id;
	private final String impVol;
	
	public SpanImpVolDoc(String _id, String impVol) {
		super();
		this._id = _id;
		this.impVol = impVol;
	}
	
	public String get_id() {
		return _id;
	}
	
	public String getImpVol() {
		return impVol;
	}

	public String toString() {
		return this.getDBObject().toString();
	}
	
	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("_id", _id);
		doc.put("impVol", impVol);
		return doc;
	}
	
	/**
	 * 
	 * @param cur
	 * @param yyyyMmDd
	 * @return Map<String, BigDecimal> 
	 */
	public  static  Map<String, BigDecimal> getImpVolFromMongoDocs(
			DBCursor cur,Long yyyyMmDd){
		Gson gson = new Gson();
		Map<String, BigDecimal> ret = new HashMap<String, BigDecimal>();
		try {
			while(cur.hasNext()) {
				DBObject docObj = cur.next();
				SpanImpVolDoc doc = gson.fromJson(docObj.toString(), SpanImpVolDoc.class);
				BigDecimal impVol = new BigDecimal(doc.getImpVol());
				ret.put(doc.get_id(),impVol );
			}
		} finally {
			cur.close();
		}
		
		return ret;
		
	}

}
