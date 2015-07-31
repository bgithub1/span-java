//9:05
package com.billybyte.spanjava.mains;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import com.billybyte.commonstaticmethods.Dates;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.SecEnums.SecCurrency;
import com.billybyte.marketdata.SecEnums.SecExchange;
import com.billybyte.marketdata.MarketDataComLib;
import com.billybyte.marketdata.ShortNameInfo;
import com.billybyte.marketdata.SecEnums.SecSymbolType;
import com.billybyte.marketdata.futures.SpanHist;
import com.billybyte.mongo.MongoCollectionWrapper;
import com.billybyte.mongo.MongoDatabaseNames;
import com.billybyte.mongo.MongoDoc;

import com.billybyte.spanjava.utils.CmeSpanUtils.RecordType;
import com.billybyte.ui.RedirectedConsoleForJavaProcess;
import com.billybyte.ui.RedirectedConsoleForJavaProcess.ConsoleType;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Create history db from span files
 * @author bperlman1
 *
 */
public class RunBuildHistoricalFutDbCreate {
	private static final String futType = SecSymbolType.FUT.toString();
	private static final String cmbType = SecSymbolType.CMB.toString();
	private static final String SEP = MarketDataComLib.DEFAULT_SHORTNAME_SEPARATOR;
	private static final String SPLIT_STRING = "\\"+SEP;
	
