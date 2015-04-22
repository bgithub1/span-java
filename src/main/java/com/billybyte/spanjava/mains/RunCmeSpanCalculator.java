package com.billybyte.spanjava.mains;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.span.RiskArrayDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanContractId;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.billybyte.spanjava.utils.CmeSpanCalculator;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class RunCmeSpanCalculator {
	
	public static void main(String[] args) {
	
		try {
			
			Gson gson = new Gson();
			
			MongoWrapper m = new MongoWrapper("localhost",27017);

			DB spanArrayDb = m.getDB(SpanMongoUtils.ARRAY_DB);
			DB flatCombCommDb = m.getDB(SpanMongoUtils.FLAT_COMB_COMM_DB);
			DB interMonthDb = m.getDB(SpanMongoUtils.INTER_MONTH_SPREAD_DB);
			DB flatInterCommDb = m.getDB(SpanMongoUtils.FLAT_INTER_COMM_SPREAD_DB);
			DB interCommSpreadDb = m.getDB(SpanMongoUtils.INTER_COMM_SPREAD_DB);
			DB interCommTierDb = m.getDB(SpanMongoUtils.INTER_COMM_TIER_DB);
			DB delivChargeDb = m.getDB(SpanMongoUtils.DELIV_CHARGE_DB);
			
			DBCollection riskArrayColl = spanArrayDb.getCollection(SpanMongoUtils.ARRAY_CL);
			DBCollection flatCombCommColl = flatCombCommDb.getCollection(SpanMongoUtils.FLAT_COMB_COMM_CL);
			DBCollection interMonthColl = interMonthDb.getCollection(SpanMongoUtils.INTER_MONTH_SPREAD_CL);
			DBCollection flatInterCommColl = flatInterCommDb.getCollection(SpanMongoUtils.FLAT_INTER_COMM_SPREAD_CL);
			DBCollection interCommSpreadColl = interCommSpreadDb.getCollection(SpanMongoUtils.INTER_COMM_SPREAD_CL);
			DBCollection interCommTierColl = interCommTierDb.getCollection(SpanMongoUtils.INTER_COMM_TIER_CL);
			DBCollection delivChargeColl = delivChargeDb.getCollection(SpanMongoUtils.DELIV_CHARGE_CL);
			
			CmeSpanCalculator calc = 
					new CmeSpanCalculator(
							riskArrayColl, flatCombCommColl, interMonthColl, interCommSpreadColl, 
							flatInterCommColl, interCommTierColl, delivChargeColl);

			SpanProdId hoProdId = new SpanProdId("NYM", "HO", "FUT");
			SpanProdId clProdId = new SpanProdId("NYM", "CL", "FUT");
			SpanProdId optProdId = new SpanProdId("NYM", "ON", "OOF");

			DBObject queryObj1 = new BasicDBObject();
			queryObj1.put("contractId.prodId", hoProdId.getDBObject());
			queryObj1.put("futConMonth", "201306");
			
			DBObject queryObj2 = new BasicDBObject();
			queryObj2.put("contractId.prodId", clProdId.getDBObject());
			queryObj2.put("futConMonth", "201306");

			DBObject queryObj3 = new BasicDBObject();
			queryObj3.put("contractId.prodId", optProdId.getDBObject());
			queryObj3.put("futConMonth", "201312");

			RiskArrayDoc sampleDoc1 = gson.fromJson(riskArrayColl.findOne(queryObj1).toString(), RiskArrayDoc.class);
			RiskArrayDoc sampleDoc2 = gson.fromJson(riskArrayColl.findOne(queryObj2).toString(), RiskArrayDoc.class);
			RiskArrayDoc sampleDoc3 = gson.fromJson(riskArrayColl.findOne(queryObj3).toString(), RiskArrayDoc.class);
			
			Map<SpanContractId,Integer> posQtyMap = new HashMap<SpanContractId,Integer>();
			SpanContractId id1 = new SpanContractId(
					sampleDoc1.getContractId().getProdId().getExchAcro(), sampleDoc1.getContractId().getProdId().getProdCommCode(), 
					sampleDoc1.getContractId().getProdId().getProdTypeCode(), 
					sampleDoc1.getContractId().getFutMonth(), sampleDoc1.getContractId().getFutDayWeek(), 
					sampleDoc1.getContractId().getOptMonth(), sampleDoc1.getContractId().getOptDayWeek(), 
					sampleDoc1.getContractId().getOptRightCode(), sampleDoc1.getContractId().getStrike());
			SpanContractId id2 = new SpanContractId(
					sampleDoc2.getContractId().getProdId().getExchAcro(), sampleDoc2.getContractId().getProdId().getProdCommCode(), 
					sampleDoc2.getContractId().getProdId().getProdTypeCode(), 
					sampleDoc1.getContractId().getFutMonth(), sampleDoc1.getContractId().getFutDayWeek(), 
					sampleDoc1.getContractId().getOptMonth(), sampleDoc1.getContractId().getOptDayWeek(), 
					sampleDoc2.getContractId().getOptRightCode(), sampleDoc2.getContractId().getStrike());
			SpanContractId id3 = new SpanContractId(
					sampleDoc3.getContractId().getProdId().getExchAcro(), sampleDoc3.getContractId().getProdId().getProdCommCode(), 
					sampleDoc3.getContractId().getProdId().getProdTypeCode(), 
					sampleDoc1.getContractId().getFutMonth(), sampleDoc1.getContractId().getFutDayWeek(), 
					sampleDoc1.getContractId().getOptMonth(), sampleDoc1.getContractId().getOptDayWeek(), 
					sampleDoc3.getContractId().getOptRightCode(), sampleDoc3.getContractId().getStrike());

			Utils.prt(id1.toString()+","+sampleDoc1.getSettle());
			Utils.prt(id2.toString()+","+sampleDoc2.getSettle());
			Utils.prt(id3.toString()+","+sampleDoc3.getSettle());

			posQtyMap.put(id1, 10); // ho -3
			posQtyMap.put(id2, -10); // cl 10
			posQtyMap.put(id3, 0); // lo -4

			BigDecimal margin = calc.calculateSpanMargin(posQtyMap);

			Utils.prt(margin.toString());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
	}
	
}
