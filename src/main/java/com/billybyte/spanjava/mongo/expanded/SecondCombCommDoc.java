package com.billybyte.spanjava.mongo.expanded;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.expanded.subdocs.TierDayWeekRangeDoc;
import com.billybyte.spanjava.mongo.expanded.subdocs.TierMonthRangeDoc;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.TierDayWeekRange;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.TierMonthRange;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SecondCombCommDoc implements MongoDoc {

	private final String combCommCode;
	private final String intraCommSpreadChargeMethCode;
	private final List<TierMonthRangeDoc> tierMonthRangeList;
	private final String initToMaintRatioMember;
	private final String initToMaintRatioHedger;
	private final String initToMaintRatioSpec;
	private final List<TierDayWeekRangeDoc> tierDayWeekRangeList;

	public SecondCombCommDoc(String combCommCode,
			String intraCommSpreadChargeMethCode,
			List<TierMonthRange> tierMonthRangeList,
			String initToMaintRatioMember, String initToMaintRatioHedger,
			String initToMaintRatioSpec,
			List<TierDayWeekRange> tierDayWeekRangeList) {
		super();
		this.combCommCode = combCommCode;
		this.intraCommSpreadChargeMethCode = intraCommSpreadChargeMethCode;
		this.tierMonthRangeList = new ArrayList<TierMonthRangeDoc>();
		for(TierMonthRange rec:tierMonthRangeList) {
			if(rec.getTierNumber().trim().length()>0) {
				this.tierMonthRangeList.add(new TierMonthRangeDoc(rec));
			}
		}
		this.initToMaintRatioMember = initToMaintRatioMember;
		this.initToMaintRatioHedger = initToMaintRatioHedger;
		this.initToMaintRatioSpec = initToMaintRatioSpec;
		this.tierDayWeekRangeList = new ArrayList<TierDayWeekRangeDoc>();
		for(TierDayWeekRange rec:tierDayWeekRangeList) {
			if(rec.getStartDayWeekCode().trim().length()>0) {
				this.tierDayWeekRangeList.add(new TierDayWeekRangeDoc(rec));
			}
		}
	}

	public SecondCombCommDoc(String combCommCode,
			String intraCommSpreadChargeMethCode,
			String initToMaintRatioMember, String initToMaintRatioHedger,
			String initToMaintRatioSpec,
			List<TierMonthRangeDoc> tierMonthRangeList,
			List<TierDayWeekRangeDoc> tierDayWeekRangeList) {
		super();
		this.combCommCode = combCommCode;
		this.intraCommSpreadChargeMethCode = intraCommSpreadChargeMethCode;
		this.tierMonthRangeList = tierMonthRangeList;
		this.initToMaintRatioMember = initToMaintRatioMember;
		this.initToMaintRatioHedger = initToMaintRatioHedger;
		this.initToMaintRatioSpec = initToMaintRatioSpec;
		this.tierDayWeekRangeList = tierDayWeekRangeList;
	}
	
	public String getCombCommCode() {
		return combCommCode;
	}

	public String getIntraCommSpreadChargeMethCode() {
		return intraCommSpreadChargeMethCode;
	}

	public List<TierMonthRangeDoc> getTierMonthRangeList() {
		return tierMonthRangeList;
	}

	public String getInitToMaintRatioMember() {
		return initToMaintRatioMember;
	}

	public String getInitToMaintRatioHedger() {
		return initToMaintRatioHedger;
	}

	public String getInitToMaintRatioSpec() {
		return initToMaintRatioSpec;
	}

	public List<TierDayWeekRangeDoc> getTierDayWeekRangeList() {
		return tierDayWeekRangeList;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("combCommCode", combCommCode);
		doc.put("intraCommSpreadChargeMethCode", intraCommSpreadChargeMethCode);
		doc.put("initToMaintRatioMember", initToMaintRatioMember);
		doc.put("initToMaintRatioHedger", initToMaintRatioHedger);
		doc.put("initToMaintRatioSpec", initToMaintRatioSpec);
		List<DBObject> monthRangeDocList = new ArrayList<DBObject>();
		for(TierMonthRangeDoc subDoc:this.tierMonthRangeList) {
			monthRangeDocList.add(subDoc.getDBObject());
		}
		doc.put("tierMonthRangeList", monthRangeDocList);
		List<DBObject> dayWeekRangeDocList = new ArrayList<DBObject>();
		for(TierDayWeekRangeDoc subDoc:this.tierDayWeekRangeList) {
			dayWeekRangeDocList.add(subDoc.getDBObject());
		}
		doc.put("tierDayWeekRangeList", dayWeekRangeDocList);
		return doc;
	}
	
}
