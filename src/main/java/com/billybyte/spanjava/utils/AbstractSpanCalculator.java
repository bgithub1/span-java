package com.billybyte.spanjava.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.billybyte.commoncollections.Tuple;
//import com.billybyte.collectionextentions.Tuple;
import com.billybyte.spanjava.mongo.span.DeliveryChargeDoc;
import com.billybyte.spanjava.mongo.span.InterCommSpreadInfo;
import com.billybyte.spanjava.mongo.span.InterCommTierInfo;
import com.billybyte.spanjava.mongo.span.InterMonthSpreadSummaryDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanContractId;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;

public abstract class AbstractSpanCalculator {
	
	public BigDecimal calculateSpanMargin(Map<SpanContractId,Integer> posQtyMap) {
				
			// Get the 16-element span risk array for each product code
			Tuple<Map<SpanContractId,BigDecimal>,Map<SpanContractId,BigDecimal[]>> conInfoTuple = 
					getContractInfoTuple(posQtyMap.keySet());

			Map<SpanContractId,BigDecimal[]> spanArrayMap = conInfoTuple.getT2_instance();
			Map<SpanContractId,BigDecimal> deltaPerSpanProdMap = conInfoTuple.getT1_instance();
			
			// Map the unique contracts to their respective product and combined commodity codes
			Map<SpanProdId,String> prodCombCommCodeMap = getProdByCombCommCodeMap(posQtyMap.keySet());

			Map<String,BigDecimal> deltaByCombCommMap = getDeltaByCombCommMap(posQtyMap, deltaPerSpanProdMap, prodCombCommCodeMap);

			Map<String,Set<SpanProdId>> combCommSetMap = getFlatCombCommMap(prodCombCommCodeMap);
			
			// Sum each scenario at the combined commodity level and select the worst case scenario
			Map<String,SpanRiskResult> spanRiskMap = 
					calculateSpanRiskMap(posQtyMap, spanArrayMap, prodCombCommCodeMap);

			// determine inter-month spread debits
			Map<String,InterMonthSpreadSummaryDoc> interMonthSpreadInfoMap = getInterMonthSpreadInfo(combCommSetMap.keySet());

			Map<String,BigDecimal> interMonthSpreadChargeMap = 
					getInterMonthSpreadChargeMap(posQtyMap, deltaPerSpanProdMap, interMonthSpreadInfoMap, combCommSetMap);
			
			// calculate inter-comm spread credits
			List<InterCommSpreadInfo> interCommSpreadInfoList = getInterCommSpreadInfoList(combCommSetMap.keySet());
			
			Map<String,InterCommTierInfo> interCommTierMap = getInterCommTierMap(combCommSetMap.keySet());
			Map<String,DeliveryChargeDoc> delivChargeMap = getDeliveryChargeMap(combCommSetMap.keySet());
			
			Map<String,List<BigDecimal[]>> interCommSpreadCreditMap = 
					getInterCommSpreadCreditMap(
							posQtyMap, deltaPerSpanProdMap, prodCombCommCodeMap, interCommSpreadInfoList, interCommTierMap);
			
			// calculate short option minimum margin
			Tuple<Map<SpanContractId,BigDecimal>,Map<String,Integer>> shortOptMinMarginTuple = 
					getShortOptionMinMarginMap(posQtyMap.keySet(), prodCombCommCodeMap, interCommTierMap, delivChargeMap);
			
			BigDecimal shortOptionMargin = getShortOptionMargin(posQtyMap, prodCombCommCodeMap, shortOptMinMarginTuple);
			
			BigDecimal spanningRisk = getPortfolioSpan(
					spanRiskMap, interMonthSpreadChargeMap, interCommSpreadCreditMap, deltaByCombCommMap);
			
			BigDecimal maintMargin = getMaintMargin(spanningRisk, shortOptionMargin);
			
			return maintMargin;
	}
	
	protected abstract Tuple<Map<SpanContractId,BigDecimal>,Map<SpanContractId,BigDecimal[]>> getContractInfoTuple(
			Set<SpanContractId> uniqueIdSet);
	
	protected abstract Map<SpanProdId,String> getProdByCombCommCodeMap(Set<SpanContractId> uniqueIdSet);	

	protected abstract Map<String,SpanRiskResult> calculateSpanRiskMap(
			Map<SpanContractId,Integer> spanIdQtyMap, 
			Map<SpanContractId,BigDecimal[]> spanArrayMap,
			Map<SpanProdId,String> prodCombCommCodeMap);
		
