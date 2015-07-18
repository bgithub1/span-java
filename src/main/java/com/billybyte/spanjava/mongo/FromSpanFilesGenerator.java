package com.billybyte.spanjava.mongo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.billybyte.commoncollections.Tuple;
import com.billybyte.commonstaticmethods.Dates;
import com.billybyte.commonstaticmethods.RegexMethods;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.SecDef;
import com.billybyte.marketdata.SecDefFromSpanMongo;
import com.billybyte.marketdata.SecDefSimple;
import com.billybyte.marketdata.SecEnums.SecCurrency;
import com.billybyte.marketdata.SecEnums.SecExchange;
import com.billybyte.marketdata.ShortNameInfo;
import com.billybyte.marketdata.SecEnums.SecSymbolType;
import com.billybyte.mongo.MongoDoc;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.expanded.ProductSpecsDoc;
import com.billybyte.spanjava.mongo.span.CombCommProdIdDoc;
import com.billybyte.spanjava.mongo.span.FlatCombCommProdIdDoc;
import com.billybyte.spanjava.mongo.span.RiskArrayDoc;
import com.billybyte.spanjava.mongo.span.RiskDataDoc;

import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class FromSpanFilesGenerator {
	private static final Gson gson = new Gson();
	private static final BigDecimal TIMETOEXPIRY_MULTIPLIER = 	BigDecimal.ONE.divide(BigDecimal.TEN.pow(6)).multiply(new BigDecimal("365"));
	private static final String HEADER_EXCH = "exch";
	private static final String HEADER_PROD = "prod";
	private static final String HEADER_TYPE = "type";
	
	private final MongoWrapper m;
	// PriceSpec
	private final DBCollection priceSpecColl;
	// RiskData
	private final DBCollection riskDataColl;
	// RiskArray
	private final DBCollection spanArrayColl;
	// settleCollection in span 
	private final DBCollection settleColl ;
	// impliedVolCollection
	private final DBCollection impVolColl;
	// spanFirstCombCommDb
	private final DBCollection spanCombCommColl;
	// spanFlatCombCommDb
	private final DBCollection spanFlatCombCommColl;
	
	public Tuple<Map<String, SecDef>, Map<String, String>>  getSecDefs(
			Calendar dateOfSpan,
			List<SpanProdId> prodIdList){
		// Create return object.
		Map<String, SecDef> sdMap = new TreeMap<String, SecDef>();
		Map<String,String> underShortNameMap = new TreeMap<String, String>();

		for(SpanProdId id : prodIdList){
			Utils.prtObErrMess(FromSpanFilesGenerator.class, id.getProdCommCode()+"."+id.getProdTypeCode()+"."+id.getExchAcro()+":" + Calendar.getInstance().getTime().toString());
			Tuple<Map<String, SecDef>, Map<String, String>> tuplePerId = 
					getSecDefListFromSpanProdId(id,dateOfSpan);
			if(tuplePerId!=null){
				sdMap.putAll(tuplePerId.getT1_instance());
				underShortNameMap.putAll(tuplePerId.getT2_instance());
			}else{
				Utils.prtObErrMess(FromSpanFilesGenerator.class, id.toString()+" : not found");
				continue;
				
			}
		}
		return new Tuple<Map<String,SecDef>, Map<String,String>>(sdMap, underShortNameMap);
	}
	
	public ProductSpecsDoc getProducSpecsDoc(SpanProdId id){
		DBObject queryObj = new BasicDBObject();
		queryObj.put("prodId", id.getDBObject());
		//  2. Do mongo findOne
		DBObject priceSpecObj = priceSpecColl.findOne(queryObj);
		if(priceSpecObj==null){
			return null;
		}
		//  3. Convert from json to java pojo.
		ProductSpecsDoc psDoc = gson.fromJson(priceSpecObj.toString(), ProductSpecsDoc.class);
		return psDoc;
	}
	
	
	private class ProdSpecSecDefInfo {
		boolean isValid;
		DBObject queryObj;
		ProductSpecsDoc psDoc ;
		SpanProdId id;
		int strikePrecision ;
		int exchangePrecision;
		String contractPointValueString;
		// Create the value of a point, and also call it the multiplier
		BigDecimal pointValue ;
		BigDecimal multiplier;
		// Create the minTick by dividing the pointValue by 10^exchangePrecision
		BigDecimal minTick ;
		// Create a strikeMultiplier to create accurate strike prices to search for.
		BigDecimal strikeMultipler ;
		// Map ShortName stuff from PriceSpec
		String symbol ;
		SecSymbolType symbolType;
		SecExchange exchange ;
		SecCurrency currency ;

		private ProdSpecSecDefInfo(SpanProdId id){
			this.id= id;
			isValid = true;
			queryObj = new BasicDBObject();
			queryObj.put("prodId", id.getDBObject());
			//  2. Do mongo findOne
			DBObject priceSpecObj = priceSpecColl.findOne(queryObj);
			if(priceSpecObj==null){
				Utils.prtObErrMess(FromSpanFilesGenerator.class, id.toString()+" : not found");
				isValid=false;
				return;
			}
			//  3. Convert from json to java pojo.
			psDoc = gson.fromJson(priceSpecObj.toString(), ProductSpecsDoc.class);
			// get important product spec info:
			strikePrecision = new Integer(psDoc.getStrikePriceDecLoc().trim());
			exchangePrecision = new Integer(psDoc.getSettlePriceDecLoc().trim());
			contractPointValueString = psDoc.getContValueFact();
			// This line inserts a decimal point after 7 digits of the contractPointValueString field.  
			//   The contractPointValueString field's first 7 digits are the integer part of the value of 1 point (e.g for NG, 10,000).
			contractPointValueString = contractPointValueString.substring(0,7)+"."+contractPointValueString.substring(7,contractPointValueString.length());
			// Create the value of a point, and also call it the multiplier
			pointValue = new BigDecimal(contractPointValueString).setScale(exchangePrecision,RoundingMode.HALF_EVEN);
			multiplier = pointValue;
			// Create the minTick by dividing the pointValue by 10^exchangePrecision
			minTick = pointValue.divide(BigDecimal.TEN.pow(exchangePrecision));
			// Create a strikeMultiplier to create accurate strike prices to search for.
			strikeMultipler = BigDecimal.ONE.divide(BigDecimal.TEN.pow(strikePrecision));
			// Map ShortName stuff from PriceSpec
			symbol = psDoc.getProdId().getProdCommCode();
			symbolType = SecSymbolType.fromString(psDoc.getProdId().getProdTypeCode().trim());
			exchange = SecExchange.fromString(psDoc.getProdId().getExchAcro());
			currency = SecCurrency.fromString(psDoc.getSettleCurrISO());

		}
	}
	
	private class RiskDataSecDefInfo {
		boolean isValid;
		ProdSpecSecDefInfo psSdInfo;
		RiskDataDoc rdd ;
		Integer daysToExpiryInt ;
		Calendar expiryCal;
		int expiryYear ;
		int expiryMonth ;
		int expiryDay ;
		int contractYear;
		int contractMonth;
		int contractDay ;
		String futConMon ;
		String futConDay ;
		String optConMon ;
		String optConDay ;
		DBObject spanArrayQueryObj;
		// get underlying symbol and underlying info to create an underlying
		SpanProdId underSpi ;
		SecSymbolType underSymType;
		int underContractYear ;
		int underContractMonth;
		int underContractDay ;
		ShortNameInfo underSnInfo ;
		String underShortName ;
		
		private RiskDataSecDefInfo(Calendar dateOfSpan,ProdSpecSecDefInfo psSdInfo,DBObject riskDb){
			isValid=true;
			rdd = gson.fromJson(riskDb.toString(), RiskDataDoc.class);
			BigDecimal daysToExpiryBd = 
					new BigDecimal(rdd.getTimeToExp()).multiply(TIMETOEXPIRY_MULTIPLIER).setScale(0,RoundingMode.HALF_EVEN);
			if(daysToExpiryBd.compareTo(BigDecimal.ZERO)<=0){
				isValid = false;
			}
			daysToExpiryInt = daysToExpiryBd.intValue();
			expiryCal = Dates.addToCalendar(dateOfSpan, daysToExpiryInt, Calendar.DAY_OF_YEAR,true);
			expiryYear = expiryCal.get(Calendar.YEAR);
			expiryMonth = expiryCal.get(Calendar.MONTH)+1;
			expiryDay = expiryCal.get(Calendar.DAY_OF_MONTH);
			contractYear = 0;
			contractMonth = 0;
			contractDay = 0;
			futConMon = rdd.getFutConMonth();
			futConDay = rdd.getFutConDay();
			optConMon = rdd.getOptConMonth();
			optConDay = rdd.getOptConDay();

			if(psSdInfo.symbolType==SecSymbolType.FUT || psSdInfo.symbolType==SecSymbolType.CMB){
				// use futContractMonth
				contractYear = new Integer(futConMon.substring(0, 4).trim());
				contractMonth = new Integer(futConMon.substring(4, 6).trim());
				contractDay = futConDay.trim().compareTo("  ")<=0 ? 0 : new Integer(futConDay.trim());
				
			}else{
				contractYear = new Integer(optConMon.substring(0, 4).trim());
				contractMonth = new Integer(optConMon.substring(4, 6).trim());
				contractDay = optConDay.trim().compareTo("  ")<=0 ? 0 : new Integer(optConDay.trim());
			}
			
			spanArrayQueryObj = new BasicDBObject();
			spanArrayQueryObj.put("contractId.prodId",psSdInfo.id.getDBObject());
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
			
			// get underlying symbol and underlying info to create an underlying
			underSpi = getUnderlyingSpanProdId(psSdInfo.id);
			underSymType = SecSymbolType.fromString(underSpi.getProdTypeCode());
			underContractYear = new Integer(futConMon.substring(0, 4).trim());
			underContractMonth = new Integer(futConMon.substring(4, 6).trim());
			underContractDay = futConDay.trim().compareTo("  ")<=0 ? 0 : new Integer(futConDay.trim());

			// Write out underlying shortName stuff - BUT it might re RE-WRITTEN if the constructor of SpanArraySecDefInfo !!!!
			underSnInfo = 
					new ShortNameInfo(underSpi.getProdCommCode(), underSymType, psSdInfo.exchange, psSdInfo.currency, underContractYear, underContractMonth, underContractDay, null,null);
			underShortName = underSnInfo.getShortName();
		}
		
	}
	
	private class SpanArraySecDefInfo {
		RiskArrayDoc spanArrayDoc;			
		String right;
		String strikeString;
		BigDecimal strike;

		private SpanArraySecDefInfo(Calendar dateOfSpan,ProdSpecSecDefInfo psSdInfo,RiskDataSecDefInfo rdSdInfo,DBObject spanArray){
			spanArrayDoc = gson.fromJson(spanArray.toString(), RiskArrayDoc.class);			
			right = "";
			right = spanArrayDoc.getOptRightCode();
			strikeString = spanArrayDoc.getOptStrike();
			strike = null;
			if(strikeString!=null){
				strike = new BigDecimal(strikeString.trim()).multiply(psSdInfo.strikeMultipler).setScale(psSdInfo.strikePrecision, RoundingMode.HALF_EVEN);
			}
//			rdSdInfo.underSnInfo = 
//					new ShortNameInfo(rdSdInfo.underSpi.getProdCommCode(),rdSdInfo.underSymType, psSdInfo.exchange, psSdInfo.currency, rdSdInfo.underContractYear, rdSdInfo.underContractMonth, rdSdInfo.underContractDay, right,strike);
//			rdSdInfo.underShortName = rdSdInfo.underSnInfo.getShortName();
		}
	}
	
	
	public FromSpanFilesGenerator(String mongoIp,int mongoPort){
		MongoWrapper m=null;
		try {
			m = new MongoWrapper(mongoIp,mongoPort);
		} catch (UnknownHostException e) {
			Utils.IllState(e);
		}
		this.m = m;
		DB priceSpecDb = m.getDB(SpanMongoUtils.PRICE_SPEC_DB);
		priceSpecColl = priceSpecDb.getCollection(SpanMongoUtils.PRICE_SPEC_CL);
		
		DB riskDataDb = m.getDB(SpanMongoUtils.RISK_DATA_DB);
		riskDataColl = riskDataDb.getCollection(SpanMongoUtils.RISK_DATA_CL);
		
		DB arrayDb = m.getDB(SpanMongoUtils.ARRAY_DB);
		spanArrayColl = arrayDb.getCollection(SpanMongoUtils.ARRAY_CL);
	
		DB cmeSettleDb = m.getDB(SpanMongoUtils.SETTLE_DB);
		settleColl = cmeSettleDb.getCollection(SpanMongoUtils.SETTLE_CL);

		DB impVolDb = m.getDB(SpanMongoUtils.IMP_VOL_DB);		
		impVolColl = impVolDb.getCollection(SpanMongoUtils.IMP_VOL_CL);

		DB spanCombCommDb = m.getDB(SpanMongoUtils.COMB_COMM_DB);		
		spanCombCommColl = spanCombCommDb.getCollection(SpanMongoUtils.COMB_COMM_CL);

		DB spanFlatCombCommDb = m.getDB(SpanMongoUtils.FLAT_COMB_COMM_DB);		
		spanFlatCombCommColl = spanFlatCombCommDb.getCollection(SpanMongoUtils.FLAT_COMB_COMM_CL);
	
	
	}
	
	/**
	 * Execute a 3 step process of getting SecDefs and underlyings for a SpanProdId and date,
	 *   where each step relates to obtaining ProductSpecsDoc's, RiskDataDoc's and/or RiskArrayDoc's. 
	 * @param id
	 * @param dateOfSpan
	 * @return
	 */
	public Tuple<Map<String, SecDef>, Map<String, String>> getSecDefListFromSpanProdId(SpanProdId id,Calendar dateOfSpan){
		Map<String, SecDef> sdMap = new TreeMap<String, SecDef>();
		Map<String,String> underShortNameMap = new TreeMap<String, String>();

		// STEP 1.
		ProdSpecSecDefInfo psSdInfo = new ProdSpecSecDefInfo(id);
		if(!psSdInfo.isValid){
			return null;
		}
		
		// STEP 2.
		List<DBObject> riskDbList = riskDataColl.find(psSdInfo.queryObj).toArray();
		for(DBObject riskDb : riskDbList){
			RiskDataSecDefInfo rdSdInfo = new RiskDataSecDefInfo(dateOfSpan, psSdInfo, riskDb);
			if(!rdSdInfo.isValid){
				continue;
			}
			if(psSdInfo.symbolType==SecSymbolType.FUT || psSdInfo.symbolType==SecSymbolType.CMB){
				ShortNameInfo sni = 
						new ShortNameInfo(psSdInfo.symbol, psSdInfo.symbolType, psSdInfo.exchange, psSdInfo.currency, 
								rdSdInfo.contractYear, rdSdInfo.contractMonth, rdSdInfo.contractDay, null, null);
				String shortName = sni.getShortName();
				SecDef sd = 
						new SecDefSimple(
								shortName, sni, psSdInfo.symbol, 
								psSdInfo.exchangePrecision, psSdInfo.minTick, rdSdInfo.expiryYear, 
								rdSdInfo.expiryMonth, rdSdInfo.expiryDay, psSdInfo.multiplier, psSdInfo.exchange);
				// !!!!!!!!!!!!!!!!!!!!!!!!!!  put secDef here if Future !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				sdMap.put(shortName, sd);
				underShortNameMap.put(shortName, rdSdInfo.underShortName);
				continue;
			}else{
				// STEP 3 for option SecDefs.
				List<DBObject> spanArrayList = spanArrayColl.find(rdSdInfo.spanArrayQueryObj).toArray();
				for(DBObject spanArray : spanArrayList){
					SpanArraySecDefInfo saSdInfo = new SpanArraySecDefInfo(dateOfSpan, psSdInfo, rdSdInfo, spanArray);
					ShortNameInfo sni = 
							new ShortNameInfo(psSdInfo.symbol, psSdInfo.symbolType, psSdInfo.exchange, psSdInfo.currency, 
									rdSdInfo.contractYear, rdSdInfo.contractMonth, rdSdInfo.contractDay, saSdInfo.right, saSdInfo.strike);
					String shortName = sni.getShortName();
					SecDef sd = 
							new SecDefSimple(
									shortName, sni, psSdInfo.symbol, 
									psSdInfo.exchangePrecision, psSdInfo.minTick, rdSdInfo.expiryYear, 
									rdSdInfo.expiryMonth, rdSdInfo.expiryDay, psSdInfo.multiplier, psSdInfo.exchange);
					// !!!!!!!!!!!!!!!!!!!!!!!!!!  put secDef here if Option !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					sdMap.put(shortName, sd);
					underShortNameMap.put(shortName, rdSdInfo.underShortName);
				}
			}
		}
		Tuple<Map<String, SecDef>, Map<String, String>> ret = 
				new Tuple<Map<String,SecDef>, Map<String,String>>(sdMap, underShortNameMap);
		return ret;
	}
	
	/**
	 * Create a List<SpanProdId> from a csv list of exch,prod,type
	 * 	
	 * @param exchProdTypeCsvPath
	 * @return
	 */
	@Deprecated
	public  List<SpanProdId> spanProdListFromCsv(String exchProdTypeCsvPath){
		List<String[]> exchProdTypeCsv = Utils.getCSVData(exchProdTypeCsvPath);

		List<SpanProdId> spanProdList = 
				new ArrayList<SpanProdId>();
		String[] csvHeader = exchProdTypeCsv.get(0);
		int exchCol = Utils.getCsvColumnIndex(HEADER_EXCH, csvHeader);
		int prodCol = Utils.getCsvColumnIndex(HEADER_PROD, csvHeader);
		int typeCol = Utils.getCsvColumnIndex(HEADER_TYPE, csvHeader);
		for(int i = 1;i<exchProdTypeCsv.size();i++){ // start at 1 to avoid header
			String[] line = exchProdTypeCsv.get(i);
			if(line.length<3)break;
			spanProdList.add(new SpanProdId(line[exchCol], line[prodCol], line[typeCol]));
		}
		return spanProdList;
	}
	

	/**
	 * Create a List<SpanProdId> from a map that has regex entries for securities that you want to
	 *   extract from the span db's and turn into SpanProdId's, to be used for may building functions
	 *    like building SecDef's and Undrlying shortNames.
	 * @param prodIdMap
	 * @return
	 */
	public  List<SpanProdId>  spanProdListFromMap(Map<String, String> prodIdMap){
		DBObject dbo = new BasicDBObject();
		for(Entry<String, String> entry:prodIdMap.entrySet()){
			DBObject regexDbo = new BasicDBObject();
			regexDbo.put("$regex",entry.getValue());
			dbo.put(entry.getKey(),regexDbo);
		}
		
		return getSpanProdIdList(dbo);
	}

	
	public static final Calendar dateFromSpanFile(
			String spanFilePath){
		if(spanFilePath==null){
			return null;
		}
		List<String> lines = Utils.getLineData(null, spanFilePath);
		String line = lines.get(0);
		String yearRegex = "2[0-2][0-9]{2, 2}";
		String monthRegex = "((0[1-9])|(1[0-2))";
		String dayRegex = "((0[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))";
		String yyyyMmDdRegex = yearRegex+monthRegex+dayRegex;
		List<String> regexResults = RegexMethods.getRegexMatches(yyyyMmDdRegex,line);
		String yyMmDdString = regexResults.get(0);
		Calendar dateOfSpan = Dates.getCalendarFromYYYYMMDD(new Long(yyMmDdString)); 
		return dateOfSpan;
	}
	
	public void populateSpanUnderlyingDb(Map<String,String> underMap){
		DB spanUnderlyingDb = m.getDB(SpanMongoUtils.UNDER_SNINFO_DB);
		DBCollection spanUnderlyingColl= spanUnderlyingDb.getCollection(SpanMongoUtils.UNDER_SNINFO_COLL);
		List<DBObject> dbObjList = new ArrayList<DBObject>();
		
		for(Entry<String, String> entry : underMap.entrySet()){
			String id = entry.getKey();
			String underSn = entry.getValue();
			DBObject underSnObj = new BasicDBObject();
			underSnObj.put("_id", id);
			underSnObj.put("underlyingShortName", underSn);
			dbObjList.add(underSnObj);
		}
		DBObject removeDboOuter = new BasicDBObject();
		DBObject removeDboInner = new BasicDBObject();
		removeDboInner.put("$in",new ArrayList<String>(underMap.keySet()));
		removeDboOuter.put("_id",removeDboInner);
		spanUnderlyingColl.remove(removeDboOuter);
		spanUnderlyingColl.insert(dbObjList);
		
	}
	
	/**
	 * Write all SecDefs in sdMap to SECDEF_DB
	 * @param sdMap
	 */
	public  void populateSecDefDb(Map<String,SecDef> sdMap){
		DB secDefDb = m.getDB(SpanMongoUtils.SECDEF_DB);
		DBCollection secDefColl = secDefDb.getCollection(SpanMongoUtils.SECDEF_CL);
		List<DBObject> dbObjList = new ArrayList<DBObject>();
		
		for(SecDef sd : sdMap.values()){
			DBObject sdObj = new BasicDBObject();
			sdObj.put("_id", sd.getShortName());
			sdObj.put(SecDefFromSpanMongo.SD_shortName,sd.getShortName());
		    sdObj.put(SecDefFromSpanMongo.SD_symbol,sd.getSymbol());
		    sdObj.put(SecDefFromSpanMongo.SD_symbolType,sd.getSymbolType().toString());
		    sdObj.put(SecDefFromSpanMongo.SD_contractYear,sd.getContractYear());
		    sdObj.put(SecDefFromSpanMongo.SD_contractMonth,sd.getContractMonth());
		    sdObj.put(SecDefFromSpanMongo.SD_contractDay,sd.getContractDay());
		    sdObj.put(SecDefFromSpanMongo.SD_expiryYear,sd.getExpiryYear());
		    sdObj.put(SecDefFromSpanMongo.SD_expiryMonth,sd.getExpiryMonth());
		    sdObj.put(SecDefFromSpanMongo.SD_expiryDay,sd.getExpiryDay());
		    BigDecimal strike = sd.getStrike();
		    if(strike!=null){
			    sdObj.put(SecDefFromSpanMongo.SD_strike,sd.getStrike().doubleValue());
			    sdObj.put(SecDefFromSpanMongo.SD_right,sd.getRight());
		    }else{
		    	sdObj.put(SecDefFromSpanMongo.SD_strike,"");
			    sdObj.put(SecDefFromSpanMongo.SD_right,"");
		    }
		    sdObj.put(SecDefFromSpanMongo.SD_multiplier,sd.getMultiplier().doubleValue());
		    sdObj.put(SecDefFromSpanMongo.SD_exchangePrecision,sd.getExchangePrecision());
		    sdObj.put(SecDefFromSpanMongo.SD_minTick,sd.getMinTick().doubleValue());
		    sdObj.put(SecDefFromSpanMongo.SD_exchange,sd.getExchange().toString());
		    sdObj.put(SecDefFromSpanMongo.SD_currency,sd.getCurrency().toString());
		    sdObj.put(SecDefFromSpanMongo.SD_exchangeSymbol,sd.getExchangeSymbol());
		    sdObj.put(SecDefFromSpanMongo.SD_primaryExch,sd.getPrimaryExch().toString());
		    dbObjList.add(sdObj);
		}
		DBObject removeDboOuter = new BasicDBObject();
		DBObject removeDboInner = new BasicDBObject();
		removeDboInner.put("$in",new ArrayList<String>(sdMap.keySet()));
		removeDboOuter.put("_id",removeDboInner);
		secDefColl.remove(removeDboOuter);
		secDefColl.insert(dbObjList);
		
		// double check that they all were written and can be fetched
		List<DBObject> sdDbosJustWritten = 
				secDefColl.find(removeDboOuter).toArray();
		List<SecDef> sdsJustWritten = new ArrayList<SecDef>();
		for(DBObject sdDbo : sdDbosJustWritten){
			sdsJustWritten.add(new SecDefFromSpanMongo(sdDbo));
		}
		return;
	}
	
	public <T> List<T> getSpanList(
			String dbName,
			String collectionName,
			Class<T> classOfT,
			DBObject searchObj){
		// PriceSpec
		DB priceSpecDb = m.getDB(dbName);
		DBCollection priceSpecColl = priceSpecDb.getCollection(collectionName);
		List<DBObject> dboList = priceSpecColl.find(searchObj).toArray();
		List<T> ret = new ArrayList<T>();
		for(DBObject dbo: dboList){
			T t = gson.fromJson(dbo.toString(), classOfT);
			ret.add(t);
		}
		return ret;
	}

	public List<SpanProdId> getSpanProdIdList(DBObject searchObj){
		List<ProductSpecsDoc> psDocList =
				getSpanList(SpanMongoUtils.PRICE_SPEC_DB, 
						SpanMongoUtils.PRICE_SPEC_CL, 
						ProductSpecsDoc.class, searchObj);
		List<SpanProdId> ret = new ArrayList<SpanProdId>();
		for(ProductSpecsDoc psd : psDocList){
			ret.add(psd.getProdId());
		}
		return ret;
	}

	public void processSettlesAndImpVolsFromSpanProdIds(List<SpanProdId> prodIdList){
		
		for(SpanProdId prodId:prodIdList){
			Map<String, BigDecimal> settleMap  = new TreeMap<String, BigDecimal>();
			Map<String, BigDecimal> impVolMap  = new TreeMap<String, BigDecimal>();
			DBObject priceSpecSearch = new BasicDBObject();
			priceSpecSearch.put("prodId", prodId.getDBObject());
			DBObject priceSpecObj = priceSpecColl.findOne(priceSpecSearch);
			ProductSpecsDoc psDoc = gson.fromJson(priceSpecObj.toString(), ProductSpecsDoc.class);
			DBObject spanArrayQueryObj = new BasicDBObject();
			spanArrayQueryObj.put("contractId.prodId", prodId.getDBObject());
			int strikePrecision = new Integer(psDoc.getStrikePriceDecLoc().trim());
			BigDecimal strikeMultipler = BigDecimal.ONE.divide(BigDecimal.TEN.pow(strikePrecision));
			int exchangePrecision = new Integer(psDoc.getSettlePriceDecLoc().trim());
			BigDecimal priceMultipler = BigDecimal.ONE.divide(BigDecimal.TEN.pow(exchangePrecision));
			List<DBObject> spanArrayList = spanArrayColl.find(spanArrayQueryObj).toArray();
			
			// iterate though list
			for(DBObject spaDbo : spanArrayList){
				RiskArrayDoc spanArrayDoc = gson.fromJson(spaDbo.toString(), RiskArrayDoc.class);
				SpanProdId thisSpi = spanArrayDoc.getContractId().getProdId();
				String exch = thisSpi.getExchAcro();
				exch = SecExchange.fromString(exch).toString();
				String type = thisSpi.getProdTypeCode();
				if(type.compareTo(SecSymbolType.OOC.toString())==0 || type.compareTo(SecSymbolType.OOF.toString())==0){
					type = SecSymbolType.FOP.toString();
				}
				String symbol = thisSpi.getProdCommCode();
				String curr = psDoc.getSettleCurrISO();
				String yyyyMm = spanArrayDoc.getFutConMonth();
				String futConDay = spanArrayDoc.getFutConDay().trim();
				int contractDay = futConDay.compareTo("  ")<=0 ? 0 : new Integer(futConDay);
				String right = null;
				BigDecimal strike = null;
				if(type.compareTo(SecSymbolType.FOP.toString())==0){
					yyyyMm = spanArrayDoc.getOptConMonth();
					String optConDay = spanArrayDoc.getOptConDay().trim();
					contractDay = optConDay.compareTo("  ")<=0 ? 0 : new Integer(optConDay);
					right = spanArrayDoc.getOptRightCode().trim();
					String strikeString = spanArrayDoc.getOptStrike();
					strike = new BigDecimal(strikeString.trim()).multiply(strikeMultipler).setScale(strikePrecision, RoundingMode.HALF_EVEN);				}
				SecSymbolType  symbolType = SecSymbolType.fromString(type);
				SecExchange exchange = SecExchange.fromString(exch);
				SecCurrency currency = SecCurrency.fromString(curr);
				int contractYear = new Integer(yyyyMm.substring(0,4));
				int contractMonth = new Integer(yyyyMm.substring(4,6));

				ShortNameInfo sni = 
						new ShortNameInfo(symbol, symbolType, exchange, currency, 
								contractYear, contractMonth, contractDay, right, strike);
				String shortName = sni.getShortName();
				String priceString = spanArrayDoc.getSettle();
				BigDecimal settle = new BigDecimal(priceString).multiply(priceMultipler).setScale(exchangePrecision, RoundingMode.HALF_EVEN);
				BigDecimal impVol = new BigDecimal(spanArrayDoc.getImpliedVol());
				settleMap.put(shortName, settle);
				impVolMap.put(shortName,impVol);
				
			}
			writeToMongo(settleMap, "settlePrice", settleColl);
			writeToMongo(impVolMap,"impVol",impVolColl);
		}
	}
	
	private void writeToMongo(Map<String, BigDecimal> settleMap,String valueName,DBCollection coll){
		DBObject removeDboOuter = new BasicDBObject();
		DBObject removeDboInner = new BasicDBObject();
		removeDboInner.put("$in",new ArrayList<String>(settleMap.keySet()));
		removeDboOuter.put("_id",removeDboInner);
		coll.remove(removeDboOuter);
		List<MongoDoc> mongoDocList = new ArrayList<MongoDoc>();
		for(Entry<String, BigDecimal> entry : settleMap.entrySet()){
			MongoDoc doc = null;
			if(valueName.contains("impVol")){
				doc = 
						new SpanImpVolDoc(entry.getKey(), entry.getValue().toString());
			}else{
				doc = 
						new SpanSettleDoc(entry.getKey(), entry.getValue().toString());
				
			}
			mongoDocList.add(doc);
		}
		SpanMongoUtils.batchInsert(coll, mongoDocList);

	}
	
	
	
	public SpanProdId getBestOptionContract(String symbol, String exch){
		DBObject combCommSearch = new BasicDBObject();
		combCommSearch.put("prodIdSet.prodCommCode",symbol);
		combCommSearch.put("prodIdSet.exchAcro",exch);
		List<DBObject> commCommDboList =  spanCombCommColl.find(combCommSearch).toArray();
		DBObject commCommDbo =  commCommDboList.get(0);
		CombCommProdIdDoc combcom = gson.fromJson(commCommDbo.toString(), CombCommProdIdDoc.class);
		// find riskArrayParamBlockList item that has commProdCode = symbol
		Set<SpanProdId> spanProdIdSet = combcom.getProdIdSet();
		// get this type to see if it's a future or a CMB
		String comcodeType = null;
		for(SpanProdId spanProdId : spanProdIdSet){
			String thisSymbol = spanProdId.getProdCommCode();
			if(thisSymbol.compareTo(symbol)==0){
				comcodeType = spanProdId.getProdTypeCode();
			}
		}
		
		String optTypeToUse = "OOF";
		if(comcodeType.compareTo("CMB")==0){
			optTypeToUse = "OOC";
		}
		// loop again to find option contract with most contracts
		int mostContracts = -1;
		SpanProdId  optProdIdWithMostContracts = null;
		for(SpanProdId spanProdId : spanProdIdSet){
			if(spanProdId.getProdTypeCode().compareTo(optTypeToUse)!=0){
				continue;
			}
			String thisSymbol = spanProdId.getProdCommCode();
			SpanProdId prodid = new SpanProdId(exch, thisSymbol, optTypeToUse);
			DBObject spanArraySearch = new BasicDBObject();
			spanArraySearch.put("contractId.prodId", prodid.getDBObject());
			int numContracts = spanArrayColl.find(spanArraySearch).count();
			if(numContracts>mostContracts){
				mostContracts = numContracts;
				optProdIdWithMostContracts = new SpanProdId(exch, thisSymbol, optTypeToUse);
			}
		}
		
		return optProdIdWithMostContracts;
	}
	
	public SpanProdId getUnderlyingSpanProdId(SpanProdId optSpanProdId){
		DBObject search = new BasicDBObject();
		search.put("prodId",optSpanProdId.getDBObject());
		DBObject combcomDbo = spanFlatCombCommColl.find(search).toArray().get(0);
		FlatCombCommProdIdDoc combcom = gson.fromJson(combcomDbo.toString(), FlatCombCommProdIdDoc.class);
		
		String underSymbol = combcom.getCombCommCode().split("-")[1];
		DBObject underSearch = new BasicDBObject();
		underSearch.put("prodId.prodCommCode", underSymbol);
		underSearch.put("prodId.exchAcro", optSpanProdId.getExchAcro());
		DBObject underCombcomDbo = spanFlatCombCommColl.find(underSearch).toArray().get(0);
		FlatCombCommProdIdDoc underCombcom = gson.fromJson(underCombcomDbo.toString(), FlatCombCommProdIdDoc.class);
		return underCombcom.getProdId();
	}

}
