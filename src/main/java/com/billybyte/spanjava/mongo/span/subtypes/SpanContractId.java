package com.billybyte.spanjava.mongo.span.subtypes;

import com.billybyte.mongo.MongoDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SpanContractId implements MongoDoc {

	private final SpanProdId prodId;
	private final String futMonth;
	private final String futDayWeek;
	private final String optMonth;
	private final String optDayWeek;
	private final String optRightCode;
	private final String strike;
	
	public SpanContractId(String exchAcro, String prodCode, String prodType,
			String futMonth, String futDayWeek, String optMonth, String optDayWeek, String optRightCode, String strike) {
		super();
		this.prodId = new SpanProdId(exchAcro.trim(), prodCode.trim(), prodType.trim());
		this.futMonth = futMonth;
		this.futDayWeek = futDayWeek;
		this.optMonth = optMonth;
		this.optDayWeek = optDayWeek;
		this.optRightCode = optRightCode;
		this.strike = strike;
	}
	
	public SpanProdId getProdId() {
		return prodId;
	}
	
	public String getFutMonth() {
		return futMonth;
	}
	
	public String getFutDayWeek() {
		return futDayWeek;
	}

	public String getOptMonth() {
		return optMonth;
	}
	
	public String getOptDayWeek() {
		return optDayWeek;
	}

	public String getOptRightCode() {
		return optRightCode;
	}
	
	public String getStrike() {
		return strike;
	}

	public String toString() {
//		String ret = prodId.getExchAcro()+","+prodId.getProdCommCode()+","+prodId.getProdTypeCode()+","+futMonth+","+futDayWeek;
//		if(optRightCode!=null) ret = ret+","+optMonth+","+optDayWeek+","+optRightCode+","+strike;
//		return ret;
		return this.getDBObject().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((futDayWeek == null) ? 0 : futDayWeek.hashCode());
		result = prime * result
				+ ((futMonth == null) ? 0 : futMonth.hashCode());
		result = prime * result
				+ ((optDayWeek == null) ? 0 : optDayWeek.hashCode());
		result = prime * result
				+ ((optMonth == null) ? 0 : optMonth.hashCode());
		result = prime * result
				+ ((optRightCode == null) ? 0 : optRightCode.hashCode());
		result = prime * result + ((prodId == null) ? 0 : prodId.hashCode());
		result = prime * result + ((strike == null) ? 0 : strike.hashCode());
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
		SpanContractId other = (SpanContractId) obj;
		if (futDayWeek == null) {
			if (other.futDayWeek != null)
				return false;
		} else if (!futDayWeek.equals(other.futDayWeek))
			return false;
		if (futMonth == null) {
			if (other.futMonth != null)
				return false;
		} else if (!futMonth.equals(other.futMonth))
			return false;
		if (optDayWeek == null) {
			if (other.optDayWeek != null)
				return false;
		} else if (!optDayWeek.equals(other.optDayWeek))
			return false;
		if (optMonth == null) {
			if (other.optMonth != null)
				return false;
		} else if (!optMonth.equals(other.optMonth))
			return false;
		if (optRightCode == null) {
			if (other.optRightCode != null)
				return false;
		} else if (!optRightCode.equals(other.optRightCode))
			return false;
		if (prodId == null) {
			if (other.prodId != null)
				return false;
		} else if (!prodId.equals(other.prodId))
			return false;
		if (strike == null) {
			if (other.strike != null)
				return false;
		} else if (!strike.equals(other.strike))
			return false;
		return true;
	}

	@Override
	public DBObject getDBObject() {
		
		DBObject doc = new BasicDBObject();
		doc.put("prodId", prodId.getDBObject());
		doc.put("futMonth", futMonth);
		doc.put("futDayWeek", futDayWeek);
		doc.put("optMonth", optMonth);
		doc.put("optDayWeek", optDayWeek);
		doc.put("optRightCode", optRightCode);
		doc.put("strike", strike);
		return doc;
	}
	
}
