package com.billybyte.spanjava.mains;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.span.RiskArrayDoc;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class FindRiskArraySet {

	public static void main(String[] args) {
		
		try {
			
			Gson gson = new Gson();
			
			Utils.prt("loading docs...");
			
			MongoWrapper m = new MongoWrapper("127.0.0.1", 27017);
			
			DB db = m.getDB(SpanMongoUtils.ARRAY_DB);

			DBCollection coll = db.getCollection(SpanMongoUtils.ARRAY_CL);
			
			DBCursor cursor = coll.find();
			
			Map<String,Set<String>> prodCodeTypeMap = new HashMap<String,Set<String>>();
			
			try {
				
				while(cursor.hasNext()) {

					DBObject obj = cursor.next();
					RiskArrayDoc doc = gson.fromJson(obj.toString(), RiskArrayDoc.class);
					
					String prodCode = doc.getContractId().getProdId().getProdCommCode();
					String prodTypeCode = doc.getContractId().getProdId().getProdTypeCode();
					
					if(prodCodeTypeMap.containsKey(prodCode)) {
						prodCodeTypeMap.get(prodCode).add(prodTypeCode);
					} else {
						Set<String> newSet = new HashSet<String>();
						newSet.add(prodTypeCode);
						prodCodeTypeMap.put(prodCode, newSet);
					}
					
				}
				
			} finally {
				cursor.close();
			}
			
			for(Entry<String,Set<String>> entry:prodCodeTypeMap.entrySet()) {
				if(entry.getValue().size()>1){
					String str = entry.getKey();
					for(String prodType:entry.getValue()) {
						str = str+","+prodType;
					}
					Utils.prt(str);
				}
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
}
