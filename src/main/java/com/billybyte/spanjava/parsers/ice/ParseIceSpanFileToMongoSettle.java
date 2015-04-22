package com.billybyte.spanjava.parsers.ice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.billybyte.commoncollections.Tuple;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.mongo.MongoDoc;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanSettleDoc;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.resources.SpanUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class ParseIceSpanFileToMongoSettle {
	
	private final BufferedReader br;
	private final DBCollection settleColl;	
		
	public ParseIceSpanFileToMongoSettle(Reader reader, DBCollection settleColl) throws IOException {
		this.br = new BufferedReader(reader);
		
		this.settleColl = settleColl;
		
	}
	
	public static void main(String[] args) {
		String folder = "/Users/bperlman1/dropbox/jrtr/deployment_3_0/meteorlaunch/bsr/xml/db/unzipfolder/";
		String fileName = "ice.20131004.pa5";
		String spanFileName = folder+fileName;
		
		try {
			
			MongoWrapper m = new MongoWrapper("127.0.0.1",27022);
//			m.setWriteConcern(WriteConcern.SAFE);
			DB db = m.getDB(SpanMongoUtils.SETTLE_DB);
			
			DBCollection coll = db.getCollection(SpanMongoUtils.SETTLE_CL);
			
			coll.remove(new BasicDBObject());
			
			Calendar beforeTime = Calendar.getInstance();
			ParseIceSpanFileToMongoSettle spanParser = new ParseIceSpanFileToMongoSettle(new FileReader(spanFileName), coll);
			
			@SuppressWarnings("unchecked")
			Map<String,String> prodConvMap = Utils.getXmlData(Map.class, SpanUtils.class, "iceProdConvMap.xml");
			
			clearIceSettles(coll);
			
			Utils.prt("started processing span file...");
			
			spanParser.processSpan(true, prodConvMap);
			
			Calendar afterTime = Calendar.getInstance();

			Utils.prt("took "+(afterTime.getTimeInMillis()-beforeTime.getTimeInMillis())+" ms");
			
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void clearIceSettles(DBCollection coll) {
		DBObject iceDoc = new BasicDBObject("_id",new BasicDBObject("$regex","\\.ICE\\."));
		coll.remove(iceDoc);
		DBObject ipeDoc = new BasicDBObject("_id",new BasicDBObject("$regex","\\.IPE\\."));
		coll.remove(ipeDoc);
	}
	
	
	public Map<String,Map<String,BigDecimal>> createSpanSettleDocMap(
			boolean useProdConvMap, 
			Map<String,String> prodConvMap) throws IOException {
		Map<String,SpanSettleDoc> batchInsertMap = new HashMap<String,SpanSettleDoc>();
		
		List<List<String>> prodGroupLists = new ArrayList<List<String>>();
		List<String> combContractList = new ArrayList<String>();
		
		while(br.ready()) {
			
			String line = br.readLine();
			Integer recId = Integer.parseInt(line.substring(0,2));
			
			if(recId==40) {
				prodGroupLists.add(combContractList);
				combContractList = new ArrayList<String>();
				combContractList.add(line);
			} else {
				combContractList.add(line);				
			}
			
		}
		
		prodGroupLists.remove(0);

		Map<String,Tuple<String,List<String[]>>> prodGroupMap = 
				new HashMap<String,Tuple<String,List<String[]>>>();
		
		for(List<String> subList:prodGroupLists) {
			if(!subList.isEmpty()){
				String rec40 = subList.get(0);
				String conCode = rec40.substring(2,6); // contract code + generic contract type
//				Map<Integer,List<String>> paramMap = new HashMap<Integer,List<String>>();
				List<String[]> arrayList = new ArrayList<String[]>();
				for(int i=0;i<subList.size();i++) { //String rec:subList) {
					String rec = subList.get(i);
					Integer subRecId = Integer.parseInt(rec.substring(0,2));
					if(subRecId==50){
						Integer nextRecId = 60;//subList.get(i+1);
						int refIndex = i;
						while(nextRecId==60) {
							String nextRec = subList.get(refIndex+1);
							String[] arrayPair = new String[2];
							arrayPair[0] = rec;
							arrayPair[1] = nextRec;
							arrayList.add(arrayPair);
							refIndex++;
							if(subList.size()<=(refIndex+1)) break;
							nextRecId = Integer.parseInt(subList.get(refIndex+1).substring(0,2));
//							arrayPair[1] = subList.get(i+1);
						}
						
					}
				}
				
				Tuple<String,List<String[]>> recGroup = new Tuple<String,List<String[]>>(rec40, arrayList);
				
				prodGroupMap.put(conCode, recGroup);
			}
		}

		Map<String,Map<String,BigDecimal>> settleMap = new HashMap<String,Map<String,BigDecimal>>();
		
		int totArrayListSize = 0;

//		Map<String,MongoDoc> batchInsertMap = new HashMap<String,MongoDoc>();
		Set<String> prodSet = new HashSet<String>();
		Set<String> shortNameSet = new HashSet<String>();
		for(Entry<String,Tuple<String,List<String[]>>> entry:prodGroupMap.entrySet()) {
			String conCode = entry.getKey();
			String prodSymbol = conCode.substring(0,3).trim();
			String exchange = "ICE";
			if(useProdConvMap) {
				if(prodConvMap.containsKey(prodSymbol)) {
					String convKey = prodConvMap.get(prodSymbol);
					String[] convArr = convKey.split("\\.");
					prodSymbol = convArr[0];
					exchange = convArr[1];
				} else {
					prodSet.add(prodSymbol);
					continue;
				}
			}
			
			String prodType = conCode.substring(3,4);
			String rec40 = entry.getValue().getT1_instance();
			Double tickSize = Double.parseDouble(rec40.substring(29,35).trim());
			Long settleLog = Math.round(Math.log10(tickSize));
			Integer decSpaces = Integer.parseInt(settleLog.toString());
			
			Map<String,BigDecimal> innerMap = new HashMap<String,BigDecimal>();
			for(String[] arrayPair:entry.getValue().getT2_instance()) {
				String contractExp = arrayPair[0].substring(2,10).trim();
				String settleKey = contractExp;
				String shortName;
				if(prodType.compareTo("O")==0) {
					Double strikeDenom = Double.parseDouble(rec40.substring(69,75).trim());
					Long strikeLog = Math.round(Math.log10(strikeDenom));
					Integer strikeDecPlaces = Integer.parseInt(strikeLog.toString());

					String optRight = arrayPair[1].substring(10,11);
					String strikeString = arrayPair[1].substring(2,10).trim();
					String strikePrice = SpanMongoUtils.formatPriceWithDecimal(strikeString, strikeDecPlaces);
					settleKey = settleKey+optRight+strikePrice;
					shortName = SpanUtils.makeIceShort(prodSymbol, contractExp, optRight, strikePrice, exchange);
				} else {
					shortName = SpanUtils.makeIceShort(prodSymbol, contractExp, exchange);
				}
				String settleString = arrayPair[1].substring(17,25).trim();
				String decimalSettleString = SpanMongoUtils.formatPriceWithDecimal(settleString, decSpaces);
				
				SpanSettleDoc insertDoc = new SpanSettleDoc(shortName, decimalSettleString);
				if(!shortNameSet.contains(shortName)){
					shortNameSet.add(shortName);
					batchInsertMap.put(shortName,insertDoc);
				}
				innerMap.put(settleKey, new BigDecimal(decimalSettleString));
				
			}
			settleMap.put(conCode, innerMap);
			totArrayListSize = totArrayListSize+entry.getValue().getT2_instance().size();
		}		
	
		
		return settleMap;
	}
	
	
	public Integer processSpan(boolean useProdConvMap, Map<String,String> prodConvMap) throws IOException {
								
		List<List<String>> prodGroupLists = new ArrayList<List<String>>();
		List<String> combContractList = new ArrayList<String>();
		
		while(br.ready()) {
			
			String line = br.readLine();
			Integer recId = Integer.parseInt(line.substring(0,2));
			
			if(recId==40) {
				prodGroupLists.add(combContractList);
				combContractList = new ArrayList<String>();
				combContractList.add(line);
			} else {
				combContractList.add(line);				
			}
			
		}
		
		prodGroupLists.remove(0);
				
		Map<String,Tuple<String,List<String[]>>> prodGroupMap = 
				new HashMap<String,Tuple<String,List<String[]>>>();
		
		for(List<String> subList:prodGroupLists) {
			if(!subList.isEmpty()){
				String rec40 = subList.get(0);
				String conCode = rec40.substring(2,6); // contract code + generic contract type
//				Map<Integer,List<String>> paramMap = new HashMap<Integer,List<String>>();
				List<String[]> arrayList = new ArrayList<String[]>();
				for(int i=0;i<subList.size();i++) { //String rec:subList) {
					String rec = subList.get(i);
					Integer subRecId = Integer.parseInt(rec.substring(0,2));
					if(subRecId==50){
						Integer nextRecId = 60;//subList.get(i+1);
						int refIndex = i;
						while(nextRecId==60) {
							String nextRec = subList.get(refIndex+1);
							String[] arrayPair = new String[2];
							arrayPair[0] = rec;
							arrayPair[1] = nextRec;
							arrayList.add(arrayPair);
							refIndex++;
							if(subList.size()<=(refIndex+1)) break;
							nextRecId = Integer.parseInt(subList.get(refIndex+1).substring(0,2));
//							arrayPair[1] = subList.get(i+1);
						}
						
					}
				}
				
				Tuple<String,List<String[]>> recGroup = new Tuple<String,List<String[]>>(rec40, arrayList);
				
				prodGroupMap.put(conCode, recGroup);
			}
		}

		Map<String,Map<String,BigDecimal>> settleMap = new HashMap<String,Map<String,BigDecimal>>();
		
		int totArrayListSize = 0;
		Map<String,MongoDoc> batchInsertMap = new HashMap<String,MongoDoc>();
		Set<String> prodSet = new HashSet<String>();
		Set<String> shortNameSet = new HashSet<String>();
		for(Entry<String,Tuple<String,List<String[]>>> entry:prodGroupMap.entrySet()) {
			String conCode = entry.getKey();
			String prodSymbol = conCode.substring(0,3).trim();
			if(prodSymbol.compareTo("RBS")==0){
				Utils.prtObMess(this.getClass(), "found symbol RBS");
			}
			String exchange = "ICE";
			if(useProdConvMap) {
				if(prodConvMap.containsKey(prodSymbol)) {
					String convKey = prodConvMap.get(prodSymbol);
					String[] convArr = convKey.split("\\.");
					prodSymbol = convArr[0];
					exchange = convArr[1];
				} else {
					prodSet.add(prodSymbol);
					continue;
				}
			}
			
			String prodType = conCode.substring(3,4);
			String rec40 = entry.getValue().getT1_instance();
			// modified 20150403 by bp, enlarge ticksize from 6 to 8 bytes for pa6
//			Double tickSize = Double.parseDouble(rec40.substring(29,35).trim());
			Double tickSize = Double.parseDouble(rec40.substring(29,37).trim());
			// END modified 20150403 by bp, enlarge ticksize for pa6
			Long settleLog = Math.round(Math.log10(tickSize));
			Integer decSpaces = Integer.parseInt(settleLog.toString());
			
			Map<String,BigDecimal> innerMap = new HashMap<String,BigDecimal>();
			for(String[] arrayPair:entry.getValue().getT2_instance()) {
				String contractExp = arrayPair[0].substring(2,10).trim();
				String settleKey = contractExp;
				String shortName;
				if(prodType.compareTo("O")==0) {
					// modified 20150403 by bp, changr offset for strike denomination from 69 to 71 for pa6					
//					Double strikeDenom = Double.parseDouble(rec40.substring(69,75).trim());
					Double strikeDenom = Double.parseDouble(rec40.substring(71,77).trim());
					// END modified 20150403 by bp, changr offset for strike denomination from 69 to 71 for pa6					
					Long strikeLog = Math.round(Math.log10(strikeDenom));
					Integer strikeDecPlaces = Integer.parseInt(strikeLog.toString());

					String optRight = arrayPair[1].substring(10,11);
					String strikeString = arrayPair[1].substring(2,10).trim();
					String strikePrice = SpanMongoUtils.formatPriceWithDecimal(strikeString, strikeDecPlaces);
					settleKey = settleKey+optRight+strikePrice;
					shortName = SpanUtils.makeIceShort(prodSymbol, contractExp, optRight, strikePrice, exchange);
				} else {
					shortName = SpanUtils.makeIceShort(prodSymbol, contractExp, exchange);
				}
				// modified 20150403 make settle substring 12 chars instead of 8
//				String settleString = arrayPair[1].substring(17,25).trim();
				String settleString = arrayPair[1].substring(17,29).trim();
				String decimalSettleString = SpanMongoUtils.formatPriceWithDecimal(settleString, decSpaces);
				
				MongoDoc insertDoc = new SpanSettleDoc(shortName, decimalSettleString);
				if(!shortNameSet.contains(shortName)){
					shortNameSet.add(shortName);
					batchInsertMap.put(shortName,insertDoc);
				}
				innerMap.put(settleKey, new BigDecimal(decimalSettleString));
				
			}
			settleMap.put(conCode, innerMap);
			totArrayListSize = totArrayListSize+entry.getValue().getT2_instance().size();
		}		

		SpanMongoUtils.batchInsert(settleColl, new ArrayList<MongoDoc>(batchInsertMap.values()));
		
		Utils.prt(batchInsertMap.size()+" ice span settle docs inserted");
		return batchInsertMap.size();
		
	}

}
