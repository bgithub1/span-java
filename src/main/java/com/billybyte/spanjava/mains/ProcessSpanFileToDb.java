package com.billybyte.spanjava.mains;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.mongo.MongoDoc;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.expanded.CombCommGroupDoc;
import com.billybyte.spanjava.mongo.expanded.CurrencyConversionDoc;
import com.billybyte.spanjava.mongo.expanded.ExchangeDoc;
import com.billybyte.spanjava.mongo.expanded.FirstCombCommDoc;
import com.billybyte.spanjava.mongo.expanded.ProductSpecsDoc;
import com.billybyte.spanjava.mongo.expanded.subdocs.RiskArrayParamBlockDoc;
import com.billybyte.spanjava.mongo.span.CombCommProdIdDoc;
import com.billybyte.spanjava.mongo.span.DeliveryChargeDoc;
import com.billybyte.spanjava.mongo.span.InterCommSpreadInfo;
import com.billybyte.spanjava.mongo.span.InterCommTierInfo;
import com.billybyte.spanjava.mongo.span.InterMonthSpreadSummaryDoc;
import com.billybyte.spanjava.mongo.span.RiskArrayDoc;
import com.billybyte.spanjava.mongo.span.RiskDataDoc;
import com.billybyte.spanjava.mongo.span.subtypes.InterCommSpreadLeg;
import com.billybyte.spanjava.mongo.span.subtypes.InterMonthSpreadDetail;
import com.billybyte.spanjava.mongo.span.subtypes.InterMonthSpreadLeg;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.billybyte.spanjava.mongo.span.subtypes.TierRange;
import com.billybyte.spanjava.recordtypes.expanded.CombinedCommGroups;
import com.billybyte.spanjava.recordtypes.expanded.CurrencyConversionRate;
import com.billybyte.spanjava.recordtypes.expanded.ExchangeHeader;
import com.billybyte.spanjava.recordtypes.expanded.FirstCombComm;
import com.billybyte.spanjava.recordtypes.expanded.FirstPhysical;
import com.billybyte.spanjava.recordtypes.expanded.InterCommScanTiers;
import com.billybyte.spanjava.recordtypes.expanded.InterCommSpreads;
import com.billybyte.spanjava.recordtypes.expanded.IntraCommTiers;
import com.billybyte.spanjava.recordtypes.expanded.PriceSpecs;
import com.billybyte.spanjava.recordtypes.expanded.RiskArrayParams;
import com.billybyte.spanjava.recordtypes.expanded.SecondCombComm;
import com.billybyte.spanjava.recordtypes.expanded.SecondPhysical;
import com.billybyte.spanjava.recordtypes.expanded.ThirdCombComm;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.CombCommCode;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.IntraCommSpreadLeg;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.RiskArrayParamBlock;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.TierDayWeekRange;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.TierMonthRange;
import com.billybyte.spanjava.utils.CmeSpanUtils.RecordType;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class ProcessSpanFileToDb {

	private final DBCollection exchColl;
	private final DBCollection currConvColl;
	private final DBCollection spanArrayColl;
	private final DBCollection riskDataColl;
	private final DBCollection commGrpColl;
	private final DBCollection priceSpecColl;
	private final DBCollection firstCombCommColl;
	private final DBCollection combCommCodeColl;
	private final DBCollection interMonthSpreadColl;
	private final DBCollection interCommSpreadColl;
	private final DBCollection interCommTierColl;
	private final DBCollection delivChargeColl;
	
	private final int batchSize = 5000;
	
	public ProcessSpanFileToDb(MongoWrapper m, Boolean needsAuth, String mongoUser, String mongoPw) {
		
//		Mongo m = new Mongo("127.0.0.1", 27017);
		
		List<DB> dbList = new ArrayList<DB>();
		
		DB exchDb = m.getDB(SpanMongoUtils.EXCH_DB);
		dbList.add(exchDb);
		DB currDb = m.getDB(SpanMongoUtils.CURR_DB);
		dbList.add(currDb);
		DB arrayDb = m.getDB(SpanMongoUtils.ARRAY_DB);
		dbList.add(arrayDb);
		DB riskDataDb = m.getDB(SpanMongoUtils.RISK_DATA_DB);
		dbList.add(riskDataDb);
		DB commGrpDb = m.getDB(SpanMongoUtils.COMM_GRP_DB);
		dbList.add(commGrpDb);
		DB priceSpecDb = m.getDB(SpanMongoUtils.PRICE_SPEC_DB);
		dbList.add(priceSpecDb);
		DB firstCombCommDb = m.getDB(SpanMongoUtils.FIRST_COMB_COMM_DB);
		dbList.add(firstCombCommDb);
		DB combCommDb = m.getDB(SpanMongoUtils.COMB_COMM_DB);
		dbList.add(combCommDb);
		DB interMonthSpreadDb = m.getDB(SpanMongoUtils.INTER_MONTH_SPREAD_DB);
		dbList.add(interMonthSpreadDb);
		DB interCommSpreadDb = m.getDB(SpanMongoUtils.INTER_COMM_SPREAD_DB);
		dbList.add(interCommSpreadDb);
		DB interCommTierDb = m.getDB(SpanMongoUtils.INTER_COMM_TIER_DB);
		dbList.add(interCommTierDb);
		DB delivChargeDb = m.getDB(SpanMongoUtils.DELIV_CHARGE_DB);
		dbList.add(delivChargeDb);
		
		if(needsAuth) {
			for(DB db:dbList) {
				if(!db.authenticate(mongoUser, mongoPw.toCharArray())){
					Utils.prtObErrMess(this.getClass(), "Unable to authorize connection to db: "+db.getName());
				}
			}
		}
				
		this.exchColl = exchDb.getCollection(SpanMongoUtils.EXCH_CL);
		this.currConvColl = currDb.getCollection(SpanMongoUtils.CURR_CL);
		this.spanArrayColl = arrayDb.getCollection(SpanMongoUtils.ARRAY_CL);
		this.riskDataColl = riskDataDb.getCollection(SpanMongoUtils.RISK_DATA_CL);
		this.commGrpColl = commGrpDb.getCollection(SpanMongoUtils.COMM_GRP_CL);
		this.priceSpecColl = priceSpecDb.getCollection(SpanMongoUtils.PRICE_SPEC_CL);
		this.firstCombCommColl = firstCombCommDb.getCollection(SpanMongoUtils.FIRST_COMB_COMM_CL);
		this.combCommCodeColl = combCommDb.getCollection(SpanMongoUtils.COMB_COMM_CL);
		this.interMonthSpreadColl = interMonthSpreadDb.getCollection(SpanMongoUtils.INTER_MONTH_SPREAD_CL);
		this.interCommSpreadColl = interCommSpreadDb.getCollection(SpanMongoUtils.INTER_COMM_SPREAD_CL);
		this.interCommTierColl = interCommTierDb.getCollection(SpanMongoUtils.INTER_COMM_TIER_CL);
		this.delivChargeColl = delivChargeDb.getCollection(SpanMongoUtils.DELIV_CHARGE_CL);
	}
	
	public ProcessSpanFileToDb(MongoWrapper m) {
		
//		Mongo m = new Mongo("127.0.0.1", 27017);
		
		List<DB> dbList = new ArrayList<DB>();
		
		DB exchDb = m.getDB(SpanMongoUtils.EXCH_DB);
		dbList.add(exchDb);
		DB currDb = m.getDB(SpanMongoUtils.CURR_DB);
		dbList.add(currDb);
		DB arrayDb = m.getDB(SpanMongoUtils.ARRAY_DB);
		dbList.add(arrayDb);
		DB riskDataDb = m.getDB(SpanMongoUtils.RISK_DATA_DB);
		dbList.add(riskDataDb);
		DB commGrpDb = m.getDB(SpanMongoUtils.COMM_GRP_DB);
		dbList.add(commGrpDb);
		DB priceSpecDb = m.getDB(SpanMongoUtils.PRICE_SPEC_DB);
		dbList.add(priceSpecDb);
		DB firstCombCommDb = m.getDB(SpanMongoUtils.FIRST_COMB_COMM_DB);
		dbList.add(firstCombCommDb);
		DB combCommDb = m.getDB(SpanMongoUtils.COMB_COMM_DB);
		dbList.add(combCommDb);
		DB interMonthSpreadDb = m.getDB(SpanMongoUtils.INTER_MONTH_SPREAD_DB);
		dbList.add(interMonthSpreadDb);
		DB interCommSpreadDb = m.getDB(SpanMongoUtils.INTER_COMM_SPREAD_DB);
		dbList.add(interCommSpreadDb);
		DB interCommTierDb = m.getDB(SpanMongoUtils.INTER_COMM_TIER_DB);
		dbList.add(interCommTierDb);
		DB delivChargeDb = m.getDB(SpanMongoUtils.DELIV_CHARGE_DB);
		dbList.add(delivChargeDb);
				
		this.exchColl = exchDb.getCollection(SpanMongoUtils.EXCH_CL);
		this.currConvColl = currDb.getCollection(SpanMongoUtils.CURR_CL);
		this.spanArrayColl = arrayDb.getCollection(SpanMongoUtils.ARRAY_CL);
		this.riskDataColl = riskDataDb.getCollection(SpanMongoUtils.RISK_DATA_CL);
		this.commGrpColl = commGrpDb.getCollection(SpanMongoUtils.COMM_GRP_CL);
		this.priceSpecColl = priceSpecDb.getCollection(SpanMongoUtils.PRICE_SPEC_CL);
		this.firstCombCommColl = firstCombCommDb.getCollection(SpanMongoUtils.FIRST_COMB_COMM_CL);
		this.combCommCodeColl = combCommDb.getCollection(SpanMongoUtils.COMB_COMM_CL);
		this.interMonthSpreadColl = interMonthSpreadDb.getCollection(SpanMongoUtils.INTER_MONTH_SPREAD_CL);
		this.interCommSpreadColl = interCommSpreadDb.getCollection(SpanMongoUtils.INTER_COMM_SPREAD_CL);
		this.interCommTierColl = interCommTierDb.getCollection(SpanMongoUtils.INTER_COMM_TIER_CL);
		this.delivChargeColl = delivChargeDb.getCollection(SpanMongoUtils.DELIV_CHARGE_CL);
	}

	public static void main(String[] args) {
		
//		String fileName = "cme.s.pa2";
//		String fileName = "cme.20130205.c.pa2";
//		String fileName = "nyb.20130204.s.pa2";
//		String nybFile = "nyb.20130228.s.pa2";
		String cmeFile = "cme.20130308.a.pa2";
		
		List<String> fileList = new ArrayList<String>();
		
//		fileList.add(nybFile);
		fileList.add(cmeFile);
		
		try {
			
			Calendar beforeTime = Calendar.getInstance();

			MongoWrapper m = new MongoWrapper("127.0.0.1", 27017);

			ProcessSpanFileToDb spanParser = new ProcessSpanFileToDb(m, false, null, null);

			spanParser.clearCollections();
			
			for(String file:fileList) {
				Utils.prt("starting to process file: "+file);
								
				spanParser.processSpan(file);
			}

			Calendar afterTime = Calendar.getInstance();

			Utils.prt("took "+(afterTime.getTimeInMillis()-beforeTime.getTimeInMillis())/1000+" s");
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void clearCollections() {
		Utils.prt("Starting to clear collections...");
		exchColl.remove(new BasicDBObject());
		currConvColl.remove(new BasicDBObject());
		spanArrayColl.remove(new BasicDBObject());
		riskDataColl.remove(new BasicDBObject());
		commGrpColl.remove(new BasicDBObject());
		priceSpecColl.remove(new BasicDBObject());
		firstCombCommColl.remove(new BasicDBObject());
		combCommCodeColl.remove(new BasicDBObject());
		interMonthSpreadColl.remove(new BasicDBObject());
		interCommSpreadColl.remove(new BasicDBObject());
		interCommTierColl.remove(new BasicDBObject());
		delivChargeColl.remove(new BasicDBObject());
		Utils.prt("Cleared collections...");
	}
	
	public void processSpan(String fileName) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		List<MongoDoc> currConvDocList = new ArrayList<MongoDoc>();
		List<MongoDoc> exchDocList = new ArrayList<MongoDoc>();
		Map<String,MongoDoc> riskArrayDocMap = new HashMap<String,MongoDoc>();
		List<MongoDoc> riskDataDocList = new ArrayList<MongoDoc>();
		Map<String,MongoDoc> combGroupMap = new HashMap<String,MongoDoc>();
		List<MongoDoc> priceSpecList = new ArrayList<MongoDoc>();
		Map<String,MongoDoc> firstCombCollMap = new HashMap<String,MongoDoc>();
		Map<String,MongoDoc> combCommCodeMap = new HashMap<String,MongoDoc>();
		Map<String,MongoDoc> interMonthSpreadMap = new HashMap<String,MongoDoc>();
		Map<String,MongoDoc> interCommSpreadMap = new HashMap<String,MongoDoc>();
		List<MongoDoc> interCommTierList = new ArrayList<MongoDoc>();
		List<MongoDoc> delivChargeList = new ArrayList<MongoDoc>();
				
		int count8s = 0;
		
		int lineNum = 0;
					
		while(br.ready()) {
			
			String line = br.readLine();
//			Utils.prt(lineNum+" ("+line.length()+"): "+line);

			String recIdString = line.substring(0, 2);
			
			String enumName = RecordType.getEnumNameByValue(recIdString);
			if(enumName!=null) {
				RecordType recId = RecordType.valueOf(enumName);
				
				try {
					switch(recId) {
					
						case EXCHANGE_COMPLEX_HEADER:
//						ExchangeComplexHeader ech = new ExchangeComplexHeader(line);
//						Utils.prt(ech.toString());
							break;
						case CURR_CONV_RATE:
							CurrencyConversionRate ccr = new CurrencyConversionRate(line);
							CurrencyConversionDoc currConvDoc = new CurrencyConversionDoc(
									ccr.getFromCurrISO(), ccr.getFromCurrByte(), ccr.getToCurrISO(), ccr.getToCurrByte(), ccr.getConvMult());
							currConvDocList.add(currConvDoc);
//						Utils.prt(ccr.toString());
							break;
						case EXCHANGE_HEADER:
							ExchangeHeader eh = new ExchangeHeader(line);
							ExchangeDoc exchDoc = new ExchangeDoc(eh.getExchAcro(), eh.getExchCode());
							exchDocList.add(exchDoc);
//						Utils.prt(eh.toString());
							break;
						case PRICE_SPECS:
							PriceSpecs ps = new PriceSpecs(line);
							ProductSpecsDoc psDoc = new ProductSpecsDoc(
									ps.getExchAcro().trim(), ps.getProdCommCode().trim(), ps.getProdTypeCode().trim(), ps.getProdName(), 
									ps.getSettlePriceDecLoc(), ps.getStrikePriceDecLoc(), ps.getSettlePriceAlignCode(), 
									ps.getStrikePriceAlignCode(), ps.getContValueFact(), ps.getStandardCabOptValue(), 
									ps.getQuotePosQtyPerCont(), ps.getSettleCurrISO(), ps.getSettleCurrByte(), 
									ps.getPriceQuoteMethod(), ps.getSignForContValueFact(), ps.getContValueFactMult(), 
									ps.getExerciseStyle(), ps.getProdLongName(), ps.getPositonalProdIndic(), 
									ps.getMoneyCalcMethod(), ps.getValuationMethod(), ps.getSettleMethod(), 
									ps.getFxSpotDateCollGainCredRate(), ps.getFxPreSpotDateCollGainCredRate(), 
									ps.getFxPreSpotDateNumOfDays(), ps.getFxForwardCollGainCredRate(), 
									ps.getEquivPosFactor(), ps.getEquivPosFactExp(), 
									ps.getSignForEquivPosFactExp(), ps.getVarTickOptFlag());
							priceSpecList.add(psDoc);
							if(priceSpecList.size()==batchSize) {
								SpanMongoUtils.batchInsert(priceSpecColl, priceSpecList);
								priceSpecList.clear();
							}
//						Utils.prt(ps.toString());
							break;
						case FIRST_COMB_COMM:
							FirstCombComm fcc = new FirstCombComm(line);
							String fccKey = fcc.getCombCommCode().trim();
							List<RiskArrayParamBlockDoc> riskArrayParamBlockList = new ArrayList<RiskArrayParamBlockDoc>();
							Set<SpanProdId> prodIdSet = new HashSet<SpanProdId>();
							for(RiskArrayParamBlock rec:fcc.getRiskArrayParamBlockList()) {
								riskArrayParamBlockList.add(new RiskArrayParamBlockDoc(rec));
								prodIdSet.add(new SpanProdId(fcc.getExchAcro().trim(), rec.getCommProdCode().trim(), rec.getProdType().trim()));
							}
							if(combCommCodeMap.containsKey(fccKey)) {
								CombCommProdIdDoc prevCcpiDoc = (CombCommProdIdDoc) combCommCodeMap.get(fccKey);
								prevCcpiDoc.getProdIdSet().addAll(prodIdSet);
							} else {
								CombCommProdIdDoc ccpiDoc = new CombCommProdIdDoc(fccKey, prodIdSet);
								combCommCodeMap.put(fccKey, ccpiDoc);
							}
							FirstCombCommDoc fccDoc;
							if(firstCombCollMap.containsKey(fccKey)) {
								FirstCombCommDoc prevFccDoc = (FirstCombCommDoc) firstCombCollMap.get(fccKey);
								riskArrayParamBlockList.addAll(prevFccDoc.getRiskArrayParamBlockList());
								fccDoc = new FirstCombCommDoc(
										fcc.getExchAcro().trim(), fcc.getCombCommCode().trim(), fcc.getRiskExp(), 
										fcc.getPerfBondCurrISO(), fcc.getPerfBondCurrCode(), fcc.getOptMargStyle(), 
										fcc.getLimitOptValue(), fcc.getCombMargMethFlag(), riskArrayParamBlockList);
								firstCombCollMap.put(fccKey, fccDoc);							
							} else {
								fccDoc = new FirstCombCommDoc(
										fcc.getExchAcro().trim(), fcc.getCombCommCode().trim(), fcc.getRiskExp(), 
										fcc.getPerfBondCurrISO(), fcc.getPerfBondCurrCode(), fcc.getOptMargStyle(), 
										fcc.getLimitOptValue(), fcc.getCombMargMethFlag(), riskArrayParamBlockList);
								firstCombCollMap.put(fccKey, fccDoc);
							}
							
							if(firstCombCollMap.size()==batchSize) {
								SpanMongoUtils.batchInsert(firstCombCommColl, new ArrayList<MongoDoc>(firstCombCollMap.values()));
								firstCombCollMap.clear();
							}
							if(combCommCodeMap.size()==batchSize) {
								SpanMongoUtils.batchInsert(combCommCodeColl, new ArrayList<MongoDoc>(combCommCodeMap.values()));
								combCommCodeMap.clear();
							}
//						FirstCombComm fcc = new FirstCombComm(line);
//						Utils.prt(fcc.toString());
							break;
						case SECOND_COMB_COMM:
							SecondCombComm scc = new SecondCombComm(line);
							String imsKey = scc.getCombCommCode().trim();
							// construct tier range
							List<TierRange> tierRangeList = new ArrayList<TierRange>();
							List<TierMonthRange> tierMonthRangeList = scc.getTierMonthRangeList();
							List<TierDayWeekRange> tierDayRangeList = scc.getTierDayWeekRangeList();
							for(int i=0;i<tierMonthRangeList.size();i++) {
								TierMonthRange monthRange = tierMonthRangeList.get(i);
								if(monthRange.getTierNumber().trim().length()>0) {
									if(tierDayRangeList.size()>=(i+1)) {
										TierDayWeekRange dayRange = tierDayRangeList.get(i);
										tierRangeList.add(new TierRange(
												monthRange.getTierNumber(), monthRange.getStartMonth(), 
												monthRange.getEndMonth(), dayRange.getStartDayWeekCode(), dayRange.getEndDayWeekCode()));
									} else {
										tierRangeList.add(new TierRange(
												monthRange.getTierNumber(), monthRange.getStartMonth(), 
												monthRange.getEndMonth(), null, null));
									}
								}
							}
							
							// check to see if there is already a doc for this comb comm code - if so append the tier range list
							if(interMonthSpreadMap.containsKey(imsKey)) {
								InterMonthSpreadSummaryDoc prevDoc = (InterMonthSpreadSummaryDoc) interMonthSpreadMap.get(imsKey);
								prevDoc.getTierRangeList().addAll(tierRangeList);
							} else {
								InterMonthSpreadSummaryDoc newDoc = new InterMonthSpreadSummaryDoc(
										imsKey, scc.getIntraCommSpreadChargeMethCode(), scc.getInitToMaintRatioMember(), 
										scc.getInitToMaintRatioHedger(), scc.getInitToMaintRatioSpec(), 
										tierRangeList, new ArrayList<InterMonthSpreadDetail>());
								interMonthSpreadMap.put(imsKey, newDoc);
							}
//						Utils.prt(scc.toString());
							break;
						case THIRD_COMB_CORR:
							ThirdCombComm tcc = new ThirdCombComm(line);
							
							DeliveryChargeDoc dcDoc = new DeliveryChargeDoc(
									tcc.getCombCommCode().trim(), tcc.getDelivChargeMethodCode(), tcc.getShortOptMinChargeRate(), 
									tcc.getRiskMainBondAdjFactMember(), tcc.getRiskMainBondAdjFactHedger(), 
									tcc.getRiskMainBondAdjFactSpec(), tcc.getShortOptMinCalcMethod());
							
							delivChargeList.add(dcDoc);
//						Utils.prt(tcc.toString());
							break;
						case TIERED_SCANNED:
							InterCommScanTiers icst = new InterCommScanTiers(line);
							
							InterCommTierInfo icstInfo = new InterCommTierInfo(
									icst.getCombCommCode().trim(), icst.getScanSpreadMethodCode(), 
									icst.getWeightedFutPriceRiskCalcMethod(), icst.getInterCommScanTierList());
							
							interCommTierList.add(icstInfo);
							
							if(interCommTierList.size()==batchSize) {
								SpanMongoUtils.batchInsert(interCommTierColl, interCommTierList);
								interCommTierList.clear();
							}
							
//						Utils.prt(icst.toString());
							break;
						case INTRA_COMM_SERIES:
//						IntraCommSeries ics = new IntraCommSeries(line);
//						Utils.prt(ics.toString());
							break;
						case INTRA_COMM_TIERS:
							IntraCommTiers ict = new IntraCommTiers(line);
							String ictKey = ict.getCombCommCode().trim();
							Integer spreadPri = Integer.parseInt(ict.getSpreadPriority());
							List<InterMonthSpreadLeg> spreadLegList = new ArrayList<InterMonthSpreadLeg>();
							for(IntraCommSpreadLeg leg:ict.getIntraCommSpreadLegList()) {
								spreadLegList.add(new InterMonthSpreadLeg(
										leg.getLegNumber(), leg.getTierNumber(), leg.getDeltaPerSpreadRatio(), leg.getMrktSide()));
							}

							InterMonthSpreadSummaryDoc prevDoc = (InterMonthSpreadSummaryDoc) interMonthSpreadMap.get(ictKey);
							List<InterMonthSpreadDetail> interMonthSpreadDetailList = prevDoc.getInterMonthSpreadDetailList();
							boolean newDetail = true;
							for(InterMonthSpreadDetail detail:interMonthSpreadDetailList) {
								if(detail.getSpreadPriority()==spreadPri){
									detail.getSpreadLegList().addAll(spreadLegList);
									newDetail = false;
								}
							}
							if(newDetail) {
								InterMonthSpreadDetail newSpreadDetail = new InterMonthSpreadDetail(
										Integer.parseInt(ict.getSpreadPriority()), Integer.parseInt(ict.getNumOfLegs()), 
										ict.getChargeRate(), spreadLegList);
								interMonthSpreadDetailList.add(newSpreadDetail);
							}
//						Utils.prt(ict.toString());
							break;
						case INTER_COMM_SPREADS:
							InterCommSpreads icsp = new InterCommSpreads(line);
							
							String icspKey = icsp.getCommGrpCode().trim()+icsp.getSpreadPriority()+icsp.getInterSpreadMethodCode();
							
							List<InterCommSpreadLeg> icspLegList = icsp.getInterCommSpreadLegList();
							
							InterCommSpreadInfo icsDoc;
							
							String creditRateString = icsp.getSpreadCreditRate();
							BigDecimal spreadCreditRate;
							if(creditRateString.trim().length()>0) {
								spreadCreditRate = new BigDecimal(creditRateString.substring(0,1)+"."+creditRateString.substring(1));
							} else {
								Utils.prtObErrMess(this.getClass(), "Empty spread credit rate string for inter comm spread key: "+icspKey);
								spreadCreditRate = BigDecimal.ZERO;
							}
							
							if(interCommSpreadMap.containsKey(icspKey)) {
								InterCommSpreadInfo prevIcsDoc = (InterCommSpreadInfo) interCommSpreadMap.get(icspKey);
								
								icspLegList.addAll(prevIcsDoc.getInterCommSpreadLegList());
								
								icsDoc = new InterCommSpreadInfo(
										icsp.getCommGrpCode().trim(), Integer.parseInt(icsp.getSpreadPriority()), spreadCreditRate, 
										icsp.getInterSpreadMethodCode(), icsp.getCreditCalcMethod(), icsp.getSpreadGroupFlag(), 
										icsp.getSpreadCreditRateDefSepFlag(), icsp.getRegStatusEligibilityCode(), icspLegList);
								interCommSpreadMap.put(icspKey, icsDoc);
								
							} else {
								icsDoc = new InterCommSpreadInfo(
										icsp.getCommGrpCode().trim(), Integer.parseInt(icsp.getSpreadPriority()), spreadCreditRate, 
										icsp.getInterSpreadMethodCode(), icsp.getCreditCalcMethod(), icsp.getSpreadGroupFlag(), 
										icsp.getSpreadCreditRateDefSepFlag(), icsp.getRegStatusEligibilityCode(), icspLegList);
								interCommSpreadMap.put(icspKey, icsDoc);
							}
							
							if(interCommSpreadMap.size()==batchSize) {
								SpanMongoUtils.batchInsert(interCommSpreadColl, new ArrayList<MongoDoc>(interCommSpreadMap.values()));
								interCommSpreadMap.clear();
							}
							
//						Utils.prt(icsp.toString());
							break;
						case RISK_ARRAY_PARAMS:
							RiskArrayParams rap = new RiskArrayParams(line);
							RiskDataDoc riskDataDoc = new RiskDataDoc(rap.getExchAcro(),
									rap.getCommCode().trim(), rap.getProdTypeCode().trim(), rap.getFutConMonth(), rap.getFutConDayWeekCode(), 
									rap.getOptConMonth(), rap.getOptConDayWeekCode(), rap.getBaseVol(), rap.getIntRate(), 
									rap.getDivYield(), rap.getTimeToExpiration(), rap.getPricingModel());
							riskDataDocList.add(riskDataDoc);
							if(riskDataDocList.size()==batchSize) {
								SpanMongoUtils.batchInsert(riskDataColl, riskDataDocList);
								riskDataDocList.clear();
							}
//						Utils.prt(rap.toString());
							break;
						case DAILY_ADJ:
//						DailyAdjustment da = new DailyAdjustment(line);
//						Utils.prt(da.toString());
							break;
						case OPT_ON_COMB:
//						OptionsOnCombinations ooc = new OptionsOnCombinations(line);
//						Utils.prt(ooc.toString());
							break;
						case SPLIT_ALLOC:
//						SplitAllocation sa = new SplitAllocation(line);
//						Utils.prt(sa.toString());
							break;
						case DELTA_SPLIT_ALLOC:
//						DeltaSplitAllocation dsa = new DeltaSplitAllocation(line);
//						Utils.prt(dsa.toString());
							break;
						case COMB_COMM_GRPS:
							CombinedCommGroups ccg = new CombinedCommGroups(line);
							List<String> cccList = new ArrayList<String>();
							for(CombCommCode ccCode:ccg.getCombCommCodeList()) {
								cccList.add(ccCode.getCombCommCode().trim());
							}
							String key = ccg.getCombCommGrpCode();
							if(combGroupMap.containsKey(key)) {
								CombCommGroupDoc prevCcGDoc = (CombCommGroupDoc) combGroupMap.get(key);
								prevCcGDoc.getCombCommCodeList().addAll(cccList);
							} else {
								CombCommGroupDoc ccgDoc = new CombCommGroupDoc(ccg.getCombCommGrpCode().trim(), cccList);
								combGroupMap.put(key, ccgDoc);
							}
							if(combGroupMap.size()==batchSize) {
								SpanMongoUtils.batchInsert(commGrpColl, new ArrayList<MongoDoc>(combGroupMap.values()));
								combGroupMap.clear();
							}
//						Utils.prt(ccg.toString());
							break;
						case FIRST_PHYS_REC:
							count8s++;
							FirstPhysical fp = new FirstPhysical(line);
							String key1 = getKey(fp);
							if(riskArrayDocMap.containsKey(key1)) {
								Utils.prtErr("Already an 81 record for this?? - "+key1);
							} else {
								String[] spanArr1 = new String[16];
								spanArr1[0] = fp.getArrVal1();
								if(fp.getArrValSign1().compareTo("-")==0) spanArr1[0] = "-"+spanArr1[0];
								spanArr1[1] = fp.getArrVal2();
								if(fp.getArrValSign2().compareTo("-")==0) spanArr1[1] = "-"+spanArr1[1];
								spanArr1[2] = fp.getArrVal3();
								if(fp.getArrValSign3().compareTo("-")==0) spanArr1[2] = "-"+spanArr1[2];
								spanArr1[3] = fp.getArrVal4();
								if(fp.getArrValSign4().compareTo("-")==0) spanArr1[3] = "-"+spanArr1[3];
								spanArr1[4] = fp.getArrVal5();
								if(fp.getArrValSign5().compareTo("-")==0) spanArr1[4] = "-"+spanArr1[4];
								spanArr1[5] = fp.getArrVal6();
								if(fp.getArrValSign6().compareTo("-")==0) spanArr1[5] = "-"+spanArr1[5];
								spanArr1[6] = fp.getArrVal7();
								if(fp.getArrValSign7().compareTo("-")==0) spanArr1[6] = "-"+spanArr1[6];
								spanArr1[7] = fp.getArrVal8();
								if(fp.getArrValSign8().compareTo("-")==0) spanArr1[7] = "-"+spanArr1[7];
								spanArr1[8] = fp.getArrVal9();
								if(fp.getArrValSign9().compareTo("-")==0) spanArr1[8] = "-"+spanArr1[8];
								RiskArrayDoc riskArray1Doc = new RiskArrayDoc(fp.getExchAcro().trim(),
										fp.getCommProdCode().trim(), fp.getProdTypeCode().trim(), fp.getUndCommProdCode().trim(), 
										fp.getOptRightCode(), fp.getFutConMonth(), fp.getFutConDayWeekCode(), 
										fp.getOptConMonth(), fp.getOptConDayWeekCode(), fp.getOptStrikePrice(), 
										fp.getHighPrecSettlePrice(), null, null, spanArr1);
								riskArrayDocMap.put(key1, riskArray1Doc);
							}
//						Utils.prt(fp.toString());
							break;
						case SECOND_PHYS_REC:
							SecondPhysical sp = new SecondPhysical(line);
							String key2 = getKey(sp);
							if(!riskArrayDocMap.containsKey(key2)) {
								Utils.prtErr("Why not entry for dis: "+key2);
							} else {
								RiskArrayDoc prevRiskArrayDoc = (RiskArrayDoc) riskArrayDocMap.get(key2);
								String[] spanArr2 = prevRiskArrayDoc.getSpanArray();
								spanArr2[9] = sp.getArrVal10();
								if(sp.getArrValSign10().compareTo("-")==0) spanArr2[9] = "-"+spanArr2[9];
								spanArr2[10] = sp.getArrVal11();
								if(sp.getArrValSign11().compareTo("-")==0) spanArr2[10] = "-"+spanArr2[10];
								spanArr2[11] = sp.getArrVal12();
								if(sp.getArrValSign12().compareTo("-")==0) spanArr2[11] = "-"+spanArr2[11];
								spanArr2[12] = sp.getArrVal13();
								if(sp.getArrValSign13().compareTo("-")==0) spanArr2[12] = "-"+spanArr2[12];
								spanArr2[13] = sp.getArrVal14();
								if(sp.getArrValSign14().compareTo("-")==0) spanArr2[13] = "-"+spanArr2[13];
								spanArr2[14] = sp.getArrVal15();
								if(sp.getArrValSign15().compareTo("-")==0) spanArr2[14] = "-"+spanArr2[14];
								spanArr2[15] = sp.getArrVal16();
								if(sp.getArrValSign16().compareTo("-")==0) spanArr2[15] = "-"+spanArr2[15];
								String delta = sp.getSpanCompDelta().substring(0,1)+"."+sp.getSpanCompDelta().substring(1);
								if(sp.getSpanCompDeltaSign().compareTo("-")==0) delta = "-"+delta;
								String settleStr = prevRiskArrayDoc.getSettle();
								if(settleStr==null) settleStr = (sp.getSettlePriceSign().compareTo("-")==0) ? 
										"-"+ sp.getSettlePrice() : sp.getSettlePrice();
								String impVolString = sp.getImpVolAsFrac();
								if(impVolString.trim().length()>0) {
									impVolString = new BigDecimal(impVolString.substring(0,2)+"."+impVolString.substring(2)).toString();
								}
								
								String optStrike = sp.getOptStrikePrice();
								if(optStrike!=null&&sp.getStrikePriceSign()!=null&&sp.getStrikePriceSign().compareTo("-")==0) {
									optStrike = "-"+optStrike;
								}
								
								RiskArrayDoc newRiskArrayDoc = new RiskArrayDoc(prevRiskArrayDoc.getContractId().getProdId().getExchAcro().trim(),
										sp.getCommProdCode().trim(), sp.getProdTypeCode().trim(), sp.getUndCommProdCode().trim(), sp.getOptRightCode(), 
										sp.getFutConMonth(), sp.getFutConDayWeekCode(), sp.getOptConMonth(), sp.getOptConDayWeekCode(), 
										optStrike, settleStr, delta, impVolString, spanArr2);
								riskArrayDocMap.put(key2, newRiskArrayDoc);
							}
							if(riskArrayDocMap.size()==batchSize) {
								SpanMongoUtils.batchInsert(spanArrayColl, new ArrayList<MongoDoc>(riskArrayDocMap.values()));
								riskArrayDocMap.clear();
							}
//						Utils.prt(sp.toString());
							break;
						case FIRST_PHYS_REC_FLOAT:
//						FirstPhysicalFloat fpf = new FirstPhysicalFloat(line);
							Utils.prtErr("AHH first phys rec float");
//						Utils.prt(fpf.toString());
							break;
						case SECOND_PHYS_REC_FLOAT:
//						SecondPhysicalFloat spf = new SecondPhysicalFloat(line);
							Utils.prtErr("AHH sec phys rec float");
//						Utils.prt(spf.toString());
							break;
						default:
							Utils.prtErr("MAKE METHOD TO HANDLE THIS ENUM: "+recId);
							break;
					}// end switch here
				} catch (Exception e) {
					Utils.prtObErrMess(this.getClass(), e.getMessage());
					Utils.prtObErrMess(this.getClass(), line);
				}
				
			} else {
				Utils.prtErr("Not able to parse enum from: "+recIdString);
			}
			
			lineNum++;
		}

		SpanMongoUtils.batchInsert(currConvColl, currConvDocList, batchSize);
		
		SpanMongoUtils.batchInsert(exchColl, exchDocList, batchSize);

		SpanMongoUtils.batchInsert(spanArrayColl, new ArrayList<MongoDoc>(riskArrayDocMap.values()), batchSize);

		SpanMongoUtils.batchInsert(riskDataColl, riskDataDocList);

		SpanMongoUtils.batchInsert(commGrpColl, new ArrayList<MongoDoc>(combGroupMap.values()));

		SpanMongoUtils.batchInsert(priceSpecColl, priceSpecList);
		
		SpanMongoUtils.batchInsert(firstCombCommColl, new ArrayList<MongoDoc>(firstCombCollMap.values()));

		SpanMongoUtils.batchInsert(combCommCodeColl, new ArrayList<MongoDoc>(combCommCodeMap.values()));

		SpanMongoUtils.batchInsert(interMonthSpreadColl, new ArrayList<MongoDoc>(interMonthSpreadMap.values()));
		
		SpanMongoUtils.batchInsert(interCommSpreadColl, new ArrayList<MongoDoc>(interCommSpreadMap.values()));

		SpanMongoUtils.batchInsert(interCommTierColl, interCommTierList);

		SpanMongoUtils.batchInsert(delivChargeColl, delivChargeList);

		Utils.prt("*************************************************************************************");
		Utils.prt("Finished processing: "+lineNum+" lines, there are this many 81 records: "+count8s);
	}
	
	private String getKey(FirstPhysical rec) {
		return rec.getCommProdCode().trim()+rec.getProdTypeCode().trim()+rec.getUndCommProdCode().trim()+rec.getOptRightCode()+rec.getFutConMonth()+
				rec.getFutConDayWeekCode()+rec.getOptConMonth()+rec.getOptConDayWeekCode()+rec.getOptStrikePrice();
	}

	private String getKey(SecondPhysical rec) {
		return rec.getCommProdCode().trim()+rec.getProdTypeCode().trim()+rec.getUndCommProdCode().trim()+rec.getOptRightCode()+rec.getFutConMonth()+
				rec.getFutConDayWeekCode()+rec.getOptConMonth()+rec.getOptConDayWeekCode()+rec.getOptStrikePrice();
	}

}
