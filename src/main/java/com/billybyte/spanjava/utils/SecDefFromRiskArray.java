package com.billybyte.spanjava.utils;

import java.math.BigDecimal;

import com.billybyte.marketdata.SecDef;
import com.billybyte.marketdata.SecDefSimple;
import com.billybyte.marketdata.SecEnums.SecCurrency;
import com.billybyte.marketdata.ShortNameInfo;
import com.billybyte.marketdata.SecEnums.SecExchange;
import com.billybyte.marketdata.SecEnums.SecSymbolType;
import com.billybyte.spanjava.mongo.span.RiskArrayDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.billybyte.spanjava.recordtypes.expanded.PriceSpecs;

public class SecDefFromRiskArray  {

	public static final SecDef getSecDefFromRiskArray(RiskArrayDoc riskArrayDoc, PriceSpecs psRec){
		String symbol = riskArrayDoc.getContractId().getProdId().getProdCommCode();
		String prodTypeCode = riskArrayDoc.getContractId().getProdId().getProdTypeCode();
		SecSymbolType   symbolType = SecSymbolType.fromString(prodTypeCode);
		SecExchange exchange = 
				SecExchange.fromString(
						riskArrayDoc.getContractId().getProdId().getExchAcro());
		SecCurrency currency  = SecCurrency.fromString(psRec.getSettleCurrISO());
		int contractYear = -1;
		int contractMonth = -1;
		int contractDay = -1;
		String right = null;
		BigDecimal strike = null;
		
		if(symbolType.compareTo(SecSymbolType.FUT)==0){
			String yyyyMm = riskArrayDoc.getFutConMonth();
			contractYear = new Integer(yyyyMm.substring(0, 4));
			contractMonth = new Integer(yyyyMm.substring(4, 6));
			String dd = riskArrayDoc.getFutConDay();
			if(dd!=null && dd.compareTo("  ")>0){
				contractDay = new Integer(dd);
			}
		}else if(symbolType.compareTo(SecSymbolType.FOP)==0 || symbolType.compareTo(SecSymbolType.OOF)==0){
			String yyyyMm = riskArrayDoc.getOptConMonth();
			contractYear = new Integer(yyyyMm.substring(0, 4));
			contractMonth = new Integer(yyyyMm.substring(4, 6));
			String dd = riskArrayDoc.getOptConDay();
			if(dd!=null && dd.compareTo("  ")>0){
				contractDay = new Integer(dd);
			}
			right = riskArrayDoc.getOptRightCode();
		}
		
		
		ShortNameInfo shortNameInfo = 
				new ShortNameInfo(
						symbol, symbolType, exchange, 
						currency, contractYear, contractMonth, 
						contractDay, right, strike);
		String shortName = shortNameInfo.getShortName();
		String exchangeSymbol= shortNameInfo.getSymbol();
		int exchangePrecision =  new Integer(psRec.getSettlePriceDecLoc());
		BigDecimal minTick = null;
		int expiryYear = -1;
		int expiryMonth = -1;
		int expiryDay = -1;
		BigDecimal multiplier = null;
		SecExchange primaryExch = null;
		
		SecDef ret  = new SecDefSimple(shortName, shortNameInfo, 
				exchangeSymbol, exchangePrecision, 
				minTick, expiryYear, 
				expiryMonth, expiryDay, 
				multiplier, primaryExch);
		
		return ret;
	}
	
}
