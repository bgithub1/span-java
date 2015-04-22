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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.billybyte.commoncollections.Tuple;
//import com.billybyte.collectionextentions.Tuple;
import com.billybyte.commonstaticmethods.Utils;

public class ParseIceSpanFileProdGrps {

	private final BufferedReader br;
		
	public ParseIceSpanFileProdGrps(Reader reader) throws IOException {
		this.br = new BufferedReader(reader);
	}
	
	public static void main(String[] args) {
		
		String spanFileName = "ice.20130128.pa5";
		
		try {
			
			Calendar beforeTime = Calendar.getInstance();
			ParseIceSpanFileProdGrps spanParser = new ParseIceSpanFileProdGrps(new FileReader(spanFileName));
			
			spanParser.processSpan();
			
			Calendar afterTime = Calendar.getInstance();

			Utils.prt("took "+(afterTime.getTimeInMillis()-beforeTime.getTimeInMillis())+" ms");
			
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void processSpan() throws IOException {
						
//		Integer prevRecId = 00;
//		int recCount = 0;
		
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
//					if(subRecId<50) {
//						if(paramMap.containsKey(subRecId)){
//							paramMap.get(subRecId).add(rec);
//						} else {
//							List<String> newInnerParamList = new ArrayList<String>();
//							newInnerParamList.add(rec);
//							paramMap.put(subRecId, newInnerParamList);
//						}
//					} else 
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
		
		for(Entry<String,Tuple<String,List<String[]>>> entry:prodGroupMap.entrySet()) {
			String conCode = entry.getKey();
			String prodType = conCode.substring(3,4);
			String rec40 = entry.getValue().getT1_instance();
			Double tickSize = Double.parseDouble(rec40.substring(29,35).trim());
			Long log = Math.round(Math.log10(new Double(tickSize)));
			Integer decSpaces = Integer.parseInt(log.toString());
			Map<String,BigDecimal> innerMap = new HashMap<String,BigDecimal>();
			for(String[] arrayPair:entry.getValue().getT2_instance()) {
				String contractExp = arrayPair[0].substring(2,10);
				String settleKey = contractExp;
				if(prodType.compareTo("O")==0) {
					String optRight = arrayPair[1].substring(10,11);
					String strikePrice = arrayPair[1].substring(2,10).trim();
					settleKey = settleKey+optRight+strikePrice;
				}
				String settleString = arrayPair[1].substring(17,25).trim();
				boolean isNeg = settleString.contains("-");
				if(isNeg) settleString = settleString.replace("-","");
				String decimalSettleString;
				if(settleString.length()<decSpaces) {
					decimalSettleString = "."+settleString;
				} else {
					decimalSettleString = settleString.substring(0,settleString.length()-decSpaces)+"."+
							settleString.substring(settleString.length()-decSpaces);
				}
				if(isNeg) decimalSettleString = "-"+decimalSettleString;
				innerMap.put(settleKey, new BigDecimal(decimalSettleString));
			}
			settleMap.put(conCode, innerMap);
			totArrayListSize = totArrayListSize+entry.getValue().getT2_instance().size();
		}		

//		String prodKey = "QHHO";
		String prodKey = "H  F";
		
		TreeMap<String,BigDecimal> ngSetMap = new TreeMap<String,BigDecimal>(settleMap.get(prodKey));
		for(Entry<String,BigDecimal> entry:ngSetMap.entrySet()) {
			Utils.prt(entry.getKey()+","+entry.getValue());
		}
		
//		String rec40 = prodGroupMap.get(prodKey).getT1_instance();
		
//		Utils.prt(prodGroupMap.size()+","+totArrayListSize);
		
	}
	

}
