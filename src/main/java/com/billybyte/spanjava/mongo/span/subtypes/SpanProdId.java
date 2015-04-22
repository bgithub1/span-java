package com.billybyte.spanjava.mongo.span.subtypes;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SpanProdId implements MongoDoc {

	private final String exchAcro;
	private final String prodCommCode;
	private final String prodTypeCode;
	
	public SpanProdId(String exchAcro, String prodCommCode, String prodTypeCode) {
		super();
		this.exchAcro = exchAcro.trim();
		this.prodCommCode = prodCommCode.trim();
		this.prodTypeCode = prodTypeCode.trim();
	}

	public String getExchAcro() {
		return exchAcro;
	}

	public String getProdCommCode() {
		return prodCommCode;
	}

	public String getProdTypeCode() {
		return prodTypeCode;
	}
	
	public String toString() {
		return exchAcro+","+prodCommCode+","+prodTypeCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((exchAcro == null) ? 0 : exchAcro.hashCode());
		result = prime * result
				+ ((prodCommCode == null) ? 0 : prodCommCode.hashCode());
		result = prime * result
				+ ((prodTypeCode == null) ? 0 : prodTypeCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpanProdId other = (SpanProdId) obj;
		if (exchAcro == null) {
			if (other.exchAcro != null)
				return false;
		} else if (!exchAcro.equals(other.exchAcro))
			return false;
		if (prodCommCode == null) {
			if (other.prodCommCode != null)
				return false;
		} else if (!prodCommCode.equals(other.prodCommCode))
			return false;
		if (prodTypeCode == null) {
			if (other.prodTypeCode != null)
				return false;
		} else if (!prodTypeCode.equals(other.prodTypeCode))
			return false;
		return true;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("exchAcro", exchAcro);
		doc.put("prodCommCode", prodCommCode);
		doc.put("prodTypeCode", prodTypeCode);
		return doc;
	}
	
}
