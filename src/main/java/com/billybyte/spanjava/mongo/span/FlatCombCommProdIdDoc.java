package com.billybyte.spanjava.mongo.span;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class FlatCombCommProdIdDoc implements MongoDoc {

	private final SpanProdId prodId;
	private final String combCommCode;
	
	public FlatCombCommProdIdDoc(SpanProdId prodId, String combCommCode) {
		super();
		this.prodId = prodId;
		this.combCommCode = combCommCode;
	}

	public SpanProdId getProdId() {
		return prodId;
	}
	
	public String getCombCommCode() {
		return combCommCode;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("prodId", prodId.getDBObject());
		doc.put("combCommCode", combCommCode);
		return doc;
	}
	
	
	
}
