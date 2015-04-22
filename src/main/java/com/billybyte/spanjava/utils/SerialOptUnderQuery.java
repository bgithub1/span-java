package com.billybyte.spanjava.utils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.billybyte.commoninterfaces.QueryInterface;
import com.billybyte.commonstaticmethods.CollectionsStaticMethods;
import com.billybyte.commonstaticmethods.RegexMethods;
import com.billybyte.marketdata.SecDef;

public class SerialOptUnderQuery extends BaseUnderlyingSecDefQuery{
	private final QueryInterface<String, SecDef> query ;
	private final String regexString = 
			"\\.((USD)|(EUR)|(GBP))\\.20[1-5][0-9](([012][0-9])|(3[01]))";
	private final Pattern regexPattern = Pattern.compile(regexString);
	
	private final int[] mapper ;
	
	DecimalFormat df = new DecimalFormat("00");
	
	/**
	 * 
	 * @param sdQuery
	 * @param mapper int[] like {3,3,3,5,5,7,7,9,9,12,12,12}; for H,K,N,U,Z serials
	 */
	public SerialOptUnderQuery(
			QueryInterface<String, SecDef> sdQuery,
			int[] mapper) {
		super(sdQuery);
		this.query = sdQuery;
		this.mapper = mapper;
	}

	
	
	@Override
	public List<SecDef> get(String key, int timeoutValue,
			TimeUnit timeUnitType) {
		
		List<String> results = RegexMethods.getRegexMatches(regexPattern, key);
		if(results ==null || results.size()<1){
			return null;
		}
		
		String currencyYyyyMm = results.get(0);
		String mm =  currencyYyyyMm.substring(currencyYyyyMm.length()-2,currencyYyyyMm.length());
		if(!RegexMethods.isNumber(mm))return null;
		int month = new Integer(mm);
		if(month<1 ||month >12)return null;
		int mappedMonth = mapper[month-1];
		String mappedMonthString = df.format(mappedMonth);
		String newCurrencyYyyyMm = currencyYyyyMm.substring(0,currencyYyyyMm.length()-2)+mappedMonthString;
		if(mappedMonth <month){
			int year = new Integer(currencyYyyyMm.substring(currencyYyyyMm.length()-6,currencyYyyyMm.length()-2));
			year = year+1;
			String currencyWithDot = currencyYyyyMm.substring(0,currencyYyyyMm.length()-6);
			newCurrencyYyyyMm = currencyWithDot+new Integer(year).toString()+mappedMonthString;
		}
		
		String newKey = key.replace(currencyYyyyMm, newCurrencyYyyyMm);
		return super.get(newKey, timeoutValue, timeUnitType);
	}
	
}
