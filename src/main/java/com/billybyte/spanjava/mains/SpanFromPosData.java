package com.billybyte.spanjava.mains;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.billybyte.commoninterfaces.QueryInterface;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.MarketDataComLib;
import com.billybyte.marketdata.SecDef;
import com.billybyte.marketdata.SecDefQueryAllMarkets;
import com.billybyte.marketdata.SecEnums.SecSymbolType;
import com.billybyte.portfolio.PositionData;
import com.billybyte.queries.QueryFromListOfQueriesByRegexPattern;
import com.billybyte.spanjava.mongo.span.subtypes.SpanContractId;
import com.billybyte.spanjava.resources.SpanUtils;
import com.billybyte.spanjava.utils.BaseUnderlyingSecDefQuery;
import com.billybyte.spanjava.utils.SerialOptUnderQuery;

public class SpanFromPosData {

	private final Map<String,String> posToSpanConvMap;
		
	private final QueryFromListOfQueriesByRegexPattern<List<SecDef>> undQuery = 
			new QueryFromListOfQueriesByRegexPattern<List<SecDef>>(new String[]{}, new ArrayList<QueryInterface<String,List<SecDef>>>());
	
	private final QueryInterface<String,SecDef> baseSdQuery = new SecDefQueryAllMarkets();
	private final int[] hknuzMapper = {3,3,3,5,5,7,7,9,9,12,12,12};
	private final int[] hmuzMApper = {3,3,3,6,6,6,9,9,9,12,12,12};
	
	private final String s  = MarketDataComLib.DEFAULT_SHORTNAME_SEPARATOR;
	private final String futString = s + SecSymbolType.FUT.toString() + s;
	private final String fopString = s + SecSymbolType.FOP.toString() + s;

	public SpanFromPosData() {
		@SuppressWarnings("unchecked")
		Map<String,String> convMap = Utils.getXmlData(Map.class, SpanUtils.class, "spanConvMap.xml");
		this.posToSpanConvMap = new HashMap<String,String>();
		for(Entry<String,String> entry:convMap.entrySet()) {
			posToSpanConvMap.put(entry.getValue(), entry.getKey());
		}
		undQuery.addNewQueryInclusive(".", new BaseUnderlyingSecDefQuery(new SecDefQueryAllMarkets()));
		undQuery.addNewQueryInclusive(
				"((CC)|(KC)|(OZC)|(OZO)|(OZW))\\.FOP\\.((NYBOT)|(ECBOT))\\.USD", 
				new SerialOptUnderQuery(baseSdQuery, hknuzMapper));
		undQuery.addNewQueryInclusive(
				"^DX\\.FOP\\.NYBOT\\.USD", 
				new SerialOptUnderQuery(baseSdQuery, hmuzMApper));
	}
	
	public Map<SpanContractId,Integer> getSpanMapFromPosData(List<PositionData> posDataList) {
		
		Map<SpanContractId,Integer> retMap = new HashMap<SpanContractId,Integer>();
		
		for(PositionData pd:posDataList) {
			
			String shortName = pd.getShortName();
			
			if(shortName.contains(futString)||shortName.contains(fopString)) {
				String[] shortArr = shortName.split("\\.");
				String shortKey = shortArr[0]+"."+shortArr[1]+"."+shortArr[2];
				
				if(posToSpanConvMap.containsKey(shortKey)) {
					String convertedKey = posToSpanConvMap.get(shortKey);
					String[] convSplit = convertedKey.split("\\.");
					String contractMonth = shortArr[4];

					BigDecimal bdQty = pd.getQty();
					Integer intQty = bdQty.setScale(0, RoundingMode.HALF_EVEN).intValueExact();

					SpanContractId id;
					if(shortArr.length>5) {
						String strike = (shortArr.length>7) ? shortArr[6]+"."+shortArr[7] : shortArr[6];

						// this is done because strikes aren't in decimals in DB
						strike = strike.replace(".", "");
						int missing0s = 7-strike.length();
						for(int i=0;i<missing0s;i++) {
							strike = "0"+strike;
						}
						
						if(shortName.contains("DX.FOP.NYBOT")||shortName.contains("KC.FOP.NYBOT")) {
							strike = "0"+strike.substring(0,strike.length()-1);
						}

						List<SecDef> sdList = undQuery.get(shortName, 1, TimeUnit.SECONDS);
						
						
						String futContract = 
								new Integer((sdList.get(0).getContractYear()*100)+sdList.get(0).getContractMonth()).toString();
						
						id = new SpanContractId(convSplit[2], convSplit[0], convSplit[1], futContract, "  ", contractMonth, "  ", shortArr[5], strike);
					} else {
						
						// TODO THIS IS A HACK, MUST TAKE OUT EVENTUALLY
						if(shortName.contains("NN.FUT.NYMEX")) {
							convSplit[0] = "NG";
							intQty = intQty/4;
						}

						if(shortName.contains("HH.FUT.NYMEX")) {
							convSplit[0] = "NG";
						}

						id = new SpanContractId(convSplit[2], convSplit[0], convSplit[1], contractMonth, "  ", null, null, null, null);
					}
					
					if(retMap.containsKey(id)) {
						retMap.put(id, retMap.get(id)+intQty);
					} else {
						retMap.put(id, intQty);
					}
				} else {
					Utils.prtObErrMess(this.getClass(), "No span conversion entry for key: "+shortKey);
				}
			} else {
				Utils.prtObErrMess(this.getClass(), "Span does not support non-futures types: "+shortName);
			}
			
		}
		
		return retMap;
	}
	
}
