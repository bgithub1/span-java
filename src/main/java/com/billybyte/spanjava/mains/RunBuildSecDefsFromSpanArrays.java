package com.billybyte.spanjava.mains;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.billybyte.commoncollections.Tuple;
import com.billybyte.commonstaticmethods.Dates;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.SecDef;
import com.billybyte.marketdata.SecDefSimple;
import com.billybyte.marketdata.SecEnums.SecSymbolType;
import com.billybyte.marketdata.ShortNameInfo;
import com.billybyte.spanjava.mongo.FromSpanFilesGenerator;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.billybyte.ui.RedirectedConsoleForJavaProcess;
import com.billybyte.ui.RedirectedConsoleForJavaProcess.ConsoleType;
/**
 * Build SecDef's, settles and implied vols for products in prodIdMap.xml
 * @author bperlman1
 *
 */
public class RunBuildSecDefsFromSpanArrays {
	
	private final String mongoIp;
	private final int mongoPort;
	private final String exchProdTypeMapXmlPath;
	private final Calendar dateOfSpan;
	
	
	
	public RunBuildSecDefsFromSpanArrays(String mongoIp, int mongoPort,
			String exchProdTypeMapXmlPath,
			Calendar dateOfSpan) {
		super();
		this.mongoIp = mongoIp;
		this.mongoPort = mongoPort;
		this.exchProdTypeMapXmlPath = exchProdTypeMapXmlPath;
		this.dateOfSpan = dateOfSpan;
	}

	/**
	 * 
	 * @param args Examples: 
	 * 		"mongoIp=127.0.0.1" mongoPort=27022 "exchProdTypeCsvPath=prodIdCsv.csv "createSettlesImpVols=true" "createSecDefs=true"
	 * Alternatively - but NOT recommended:
	 * 		"mongoIp=127.0.0.1" mongoPort=27022 "exchProdTypeMapXmlPath=prodIdMap.xml "createSettlesImpVols=true" "createSecDefs=true"
	 * 		"mongoIp=127.0.0.1" mongoPort=27022 "exchProdTypeMapXmlPath=prodIdMap.xml "createSettlesImpVols=false" "createSecDefs=true"
	 */
	public static void main(String[] args) {
		
		//  1.  Set up console
		Calendar start = Calendar.getInstance();
		Utils.prtObErrMess(RunBuildSecDefsFromSpanArrays.class, "Start All Data: " + start.getTime().toString());
		Map<String, String> argPairs = 
				Utils.getArgPairsSeparatedByChar(args, "=");
		new RedirectedConsoleForJavaProcess(1000, 1000,1,1,RunBuildSecDefsFromSpanArrays.class.getSimpleName(),ConsoleType.SYSTEM_OUT);
		
		// 2. Determine date of Span run
		String spanFilePath = argPairs.get("spanFilePath");
		Calendar dateOfSpan = FromSpanFilesGenerator.dateFromSpanFile(spanFilePath);		
		if(dateOfSpan==null){
			dateOfSpan = Dates.getSettlementDay(Calendar.getInstance(), 16, 10);			
//			dateOfSpan = Dates.getCalendarFromYYYYMMDD(new Long(argPairs.get("yyyyMmDdOfSpan")));
		}		
		
		// 3. Get mongo params
		String mongoIp = argPairs.get("mongoIp")==null ?  "127.0.0.1" :argPairs.get("mongoIp") ;
		String mport = argPairs.get("mongoPort");
		int mongoPort = mport==null ? 27022 : new Integer(mport);
		String createSecDefsString = argPairs.get("createSecDefs");
		boolean createSecDefs = false;
		if(createSecDefsString!=null && new Boolean(createSecDefsString)){
			createSecDefs= true;
		}
		boolean createSettlesImpVols = false;
		String createSettlesImpVolsString = argPairs.get("createSettlesImpVols");
		if(createSettlesImpVolsString!=null && new Boolean(createSettlesImpVolsString)){
			createSettlesImpVols = true;
		}
		
		
		String exchProdTypeMapXmlPath = argPairs.get("exchProdTypeMapXmlPath");
		
		// 4. !!!!!!!!!!!!!!!!! Create the main FromSpanFilesGenerator
		RunBuildSecDefsFromSpanArrays builder = 
				new RunBuildSecDefsFromSpanArrays(mongoIp, mongoPort, exchProdTypeMapXmlPath, dateOfSpan);
		// 5.  DO the build
		builder.process(createSecDefs, createSettlesImpVols);

		// 6.  write out time it took to do it all
		Calendar end = Calendar.getInstance();
		double totalHours = Dates.getDifference(end, start, TimeUnit.MINUTES)/60.0;
		Utils.prtObErrMess(RunBuildSecDefsFromSpanArrays.class, "End All Data: " + end.getTime().toString());
		Utils.prtObErrMess(RunBuildSecDefsFromSpanArrays.class, "Total Hours: " + totalHours);
	
	
	
	}
	
