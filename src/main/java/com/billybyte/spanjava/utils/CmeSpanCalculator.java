package com.billybyte.spanjava.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.billybyte.commoncollections.Tuple;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.spanjava.mongo.span.DeliveryChargeDoc;
import com.billybyte.spanjava.mongo.span.FlatCombCommProdIdDoc;
import com.billybyte.spanjava.mongo.span.FlatInterCommSpreadDoc;
import com.billybyte.spanjava.mongo.span.InterCommSpreadInfo;
import com.billybyte.spanjava.mongo.span.InterCommTierInfo;
import com.billybyte.spanjava.mongo.span.InterMonthSpreadSummaryDoc;
import com.billybyte.spanjava.mongo.span.RiskArrayDoc;
import com.billybyte.spanjava.mongo.span.subtypes.InterCommSpreadLeg;
import com.billybyte.spanjava.mongo.span.subtypes.InterCommTierDetail;
import com.billybyte.spanjava.mongo.span.subtypes.InterMonthSpreadDetail;
import com.billybyte.spanjava.mongo.span.subtypes.InterMonthSpreadLeg;
import com.billybyte.spanjava.mongo.span.subtypes.SpanContractId;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.billybyte.spanjava.mongo.span.subtypes.TierRange;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class CmeSpanCalculator extends AbstractSpanCalculator {

	private final DBCollection riskArrayColl;
	private final DBCollection flatCombCommColl;
	private final DBCollection interMonthSpreadColl;
	private final DBCollection interCommSpreadColl;
	private final DBCollection flatInterCommSpreadColl;
	private final DBCollection interCommTierColl;
	private final DBCollection delivChargeColl;
	
	private final Map<SpanProdId,String> combCommCacheMap = new HashMap<SpanProdId,String>();
	private final Gson gson = new Gson();
	
	private final static BigDecimal HALF = new BigDecimal("0.5");
	
	public CmeSpanCalculator(
			DBCollection riskArrayColl, 
			DBCollection flatCombCommColl, 
			DBCollection interMonthSpreadColl,
			DBCollection interCommSpreadColl,
			DBCollection flatInterCommSpreadColl,
			DBCollection interCommTierColl,
			DBCollection delivChargeColl) {
		this.riskArrayColl = riskArrayColl;
		this.flatCombCommColl = flatCombCommColl;
		this.interMonthSpreadColl = interMonthSpreadColl;
		this.interCommSpreadColl = interCommSpreadColl;
		this.flatInterCommSpreadColl = flatInterCommSpreadColl;
		this.interCommTierColl = interCommTierColl;
		this.delivChargeColl = delivChargeColl;
	}
	
	@Override
	protected Tuple<Map<SpanContractId, BigDecimal>, Map<SpanContractId, BigDecimal[]>> getContractInfoTuple(
			Set<SpanContractId> uniqueIdSet) {
		Map<SpanContractId,BigDecimal[]> arrayMap = new HashMap<SpanContractId,BigDecimal[]>();
		Map<SpanContractId,BigDecimal> deltaMap = new HashMap<SpanContractId,BigDecimal>();
		for(SpanContractId id:uniqueIdSet) { 
			DBObject queryObj = new BasicDBObject();
			queryObj.put("contractId", id.getDBObject());
			DBObject riskArrayDoc = riskArrayColl.findOne(queryObj);
			if(riskArrayDoc==null) {
				Utils.prtObErrMess(this.getClass(), "Null risk array doc for "+queryObj.toString());
				continue;
			}
			RiskArrayDoc raDoc = gson.fromJson(riskArrayDoc.toString(), RiskArrayDoc.class);
			String[] strSpanArray = raDoc.getSpanArray();
			BigDecimal[] spanArr = new BigDecimal[16];
			for(int i=0;i<spanArr.length;i++) {
				spanArr[i] = new BigDecimal(strSpanArray[i]);
			}
			arrayMap.put(id, spanArr);
			deltaMap.put(id, new BigDecimal(raDoc.getDelta()));
		}
		return new Tuple<Map<SpanContractId, BigDecimal>, Map<SpanContractId, BigDecimal[]>>(deltaMap, arrayMap);
	}

	@Override
	protected Map<SpanProdId, String> getProdByCombCommCodeMap(Set<SpanContractId> uniqueIdSet) {
		Map<SpanProdId, String> retMap = new HashMap<SpanProdId,String>();
		for(SpanContractId conId:uniqueIdSet) {
			SpanProdId prodId = conId.getProdId();
			String combCommCode;
			if(combCommCacheMap.containsKey(prodId)) {
				combCommCode = combCommCacheMap.get(prodId);
			} else {
				DBObject queryObj = new BasicDBObject();
				queryObj.put("prodId", prodId.getDBObject());
				DBObject fccObj = flatCombCommColl.findOne(queryObj);
				if(fccObj==null) {
					Utils.prtObErrMess(this.getClass(), "Null flat comb comm doc returned for "+queryObj.toString());
					continue;
				}
				FlatCombCommProdIdDoc fccDoc = gson.fromJson(fccObj.toString(), FlatCombCommProdIdDoc.class);
				combCommCode = fccDoc.getCombCommCode();
				combCommCacheMap.put(prodId, combCommCode);
				retMap.put(prodId, combCommCode);
			}
			retMap.put(prodId, combCommCode);
		}
		return retMap;
	}

	@Override
	protected Map<String, SpanRiskResult> calculateSpanRiskMap(
			Map<SpanContractId, Integer> spanIdQtyMap,
			Map<SpanContractId, BigDecimal[]> spanArrayMap,
			Map<SpanProdId, String> prodByCombCommCodeMap) {
		// multiply the unit span by the corresponding contract qty
		Map<SpanContractId,BigDecimal[]> qtyAdjSpanMap = new HashMap<SpanContractId,BigDecimal[]>();
		for(Entry<SpanContractId,Integer> entry:spanIdQtyMap.entrySet()) {
			SpanContractId conId = entry.getKey();
			Integer qty = entry.getValue();
			BigDecimal[] qtyAdjSpan = new BigDecimal[16];
			BigDecimal[] unitSpan = spanArrayMap.get(conId);
			for(int i=0;i<unitSpan.length;i++) {
				qtyAdjSpan[i] = unitSpan[i].multiply(new BigDecimal(qty));
			}
			qtyAdjSpanMap.put(conId, qtyAdjSpan);
		}
		
		// get the span scenario sum by comb comm code
		Map<String,BigDecimal[]> scenarioSumMap = new HashMap<String,BigDecimal[]>();
		for(Entry<SpanContractId,BigDecimal[]> entry:qtyAdjSpanMap.entrySet()) {
			BigDecimal[] qtyAdjSpanArr = entry.getValue();
			String combComm = prodByCombCommCodeMap.get(entry.getKey().getProdId());
			if(scenarioSumMap.containsKey(combComm)) {
				BigDecimal[] prevArr = scenarioSumMap.get(combComm);
				for(int i=0;i<prevArr.length;i++) {
					prevArr[i] = prevArr[i].add(qtyAdjSpanArr[i]);
				}
			} else {
				scenarioSumMap.put(combComm, qtyAdjSpanArr);
			}
		}
		
		// determine worst scenario for each comb comm group
		Map<String,SpanRiskResult> retMap = new HashMap<String,SpanRiskResult>();
		for(Entry<String,BigDecimal[]> entry:scenarioSumMap.entrySet()) {
			String combCommCode = entry.getKey();
			BigDecimal[] spanArr = entry.getValue();
			Integer worstScenario = 0;
			BigDecimal worstLoss = spanArr[0];
			for(int i=1;i<spanArr.length;i++) {
				if(spanArr[i].compareTo(worstLoss)==1) {
					worstScenario = i+1;
					worstLoss = spanArr[i];
				}
			}
			if(worstScenario==0) continue; // TODO empty array, is this correct?
			BigDecimal timeRisk = spanArr[0].add(spanArr[1]).multiply(HALF);
			Integer pairedScenario = (worstScenario > 14) ? worstScenario : 
				((worstScenario % 2 == 0) ? worstScenario-1 : worstScenario+1);
			BigDecimal futPriceRisk = worstLoss.add(spanArr[pairedScenario-1]).multiply(HALF).subtract(timeRisk);
			SpanRiskResult spanRiskResult = new SpanRiskResult(worstScenario, worstLoss, timeRisk, futPriceRisk);
			
			retMap.put(combCommCode, spanRiskResult);
		}
		return retMap;
	}

	@Override
	protected Map<String, InterMonthSpreadSummaryDoc> getInterMonthSpreadInfo(Set<String> combCommCodeSet) {
		Map<String,InterMonthSpreadSummaryDoc> retMap = new HashMap<String,InterMonthSpreadSummaryDoc>();
		for(String combComm:combCommCodeSet) {
			DBObject queryObj = new BasicDBObject();
			queryObj.put("combCommCode", combComm);
			DBObject dbRet = interMonthSpreadColl.findOne(queryObj);
			InterMonthSpreadSummaryDoc doc = gson.fromJson(dbRet.toString(), InterMonthSpreadSummaryDoc.class);
			retMap.put(combComm, doc);
		}
		return retMap;
	}

	@Override
	protected Map<String, BigDecimal> getInterMonthSpreadChargeMap(
			Map<SpanContractId, Integer> spanIdQtyMap,
			Map<SpanContractId, BigDecimal> deltaBySpanProdMap,
			Map<String, InterMonthSpreadSummaryDoc> interMonthSpreadInfoMap,
			Map<String,Set<SpanProdId>> combCommMap) {
		Map<String,BigDecimal> retMap = new HashMap<String,BigDecimal>();
		for(Entry<String,InterMonthSpreadSummaryDoc> combCommGrp:interMonthSpreadInfoMap.entrySet()) {
			String combCommCode = combCommGrp.getKey();
			InterMonthSpreadSummaryDoc spreadInfoDoc = combCommGrp.getValue();
			Set<SpanProdId> prodIdSet = combCommMap.get(combCommCode);
			if(spreadInfoDoc.getIntraCommSpreadMethodCode().compareTo("01")==0){
				// no inter-month spread charge
				retMap.put(combCommCode, BigDecimal.ZERO);
				continue;
			} else {

				Map<Integer,Integer[]> deltaTierMap = getTotalDeltaPerTierMap(
						spanIdQtyMap, deltaBySpanProdMap, spreadInfoDoc, prodIdSet);
				
				// TODO for debugging
				for(Entry<Integer,Integer[]> display:deltaTierMap.entrySet()) {
					Utils.prt(display.getKey()+","+display.getValue()[0]+","+display.getValue()[1]);
				}
				
				List<InterMonthSpreadDetail> detailList = spreadInfoDoc.getInterMonthSpreadDetailList();
				
				TreeMap<Integer,InterMonthSpreadDetail> spreadDetailMap = getPrioritySpreadMap(detailList);
				
				Map<Integer,Tuple<Integer,BigDecimal>> spreadChargeMap = getSpreadsPerPriorityMap(spreadDetailMap, deltaTierMap);
				
				BigDecimal spreadCharge = getTotalSpreadCharge(spreadChargeMap);
				
				retMap.put(combCommCode, spreadCharge);
				
			}
			
		}
		return retMap;
	}

	private Integer getTierNumber(SpanContractId id, List<TierRange> tierList) {
		Integer conMonth = Integer.parseInt(id.getFutMonth());
		for(TierRange range:tierList) {
			Integer startMonth = Integer.parseInt(range.getStartConMonth());
			Integer endMonth = Integer.parseInt(range.getEndConMonth());
			if(conMonth>=startMonth&&conMonth<=endMonth) return Integer.parseInt(range.getTierNumber());
		}
		Utils.prtObErrMess(this.getClass(), "Unable to find determine inter-month spread tier for contract "+id.toString());
		return null;
	}
	
	private Map<Integer,Integer[]> getTotalDeltaPerTierMap(
			Map<SpanContractId, Integer> spanIdQtyMap,
			Map<SpanContractId, BigDecimal> deltaBySpanProdMap, 
			InterMonthSpreadSummaryDoc spreadInfoDoc, 
			Set<SpanProdId> prodIdSet) {
		Map<Integer,BigDecimal[]> deltaMap = new HashMap<Integer,BigDecimal[]>();
		List<TierRange> tierList = spreadInfoDoc.getTierRangeList();
		for(Entry<SpanContractId,Integer> position:spanIdQtyMap.entrySet()) {
			SpanContractId conId = position.getKey();
			if(prodIdSet.contains(conId.getProdId())) {
				BigDecimal totalDelta = new BigDecimal(position.getValue()).multiply(deltaBySpanProdMap.get(conId));
				// TODO handle daily contracts
				Integer tierNumber = getTierNumber(conId, tierList);
				if(deltaMap.containsKey(tierNumber)) {
					BigDecimal[] deltas = deltaMap.get(tierNumber);
					if(totalDelta.compareTo(BigDecimal.ZERO)==1) {
						deltas[0] = deltas[0].add(totalDelta);
					} else {
						deltas[1] = deltas[1].add(totalDelta);
					}
				} else {
					BigDecimal[] deltas = new BigDecimal[2];
					if(totalDelta.compareTo(BigDecimal.ZERO)==1) {
						deltas[0] = totalDelta;
						deltas[1] = BigDecimal.ZERO;
					} else {
						deltas[0] = BigDecimal.ZERO;
						deltas[1] = totalDelta;
					}
					deltaMap.put(tierNumber, deltas);
				}
			}
		}
		// round all the net deltas
		Map<Integer,Integer[]> retMap = new HashMap<Integer,Integer[]>();
		for(Entry<Integer,BigDecimal[]> entry:deltaMap.entrySet()) {
			BigDecimal[] deltaArr = entry.getValue();
			Integer[] intArr = new Integer[2];
			intArr[0] = deltaArr[0].setScale(0, RoundingMode.HALF_DOWN).intValueExact();
			intArr[1] = Math.abs(deltaArr[1].setScale(0, RoundingMode.HALF_DOWN).intValueExact());
			retMap.put(entry.getKey(), intArr);
		}
		return retMap;
	}
	
	private TreeMap<Integer,InterMonthSpreadDetail> getPrioritySpreadMap(List<InterMonthSpreadDetail> detailList) {
		TreeMap<Integer,InterMonthSpreadDetail> retMap = new TreeMap<Integer,InterMonthSpreadDetail>();
		for(InterMonthSpreadDetail detail:detailList) {
			Integer priority = detail.getSpreadPriority();
			if(retMap.containsKey(priority)) Utils.prtObErrMess(this.getClass(), "Already has spread with same priority!"); // TODO this line is for debugging
			retMap.put(priority, detail);
		}
		return retMap;
	}
	
	private Map<Integer,Tuple<Integer,BigDecimal>> getSpreadsPerPriorityMap(
			TreeMap<Integer,InterMonthSpreadDetail> spreadDetailMap,
			Map<Integer,Integer[]> deltaTierMap) {
		Map<Integer,Tuple<Integer,BigDecimal>> retMap = new HashMap<Integer,Tuple<Integer,BigDecimal>>();
		Map<Integer,Integer[]> deltaTierCopy = new HashMap<Integer,Integer[]>();
		deltaTierCopy.putAll(deltaTierMap);
		while(!spreadDetailMap.isEmpty()) {
			Entry<Integer,InterMonthSpreadDetail> entry = spreadDetailMap.pollFirstEntry(); // TODO should poll first or last?
			Integer sprPri = entry.getKey();
			InterMonthSpreadDetail det = entry.getValue();
			BigDecimal spreadCharge = new BigDecimal(det.getChargeRate());
			
			List<InterMonthSpreadLeg> spreadLegList = det.getSpreadLegList();
			Tuple<Integer,Integer> contSpr = containsSpread(deltaTierCopy, spreadLegList);

			// if you can build a spread, subtract the used legs from the deltaTierCopy map
			Integer numOfSpreads = 0;
			if(contSpr.getT1_instance()==1) {
				numOfSpreads = contSpr.getT2_instance();
			} else if(contSpr.getT1_instance()==-1) {
				numOfSpreads = -1*contSpr.getT2_instance();
			}
			if(numOfSpreads!=0) {
				retMap.put(sprPri, new Tuple<Integer,BigDecimal>(numOfSpreads,spreadCharge));
				deltaTierCopy = subtractSpreadsFromDeltaMap(deltaTierCopy, numOfSpreads, spreadLegList);
			}
		}

		return retMap;
	}

	/**
	 * Determines the number of spreads (if any) the delta tier map contains
	 * @param deltaTierMap
	 * @param legList
	 * @return
	 */
	private Tuple<Integer,Integer> containsSpread(Map<Integer,Integer[]> deltaTierMap, List<InterMonthSpreadLeg> legList) {
		// TODO can a position ever contain both long and short a spread?
		Integer numOfSpreads = Integer.MAX_VALUE;
		// check if position contains long the spread
		for(InterMonthSpreadLeg leg:legList) {
			Integer tierNum = leg.getTierNumber();
			if(!deltaTierMap.containsKey(tierNum)) {
				return new Tuple<Integer,Integer>(0,0);
			}
			Integer reqDeltas = leg.getDeltaPerSpreadRatio();
			Integer mrktSide = (leg.getMarketSide().compareTo("A")==0) ? 0 : 1;
			Integer[] tierDeltas = deltaTierMap.get(tierNum);
			Integer maxTierSpreads = tierDeltas[mrktSide] / reqDeltas;
			if(maxTierSpreads == 0) {
				numOfSpreads = 0;
			} else {
				if(maxTierSpreads < numOfSpreads) numOfSpreads = maxTierSpreads;
			}
		}
		if(numOfSpreads>0) return new Tuple<Integer,Integer>(1,numOfSpreads);

		// check if position contains short the spread
		numOfSpreads = Integer.MAX_VALUE;
		for(InterMonthSpreadLeg leg:legList) {
			Integer tierNum = leg.getTierNumber();
			Integer reqDeltas = leg.getDeltaPerSpreadRatio();
			Integer mrktSide = (leg.getMarketSide().compareTo("A")==0) ? 1 : 0;
			Integer[] tierDeltas = deltaTierMap.get(tierNum);
			Integer maxTierSpreads = tierDeltas[mrktSide] / reqDeltas;
			if(maxTierSpreads < 1) {
				return new Tuple<Integer,Integer>(0,0);
			} else {
				if(maxTierSpreads < numOfSpreads) numOfSpreads = maxTierSpreads;
			}
		}		
		return new Tuple<Integer,Integer>(-1,numOfSpreads);
	}

	private Map<Integer,Integer[]> subtractSpreadsFromDeltaMap(
			Map<Integer,Integer[]> deltaMap, int numOfSpreads, List<InterMonthSpreadLeg> legList) {
		Map<Integer,Integer[]> retMap = new HashMap<Integer,Integer[]>();
		retMap.putAll(deltaMap);
		for(InterMonthSpreadLeg leg:legList) {
			Integer tierNum = leg.getTierNumber();
			Integer mrktSide = (leg.getMarketSide().compareTo("A")==0) ? 0 : 1;
			if(numOfSpreads < 0) mrktSide = (mrktSide==0) ? 1 : 0;
			retMap.get(tierNum)[mrktSide] = retMap.get(tierNum)[mrktSide] - Math.abs(numOfSpreads);
		}
		return retMap;
	}
	
	private BigDecimal getTotalSpreadCharge(Map<Integer,Tuple<Integer,BigDecimal>> spreadChargeMap) {
		BigDecimal ret = BigDecimal.ZERO;
		for(Entry<Integer,Tuple<Integer,BigDecimal>> entry:spreadChargeMap.entrySet()) {
			BigDecimal numOfSpreads = new BigDecimal(entry.getValue().getT1_instance());
			BigDecimal spreadCharge = entry.getValue().getT2_instance();
			ret = ret.add(spreadCharge.multiply(numOfSpreads).abs());
		}
		return ret;
	}
	
	@Override
	protected List<InterCommSpreadInfo> getInterCommSpreadInfoList(Set<String> combCommCodeSet) {
		if(combCommCodeSet.size()<2) return new ArrayList<InterCommSpreadInfo>();
		// get the spread priorities for all combCommCodes
		Map<String,List<Integer>> priorityMap = new HashMap<String,List<Integer>>();
		for(String combCommCode:combCommCodeSet) {
			DBObject queryObj = new BasicDBObject();
			queryObj.put("combCommCode", combCommCode);
			FlatInterCommSpreadDoc doc = 
					gson.fromJson(flatInterCommSpreadColl.findOne(queryObj).toString(), FlatInterCommSpreadDoc.class);
			priorityMap.put(combCommCode, doc.getSpreadPriorityList());			
		}
		// determine if any combComms have applicable inter-comm spreads between them
		Set<Integer> sharedPriorities = new HashSet<Integer>();
		for(String combComm:priorityMap.keySet()) {
			List<Integer> currList = priorityMap.get(combComm);
			for(Entry<String,List<Integer>> entry:priorityMap.entrySet()) {
				if(entry.getKey().compareTo(combComm)!=0){
					List<Integer> compareList = entry.getValue();
					for(Integer pri:currList) {
						if(compareList.contains(pri)) sharedPriorities.add(pri);
					}
				}
			}
		}
		// fetch the pertinent inter-comm spread specifications
		List<InterCommSpreadInfo> retList = new ArrayList<InterCommSpreadInfo>();
		for(Integer spreadPriority:sharedPriorities) {
			DBObject queryObj = new BasicDBObject();
			queryObj.put("spreadPriority", spreadPriority);
			InterCommSpreadInfo spreadInfo = gson.fromJson(interCommSpreadColl.findOne(queryObj).toString(), InterCommSpreadInfo.class);
			retList.add(spreadInfo);
		}
		return retList;
	}

	@Override
	protected Map<String, InterCommTierInfo> getInterCommTierMap(Set<String> combCommCodeSet) {
		Map<String,InterCommTierInfo> retMap = new HashMap<String,InterCommTierInfo>();
		for(String combCommCode:combCommCodeSet) {
			DBObject queryObj = new BasicDBObject();
			queryObj.put("combCommCode", combCommCode);
			DBObject retDoc = interCommTierColl.findOne(queryObj);
			if(retDoc!=null) { // TODO is this check necessary?
				InterCommTierInfo tierInfo = gson.fromJson(retDoc.toString(), InterCommTierInfo.class);
				retMap.put(combCommCode, tierInfo);
			}
		}
		return retMap;
	}

	@Override
	protected Map<String,List<BigDecimal[]>> getInterCommSpreadCreditMap(
			Map<SpanContractId, Integer> spanIdQtyMap,
			Map<SpanContractId, BigDecimal> deltaPerSpanProdMap,
			Map<SpanProdId, String> combCommCodeMap,
			List<InterCommSpreadInfo> interCommSpreadInfoList,
			Map<String,InterCommTierInfo> interCommTierMap) {
		
		// create delta maps that can be used to count inter-comm spreads
		Tuple<Map<String,BigDecimal>,Map<String,Map<Integer,BigDecimal>>> interCommDeltaTuple =
				getInterCommSpreadDeltaMaps(spanIdQtyMap, combCommCodeMap, deltaPerSpanProdMap, interCommTierMap);
		
		Map<String,BigDecimal> deltaByCombCommMap = interCommDeltaTuple.getT1_instance();
		Map<String,Map<Integer,BigDecimal>> deltaByCombCommContractMap = interCommDeltaTuple.getT2_instance();

		TreeMap<Integer,InterCommSpreadInfo> interCommSpreadByPriorityMap = 
				getInterCommSpreadPrioritySortedMap(interCommSpreadInfoList);
		
		// walk through inter comm priority map and see how many spreads can be made
		Map<String,List<BigDecimal[]>> retMap = new HashMap<String,List<BigDecimal[]>>();
		while(!interCommSpreadByPriorityMap.isEmpty()) {
			Entry<Integer,InterCommSpreadInfo> entry = interCommSpreadByPriorityMap.pollFirstEntry(); // TODO should this be first or last?
			InterCommSpreadInfo spreadInfo = entry.getValue();
			BigDecimal spreadCredit = spreadInfo.getSpreadCreditRate();
//			Integer spreadPriority = spreadInfo.getSpreadPriority();
			String spreadMethodCode = spreadInfo.getSpreadMethodCode();
			List<InterCommSpreadLeg> spreadLegList = spreadInfo.getInterCommSpreadLegList();
			if(spreadMethodCode.compareTo("01")==0) {
				// single-tier based method
				Map<String,Integer> spreadsFormedMap = getSpreadsFormedMap(spreadLegList, deltaByCombCommMap);

				if(spreadsFormedMap.size()==spreadLegList.size()) {
					int spreadCount = getMaxSpreadCount(spreadsFormedMap);

					Tuple<Map<String,BigDecimal>,Map<String,BigDecimal>> spreadCreditDeltaReductionTuple = 
							getSpreadCreditAndDeltaReductionTuple(spreadLegList,spreadCount,spreadCredit);
					
					Map<String,BigDecimal> spreadCreditMap = spreadCreditDeltaReductionTuple.getT1_instance();
					Map<String,BigDecimal> deltaReductionMap = spreadCreditDeltaReductionTuple.getT2_instance();
					
					// fill ret map with pairs
					updateInterCommCreditMap(spreadCreditMap, deltaReductionMap, retMap);
					
					sumMapEntries(deltaByCombCommMap,deltaReductionMap);
					// TODO how can I remove deltas from tier map as well, is it necessary or do 20's always come first??
					
				}				
			} else if (spreadMethodCode.compareTo("20")==0){
				// tier-based method
				Map<String,Map<Integer,Integer>> spreadsFormedMap = getTierSpreadsFormedMap(spreadLegList, deltaByCombCommContractMap);
				
				int spreadsFormedLegsCount = 0; // check to see you can form spread from all legs
				for(Entry<String,Map<Integer,Integer>> combCommEntry:spreadsFormedMap.entrySet()) {
					spreadsFormedLegsCount = spreadsFormedLegsCount + combCommEntry.getValue().size();
				}
				
				if(spreadsFormedLegsCount==spreadLegList.size()) {

					int spreadCount = getMaxTierSpreadCount(spreadsFormedMap);
					
					Tuple<Map<String,BigDecimal>,Map<String,Map<Integer,BigDecimal>>> spreadCreditDeltaReductionTuple = 
							getSpreadCreditAndTierDeltaReductionTuple(spreadLegList,spreadCount,spreadCredit);
					
					Map<String,BigDecimal> spreadCreditMap = spreadCreditDeltaReductionTuple.getT1_instance();
					
					Map<String,Map<Integer,BigDecimal>> tierDeltaReductionMap = spreadCreditDeltaReductionTuple.getT2_instance();
					Map<String,BigDecimal> deltaReductionMap = flattenTierMap(tierDeltaReductionMap);
					
					updateInterCommCreditMap(spreadCreditMap,deltaReductionMap,retMap);
					
					sumMapEntries(deltaByCombCommMap,deltaReductionMap);
					sumTierMapEntries(deltaByCombCommContractMap,tierDeltaReductionMap);
					
				}
				
			} else {
				Utils.prtObErrMess(this.getClass(), "Cannot handle intercommodity spread method code: "+spreadMethodCode);
			}
			
		}
				
		return retMap;
	}
	
	private void updateInterCommCreditMap(
			Map<String,BigDecimal> creditRateMap, 
			Map<String,BigDecimal> deltaMap,
			Map<String,List<BigDecimal[]>> retMap) {
		for(String combCommCode:creditRateMap.keySet()) {
			BigDecimal[] creditArr = new BigDecimal[2];
			creditArr[0] = creditRateMap.get(combCommCode);
			creditArr[1] = deltaMap.get(combCommCode).abs();
			
			if(retMap.containsKey(combCommCode)) {
				retMap.get(combCommCode).add(creditArr);
			} else {
				List<BigDecimal[]> newList = new ArrayList<BigDecimal[]>();
				newList.add(creditArr);
				retMap.put(combCommCode, newList);
			}
		}

	}
	
	private TreeMap<Integer,InterCommSpreadInfo> getInterCommSpreadPrioritySortedMap(
			List<InterCommSpreadInfo> interCommSpreadInfoList) {
		TreeMap<Integer,InterCommSpreadInfo> interCommSpreadByPriorityMap = new TreeMap<Integer,InterCommSpreadInfo>();
		for(InterCommSpreadInfo info:interCommSpreadInfoList) {
			Integer spreadPriority = info.getSpreadPriority();
			interCommSpreadByPriorityMap.put(spreadPriority, info);
		}
		return interCommSpreadByPriorityMap;
	}
	
	// determines the amount of spreads that can be formed by commodity code given a map of deltas 
	//		and a list of info about each spread leg
	private Map<String,Integer> getSpreadsFormedMap(
			List<InterCommSpreadLeg> spreadLegList,
			Map<String,BigDecimal> deltaByCombCommMap) {
		Map<String,Integer> spreadsFormedMap = new HashMap<String,Integer>();
		for(InterCommSpreadLeg icLeg:spreadLegList) {
			String combCommCode = icLeg.getCombCommCode();
			Integer mrktSide = (icLeg.getSpreadSide().compareTo("A")==0) ? 0 : 1; // A == 0 == long
			BigDecimal reqDelta = icLeg.getDeltaSpreadRatio();
			if(mrktSide==1) reqDelta = reqDelta.multiply(new BigDecimal("-1")); // flip sign if its short side
			if(deltaByCombCommMap.containsKey(combCommCode)) {
				BigDecimal combCommDelta = deltaByCombCommMap.get(combCommCode);
				int spreads = combCommDelta.divideToIntegralValue(reqDelta).intValueExact();
				spreadsFormedMap.put(combCommCode, spreads);
			}
		}
		return spreadsFormedMap;
	}
	
	// determines the amount of spreads that can be formed by commodity code given a map of deltas 
	//		and a list of info about each spread leg
	private Map<String,Map<Integer,Integer>> getTierSpreadsFormedMap(
			List<InterCommSpreadLeg> spreadLegList,
			Map<String,Map<Integer,BigDecimal>> deltaByCombCommTierMap) {
		Map<String,Map<Integer,Integer>> spreadsFormedMap = new HashMap<String,Map<Integer,Integer>>();
		for(InterCommSpreadLeg icLeg:spreadLegList) {
			String combCommCode = icLeg.getCombCommCode();
			Integer mrktSide = (icLeg.getSpreadSide().compareTo("A")==0) ? 0 : 1; // A == 0 == long
			BigDecimal reqDelta = icLeg.getDeltaSpreadRatio();
			Integer tier = icLeg.getTierNumber();
			if(mrktSide==1) reqDelta = reqDelta.multiply(new BigDecimal("-1")); // flip sign if its short side
			
			if(deltaByCombCommTierMap.containsKey(combCommCode)) {
				Map<Integer,BigDecimal> tierMap = deltaByCombCommTierMap.get(combCommCode);
				if(tierMap.containsKey(tier)) {
					BigDecimal combCommDelta = tierMap.get(tier);
					int spreads = combCommDelta.divideToIntegralValue(reqDelta).intValueExact();
					if(spreadsFormedMap.containsKey(combCommCode)) {
						spreadsFormedMap.get(combCommCode).put(tier, spreads);
					} else {
						Map<Integer,Integer> newInnerMap = new HashMap<Integer,Integer>();
						newInnerMap.put(tier, spreads);
						spreadsFormedMap.put(combCommCode, newInnerMap);
					}
				}
			}
		}
		return spreadsFormedMap;
	}

	
	// determine the maximum number of long/short spreads that can be moved given a spreadsFormedMap
	private int getMaxSpreadCount(Map<String,Integer> spreadsFormedMap) {
		int spreadCount = 0;
		TreeSet<Integer> spreadCountTreeSet = new TreeSet<Integer>(spreadsFormedMap.values());
		int firstCount = spreadCountTreeSet.first();
		int lastcount = spreadCountTreeSet.last();
		if(firstCount!=0&&lastcount!=0&&!(firstCount<0&&lastcount>0)) {
			if(firstCount>0) {
				spreadCount = firstCount;
			} else {
				spreadCount = lastcount;
			}
		}
		return spreadCount;
	}

	// determine the maximum number of long/short spreads that can be moved given a spreadsFormedMap
	private int getMaxTierSpreadCount(Map<String,Map<Integer,Integer>> spreadsFormedMap) {
		int spreadCount = 0;
		TreeSet<Integer> spreadCountTreeSet = new TreeSet<Integer>();
		for(Entry<String,Map<Integer,Integer>> entry:spreadsFormedMap.entrySet()) {
			spreadCountTreeSet.addAll(entry.getValue().values());
		}
		int firstCount = spreadCountTreeSet.first();
		int lastcount = spreadCountTreeSet.last();
		if(firstCount!=0&&lastcount!=0&&(!(firstCount<0&&lastcount>0))) {
			if(firstCount>0) {
				spreadCount = firstCount;
			} else {
				spreadCount = lastcount;
			}
		}
		return spreadCount;
	}

	// iterates through the legs of a intercommodity spread and determines credit and delta reduction by combComm
	private Tuple<Map<String,BigDecimal>,Map<String,BigDecimal>> getSpreadCreditAndDeltaReductionTuple(
			List<InterCommSpreadLeg> spreadLegList,
			int spreadsFormed,
			BigDecimal spreadCredit) {
		Map<String,BigDecimal> spreadCreditMap = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> deltaReductionMap = new HashMap<String,BigDecimal>();
		
		for(InterCommSpreadLeg icLeg:spreadLegList) {
			String combCommCode = icLeg.getCombCommCode();
			BigDecimal mrktSideMult = (icLeg.getSpreadSide().compareTo("A")==0) ? new BigDecimal("-1") : new BigDecimal("1");
			BigDecimal legSpreadCredit = spreadCredit;
			if(icLeg.getCreditRate()!=null) legSpreadCredit = new BigDecimal(icLeg.getCreditRate());
			BigDecimal deltaPerSpread = icLeg.getDeltaSpreadRatio();
//			BigDecimal credit = legSpreadCredit.multiply(new BigDecimal(Math.abs(spreadsFormed)));
			spreadCreditMap.put(combCommCode, legSpreadCredit.multiply(deltaPerSpread)); // TODO is this right??
			BigDecimal deltaReduction = icLeg.getDeltaSpreadRatio().multiply(mrktSideMult).multiply(new BigDecimal(spreadsFormed));
			deltaReductionMap.put(combCommCode, deltaReduction);
		}
		return new Tuple<Map<String,BigDecimal>,Map<String,BigDecimal>>(spreadCreditMap,deltaReductionMap);
	}

	// iterates through the legs of a intercommodity spread and determines credit and delta reduction by combComm
	private Tuple<Map<String,BigDecimal>,Map<String,Map<Integer,BigDecimal>>> getSpreadCreditAndTierDeltaReductionTuple(
			List<InterCommSpreadLeg> spreadLegList,
			int spreadsFormed,
			BigDecimal spreadCredit) {
		Map<String,BigDecimal> spreadCreditMap = new HashMap<String,BigDecimal>();
		Map<String,Map<Integer,BigDecimal>> deltaReductionMap = new HashMap<String,Map<Integer,BigDecimal>>();
		
		for(InterCommSpreadLeg icLeg:spreadLegList) {
			String combCommCode = icLeg.getCombCommCode();
			BigDecimal mrktSideMult = (icLeg.getSpreadSide().compareTo("A")==0) ? new BigDecimal("-1") : new BigDecimal("1");
			BigDecimal legSpreadCredit = spreadCredit;
			if(icLeg.getCreditRate()!=null&&icLeg.getCreditRate().trim().length()>0) legSpreadCredit = 
					new BigDecimal(icLeg.getCreditRate());
//			BigDecimal credit = legSpreadCredit.multiply(new BigDecimal(Math.abs(spreadsFormed)));
			BigDecimal deltaPerSpread = icLeg.getDeltaSpreadRatio();
			spreadCreditMap.put(combCommCode, legSpreadCredit.multiply(deltaPerSpread));
			BigDecimal deltaReduction = icLeg.getDeltaSpreadRatio().multiply(mrktSideMult).multiply(new BigDecimal(spreadsFormed));
			Integer tier = icLeg.getTierNumber();
			if(deltaReductionMap.containsKey(combCommCode)) {
				deltaReductionMap.get(combCommCode).put(tier, deltaReduction);
			} else {
				Map<Integer,BigDecimal> newInnerMap = new HashMap<Integer,BigDecimal>();
				newInnerMap.put(tier, deltaReduction);
				deltaReductionMap.put(combCommCode, newInnerMap);
			}
		}
		return new Tuple<Map<String,BigDecimal>,Map<String,Map<Integer,BigDecimal>>>(spreadCreditMap,deltaReductionMap);
	}

	private Map<String,BigDecimal> flattenTierMap(Map<String,Map<Integer,BigDecimal>> tierMap) {
		Map<String,BigDecimal> retMap = new HashMap<String,BigDecimal>();
		for(Entry<String,Map<Integer,BigDecimal>> entry:tierMap.entrySet()) {
			String combCommCode = entry.getKey();
			BigDecimal tierTotal = BigDecimal.ZERO;
			for(Entry<Integer,BigDecimal> subEntry:entry.getValue().entrySet()) {
				tierTotal = tierTotal.add(subEntry.getValue());
			}
			retMap.put(combCommCode, tierTotal);
		}
		return retMap;
	}
	
	/**
	 * Sums the entries of the add map with the entries of the changingMap
	 * NOTE: This affects the entries of the changingMap passed in!!!!
	 * @param changingMap
	 * @param addMap
	 */
	private void sumMapEntries(Map<String,BigDecimal> changingMap, Map<String,BigDecimal> addMap) {
		for(Entry<String,BigDecimal> entry:addMap.entrySet()) {
			String key = entry.getKey();
			BigDecimal value = entry.getValue();
			if(changingMap.containsKey(key)) {
				changingMap.put(key, changingMap.get(key).add(value));
			} else {
				changingMap.put(key, value);
			}
		}
	}

	private void sumTierMapEntries(Map<String,Map<Integer,BigDecimal>> changingMap, Map<String,Map<Integer,BigDecimal>> addMap) {
		for(Entry<String,Map<Integer,BigDecimal>> entry:addMap.entrySet()) {
			String key = entry.getKey();
			Map<Integer,BigDecimal> innerMap = entry.getValue();
			if(changingMap.containsKey(key)) {
				Map<Integer,BigDecimal> innerChangeMap = changingMap.get(key);
				for(Entry<Integer,BigDecimal> innerEntry:innerMap.entrySet()) {
					Integer tier = innerEntry.getKey();
					BigDecimal value = innerEntry.getValue();
					if(innerChangeMap.containsKey(tier)) {
						innerChangeMap.put(tier, innerChangeMap.get(tier).add(value));
					} else {
						innerChangeMap.put(tier, value);
					}
				}
			} else {
				changingMap.put(key, innerMap);
			}
//			if(changingMap.containsKey(key)) {
//				changingMap.put(key, changingMap.get(key).add(value));
//			} else {
//				changingMap.put(key, value);
//			}
		}
	}

	private Tuple<Map<String,BigDecimal>,Map<String,Map<Integer,BigDecimal>>> getInterCommSpreadDeltaMaps(
			Map<SpanContractId, Integer> spanIdQtyMap,
			Map<SpanProdId, String> combCommCodeMap,
			Map<SpanContractId, BigDecimal> deltaPerSpanProdMap,
			Map<String,InterCommTierInfo> interCommTierMap) {
		Map<String,BigDecimal> deltaByCombCommMap = new HashMap<String,BigDecimal>();
		Map<String,Map<Integer,BigDecimal>> deltaByCombCommContractMap = new HashMap<String,Map<Integer,BigDecimal>>();
		for(Entry<SpanContractId,Integer> entry:spanIdQtyMap.entrySet()) {
			SpanContractId id = entry.getKey();
			String combCommCode = combCommCodeMap.get(id.getProdId());
			Integer contractMonth = Integer.parseInt(id.getFutMonth()); // TODO fut or opt month?
			InterCommTierInfo tierInfo = interCommTierMap.get(combCommCode);
			Integer tier = null;
			for(InterCommTierDetail tierDet:tierInfo.getInterCommTierList()) {
				// TODO handle dailys
				Integer startMonth = Integer.parseInt(tierDet.getStartConMonth());
				Integer endMonth = Integer.parseInt(tierDet.getEndConMonth());
				if(startMonth<=contractMonth&&contractMonth<=endMonth) tier = tierDet.getTierNumber();
			}
			BigDecimal netDelta = deltaPerSpanProdMap.get(id).multiply(new BigDecimal(entry.getValue()));
			if(deltaByCombCommMap.containsKey(combCommCode)) {
				BigDecimal prevDelta = deltaByCombCommMap.get(combCommCode);
				deltaByCombCommMap.put(combCommCode, prevDelta.add(netDelta));
				if(deltaByCombCommContractMap.get(combCommCode).containsKey(tier)) {
					BigDecimal prevConDelta = deltaByCombCommContractMap.get(combCommCode).get(tier);
					deltaByCombCommContractMap.get(combCommCode).put(tier, prevConDelta.add(netDelta));
				} else {
					deltaByCombCommContractMap.get(combCommCode).put(tier, netDelta);
				}
			} else {
				deltaByCombCommMap.put(combCommCode, netDelta);
				Map<Integer,BigDecimal> newSubMap = new HashMap<Integer,BigDecimal>();
				newSubMap.put(tier, netDelta);
				deltaByCombCommContractMap.put(combCommCode, newSubMap);
			}
		}
		return new Tuple<Map<String,BigDecimal>,Map<String,Map<Integer,BigDecimal>>>(deltaByCombCommMap,deltaByCombCommContractMap);
	}
	
	@Override
	protected Map<String, DeliveryChargeDoc> getDeliveryChargeMap(Set<String> combCommCodeSet) {
		Map<String,DeliveryChargeDoc> retMap = new HashMap<String,DeliveryChargeDoc>();
		for(String combCommCode:combCommCodeSet) {
			DBObject queryObj = new BasicDBObject();
			queryObj.put("combCommCode", combCommCode);
			DeliveryChargeDoc retDoc = gson.fromJson(delivChargeColl.findOne(queryObj).toString(),DeliveryChargeDoc.class);
			retMap.put(combCommCode, retDoc);
		}
		return retMap;
	}

	@Override
	protected Tuple<Map<SpanContractId,BigDecimal>,Map<String,Integer>> getShortOptionMinMarginMap(
			Set<SpanContractId> contractIdSet,
			Map<SpanProdId, String> prodCombCommCodeMap,
			Map<String, InterCommTierInfo> interCommTierMap,
			Map<String, DeliveryChargeDoc> delivChargeMap) {
		Map<SpanContractId, BigDecimal> shortOptChargeMap = new HashMap<SpanContractId,BigDecimal>();
		Map<String,Integer> shortOptMethodMap = new HashMap<String,Integer>();
		for(SpanContractId conId:contractIdSet) {
			String combCommCode = prodCombCommCodeMap.get(conId.getProdId());
			InterCommTierInfo tierInfo = interCommTierMap.get(combCommCode);
			DeliveryChargeDoc delivChargeDoc = delivChargeMap.get(combCommCode);
			BigDecimal shortOptCharge = new BigDecimal(delivChargeDoc.getShortOptMinCharge());
			Integer shortOptMethodCode = (delivChargeDoc.getShortOptCalcMethod().compareTo("1")==0) ? 1 : 2;
			shortOptMethodMap.put(combCommCode, shortOptMethodCode);
			// check to see if it is a tier based spread method
			if(tierInfo.getSpreadMethodCode().compareTo("30")==0) {
				// TODO update to handle daily contracts
				Integer contractMonth = Integer.parseInt(conId.getFutMonth()); // fut or opt?
				for(InterCommTierDetail tier:tierInfo.getInterCommTierList()) {
					Integer startMonth = Integer.parseInt(tier.getStartConMonth());
					Integer endMonth = Integer.parseInt(tier.getEndConMonth());
					if(startMonth<=contractMonth&&endMonth>=contractMonth) {
						shortOptCharge = new BigDecimal(tier.getShortOptMinCharge());
						break;
					}
				}
			}
			shortOptChargeMap.put(conId, shortOptCharge);
		}
		return new Tuple<Map<SpanContractId,BigDecimal>,Map<String,Integer>>(shortOptChargeMap,shortOptMethodMap);
	}
	
	@Override
	protected BigDecimal getShortOptionMargin(
			Map<SpanContractId, Integer> posQtyMap,
			Map<SpanProdId, String> prodCombCommCodeMap,
			Tuple<Map<SpanContractId, BigDecimal>,Map<String,Integer>> shortOptMinMarginTuple) {
		
		Map<SpanContractId,BigDecimal> shortChargeByConMap = shortOptMinMarginTuple.getT1_instance();
		Map<String,Integer> combCommShortMethodMap = shortOptMinMarginTuple.getT2_instance();

		Map<String,Tuple<Integer,BigDecimal>[]> combCommShortMap = new HashMap<String,Tuple<Integer,BigDecimal>[]>();

		for(Entry<SpanContractId,Integer> entry:posQtyMap.entrySet()) {
			SpanContractId conId = entry.getKey();
			Integer qty = entry.getValue();
			if(conId.getOptRightCode()!=null && qty < 0) {
				Integer callPut = (conId.getOptRightCode().compareTo("C")==0) ? 0 : 1; // call = 0, put = 1
				String combCommCode = prodCombCommCodeMap.get(conId.getProdId());
				BigDecimal conShortOptCharge = shortChargeByConMap.get(conId).multiply(new BigDecimal(-qty));
				if(combCommShortMap.containsKey(combCommCode)) {
					Tuple<Integer,BigDecimal>[] currArr = combCommShortMap.get(combCommCode);
					currArr[callPut] = new Tuple<Integer,BigDecimal>(
							currArr[callPut].getT1_instance()-qty,currArr[callPut].getT2_instance().add(conShortOptCharge));
				} else {
					@SuppressWarnings("unchecked")
					Tuple<Integer,BigDecimal>[] newArr = new Tuple[2];
					// TODO kinda stupid initialization?
					newArr[0] = new Tuple<Integer,BigDecimal>(0,BigDecimal.ZERO);
					newArr[1] = new Tuple<Integer,BigDecimal>(0,BigDecimal.ZERO);
					newArr[callPut] = new Tuple<Integer,BigDecimal>(-qty,conShortOptCharge);
					combCommShortMap.put(combCommCode, newArr);
				}
			}
		}		
		BigDecimal totalShortOpt = BigDecimal.ZERO;
		for(Entry<String,Tuple<Integer,BigDecimal>[]> entry:combCommShortMap.entrySet()) {
			String combCommCode = entry.getKey();
			Tuple<Integer,BigDecimal>[] shortChargeArr = entry.getValue();
			Integer shortOptMethod = combCommShortMethodMap.get(combCommCode);
			if(shortOptMethod==1) {
				// take short charge of whichever is greater, puts or calls
				if(shortChargeArr[0].getT1_instance()>shortChargeArr[1].getT1_instance()){
					totalShortOpt = totalShortOpt.add(shortChargeArr[0].getT2_instance());
				} else {
					totalShortOpt = totalShortOpt.add(shortChargeArr[1].getT2_instance());
				}
			} else {
				// take short charge of all options
				totalShortOpt = totalShortOpt.add(shortChargeArr[0].getT2_instance()).add(shortChargeArr[1].getT2_instance());
			}
		}
		return totalShortOpt;
	}


	@Override
	protected BigDecimal getPortfolioSpan(
			Map<String, SpanRiskResult> spanRiskMap,
			Map<String, BigDecimal> interMonthSpreadChargeMap,
			Map<String,List<BigDecimal[]>> interCommSpreadCreditMap,
			Map<String,BigDecimal> deltaByCombCommMap) {
		BigDecimal ret = BigDecimal.ZERO;
		for(Entry<String,SpanRiskResult> entry:spanRiskMap.entrySet()) {
			ret = ret.add(entry.getValue().getScanRisk());
		}
		for(Entry<String,BigDecimal> entry:interMonthSpreadChargeMap.entrySet()) {
			ret = ret.add(entry.getValue());
		}
		Map<String,BigDecimal> effectiveInterCommCreditMap = new HashMap<String,BigDecimal>();
		for(Entry<String,List<BigDecimal[]>> entry:interCommSpreadCreditMap.entrySet()) {
			String combCommCode = entry.getKey();
			BigDecimal totalDelta = deltaByCombCommMap.get(combCommCode);
			BigDecimal totalCredit = BigDecimal.ZERO;
			for(BigDecimal[] creditInfo:entry.getValue()) {
				BigDecimal creditRate = creditInfo[0];
				BigDecimal creditDeltas = creditInfo[1];
				if(totalDelta.compareTo(BigDecimal.ZERO)==0) continue;
				BigDecimal deltaPercent = creditDeltas.divide(totalDelta, RoundingMode.HALF_EVEN);
				BigDecimal futPriceRisk = spanRiskMap.get(combCommCode).getFutPriceRisk();
				
				BigDecimal effectiveCredit = 
//						creditDeltas.divide(totalDelta).multiply(spanRiskMap.get(combCommCode).getFutPriceRisk()).multiply(creditRate);
						deltaPercent.multiply(futPriceRisk).multiply(creditRate);

				totalCredit = totalCredit.add(effectiveCredit.abs());
			}
			effectiveInterCommCreditMap.put(combCommCode, totalCredit);
		}
		for(Entry<String,BigDecimal> credit:effectiveInterCommCreditMap.entrySet()) {
			ret = ret.subtract(credit.getValue());
		}
		return ret;
	}

}
