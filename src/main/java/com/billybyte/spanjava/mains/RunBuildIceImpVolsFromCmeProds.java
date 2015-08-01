package com.billybyte.spanjava.mains;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.mongo.MongoDatabaseNames;
import com.billybyte.mongo.MongoDoc;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanImpVolDoc;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.SpanSettleDoc;
import com.billybyte.spanjava.resources.SpanUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class RunBuildIceImpVolsFromCmeProds {

	private final static String iceRegex = "((ICE)|(IPE))";
	private final static int batchSize = 5000;
	
	public static void main(String[] args) {
		
		String mongoIp = args[0];
		Integer mongoPort = Integer.parseInt(args[1]);
		Boolean needsAuth = Boolean.parseBoolean(args[2]);
		String mongoUser = (needsAuth) ? args[3] : null;
		String mongoPw = (needsAuth) ? args[4] : null;
		
		MongoWrapper m;
		try {
			m = new MongoWrapper(mongoIp, mongoPort);

//			List<DB> dbList = new ArrayList<DB>();
//			dbList.add(settleDb);
//			dbList.add(impVolDb);
			DB settleDb; // = m.getDB(SpanMongoUtils.SETTLE_DB);
			DB impVolDb; // = m.getDB(SpanMongoUtils.IMP_VOL_DB);
			
			if(needsAuth) {
				settleDb = m.getAuthDB(SpanMongoUtils.SETTLE_DB, mongoUser, mongoPw);
				impVolDb = m.getAuthDB(SpanMongoUtils.IMP_VOL_DB, mongoUser, mongoPw);
//				for(DB db:dbList) {
//					if(!db.authenticate(mongoUser, mongoPw.toCharArray())) {
//						Utils.prtObErrMess(RunBuildIceImpVolsFromCmeProds.class, "Unable to authenticate mongo db: "+db.getName());				
//					}
//				}
			} else {
				settleDb = m.getDB(SpanMongoUtils.SETTLE_DB);
				impVolDb = m.getDB(SpanMongoUtils.IMP_VOL_DB);
			}
			
			DBCollection settleColl = settleDb.getCollection(SpanMongoUtils.SETTLE_CL);
			DBCollection impVolColl = impVolDb.getCollection(SpanMongoUtils.IMP_VOL_CL);
			
			buildIceImpVolsFromCmeDb(settleColl, impVolColl);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * Ice span does not have implied vols, but the cme does.
	 * 
	 * Build vols for ice/cme look-a-likes
	 * 
	 * @param spanSettleColl
	 * @param impVolColl
	 */
	public static void buildIceImpVolsFromCmeDb(DBCollection spanSettleColl, DBCollection impVolColl) {
		
		@SuppressWarnings("unchecked")
//		Map<String,String> iceCmeAssocMap = Utils.getXmlData(Map.class, SpanUtils.class, "iceToCmeProdConvMap.xml");
		Map<String,String> iceCmeAssocMap = (Map<String,String>)Utils.getXmlData(Map.class, null, "iceToCmeProdConvMap.xml");
		for(Entry<String, String> entry : iceCmeAssocMap.entrySet()){
			updateRecs(entry,spanSettleColl,impVolColl);
		}
		
//		// get all short names for ice products from the settlement collection
//		DBObject iceSettleQuery = new BasicDBObject();
//		iceSettleQuery.put("_id", new BasicDBObject("$regex",iceRegex));
//		
//		DBCursor settleCursor = spanSettleColl.find(iceSettleQuery);
//		
//		Map<String,String> impVolConvMap = new HashMap<String,String>();
//		Set<String> missingKeys = new HashSet<String>();
//		
//		try {
//			
//			while(settleCursor.hasNext()) {
//				
//				DBObject settleDoc = settleCursor.next();
//				
//				// if the key of the ice short name is in the conversion map, add it to the cme/ice conversion map
//				String shortName = (String) settleDoc.get("_id");
//				String[] shortSplit = shortName.split("\\.");
//				String shortKey = shortSplit[0]+"."+shortSplit[1]+"."+shortSplit[2];
//				
//				if(iceCmeAssocMap.containsKey(shortKey)) {
//					String cmeKey = iceCmeAssocMap.get(shortKey);
//					String cmeShort = cmeKey+"."+shortSplit[3]+"."+shortSplit[4];
//					if(shortSplit.length>5) cmeShort = cmeShort+"."+shortSplit[5]+"."+shortSplit[6];
//					if(shortSplit.length>7) cmeShort = cmeShort+"."+shortSplit[7];
//					impVolConvMap.put(cmeShort, shortName);
//					
//				} else {
//					missingKeys.add(shortKey);
//				}
//				
//			}
//			
//		} finally {
//			settleCursor.close();
//		}
//		
//		// get all implied vols for the cme-converted short names
//		DBObject impVolQuery = new BasicDBObject();
//		impVolQuery.put("_id", new BasicDBObject("$in",impVolConvMap.keySet()));
//		
//		DBCursor impVolCursor = impVolColl.find(impVolQuery);
//		
//		List<MongoDoc> impVolInsertList = new ArrayList<MongoDoc>();
//		
//		try {
//			
//			while(impVolCursor.hasNext()) {
//				
//				// map the vols back to their corresponding ice products and add them to the implied vol collection
//				DBObject doc = impVolCursor.next();				
//				String cmeShort = (String) doc.get("_id");
//				String iceShort = impVolConvMap.get(cmeShort);
//				MongoDoc impVolDoc = new SpanImpVolDoc(iceShort, (String) doc.get("impVol"));
//				impVolInsertList.add(impVolDoc);
//				
//				if(impVolInsertList.size()==batchSize) {
//					SpanMongoUtils.batchInsert(impVolColl, impVolInsertList, batchSize);
//					impVolInsertList.clear();
//				}
//			}
//			
//		} finally {
//			impVolCursor.close();
//		}
//		
//		SpanMongoUtils.batchInsert(impVolColl, impVolInsertList, batchSize);
//		impVolInsertList.clear();
//		
//		for(String missingIceKey:missingKeys) {
//			Utils.prtObErrMess(RunBuildIceImpVolsFromCmeProds.class,missingIceKey);
//		}
//		
//		Utils.prtObErrMess(RunBuildIceImpVolsFromCmeProds.class,missingKeys.size()+" missing keys");
	}
	
	private static void updateRecs(Entry<String, String> iceToCmeConvEntry, DBCollection spanSettleColl, DBCollection impVolColl){
		String icePartial = iceToCmeConvEntry.getKey();
		String cmePartial = iceToCmeConvEntry.getValue();
		DBCursor cmeSetCurr = impVolColl.find(new BasicDBObject("_id",new BasicDBObject("$regex",cmePartial)));
		Map<String,MongoDoc> potentialIceSnsToUse = new HashMap<String, MongoDoc>();
		// get a bunch of cme shortNames, and turn them into ice shortNames
		try {
			while(cmeSetCurr.hasNext()){
				DBObject doc = cmeSetCurr.next();	
				String cmeSn = (String)doc.get("_id");
				String iceSn = cmeSn.replace(cmePartial, icePartial);
				String impVolData =  (String) doc.get("impVol");
				MongoDoc impVolDoc = new SpanImpVolDoc(iceSn,impVolData);
				potentialIceSnsToUse.put(iceSn, impVolDoc);
			}
		} finally {
			cmeSetCurr.close();
		}
		// get the subset of the iceshortnames that have settlements
		Map<String,MongoDoc> actualIceSnsToUse = new HashMap<String, MongoDoc>();
		DBCursor iceSetCurr = spanSettleColl.find(new BasicDBObject("_id",new BasicDBObject("$in", potentialIceSnsToUse.keySet())));
		try {
			while(iceSetCurr.hasNext()){
				DBObject doc = iceSetCurr.next();
				String iceSn = ((String)doc.get("_id"));
				actualIceSnsToUse.put(iceSn,potentialIceSnsToUse.get(iceSn));
			}
		} finally {
			iceSetCurr.close();
		}
		// now remove keys that are there
		DBObject keysToRemove = new BasicDBObject(new BasicDBObject("_id",new BasicDBObject("$in", actualIceSnsToUse.keySet())));
		impVolColl.remove(keysToRemove);
		List<MongoDoc> volsToInsert = new ArrayList<MongoDoc>(actualIceSnsToUse.values());
		SpanMongoUtils.batchInsert(impVolColl, volsToInsert, batchSize);
		
		
	}
	
}