	@SuppressWarnings("unchecked")
	public void process(boolean createSecDefsString,boolean createSettlesImpVols){
		FromSpanFilesGenerator fsg = new  FromSpanFilesGenerator(mongoIp, mongoPort);

		// 5.  Create List<SpanProdId> prodIdList
		/// get a List<SpanProdId> via either a csv list that will get
		//   converted to SpanProdId's or a map that will get converted to a list of SpanProdId's.
		List<SpanProdId> prodIdList = null;
		// first try csv list
//		String exchProdTypeMapXmlPath = argPairs.get("exchProdTypeMapXmlPath");
		if(exchProdTypeMapXmlPath!=null){
			Map<String, String> prodIdMap = Utils.getXmlData(Map.class, null, exchProdTypeMapXmlPath);
			prodIdList = fsg.spanProdListFromMap(prodIdMap);
		}
		
		//  6.  Main loop to create SecDefs
		if(createSecDefsString){
			int numProdIds = prodIdList.size();
			int subListSize = 20;
			for(int i = 0;i<numProdIds;i=i+subListSize){
				int upperBound = i+subListSize;
				if(upperBound>numProdIds){
					upperBound = numProdIds;
				}
				List<SpanProdId> subList = prodIdList.subList(i, upperBound);
				
				// Create the secdefs here
//				Map<String, SecDef> sdMap = fsg.getSecDefs(dateOfSpan, subList);
				Tuple<Map<String, SecDef>,Map<String,String>> tuple = fsg.getSecDefs(dateOfSpan, subList);
				Map<String, SecDef> sdMap = tuple.getT1_instance();
				// HACK ALERT
				sdMap = convertSecDefs(sdMap);
				Utils.prt("SecDefs:");
				for(String sn : sdMap.keySet()){
					SecDef sd = sdMap.get(sn);
					Utils.prt(sd.toString());
				}
				Map<String,String> underMap = tuple.getT2_instance();
				// HACK ALERT
				underMap = convertUnderShortNames(underMap);
				Utils.prt("underlyingShortNames:");
				for(String sn:underMap.keySet()){
					String underSn = underMap.get(sn);
					Utils.prt(sn+","+underSn);
				}
				
				// write the secdefs to mongo here
				Utils.prtObErrMess(FromSpanFilesGenerator.class, "Start writing data to mongo: " + Calendar.getInstance().getTime().toString());
				fsg.populateSecDefDb(sdMap);
				fsg.populateSpanUnderlyingDb(underMap);
				Utils.prtObErrMess(FromSpanFilesGenerator.class, "End writing data to mongo: " + Calendar.getInstance().getTime().toString());
			}
		}
		
		// 7.  Create and write the settles and implied vols
		if(createSettlesImpVols){
			fsg.processSettlesAndImpVolsFromSpanProdIds(prodIdList);
		}
		
		
	}
	
	private Map<String, SecDef> convertSecDefs(Map<String,SecDef> sdMap){
		// KIND OF A HACK ALERT !!!!!!!!!!!!!
		// change SecDefSymbolType's here to either FUT or FOP
		Map<String,SecDef> ret = new HashMap<String, SecDef>();
		for(Entry<String, SecDef> entry:sdMap.entrySet()){
			String sn = entry.getKey();
			SecDef sd = entry.getValue();
			SecSymbolType newSecSymType = null;
			if(SecSymbolType.OOF==sd.getSymbolType()){
				newSecSymType = SecSymbolType.FOP;
			}else if(SecSymbolType.OOC==sd.getSymbolType()){
				newSecSymType = SecSymbolType.FOP;
			}else if(SecSymbolType.CMB==sd.getSymbolType()){
				newSecSymType = SecSymbolType.FUT;
			}else{
				ret.put(sn, sd);
				continue;
			}
			ShortNameInfo newShortNameInfo = 
					new ShortNameInfo(sd.getSymbol(), newSecSymType, sd.getExchange(), 
							sd.getCurrency(), sd.getContractYear(), sd.getContractMonth(), 
							sd.getContractDay(), sd.getRight(), sd.getStrike());
			String newSn = newShortNameInfo.getShortName();
			SecDef newSd = 
					new SecDefSimple(newSn, newShortNameInfo, sd.getExchangeSymbol(), 
							sd.getExchangePrecision(), sd.getMinTick(), sd.getExpiryYear(), 
							sd.getExpiryMonth(), sd.getExpiryDay(), sd.getMultiplier(), 
							sd.getPrimaryExch());
			ret.put(newSn,newSd);
		}
		return ret;
	}
	
	private final Map<String,String> convertUnderShortNames(Map<String,String> underMap){
		Map<String, String> ret = new HashMap<String, String>();
		for(Entry<String, String> entry:underMap.entrySet()){
			String derivSn = entry.getKey();
			String newDerivSn = derivSn.replace("OOF", "FOP").replace("OOC", "FOP").replace("CMB", "FUT");
			String underSn = entry.getValue();
			String newUnderSn = underSn.replace("CMB", "FUT");
			ret.put(newDerivSn,newUnderSn);
		}
		return ret;
	}
	
}
