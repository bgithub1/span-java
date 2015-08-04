package com.billybyte.spanjava.mains;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.billybyte.commoninterfaces.QueryInterface;
import com.billybyte.commonstaticmethods.Dates;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.SecDef;
import com.billybyte.marketdata.SecDefQueryAllMarkets;
import com.billybyte.marketdata.SecDefQuerySpanMongo;
import com.billybyte.marketdata.ShortNameInfo;
import com.billybyte.marketdata.SecEnums.SecCurrency;
import com.billybyte.marketdata.SecEnums.SecExchange;
import com.billybyte.marketdata.SecEnums.SecSymbolType;
import com.billybyte.mongo.MongoDoc;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanImpVolDoc;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.SpanSettleDoc;
import com.billybyte.spanjava.mongo.expanded.ProductSpecsDoc;
import com.billybyte.spanjava.mongo.span.RiskArrayDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.billybyte.spanjava.recordtypes.expanded.PriceSpecs;
import com.billybyte.spanjava.resources.SpanUtils;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class RunGenerateSettlesFromSpanArrayDb {

	private final DBCollection priceSpecColl;
	private final DBCollection settleColl;
	private final DBCollection spanArrayColl;
	private final DBCollection impVolColl;
	
	private final QueryInterface<String,SecDef> sdQuery;
	
	private final Gson gson = new Gson();
	private final int batchSize = 5000;
	
	public RunGenerateSettlesFromSpanArrayDb(
			DBCollection priceSpecColl,
			DBCollection spanArrayColl,
			DBCollection settleColl, 
			DBCollection impVolColl,
			QueryInterface<String,SecDef> sdQuery) {
		
		this.priceSpecColl = priceSpecColl;
		this.spanArrayColl = spanArrayColl;
		this.settleColl = settleColl;
		this.impVolColl = impVolColl;
		this.sdQuery = sdQuery;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/// get arg pairs 
		Map<String, String> ap = Utils.getArgPairsSeparatedByChar(args, "=");
		String mongoIp = ap.get("mongoIp")==null ? "127.0.0.1" : ap.get("mongoIp");
		String mongoPort = ap.get("mongoPort")==null ? "27022" : ap.get("mongoPort");
		Integer mport = new Integer(mongoPort);
		String spanConvMapXml = ap.get("spanConvMapXml")==null ? "spanConvMap.xml" : ap.get("spanConvMapXml");
		String symbolRegex =  ap.get("symbolRegex")==null ? "." : ap.get("symbolRegex");
		String typeRegex =  ap.get("typeRegex")==null ? "." : ap.get("typeRegex");
		String exchRegex =  ap.get("exchRegex")==null ? "." : ap.get("exchRegex");
		
		@SuppressWarnings("unchecked")
//		Map<String,String> convMap = Utils.getXmlData(Map.class, SpanUtils.class, "spanConvMap.xml");
		Map<String,String> convMap = (Map<String,String>)Utils.getXmlData(Map.class, null, spanConvMapXml);
	
		
		try {
			
			
			MongoWrapper m = new MongoWrapper(mongoIp, mport);
			
			DB priceSpecDb = m.getDB(SpanMongoUtils.PRICE_SPEC_DB);
			DB cmeSettleDb = m.getDB(SpanMongoUtils.SETTLE_DB);
			DB spanArrayDb = m.getDB(SpanMongoUtils.ARRAY_DB);
			DB impVolDb = m.getDB(SpanMongoUtils.IMP_VOL_DB);
			
			DBCollection priceSpecColl = priceSpecDb.getCollection(SpanMongoUtils.PRICE_SPEC_CL);
			DBCollection settleColl = cmeSettleDb.getCollection(SpanMongoUtils.SETTLE_CL);
			DBCollection spanArrayColl = spanArrayDb.getCollection(SpanMongoUtils.ARRAY_CL);
			DBCollection impVolColl = impVolDb.getCollection(SpanMongoUtils.IMP_VOL_CL);

			Calendar beforeTime = Calendar.getInstance();
			
			QueryInterface<String,SecDef> sdQuery = new SecDefQueryAllMarkets();
			
			RunGenerateSettlesFromSpanArrayDb spanParser = 
					new RunGenerateSettlesFromSpanArrayDb(priceSpecColl, spanArrayColl, settleColl, impVolColl, sdQuery);
			
			Utils.prt("starting to process the span file...");
			
			spanParser.clearSettleAndImpVolCollection();
			DBObject searchObj = new BasicDBObject();
			// symbol regex
			DBObject searchObjSymbolRegex = new BasicDBObject();
			searchObjSymbolRegex.put("$regex", symbolRegex);
			searchObj.put("contractId.prodId.prodCommCode", searchObjSymbolRegex);
			// type regex
			DBObject searchObjTypeRegex = new BasicDBObject();
			searchObjTypeRegex.put("$regex", typeRegex);
			searchObj.put("contractId.prodId.prodTypeCode", searchObjTypeRegex);
			// exchange regex
			DBObject searchObjExchRegex = new BasicDBObject();
			searchObjExchRegex.put("$regex", exchRegex);
			searchObj.put("contractId.prodId.exchAcro", searchObjExchRegex);

			// process the settles from the spanArrays
			spanParser.processSpan(true, convMap,searchObj);

			Calendar afterTime = Calendar.getInstance();

			Utils.prt("took "+(afterTime.getTimeInMillis()-beforeTime.getTimeInMillis())/1000+" s");
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void clearSettleAndImpVolCollection() {
		settleColl.remove(new BasicDBObject());
		impVolColl.remove(new BasicDBObject());
		Utils.prt("Span based settle and implied vol collections cleared");
	}
	
	public Integer processSpan(Boolean useConvMap, Map<String,String> convMap,DBObject searchObj) throws IOException {
		
		Calendar today = Calendar.getInstance();
		
		Map<String,Integer[]> decLocMap = new HashMap<String,Integer[]>();
		
		List<MongoDoc> settleList = new ArrayList<MongoDoc>();
		List<MongoDoc> volList = new ArrayList<MongoDoc>();
		
		Set<String> shortNameSet = new HashSet<String>();
		
		int settleRecs = 0;
		
		
		DBCursor cursor = null;
		if(searchObj!=null){
			cursor = spanArrayColl.find(searchObj);
		}else{
			cursor = spanArrayColl.find();
		}
		
		
		try {
			while(cursor.hasNext()) {
				
				RiskArrayDoc doc = gson.fromJson(cursor.next().toString(), RiskArrayDoc.class);
				
				String exchAcro = doc.getContractId().getProdId().getExchAcro();
				String prodCommCode = doc.getContractId().getProdId().getProdCommCode();
				String prodTypeCode = doc.getContractId().getProdId().getProdTypeCode();
				String decLocKey = prodCommCode+"."+prodTypeCode+"."+exchAcro;
				Integer[] decSpaces = new Integer[2];
				if(decLocMap.containsKey(decLocKey)) {
					decSpaces = decLocMap.get(decLocKey);
				} else {
					// TODO looking for the PriceSpec docs could be batched better for better remote db performance
					DBObject queryObj = new BasicDBObject();
					SpanProdId id = new SpanProdId(exchAcro, prodCommCode, prodTypeCode);
					queryObj.put("prodId", id.getDBObject());
					DBObject retObj = priceSpecColl.findOne(queryObj);
					if(retObj==null) {
						Utils.prtObErrMess(this.getClass(), "No price specification document for "+decLocKey);
						decLocMap.put(decLocKey, null);
						continue;
					} else {
						PriceSpecs psRec = gson.fromJson(retObj.toString(), PriceSpecs.class);
						decSpaces[0] = Integer.parseInt(psRec.getSettlePriceDecLoc());
						decSpaces[1] = 
								(psRec.getStrikePriceDecLoc().trim().length()==0||Integer.parseInt(psRec.getStrikePriceDecLoc())<0) ? 
										0 : Integer.parseInt(psRec.getStrikePriceDecLoc());
						decLocMap.put(decLocKey, decSpaces);
					}
				}
				
				if(decSpaces!=null) {
					
					String futContract = doc.getFutConMonth();
					String futDayWeek = doc.getFutConDay();
					String optRight = doc.getOptRightCode();
					String optStrike = doc.getOptStrike();
					String optContract = doc.getOptConMonth();
					String optDayWeek = doc.getOptConDay();
					
					String shortName;
					if(useConvMap) {
						shortName = getShortName(
								exchAcro, prodCommCode, prodTypeCode, futContract, futDayWeek, optRight, optStrike, 
								optContract, optDayWeek, decSpaces, convMap);
						if(shortName==null) continue;
					} else {
						shortName = getShortName(exchAcro, prodCommCode, prodTypeCode, futContract, futDayWeek, optRight,
								optStrike, optContract, optDayWeek, decSpaces);
					}
					
					if(shortNameSet.contains(shortName)) {
						Utils.prtObErrMess(this.getClass(), "Duplicate short name: "+shortName);
						continue;
					} else {
						shortNameSet.add(shortName);
					}
					
					SecDef sd = sdQuery.get(shortName, 5, TimeUnit.SECONDS);
					
					if(sd==null) {
						Utils.prtObErrMess(this.getClass(), "Null SecDef for "+shortName);
						continue;
					}
					
					Long dte = com.billybyte.spanjava.resources.SpanUtils.getDaysToExpiryFromSecDef(sd, today);
					if(dte<=0) {
						Utils.prtObErrMess(this.getClass(), "Expired contract: "+shortName);
						continue;
					}
					
					String settleStr = doc.getSettle();

					if(settleStr==null) {
						Utils.prtObErrMess(this.getClass(), "null");
					}
					String settleDec = SpanMongoUtils.formatPriceWithDecimal(settleStr, decSpaces[0]);

					settleDec = new BigDecimal(settleDec).toString();
					
					MongoDoc settleDoc = new SpanSettleDoc(shortName, settleDec);
					
					settleList.add(settleDoc);
					settleRecs++;

					if(settleList.size()==batchSize) {
						SpanMongoUtils.batchInsert(settleColl, settleList);
						settleList.clear();
					}

					// check to see if it is an option, and if so also build an implied vol record
					if(doc.getOptRightCode()!=null&&doc.getOptRightCode().trim().length()>0) {
						String impVol = doc.getImpliedVol();
						
						MongoDoc impVolDoc = new SpanImpVolDoc(shortName, impVol);
						
						volList.add(impVolDoc);
						
						if(volList.size()==batchSize) {
							SpanMongoUtils.batchInsert(impVolColl, volList);
							volList.clear();
						}

					}
					
				}
					
			}
			
		} finally {
			cursor.close();
		}

		if(!settleList.isEmpty()) {
			SpanMongoUtils.batchInsert(settleColl, settleList);
		}
		
		if(!volList.isEmpty()) {
			SpanMongoUtils.batchInsert(impVolColl, volList);
		}

//		FileWriter writer = new FileWriter("nybSpanKeys.csv");
//		
//		String lineEnd = System.getProperty("line.separator");
//		
//		for(String spanKey:decLocMap.keySet()) {
//			writer.write(spanKey+lineEnd);
//		}
//		writer.close();
		Utils.prt("*************************************************************************************");
		Utils.prt("Built "+settleRecs+" settlement records");
		return settleRecs;
	}
	
	private String getShortName(String exchAcro, String prodCommCode, String prodTypeCode, String futContract,
			String futDayWeek, String optRight, String optStrike, String optContract, String optDayWeek,Integer[] decLocArr) {
		String symbol = prodCommCode.trim();
		String prodType = prodTypeCode.trim();
		
		String contract;
		if((optRight.compareTo("P")==0)||(optRight.compareTo("C")==0)) {
			if(optStrike==null||decLocArr[1]==null) {
				Utils.prtObErrMess(this.getClass(), "is null");
			}
			String strikeDecStr = SpanMongoUtils.formatPriceWithDecimal(optStrike, decLocArr[1]);
			contract = optContract+"."+optRight+"."+new BigDecimal(strikeDecStr).toString();
//			if(decLocArr[1]<decLocArr[0]) {
//				int numOfZerosToAdd = decLocArr[0]-decLocArr[1];
//				for(int i=0;i<numOfZerosToAdd;i++){
//					contract = contract+"0";
//				}
//			}
		} else {
			contract = futContract;
		}
		
		String ret = symbol+"."+prodType+"."+exchAcro+".USD."+contract;
		
		return ret;
	}

	private String getShortName(
			String exchAcro, String prodCommCode, String prodTypeCode, String futContract, 
			String futDayWeek, String optRight, String optStrike, String optContract, 
			String optDayWeek, Integer[] decLocArr, Map<String,String> convMap) {
		
		String origShort = getShortName(exchAcro, prodCommCode, prodTypeCode, futContract, 
				futDayWeek, optRight, optStrike, optContract, optDayWeek, decLocArr);
		
		String[] origSplit = origShort.split("\\.");
		String key = origSplit[0]+"."+origSplit[1]+"."+origSplit[2];
		String contract = origSplit[4];
		if(origSplit.length>5) {
			contract = contract+"."+origSplit[5]+"."+origSplit[6];
			if(origSplit.length>7) contract = contract+"."+origSplit[7];
		}
		
		if(convMap.containsKey(key)) {
			return convMap.get(key)+".USD."+contract;
		} else {
			return null;
		}
		
	}
	

	
}
