package com.billybyte.spanjava.mains;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.MarketDataComLib;
import com.billybyte.marketdata.SecEnums.SecSymbolType;
import com.billybyte.mongo.MongoDoc;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanImpVolDoc;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.SpanSettleDoc;
import com.billybyte.spanjava.resources.SpanUtils;
import com.google.gson.Gson;
//import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class FindFutImpVolFromOptChain {

	private final static String s  = MarketDataComLib.DEFAULT_SHORTNAME_SEPARATOR;
	private final static String futString = s + SecSymbolType.FUT.toString() + s;
	private final static String fopString = s + SecSymbolType.FOP.toString() + s;
	private final static String futRegexString = "\\"+ s + SecSymbolType.FUT.toString() + "\\" + s;
	private final static int batchSize = 5000;
	
	public static void main(String[] args) {
		
		String mongoIp = args[0];
		Integer mongoPort = Integer.parseInt(args[1]);
		Boolean needsAuth = Boolean.parseBoolean(args[2]);
		String mongoUser = (needsAuth) ? args[3] : null;
		String mongoPw = (needsAuth) ? args[4] : null;
		
		try {
			MongoWrapper m = new MongoWrapper(mongoIp, mongoPort);

			List<DB> dbList = new ArrayList<DB>();
			DB settleDb = m.getDB(SpanMongoUtils.SETTLE_DB);
			dbList.add(settleDb);
			DB impVolDb = m.getDB(SpanMongoUtils.IMP_VOL_DB);
			dbList.add(impVolDb);
			
			for(DB db:dbList) {
				if(needsAuth) {
					if(!db.authenticate(mongoUser, mongoPw.toCharArray())) {
						Utils.prtObErrMess(FindFutImpVolFromOptChain.class, "Failed to authenticate mongo db");
					}
				}
			}
			
			DBCollection settleColl = settleDb.getCollection(SpanMongoUtils.SETTLE_CL);
			DBCollection impVolColl = impVolDb.getCollection(SpanMongoUtils.IMP_VOL_CL);
			
			Calendar begTime = Calendar.getInstance();
			
			Utils.prt("Finding implied volatilities for futures contracts from option chains");
			insertImpVolsForFutures(settleColl, impVolColl);
			
			Calendar endTime = Calendar.getInstance();
			Utils.prt("Finished - "+(endTime.getTimeInMillis()-begTime.getTimeInMillis())/1000+" s");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void insertImpVolsForFutures(DBCollection settleColl, DBCollection impVolColl) {
		
		Gson gson = new Gson();
		
		@SuppressWarnings("unchecked")
		Map<String,String> bestOptSymbolMap = Utils.getXmlData(Map.class, SpanUtils.class, "bestOptSymbolMap.xml");

		Map<String,BigDecimal> impVolMap = new HashMap<String,BigDecimal>();
		
		DBCursor impVolCursor = impVolColl.find();
		
		try {
			while(impVolCursor.hasNext()) {
				DBObject docObj = impVolCursor.next();
				SpanImpVolDoc impVolDoc = gson.fromJson(docObj.toString(), SpanImpVolDoc.class);
				impVolMap.put(impVolDoc.get_id(), new BigDecimal(impVolDoc.getImpVol()));
			}
		} finally {
			impVolCursor.close();
		}
		
		Map<String,TreeSet<BigDecimal>> strikeMap = new HashMap<String,TreeSet<BigDecimal>>();
		for(String shortName:impVolMap.keySet()){
			String[] shortArr = shortName.split("\\.");
			if(shortArr.length>6) {
				String strikeStr = (shortArr.length>7) ? shortArr[6]+"."+shortArr[7] : shortArr[6];
				BigDecimal strike = new BigDecimal(strikeStr);
				String contract = shortArr[0]+"."+shortArr[1]+"."+shortArr[2]+"."+shortArr[3]+"."+shortArr[4];
				if(strikeMap.containsKey(contract)) {
					strikeMap.get(contract).add(strike);
				} else {
					TreeSet<BigDecimal> strikeSet = new TreeSet<BigDecimal>();
					strikeSet.add(strike);
					strikeMap.put(contract, strikeSet);
				}
			}
		}
				
		DBObject queryObj = new BasicDBObject();
		queryObj.put("_id", new BasicDBObject("$regex",futRegexString));
		DBCursor settleCursor = settleColl.find(queryObj);
		
		List<MongoDoc> futImpVolDocList = new ArrayList<MongoDoc>();
		
		Set<String> noImpVolForContract = new HashSet<String>();

		try {
			
			while(settleCursor.hasNext()) {
				DBObject docObj = settleCursor.next();
				
				SpanSettleDoc settleDoc = gson.fromJson(docObj.toString(), SpanSettleDoc.class);
				
				String shortName = settleDoc.get_id();
				BigDecimal settlePrice = new BigDecimal(settleDoc.getSettlePrice());
				
				String[] shortArr = shortName.split("\\.");
				String convKey = shortArr[0]+"."+shortArr[1]+"."+shortArr[2];
				String contract = shortArr[0]+"."+shortArr[1]+"."+shortArr[2]+"."+shortArr[3]+"."+shortArr[4];
				if(bestOptSymbolMap.containsKey(convKey)) {
					contract = contract.replace(convKey, bestOptSymbolMap.get(convKey));
				} else {
					contract = contract.replace(futString, fopString);
				}
				
				if(strikeMap.containsKey(contract)) {
					TreeSet<BigDecimal> strikeSet = strikeMap.get(contract);
					BigDecimal closestStrike = getClosestStrikeFromStrikeMap(settlePrice, strikeSet);
					String optRight = (closestStrike.subtract(settlePrice).compareTo(BigDecimal.ZERO)==1) ? "C" : "P";
					String optString = contract+"."+optRight+"."+closestStrike.toString();
					BigDecimal impVol = impVolMap.get(optString);
					if(impVol==null) {
						if(optRight.compareTo("C")==0) {
							optString = optString.replace(".C.", ".P.");
						} else {
							optString = optString.replace(".P.", ".C.");
						}
						impVol = impVolMap.get(optString);
						if(impVol==null) {
							Utils.prtObErrMess(FindFutImpVolFromOptChain.class, "Null implied vol for option string "+optString);
							continue;
						}
					}

					SpanImpVolDoc impVolDoc = new SpanImpVolDoc(shortName, impVol.toString());
					futImpVolDocList.add(impVolDoc);
					
					if(futImpVolDocList.size()==batchSize) {
						SpanMongoUtils.batchInsert(impVolColl, futImpVolDocList);
						futImpVolDocList.clear();
					}

					
				} else {
					noImpVolForContract.add(contract);
				}
				
			}
		} finally {
			settleCursor.close();
		}
		
		SpanMongoUtils.batchInsert(impVolColl, futImpVolDocList);

		for(String missingContract:noImpVolForContract) {
			Utils.prtObErrMess(FindFutImpVolFromOptChain.class, "No implied vol for contract: "+missingContract);
		}
		Utils.prt(noImpVolForContract.size());
	}
	
	private static BigDecimal getClosestStrikeFromStrikeMap(BigDecimal settle, TreeSet<BigDecimal> strikeSet) {
		BigDecimal lower = strikeSet.lower(settle);
		BigDecimal higher = strikeSet.ceiling(settle);
		if(lower==null) return higher;
		if(higher==null) return lower;
		if(higher.subtract(settle).compareTo(settle.subtract(lower))==1) {
			return lower;
		} else {
			return higher;
		}
	}
	
}
