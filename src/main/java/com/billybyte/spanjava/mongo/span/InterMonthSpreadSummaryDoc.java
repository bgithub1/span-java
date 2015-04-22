package com.billybyte.spanjava.mongo.span;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.span.subtypes.InterMonthSpreadDetail;
import com.billybyte.spanjava.mongo.span.subtypes.TierRange;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class InterMonthSpreadSummaryDoc implements MongoDoc {

	private final String combCommCode;
	private final String intraCommSpreadMethodCode;
	private final String memberMaintRatio;
	private final String hedgerMaintRatio;
	private final String specMaintRatio;
	private final List<TierRange> tierRangeList;
	private final List<InterMonthSpreadDetail> interMonthSpreadDetailList;
	
	public InterMonthSpreadSummaryDoc(String combCommCode,
			String intraCommSpreadMethodCode, String memberMaintRatio, String hedgerMaintRatio, String specMaintRatio, 
			List<TierRange> tierRangeList, List<InterMonthSpreadDetail> interMonthSpreadDetailList) {
		super();
		this.combCommCode = combCommCode;
		this.intraCommSpreadMethodCode = intraCommSpreadMethodCode;
		this.memberMaintRatio = memberMaintRatio;
		this.hedgerMaintRatio = hedgerMaintRatio;
		this.specMaintRatio = specMaintRatio;
		this.tierRangeList = tierRangeList;
		this.interMonthSpreadDetailList = interMonthSpreadDetailList;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public String getIntraCommSpreadMethodCode() {
		return intraCommSpreadMethodCode;
	}
	
	public String getMemberMaintRatio() {
		return memberMaintRatio;
	}

	public String getHedgerMaintRatio() {
		return hedgerMaintRatio;
	}

	public String getSpecMaintRatio() {
		return specMaintRatio;
	}

	public List<TierRange> getTierRangeList() {
		return tierRangeList;
	}

	public List<InterMonthSpreadDetail> getInterMonthSpreadDetailList() {
		return interMonthSpreadDetailList;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("combCommCode", combCommCode);
		doc.put("intraCommSpreadMethodCode", intraCommSpreadMethodCode);
		doc.put("memberMaintRatio", memberMaintRatio);
		doc.put("hedgerMaintRatio", hedgerMaintRatio);
		doc.put("specMaintRatio", specMaintRatio);
		List<DBObject> tierList = new ArrayList<DBObject>();
		for(TierRange range:tierRangeList) {
			tierList.add(range.getDBObject());
		}
		doc.put("tierRangeList",tierList);
		List<DBObject> interMonthDetailList = new ArrayList<DBObject>();
		for(InterMonthSpreadDetail detail:interMonthSpreadDetailList) {
			interMonthDetailList.add(detail.getDBObject());
		}
		doc.put("interMonthSpreadDetailList", interMonthDetailList);
		return doc;
	}

}
