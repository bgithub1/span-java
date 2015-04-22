package com.billybyte.spanjava.mains;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.billybyte.commonstaticmethods.CollectionsStaticMethods;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.spanjava.parsers.ice.ParseIceSpanFileToMongoSettle;
import com.billybyte.spanjava.resources.SpanUtils;

public class TestIceParser {
	public static void main(String[] args) {
		String spanFileName = "./unzipFolder/ice.20130730.pa5";
		ParseIceSpanFileToMongoSettle spanParser=null;
		try {
			spanParser = new ParseIceSpanFileToMongoSettle(new FileReader(spanFileName), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		@SuppressWarnings("unchecked")
		Map<String,String> prodConvMap = Utils.getXmlData(Map.class, SpanUtils.class, "iceProdConvMap.xml");
		try {
			TreeMap<String, Map<String,BigDecimal>> ret = 
					new TreeMap<String, Map<String,BigDecimal>>(spanParser.createSpanSettleDocMap(true, prodConvMap));
			Set<String> prodSet = ret.keySet();
			CollectionsStaticMethods.prtSetItems(prodSet);
			Entry<String,Map<String, BigDecimal>> ccMap = ret.ceilingEntry("CC");
			Utils.prt(ccMap.getKey());
			CollectionsStaticMethods.prtMapItems(ccMap.getValue());
//			for(Entry<String, Map<String,BigDecimal>> entry:ret.entrySet()){
//				String prod = entry.getKey();
//				Utils.prt(prod);
//				Map<String,BigDecimal> prodMap = entry.getValue();
//				CollectionsStaticMethods.prtMapItems(prodMap);
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
