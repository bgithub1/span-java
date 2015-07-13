package com.billybyte.spanjava.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.billybyte.commonstaticmethods.Dates;
import com.billybyte.commonstaticmethods.RegexMethods;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.SecDef;
import com.billybyte.marketdata.SecDefSimple;
import com.billybyte.marketdata.SecEnums.SecCurrency;
import com.billybyte.marketdata.SecEnums.SecExchange;
import com.billybyte.marketdata.ShortNameInfo;
import com.billybyte.marketdata.SecEnums.SecSymbolType;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.expanded.ProductSpecsDoc;
import com.billybyte.spanjava.mongo.span.RiskArrayDoc;
import com.billybyte.spanjava.mongo.span.RiskDataDoc;

import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.billybyte.ui.RedirectedConsoleForJavaProcess;
import com.billybyte.ui.RedirectedConsoleForJavaProcess.ConsoleType;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class ToSecDef {
	private static final Gson gson = new Gson();
	private static final BigDecimal TIMETOEXPIRY_MULTIPLIER = 	BigDecimal.ONE.divide(BigDecimal.TEN.pow(6)).multiply(new BigDecimal("365"));
	
	
	
	public static void main(String[] args) {
		Utils.prtObErrMess(ToSecDef.class, Calendar.getInstance().getTime().toString());
		Map<String, String> argPairs = 
				Utils.getArgPairsSeparatedByChar(args, "=");
		new RedirectedConsoleForJavaProcess(1000, 1000,1,1,ToSecDef.class.getSimpleName(),ConsoleType.SYSTEM_OUT);
		
		// Determine date of Span run
		Calendar dateOfSpan = null;
		
		String spanFilePath = argPairs.get("spanFilePath");
		if(spanFilePath!=null){
			List<String> lines = Utils.getLineData(null, spanFilePath);
			String line = lines.get(0);
			String yearRegex = "2[0-2][0-9]{2, 2}";
			String monthRegex = "((0[1-9])|(1[0-2))";
			String dayRegex = "((0[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))";
			String yyyyMmDdRegex = yearRegex+monthRegex+dayRegex;
			List<String> regexResults = RegexMethods.getRegexMatches(yyyyMmDdRegex,line);
			String yyMmDdString = regexResults.get(0);
			dateOfSpan = Dates.getCalendarFromYYYYMMDD(new Long(yyMmDdString)); 
			lines = null; // force garbage collection
		}else{
			dateOfSpan = Dates.getCalendarFromYYYYMMDD(new Long(argPairs.get("yyyyMmDdOfSpan")));
		}
				
		SpanProdId[] idArray = {
				new SpanProdId("NYM", "LO1", "OOF"),
				new SpanProdId("NYM", "LO2", "OOF"),
				new SpanProdId("NYM", "LO3", "OOF"),
				new SpanProdId("NYM", "LO4", "OOF"),
				new SpanProdId("NYM", "LO5", "OOF"),
				new SpanProdId("NYM", "CS", "FUT"),
				new SpanProdId("NYM", "AO", "OOF"),
				new SpanProdId("NYM", "AO", "OOC"),
//				new SpanProdId("NYM", "ON", "OOF"),
//				new SpanProdId("NYM", "NG", "FUT"),
		};
		
		List<SpanProdId> prodIdList = Arrays.asList(idArray);
		
		String mongoIp = "127.0.0.1";
		int mongoPort = 27022;
		MongoWrapper m=null;
		try {
			m = new MongoWrapper(mongoIp,mongoPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// PriceSpec
		DB priceSpecDb = m.getDB(SpanMongoUtils.PRICE_SPEC_DB);
		DBCollection priceSpecColl = priceSpecDb.getCollection(SpanMongoUtils.PRICE_SPEC_CL);
		// RiskData
		DB riskDataDb = m.getDB(SpanMongoUtils.RISK_DATA_DB);
		DBCollection riskDataColl = riskDataDb.getCollection(SpanMongoUtils.RISK_DATA_CL);
		// RiskArray
		DB arrayDb = m.getDB(SpanMongoUtils.ARRAY_DB);
		DBCollection spanArrayColl = arrayDb.getCollection(SpanMongoUtils.ARRAY_CL);

		for(SpanProdId id : prodIdList){
			// Get PriceSpec data
			//  1. Create key and put it in map
			DBObject queryObj = new BasicDBObject();
			queryObj.put("prodId", id.getDBObject());
			//  2. Do mongo findOne
			DBObject priceSpecObj = priceSpecColl.findOne(queryObj);
			if(priceSpecObj==null){
				Utils.prtObErrMess(ToSecDef.class, id.toString()+" : not found");
				continue;
			}
			//  3. Convert from json to java pojo.
			ProductSpecsDoc psDoc = gson.fromJson(priceSpecObj.toString(), ProductSpecsDoc.class);
			// get important product spec info:
			int strikePrecision = new Integer(psDoc.getStrikePriceDecLoc().trim());
			int exchangePrecision = new Integer(psDoc.getSettlePriceDecLoc().trim());
			String contractPointValueString = psDoc.getContValueFact();
			// This line inserts a decimal point after 7 digits of the contractPointValueString field.  
			//   The contractPointValueString field's first 7 digits are the integer part of the value of 1 point (e.g for NG, 10,000).
			contractPointValueString = contractPointValueString.substring(0,7)+"."+contractPointValueString.substring(7,contractPointValueString.length());
			// Create the value of a point, and also call it the multiplier
			BigDecimal pointValue = new BigDecimal(contractPointValueString).setScale(exchangePrecision,RoundingMode.HALF_EVEN);
			BigDecimal multiplier = pointValue;
			// Create the minTick by dividing the pointValue by 10^exchangePrecision
			BigDecimal minTick = pointValue.divide(BigDecimal.TEN.pow(exchangePrecision));
			// Create a strikeMultiplier to create accurate strike prices to search for.
			BigDecimal strikeMultipler = BigDecimal.ONE.divide(BigDecimal.TEN.pow(strikePrecision));
			// Map ShortName stuff from PriceSpec
			String symbol = psDoc.getProdId().getProdCommCode();
			SecSymbolType symbolType = SecSymbolType.fromString(psDoc.getProdId().getProdTypeCode().trim());
			SecExchange exchange = SecExchange.fromString(psDoc.getProdId().getExchAcro());
			SecCurrency currency = SecCurrency.fromString(psDoc.getSettleCurrISO());
			
			// Get RiskData array for this PriceSpec
			List<DBObject> riskDbList = riskDataColl.find(queryObj).toArray();
			for(DBObject riskDb : riskDbList){
				RiskDataDoc rdd = gson.fromJson(riskDb.toString(), RiskDataDoc.class);
				BigDecimal daysToExpiryBd = 
						new BigDecimal(rdd.getTimeToExp()).multiply(TIMETOEXPIRY_MULTIPLIER).setScale(0,RoundingMode.HALF_EVEN);
				Integer daysToExpiryInt = daysToExpiryBd.intValue();
				Calendar expiryCal = Dates.addToCalendar(dateOfSpan, daysToExpiryInt, Calendar.DAY_OF_YEAR,true);
				int expiryYear = expiryCal.get(Calendar.YEAR);
				int expiryMonth = expiryCal.get(Calendar.MONTH)+1;
				int expiryDay = expiryCal.get(Calendar.DAY_OF_MONTH);
				int contractYear = 0;
				int contractMonth = 0;
				int contractDay = 0;
				String futConMon = rdd.getFutConMonth();
				String futConDay = rdd.getFutConDay();
				String optConMon = rdd.getOptConMonth();
				String optConDay = rdd.getOptConDay();

				if(symbolType==SecSymbolType.FUT){
					// use futContractMonth
					contractYear = new Integer(futConMon.substring(0, 4).trim());
					contractMonth = new Integer(futConMon.substring(4, 6).trim());
					contractDay = futConDay.trim().compareTo("  ")<=0 ? 0 : new Integer(futConDay.trim());
					ShortNameInfo sni = 
							new ShortNameInfo(symbol, symbolType, exchange, currency, 
									contractYear, contractMonth, contractDay, null, null);
					String shortName = sni.getShortName();
					SecDef sd = 
							new SecDefSimple(
									shortName, sni, symbol, 
									exchangePrecision, minTick, expiryYear, 
									expiryMonth, expiryDay, multiplier, exchange);
					Utils.prt(sd.toString());
					continue;
				}else{
					contractYear = new Integer(optConMon.substring(0, 4).trim());
					contractMonth = new Integer(optConMon.substring(4, 6).trim());
					contractDay = optConDay.trim().compareTo("  ")<=0 ? 0 : new Integer(optConDay.trim());
				}
				
				DBObject spanArrayQueryObj = new BasicDBObject();
				spanArrayQueryObj.put("contractId.prodId",id.getDBObject());
				spanArrayQueryObj.put("contractId.futMonth",futConMon);
				
				if(optConMon.trim().compareTo("  ")>0){
					spanArrayQueryObj.put("contractId.optMonth",optConMon);	
				}
				if(futConDay.trim().compareTo("  ")>0){
					spanArrayQueryObj.put("contractId.futDayWeek",futConDay);	
				}
				if(optConDay.trim().compareTo("  ")>0){
					spanArrayQueryObj.put("contractId.optDayWeek",optConDay);	
				}
				
				
				// Get Risk Array (strike and putcall items) for each RiskData item
				List<DBObject> spanArrayList = spanArrayColl.find(spanArrayQueryObj).toArray();
				for(DBObject spanArray : spanArrayList){
					RiskArrayDoc spanArrayDoc = gson.fromJson(spanArray.toString(), RiskArrayDoc.class);			
					String right = "";
					right = spanArrayDoc.getOptRightCode();
					String strikeString = spanArrayDoc.getOptStrike();
					BigDecimal strike = null;
					if(strikeString!=null){
						strike = new BigDecimal(strikeString.trim()).multiply(strikeMultipler).setScale(strikePrecision, RoundingMode.HALF_EVEN);
					}
					
					
					ShortNameInfo sni = 
							new ShortNameInfo(symbol, symbolType, exchange, currency, 
									contractYear, contractMonth, contractDay, right, strike);
					String shortName = sni.getShortName();
					SecDef sd = 
							new SecDefSimple(
									shortName, sni, symbol, 
									exchangePrecision, minTick, expiryYear, 
									expiryMonth, expiryDay, multiplier, exchange);
					Utils.prt(sd.toString());
				
				}
			}
		}

		Utils.prtObErrMess(ToSecDef.class, Calendar.getInstance().getTime().toString());
 
	}

	
}
