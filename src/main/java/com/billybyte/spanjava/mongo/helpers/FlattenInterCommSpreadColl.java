package com.billybyte.spanjava.mongo.helpers;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.billybyte.commonstaticmethods.LoggingUtils;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.mongo.MongoDoc;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.span.FlatInterCommSpreadDoc;
import com.billybyte.spanjava.mongo.span.InterCommSpreadInfo;
import com.billybyte.spanjava.mongo.span.subtypes.InterCommSpreadLeg;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class FlattenInterCommSpreadColl {

	private final MongoWrapper m;
	private final Integer batchSize;
	
	private final LoggingUtils logger = new LoggingUtils(this.getClass());
	
	public FlattenInterCommSpreadColl(MongoWrapper m, Integer batchSize) {
		this.m = m;
		this.batchSize = batchSize;
	}
	
	public void flatten() {
		
		Gson gson = new Gson();

		try {
						
			DB interCommDb = m.getDB(SpanMongoUtils.INTER_COMM_SPREAD_DB);

			DBCollection interCommColl = interCommDb.getCollection(SpanMongoUtils.INTER_COMM_SPREAD_CL);
			
			DBCursor cursor = interCommColl.find();
			
			Map<String,Set<Integer>> combCommGrpLookUpMap = new HashMap<String,Set<Integer>>();
			
			try {
				
				logger.info("Looking up inter comm spread docs...");
				
				while(cursor.hasNext()) {
					
					DBObject dbObj = cursor.next();
					
					InterCommSpreadInfo icsDoc = gson.fromJson(dbObj.toString(), InterCommSpreadInfo.class);
					
					Integer priority = icsDoc.getSpreadPriority();
					
					for(InterCommSpreadLeg leg:icsDoc.getInterCommSpreadLegList()) {
						String combCommCode = leg.getCombCommCode().trim();
						
						if(combCommGrpLookUpMap.containsKey(combCommCode)) {
							combCommGrpLookUpMap.get(combCommCode).add(priority);
						} else {
							Set<Integer> newSet = new HashSet<Integer>();
							newSet.add(priority);
							combCommGrpLookUpMap.put(combCommCode, newSet);
						}
					}
				}
				
				DB flatInterCommDb = m.getDB(SpanMongoUtils.FLAT_INTER_COMM_SPREAD_DB);

				DBCollection flatInterCommColl = flatInterCommDb.getCollection(SpanMongoUtils.FLAT_INTER_COMM_SPREAD_CL);

				flatInterCommColl.remove(new BasicDBObject());
				
				List<MongoDoc> insertList = new ArrayList<MongoDoc>();
				
				for(Entry<String,Set<Integer>> entry:combCommGrpLookUpMap.entrySet()) {
					
					FlatInterCommSpreadDoc flatDoc = 
							new FlatInterCommSpreadDoc(entry.getKey(), new ArrayList<Integer>(entry.getValue()));
					
					insertList.add(flatDoc);
					
				}
				
				SpanMongoUtils.batchInsert(flatInterCommColl, insertList, batchSize);

				logger.info("finished updating");

			} finally {
				cursor.close();
			}
						
		} catch (MongoException e) {
			e.printStackTrace();
		}		
		
	}
	
	public static void main(String[] args) {
		
		String mongoIp = args[0];
		Integer mongoPort = Integer.parseInt(args[1]);
		
		Gson gson = new Gson();
		
		int batchSize = 5000;

		try {
			
			MongoWrapper m = new MongoWrapper(mongoIp, mongoPort);
			
			DB interCommDb = m.getDB(SpanMongoUtils.INTER_COMM_SPREAD_DB);

			DBCollection interCommColl = interCommDb.getCollection(SpanMongoUtils.INTER_COMM_SPREAD_CL);
			
			DBCursor cursor = interCommColl.find();
			
			Map<String,Set<Integer>> combCommGrpLookUpMap = new HashMap<String,Set<Integer>>();
			
			try {
				
				Utils.prt("Looking up inter comm spread docs...");
				
				while(cursor.hasNext()) {
					
					DBObject dbObj = cursor.next();
					
					InterCommSpreadInfo icsDoc = gson.fromJson(dbObj.toString(), InterCommSpreadInfo.class);
					
					Integer priority = icsDoc.getSpreadPriority();
					
					for(InterCommSpreadLeg leg:icsDoc.getInterCommSpreadLegList()) {
						String combCommCode = leg.getCombCommCode().trim();
						
						if(combCommGrpLookUpMap.containsKey(combCommCode)) {
							combCommGrpLookUpMap.get(combCommCode).add(priority);
						} else {
							Set<Integer> newSet = new HashSet<Integer>();
							newSet.add(priority);
							combCommGrpLookUpMap.put(combCommCode, newSet);
						}
						
					}
					
				}
				
				DB flatInterCommDb = m.getDB(SpanMongoUtils.FLAT_INTER_COMM_SPREAD_DB);

				DBCollection flatInterCommColl = flatInterCommDb.getCollection(SpanMongoUtils.FLAT_INTER_COMM_SPREAD_CL);

				flatInterCommColl.remove(new BasicDBObject());
				
				List<MongoDoc> insertList = new ArrayList<MongoDoc>();
				
				for(Entry<String,Set<Integer>> entry:combCommGrpLookUpMap.entrySet()) {
					
					FlatInterCommSpreadDoc flatDoc = 
							new FlatInterCommSpreadDoc(entry.getKey(), new ArrayList<Integer>(entry.getValue()));
					
					insertList.add(flatDoc);
					
				}
				
				SpanMongoUtils.batchInsert(flatInterCommColl, insertList, batchSize);

				Utils.prt("finished updating");

			} finally {
				cursor.close();
			}
						
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}		
	}
	
}
