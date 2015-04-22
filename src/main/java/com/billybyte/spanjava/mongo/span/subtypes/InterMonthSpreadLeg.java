package com.billybyte.spanjava.mongo.span.subtypes;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class InterMonthSpreadLeg implements MongoDoc {

	private final Integer legNumber;
	private final Integer tierNumber;
	private final Integer deltaPerSpreadRatio;
	private final String marketSide;
	
	public InterMonthSpreadLeg(Integer legNumber, Integer tierNumber,
			Integer deltaPerSpreadRatio, String marketSide) {
		super();
		this.legNumber = legNumber;
		this.tierNumber = tierNumber;
		this.deltaPerSpreadRatio = deltaPerSpreadRatio;
		this.marketSide = marketSide;
	}

	public Integer getLegNumber() {
		return legNumber;
	}

	public Integer getTierNumber() {
		return tierNumber;
	}

	public Integer getDeltaPerSpreadRatio() {
		return deltaPerSpreadRatio;
	}

	public String getMarketSide() {
		return marketSide;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("legNumber", legNumber);
		doc.put("tierNumber", tierNumber);
		doc.put("deltaPerSpreadRatio", deltaPerSpreadRatio);
		doc.put("marketSide", marketSide);
		return doc;
	}

}