	protected abstract Map<String,InterMonthSpreadSummaryDoc> getInterMonthSpreadInfo(Set<String> combCommCodeSet);

	protected abstract Map<String,BigDecimal> getInterMonthSpreadChargeMap(
			Map<SpanContractId,Integer> spanIdQtyMap,
			Map<SpanContractId,BigDecimal> deltaBySpanProdMap,
			Map<String,InterMonthSpreadSummaryDoc> interMonthSpreadInfoMap,
			Map<String,Set<SpanProdId>> combCommMap);
	
	protected abstract List<InterCommSpreadInfo> getInterCommSpreadInfoList(Set<String> combCommCodeSet);	
	
	protected abstract Map<String,InterCommTierInfo> getInterCommTierMap(Set<String> combCommCodeSet);
	
	protected abstract Map<String,DeliveryChargeDoc> getDeliveryChargeMap(Set<String> combCommCodeSet);
	
	protected abstract Map<String,List<BigDecimal[]>> getInterCommSpreadCreditMap(
			Map<SpanContractId,Integer> spanIdQtyMap,
			Map<SpanContractId,BigDecimal> deltaPerSpanProdMap,
			Map<SpanProdId,String> prodCombCommCodeList,
			List<InterCommSpreadInfo> interCommSpreadInfoMap,
			Map<String,InterCommTierInfo> interCommTierMap);
	
	protected abstract Tuple<Map<SpanContractId,BigDecimal>,Map<String,Integer>> getShortOptionMinMarginMap(
			Set<SpanContractId> contractIdSet,
			Map<SpanProdId,String> prodCombCommCodeMap,
			Map<String,InterCommTierInfo> interCommTierMap, 
			Map<String,DeliveryChargeDoc> delivChargeMap);	

	protected abstract BigDecimal getShortOptionMargin(
			Map<SpanContractId,Integer> posQtyMap,
			Map<SpanProdId,String> prodCombCommCodeMap,
			Tuple<Map<SpanContractId,BigDecimal>,Map<String,Integer>> shortOptionMarginTuple);
	
	protected abstract BigDecimal getPortfolioSpan(
			Map<String,SpanRiskResult> spanRiskMap,
			Map<String,BigDecimal> interMonthSpreadChargeMap,
			Map<String,List<BigDecimal[]>> interCommSpreadCreditMap,
			Map<String,BigDecimal> deltaByCombCommMap);
	
	private BigDecimal getMaintMargin(BigDecimal spanningRisk, BigDecimal shortOptMin) {
		if(spanningRisk.compareTo(shortOptMin)==1) {
			return spanningRisk;
		} else {
			return shortOptMin;
		}
	}
	
	private Map<String,Set<SpanProdId>> getFlatCombCommMap(Map<SpanProdId,String> combCommMap) {
		Map<String,Set<SpanProdId>> retMap = new HashMap<String,Set<SpanProdId>>();
		for(Entry<SpanProdId,String> entry:combCommMap.entrySet()) {
			SpanProdId id = entry.getKey();
			String combCommCode = entry.getValue();
			if(retMap.containsKey(combCommCode)){
				retMap.get(combCommCode).add(id);
			} else {
				Set<SpanProdId> newSet = new HashSet<SpanProdId>();
				newSet.add(id);
				retMap.put(combCommCode, newSet);
			}
		}
		return retMap;
	}
	
	private Map<String,BigDecimal> getDeltaByCombCommMap(
			Map<SpanContractId,Integer> posQtyMap,
			Map<SpanContractId,BigDecimal> deltaPerSpanProdMap,
			Map<SpanProdId,String> prodCombCommCodeMap) {
		Map<String,BigDecimal> retMap = new HashMap<String,BigDecimal>();
		for(Entry<SpanContractId,Integer> entry:posQtyMap.entrySet()) {
			SpanContractId conId = entry.getKey();
			String combCommCode = prodCombCommCodeMap.get(conId.getProdId());
			BigDecimal netDelta = deltaPerSpanProdMap.get(conId).multiply(new BigDecimal(entry.getValue()));
			if(retMap.containsKey(combCommCode)) {
				retMap.put(combCommCode, retMap.get(combCommCode).add(netDelta));
			} else {
				retMap.put(combCommCode, netDelta);
			}
		}
		return retMap;
	}
	
	//Map<String,BigDecimal> deltaByCombCommMap = getDeltaByCombComm(posQtyMap, deltaPerSpanProdMap, prodCombCommCodeMap);
}
