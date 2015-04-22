package com.billybyte.spanjava.mongo;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.mongo.MongoDoc;
import com.billybyte.mongo.MongoWrapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SpanMongoUtils {

	public static String EXCH_DB = "spanExchDb";
	public static String EXCH_CL = "spanExchColl";	

	public static String CURR_DB = "spanCurrConvDb";
	public static String CURR_CL = "spanCurrConvColl";	
	
	public static String ARRAY_DB = "spanArrayDb";
	public static String ARRAY_CL = "spanArrayColl";	
	
	public static String RISK_DATA_DB = "spanRiskDataDb";
	public static String RISK_DATA_CL = "spanRiskDataColl";

	public static String COMM_GRP_DB = "spanCommGrpDb";
	public static String COMM_GRP_CL = "spanCommGrpColl";

	public static String PRICE_SPEC_DB = "spanPriceSpecDb";
	public static String PRICE_SPEC_CL = "spanPriceSpecColl";

	public static String SETTLE_DB = "spanSettleDb";
	public static String SETTLE_CL = "spanSettleColl";

	public static String FIRST_COMB_COMM_DB = "spanFirstCombCommDb";
	public static String FIRST_COMB_COMM_CL = "spanFirstCombCommColl";

	public static String SECOND_COMB_COMM_DB = "spanSecondCombCommDb";
	public static String SECOND_COMB_COMM_CL = "spanSecondCombCommColl";

	public static String COMB_COMM_DB = "spanCombCommDb";
	public static String COMB_COMM_CL = "spanCombCommColl";

	public static String FLAT_COMB_COMM_DB = "spanFlatCombCommDb";
	public static String FLAT_COMB_COMM_CL = "spanFlatCombCommColl";

	public static String INTER_MONTH_SPREAD_DB = "spanInterMonthSpreadDb";
	public static String INTER_MONTH_SPREAD_CL = "spanInterMonthSpreadColl";

	public static String INTER_COMM_SPREAD_DB = "spanInterCommSpreadDb";
	public static String INTER_COMM_SPREAD_CL = "spanInterCommSpreadColl";

	public static String FLAT_INTER_COMM_SPREAD_DB = "spanFlatInterCommSpreadDb";
	public static String FLAT_INTER_COMM_SPREAD_CL = "spanFlatInterCommSpreadColl";

	public static String INTER_COMM_TIER_DB = "spanInterCommTierDb";
	public static String INTER_COMM_TIER_CL = "spanInterCommTierColl";

	public static String DELIV_CHARGE_DB = "spanDelivChargeDb";
	public static String DELIV_CHARGE_CL = "spanDelivChargeColl";

	public static String IMP_VOL_DB = "spanImpVolDb";
	public static String IMP_VOL_CL = "spanImpVolColl";

	public static String HIST_SETTLES_DB = "spanRecHistSettlesDb"; // "spanHistSettlesDb"
	public static String HIST_SETTLES_CL = "spanRecHistSettlesColl"; // "spanHistSettlesCl"
	
	public static void batchInsert(DBCollection coll, List<MongoDoc> docList) {
		for(MongoDoc doc:docList) {
			try {
				coll.insert(doc.getDBObject());
			} catch (Exception e) {
				Utils.prtObErrMess(SpanMongoUtils.class, e.getLocalizedMessage());
			}
		}
//		List<DBObject> uploadList = new ArrayList<DBObject>();
//		Set<String> idSet = new HashSet<String>();
//		for(MongoDoc doc:docList) {
//			DBObject dbObj = doc.getDBObject();
//			if(dbObj.containsField("_id")) {
//				String id = dbObj.get("_id").toString();
//				if(idSet.contains(id)) {
//					Utils.prtObErrMess(SpanMongoUtils.class, "Trying to insert duplicate MongoDoc _id - "+id);
//				} else {
//					idSet.add(id);
//					uploadList.add(dbObj);
//				}
//			} else {
//				uploadList.add(dbObj);
//			}
//		}
//		try {
//			coll.insert(uploadList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		uploadList.clear();
	}
	
	public static void batchInsert(DBCollection coll, List<MongoDoc> docList, int batchSize) {
		
		List<DBObject> batchInsertList = new ArrayList<DBObject>();
		
		for(MongoDoc doc:docList) {
			batchInsertList.add(doc.getDBObject());
			
			if(batchInsertList.size()==batchSize) {
				coll.insert(batchInsertList);
				batchInsertList.clear();
			}
		}
		
		if(!batchInsertList.isEmpty()) {
			coll.insert(batchInsertList);
			batchInsertList.clear();
		}
		
	}
	
	public static String formatPriceWithDecimal(String settleString, Integer decSpaces) {
		boolean isNeg = settleString.contains("-");
		if(isNeg) settleString = settleString.replace("-","");
		String decimalSettleString;
		if(settleString.length()<decSpaces) {
			decimalSettleString = "0.";
			for(int i=0;i<decSpaces-settleString.length();i++) {
				decimalSettleString = decimalSettleString+"0";
			}
			decimalSettleString = decimalSettleString+settleString;
		} else {
			decimalSettleString = settleString.substring(0,settleString.length()-decSpaces)+"."+
					settleString.substring(settleString.length()-decSpaces);
		}
		if(isNeg) decimalSettleString = "-"+decimalSettleString;
		return decimalSettleString;
	}
	
	public static DBCursor getCursor(
			MongoWrapper m,
			String dbName,
			String colName){
		DB db = m.getDB(dbName);
		DBCollection coll = db.getCollection(colName);
		DBCursor ret = coll.find();
		return ret;
	}
	
	public static DBCursor getSpanSettleCursor(			
			MongoWrapper m){
		return getCursor(m,SpanMongoUtils.SETTLE_DB,
				SpanMongoUtils.SETTLE_CL);
	}

	public static DBCursor getSpanImpVolCursor(			
			MongoWrapper m){
		return getCursor(m,SpanMongoUtils.IMP_VOL_DB,
				SpanMongoUtils.IMP_VOL_CL);
	}

}
