package com.billybyte.spanjava.mongo.span;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CombCommProdIdDoc implements MongoDoc {

	private final String combCommCode;
	private final Set<SpanProdId> prodIdSet;
	
	public CombCommProdIdDoc(String combCommCode, Set<SpanProdId> prodIdSet) {
		super();
		this.combCommCode = combCommCode;
		this.prodIdSet = prodIdSet;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public Set<SpanProdId> getProdIdSet() {
		return prodIdSet;
	}
	
	public String toString() {
		return this.getDBObject().toString();
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("combCommCode", combCommCode);
		List<DBObject> prodCodeDocSet = new ArrayList<DBObject>();
		for(SpanProdId id:prodIdSet) {
			prodCodeDocSet.add(id.getDBObject());
		}
		doc.put("prodIdSet", prodCodeDocSet);
		return doc;
	}
	
	
	
}