	/**
	 * 
	 * @param args  "mongoIp=127.0.0.1" mongoPort=27022 daysBack=60 "spanUnzipFolder=myHome/dbbuild_1_0/unzipFolder" "spanFileTemplate=cme.YYYYMMDD.s.pa2" prodConvMapPath=spanConvMap.xml
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		
		Calendar beg = Calendar.getInstance();
		new RedirectedConsoleForJavaProcess(700, 700,1,1,RunBuildHistoricalFutDbCreate.class.getSimpleName()+" errors",ConsoleType.SYSTEM_ERR);
		new RedirectedConsoleForJavaProcess(700, 700,701,1,RunBuildHistoricalFutDbCreate.class.getSimpleName()+" status",ConsoleType.SYSTEM_OUT);
		Map<String,String> ap = 
				(Map<String,String>)Utils.getArgPairsSeparatedByChar(args, "=");
		
		// ****************** get exchange mapping info **************
		String prodConvMapPath = ap.get("prodConvMapPath");
		Map<String, String> prodConvMap = new HashMap<String, String>();
		if(prodConvMapPath!=null){
			prodConvMap = (Map<String, String>)Utils.getXmlData(Map.class, null, prodConvMapPath);
		}
		// ****************** get mongo info **************
		String ip = ap.get("mongoIp");
		Integer port = new Integer(ap.get("mongoPort"));
		MongoCollectionWrapper mcw = 
				new MongoCollectionWrapper(
						ip, port, 
						MongoDatabaseNames.SPAN_HIST_DB	,
						MongoDatabaseNames.SPAN_HIST_CL);

		// ****************** get days to use for history **************
		int daysBack = new Integer(ap.get("daysBack")) * -1;
		Calendar endDate = Calendar.getInstance();
		Calendar startDate = Dates.addBusinessDays("US", endDate,daysBack);
		String spanUnzipFolder = ap.get("spanUnzipFolder");
		String spanFileTemplate = ap.get("spanFileTemplate");
		String lastChar = 
				spanUnzipFolder.substring(spanUnzipFolder.length()-1, spanUnzipFolder.length());
		if(lastChar.compareTo("/")!=0){
			spanUnzipFolder = spanUnzipFolder+"/";
		}
		// ****************** create span file name list **************
		List<DateTime> dtList = 
				Dates.getAllBusinessDays(startDate, endDate);
		
		
		// ****************** drop the index to speed things up ****************
		Utils.prtObMess(RunBuildHistoricalFutDbCreate.class, "processing : Dropping  indexes for collection " + MongoDatabaseNames.SPAN_HIST_CL );

		List<DBObject> indexes = mcw.getCollection().getIndexInfo();
		for(DBObject index : indexes){
			if(index.toMap().get("name").toString().contains("_id")){
				continue;// don't drop the id field
			}
			String indexName = index.toMap().get("name").toString();
			mcw.getCollection().dropIndex(indexName);
		}
		// 
		// ****************** remove everything ****************
		Utils.prtObMess(RunBuildHistoricalFutDbCreate.class, "processing : removing all records for  collection " + MongoDatabaseNames.SPAN_HIST_CL );
		mcw.getCollection().remove(new BasicDBObject());
		
		// *******************loop thru each date ***********************
		for(DateTime dt : dtList){
			int year = dt.getYear();
			int month = dt.getMonthOfYear();
			int day = dt.getDayOfMonth();
			Integer yyyyMmDd = new Integer(year*100*100 + month*100 + day);
			String fileName = spanFileTemplate.replace("YYYYMMDD", yyyyMmDd.toString());
			String filePath =spanUnzipFolder+fileName; 
			// **********************
			try {
				// see if file exists
				long len = Utils.getFileLength(filePath);
				if(len<1){
					// file does not exist, move on
					Utils.prtObErrMess(RunBuildHistoricalFutDbCreate.class, "no span file for: " + filePath);
					continue;
				}
				List<String> lineData = Utils.getLineData(null, filePath);
				if(lineData==null || lineData.size()<1){
					// file does not exist, move on
					Utils.prtObErrMess(RunBuildHistoricalFutDbCreate.class, "no span file for: " + filePath);
					continue;
				}
				Utils.prtObMess(RunBuildHistoricalFutDbCreate.class, "processing : " + filePath);

				// ****************** CALL processFile here ***********************
				List<SpanHist> spList = 
						processFile(yyyyMmDd,lineData,prodConvMap);
				List<DBObject> dboList = mcw.toDboList(spList);
				mcw.getCollection().insert(dboList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		// set indices		
		DBObject indexKey = new BasicDBObject();
		indexKey.put(SpanHist.SHORTNAME_FIELD,1);
		mcw.getCollection().createIndex(indexKey);
		indexKey = new BasicDBObject();
		indexKey.put(SpanHist.YYYYMMDD_FIELD,1);
		mcw.getCollection().createIndex(indexKey);
		
		Calendar end = Calendar.getInstance();
		long totalMin = Dates.getDifference(end, beg, TimeUnit.MINUTES);
		Utils.prtObMess(RunBuildHistoricalFutDbCreate.class, "ALL PROCESSING DONE.  Total minutes:  " + totalMin);

	}
	
	private static final List<SpanHist> processFile(
			Integer yyyyMmDd,
			List<String> lineList,
			Map<String, String> prodConvMap){
		
		Map<String,FinalSettle> finalStringSettleMap = new HashMap<String, FinalSettle>();
		Map<String,FirstPhys> firstPhsMap = new HashMap<String,FirstPhys>();
		Map<String, PsDoc> priceSpecMap = new HashMap<String, PsDoc>();
		
		for(int lineNum=0;lineNum<lineList.size();lineNum++){
			String line = lineList.get(lineNum);
			String recIdString = line.substring(0, 2);
			
			String enumName = RecordType.getEnumNameByValue(recIdString);
			if(enumName!=null) {
				RecordType recId = RecordType.valueOf(enumName);
				String prodTypeCode=null;
				try {
					switch(recId) {
					case PRICE_SPECS:
						PsDoc psDoc = new PsDoc(line);
						String psKey = getKey(psDoc);
						priceSpecMap.put(psKey,psDoc);
						break;

					case FIRST_PHYS_REC:
						prodTypeCode = line.substring(25,28);
						if(!(prodTypeCode.compareTo(futType)==0 || prodTypeCode.compareTo(cmbType)==0)){
							break;
						}

						FirstPhys fp1Doc = new FirstPhys(line);
						String key1 = getKey(fp1Doc);
						if(firstPhsMap.containsKey(key1)) {
						} else {
							firstPhsMap.put(key1, fp1Doc);
							FinalSettle fs1 = new FinalSettle(fp1Doc.exchAcro, fp1Doc.prodCommCode, fp1Doc.prodTypeCode, 
									fp1Doc.futMonth, fp1Doc.futDayWeek, fp1Doc.optMonth, 
									fp1Doc.optDayWeek, fp1Doc.highPrecSettlePrice);
							finalStringSettleMap.put(key1,fs1);
						}
						break;
					case SECOND_PHYS_REC:
						prodTypeCode = line.substring(25,28);
						if(!(prodTypeCode.compareTo(futType)==0 || prodTypeCode.compareTo(cmbType)==0)){
							break;
						}

//						SecondPhysical sp = new SecondPhysical(line);
						SecondPhys spDoc = new SecondPhys(line);
						String key2 = getKey(spDoc);
						if(!firstPhsMap.containsKey(key2)) {
							Utils.prtErr("Why not entry for dis: "+key2);
						} else {
							FinalSettle fs2 = finalStringSettleMap.get(key2);
							if(fs2.settleString==null){
								FirstPhys prevRiskArrayDoc = firstPhsMap.get(key2);
								String newSettleString = prevRiskArrayDoc.highPrecSettlePrice;
								if(newSettleString==null) newSettleString =  
										(spDoc.settlePriceSign.compareTo("-")==0) ? 
										"-"+ spDoc.settlePrice : spDoc.settlePrice;
								fs2.settleString = newSettleString;
								finalStringSettleMap.put(key2,fs2);
							}
						}
						break;
						
					default:
						break;
					}
				
				} catch (Exception e) {
					Utils.prtObErrMess(RunBuildHistoricalFutDbCreate.class, e.getMessage());
					Utils.prtObErrMess(RunBuildHistoricalFutDbCreate.class, line);
				}
			}
		
		}
		
		// *********************  write settlements to settleMap ***********************
		Map<String, BigDecimal> settleMap  = new TreeMap<String, BigDecimal>();
		for(Entry<String,FinalSettle> entry : finalStringSettleMap.entrySet()){
			FinalSettle rad = entry.getValue();
			String symbol = rad.prodCommCode;
			String type = rad.prodTypeCode;
			if(!(type.contains(futType) || type.contains(cmbType))){
				continue;  // ignore if not futures
			}
			String exch = rad.exchAcro;

			
			String psKey = symbol+type+exch;
			// get psDoc
			PsDoc psDoc =  priceSpecMap.get(psKey);
			if(psDoc==null){
				 Utils.prtObErrMess(RunBuildHistoricalFutDbCreate.class,"Missing PsDoc for key : " + psKey );
				 continue;
			}
			int prec = new Integer(psDoc.settlePriceDecLoc);

			// see if we have to make any conversions to the symbol, type or exch before putting it in the final db
			String converted = prodConvMap.get(symbol+SEP+type+SEP+exch);
			if(converted!=null){
				String[] split = converted.split(SPLIT_STRING);
				symbol = split[0];
				type=  split[1];
				exch = split[2];
			}

			
			SecSymbolType symbolType = SecSymbolType.fromString( type);
			SecExchange exchange = SecExchange.fromString(exch);
			SecCurrency currency=SecCurrency.USD;
			try {
				currency = SecCurrency.fromString(psDoc.settleCurrISO);
			} catch (Exception e) {
				Utils.prtObErrMess(RunBuildHistoricalFutDbCreate.class, "currency "+psDoc.settleCurrISO + " not suppported");
				continue;
			}
			Integer contractYear = new Integer(rad.futMonth.substring(0,4));
			Integer contractMonth = new Integer(rad.futMonth.substring(4,6));
			Integer contractDay = null;
			if(rad.futDayWeek.trim().compareTo("  ")>0){
				contractDay = new Integer(rad.futDayWeek);				
			}
			ShortNameInfo sni = 
					new ShortNameInfo(symbol, symbolType, exchange, 
							currency, contractYear, contractMonth, 
							contractDay, null, null);
			String shortName = sni.getShortName();
			BigDecimal multiplier = BigDecimal.ONE.divide(BigDecimal.TEN.pow(prec));
			BigDecimal settle = new BigDecimal(rad.settleString).multiply(multiplier).setScale(prec, RoundingMode.HALF_EVEN);
			settleMap.put(shortName, settle);
		}


		// ******************** write settleMap to db ***********************
		List<SpanHist> shList = new ArrayList<SpanHist>();
		for(Entry<String, BigDecimal> entry: settleMap.entrySet()){
			SpanHist sh = new SpanHist(entry.getKey(), yyyyMmDd, entry.getValue());
			shList.add(sh);
		}
		return shList;
	}
	
	private static String getKey(PsDoc rec) {
		return rec.prodCommCode+rec.prodTypeCode+rec.exchAcro;
	}

	
	private static String getKey(FirstPhys rec) {
		return rec.prodCommCode+rec.prodTypeCode+rec.exchAcro+rec.futMonth+rec.futDayWeek;
	}

	private static String getKey(SecondPhys rec) {
		return rec.prodCommCode+rec.prodTypeCode+rec.exchAcro+rec.futMonth+rec.futDayWeek;
	}

	private static class PsDoc{
		
		private final String exchAcro ;
		private final String prodCommCode;
		private final String prodTypeCode;
		private final String settleCurrISO;
		private final String settlePriceDecLoc;

		private PsDoc(String line){
			this.exchAcro = line.substring(2,5).trim();
			this.prodCommCode = line.substring(5,15).trim();
			this.prodTypeCode = line.substring(15,18).trim();
			this.settleCurrISO = line.substring(65,68).trim();
			this.settlePriceDecLoc = line.substring(33,36).trim();

		}
	}
	
	private static class FirstPhys {
		private final String exchAcro ;
		private final String prodCommCode;
		private final String prodTypeCode;
		private final String futMonth;
		private final String futDayWeek;
		private final String optMonth;
		private final String optDayWeek;
		private final String highPrecSettlePrice; 
		private final String highPrecSettlePriceFlag;

		private FirstPhys(String line){
			this.exchAcro = line.substring(2,5).trim();
			this.prodCommCode = line.substring(5,15).trim();
			this.prodTypeCode = line.substring(25,28).trim();
			this.futMonth = line.substring(29,35).trim();
			this.futDayWeek = line.substring(35,37).trim();
			this.optMonth = line.substring(38,44).trim();
			this.optDayWeek = line.substring(44,46).trim();
		
			String highPrecSettlePrice = null;
			String highPrecSettlePriceFlag = null;
			
			if(line.length()>=122) {
				highPrecSettlePrice = line.substring(108,122).trim();
				highPrecSettlePriceFlag = line.substring(122,123).trim();
			}
			
			this.highPrecSettlePrice = highPrecSettlePrice;
			this.highPrecSettlePriceFlag = highPrecSettlePriceFlag;

		}
	}


	private static class SecondPhys {
		private final String exchAcro ;
		private final String prodCommCode;
		private final String prodTypeCode;
		private final String futMonth;
		private final String futDayWeek;
		private final String optMonth;
		private final String optDayWeek;
		private final String settlePrice;
		private final String settlePriceSign;

		private SecondPhys(String line){
			this.exchAcro = line.substring(2,5).trim();
			this.prodCommCode = line.substring(5,15).trim();
			this.prodTypeCode = line.substring(25,28).trim();
			this.futMonth = line.substring(29,35).trim();
			this.futDayWeek = line.substring(35,37).trim();
			this.optMonth = line.substring(38,44).trim();
			this.optDayWeek = line.substring(44,46).trim();

			this.settlePrice = line.substring(110,117).trim();
			this.settlePriceSign = line.substring(117,118).trim();

		}
	}
	
	private static class FinalSettle {
		private final String exchAcro ;
		private final String prodCommCode;
		private final String prodTypeCode;
		private final String futMonth;
		private final String futDayWeek;
		private final String optMonth;
		private final String optDayWeek;
		private String settleString;
		
		private FinalSettle(String exchAcro, String prodCommCode,
				String prodTypeCode, String futMonth, String futDayWeek,
				String optMonth, String optDayWeek, String settleString) {
			super();
			this.exchAcro = exchAcro;
			this.prodCommCode = prodCommCode;
			this.prodTypeCode = prodTypeCode;
			this.futMonth = futMonth;
			this.futDayWeek = futDayWeek;
			this.optMonth = optMonth;
			this.optDayWeek = optDayWeek;
			this.settleString = settleString;
		}
		
	}

}
