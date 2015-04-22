package com.billybyte.spanjava.resources;
//com.billybyte.spanjava.resources.SpanUtils

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlType;

import com.billybyte.commonstaticmethods.CollectionsStaticMethods;
import com.billybyte.commonstaticmethods.Dates;
import com.billybyte.commonstaticmethods.Reflection;
import com.billybyte.commonstaticmethods.Rounding;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.SecDef;
import com.billybyte.marketdata.SecDefSimple;
import com.billybyte.marketdata.SecEnums.SecCurrency;
import com.billybyte.marketdata.SecEnums.SecExchange;
import com.billybyte.marketdata.SecEnums.SecSymbolType;
import com.billybyte.marketdata.ShortNameInfo;
import com.billybyte.marketdata.futures.FuturesProduct;
import com.billybyte.spanjava.generated.Fut;
import com.billybyte.spanjava.generated.FutPf;
import com.billybyte.spanjava.generated.OocPf;
import com.billybyte.spanjava.generated.OofPf;
import com.billybyte.spanjava.generated.Series;

public class SpanUtils {
	public static final Set<Integer> ALL_VALID_PRODUCT_MONTHS = 
			CollectionsStaticMethods.setFromArray(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12});
	/**
	 * Print all values in a series.  Series objects consist of information like 201201 (monthYear)
	 *   and expiry dates, first trading dates, etc.
	 *   Each Opt record the getOpt list has info about particular strikes.
	 * @param xmlType
	 * @param seriesList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void printOptionSeriesValues(
			XmlType xmlType,
			List<Series> seriesList){
		
		for(Series series  :seriesList){
			// print monthYear
			Utils.prt(series.getPe().toString());
			// get series propOrder
			String[] seriesPropNames = series.getClass().getAnnotation(XmlType.class).propOrder();
			// print series header
			Utils.prt(Arrays.toString(seriesPropNames));
			Reflection.printObjectFields(series, Reflection.getMethodMapNoArgGetter(series, seriesPropNames),seriesPropNames);
			// print object header
			Utils.prt(Arrays.toString(xmlType.propOrder()));
			List oList = series.getOpt();
			Reflection.printListObjects(oList, xmlType.propOrder());
		}
	}

	
	
	/**
	 * Print product info from span file	
	 * 
	 * @param prodType
	 * @param prod
	 */
	public static void printProductInfo(List<Object> prodList,String[] prodFields){
		Map<String,Method> mMap = Reflection.getMethodMapNoArgGetter(prodList.get(0),
				prodFields);
		int fieldCount = mMap.size();
		List<Method> mList = new ArrayList<Method>();
		for(String prodField:prodFields){
			mList.add(mMap.get(prodField));
		}
		TreeSet<String> lines = new TreeSet<String>();
		for(Object prod:prodList){
			List<Object> fieldValues = Reflection.getFieldsFromPojo(prod, mList);
			String line = "";
			for(int i = 0;i<fieldValues.size();i++){
				Object fieldValue = fieldValues.get(i);
				
				line+=((fieldValue==null?"null":fieldValue.toString())+(i<fieldCount-1?",":""));
			}
			
			lines.add(line);
		}
		for(String s:lines){
			Utils.prt(s);
		}
		
	}
	
	/**
	 * Convert a span futPf object into a FuturesProduct object
	 * @param futPf - span futFp object that defines a futures product
	 */
	public static FuturesProduct getFuturesProductFromSpanProduct(
			String exch,
			FutPf futPf){
		
		Set<String> symbols = 
				CollectionsStaticMethods.setFromArray(
						new String[]{futPf.getPfCode()});
		SecExchange exchange = SecExchange.valueOf(exch);
		Set<SecSymbolType> validTypes = 
				CollectionsStaticMethods.setFromArray(
						new SecSymbolType[]{SecSymbolType.FUT});
		BigDecimal notionalContractSize = 
				futPf.getNotionAmt();
		int prec = futPf.getPriceDl().intValue();
		BigDecimal notionalTickSize = 
				notionalContractSize.divide(
						BigDecimal.TEN.pow(prec),0,RoundingMode.HALF_EVEN);
		BigDecimal pointValue = futPf.getCvf();		
		BigDecimal minOrderTick = notionalTickSize.divide(notionalContractSize,RoundingMode.HALF_EVEN);
		BigDecimal ticValue = minOrderTick;
		long startTimeInMills = 0;
		long endTimeInMills = 0;
		FuturesProduct fp = 
				new FuturesProduct(
						symbols, exchange, validTypes, ALL_VALID_PRODUCT_MONTHS, 
						futPf.getName(), notionalContractSize, notionalTickSize, pointValue, 
						ticValue, startTimeInMills, endTimeInMills, minOrderTick, 
						"", futPf.getPfCode());
		return fp;
	}


	/**
	 * Convert a span futPf object into a FuturesProduct object
	 * @param oofPf - span futFp object that defines a futures product
	 */
	public static FuturesProduct getFuturesProductFromSpanProduct(
			String exch,
			OofPf oofPf){
		
		Set<String> symbols = 
				CollectionsStaticMethods.setFromArray(
						new String[]{oofPf.getPfCode()});
		SecExchange exchange = SecExchange.valueOf(exch);
		Set<SecSymbolType> validTypes = 
				CollectionsStaticMethods.setFromArray(
						new SecSymbolType[]{SecSymbolType.FUT});
		BigDecimal notionalContractSize = 
				oofPf.getNotionAmt();
		int prec = oofPf.getPriceDl().intValue();
		BigDecimal notionalTickSize = 
				notionalContractSize.divide(
						BigDecimal.TEN.pow(prec),0,RoundingMode.HALF_EVEN);
		BigDecimal pointValue = oofPf.getCvf();		
		BigDecimal minOrderTick = notionalTickSize.divide(notionalContractSize,RoundingMode.HALF_EVEN);
		BigDecimal ticValue = minOrderTick;
		long startTimeInMills = 0;
		long endTimeInMills = 0;
		FuturesProduct fp = 
				new FuturesProduct(
						symbols, exchange, validTypes, ALL_VALID_PRODUCT_MONTHS, 
						oofPf.getName(), notionalContractSize, notionalTickSize, pointValue, 
						ticValue, startTimeInMills, endTimeInMills, minOrderTick, 
						"", oofPf.getPfCode());
		return fp;
	}

	/**
	 * Convert a span futPf object into a FuturesProduct object
	 * @param oocPf - span futFp object that defines a futures product
	 */
	public static FuturesProduct getFuturesProductFromSpanProduct(
			String exch,
			OocPf oocPf){
		
		Set<String> symbols = 
				CollectionsStaticMethods.setFromArray(
						new String[]{oocPf.getPfCode()});
		SecExchange exchange = SecExchange.valueOf(exch);
		Set<SecSymbolType> validTypes = 
				CollectionsStaticMethods.setFromArray(
						new SecSymbolType[]{SecSymbolType.FUT});
		BigDecimal notionalContractSize = 
				oocPf.getNotionAmt();
		int prec = oocPf.getPriceDl().intValue();
		BigDecimal notionalTickSize = 
				notionalContractSize.divide(
						BigDecimal.TEN.pow(prec),0,RoundingMode.HALF_EVEN);
		BigDecimal pointValue = oocPf.getCvf();		
		BigDecimal minOrderTick = notionalTickSize.divide(notionalContractSize,RoundingMode.HALF_EVEN);
		BigDecimal ticValue = minOrderTick;
		SecCurrency currency = SecCurrency.valueOf(oocPf.getCurrency());
		long startTimeInMills = 0;
		long endTimeInMills = 0;
		FuturesProduct fp = 
				new FuturesProduct(
						symbols, exchange, validTypes, ALL_VALID_PRODUCT_MONTHS, 
						oocPf.getName(), notionalContractSize, notionalTickSize, pointValue, 
						ticValue, startTimeInMills, endTimeInMills, minOrderTick, 
						"", oocPf.getPfCode(),currency);
		return fp;
	}

	public static SecDef getSecDefFromFutPf(
			String exch,
			FutPf futPf,
			Fut fut){
		
		FuturesProduct fp = getFuturesProductFromSpanProduct(exch, futPf);
		String symbol = fp.getSymbols().toArray(new String[]{})[0];
		int monthYear = fut.getPe().intValue();
		int contractYear = monthYear/100; // truncate
		int contractMonth = monthYear - contractYear*100;
		ShortNameInfo info = 
				new ShortNameInfo(symbol, SecSymbolType.FUT, fp.getExchange(), 
						fp.getCurrency(), contractYear, contractMonth, null, null,null);
		String shortName = info.getShortName();
		BigDecimal minTick = fp.getMinOrderTick();
		int exchangePrecision = Rounding.leastSignificantDigit(minTick);
		String ldot = fut.getLdot().toString();
		int expiryYear = new Integer(ldot.substring(0,4));
		int expiryMonth = new Integer(ldot.substring(4,6));
		int expiryDay = new Integer(ldot.substring(6,8));
		BigDecimal multiplier = fp.getNotionalContractSize();
		SecExchange primaryExch = fp.getExchange();
		SecDef sd = 
				new SecDefSimple(shortName, info, symbol, 
						exchangePrecision, minTick, expiryYear,
						expiryMonth, expiryDay, multiplier, primaryExch);
		return sd;
	}

	public static String makeIceShort(String symbol, String contract, String exchange) {
		return symbol+".FUT."+exchange+".USD."+contract.substring(0,6);
	}

	public static String makeIceShort(String symbol, String contract, String optRight, String strike, String exchange) {
		return symbol+".FOP."+exchange+".USD."+contract.substring(0,6)+"."+optRight+"."+strike;
	}

	public static long getDaysToExpiryFromSecDef(SecDef sd, Calendar today){
		int expYear = sd.getExpiryYear();
		int expMonth = sd.getExpiryMonth();
		int expDay = sd.getExpiryDay();
		Calendar expiry = Calendar.getInstance();
		expiry.set(expYear, expMonth-1,expDay);
		return Dates.getDifference(today,expiry, TimeUnit.DAYS)+1;
		
	}

}
