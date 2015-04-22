package com.billybyte.spanjava.recordtypes.expanded;

public class DeltaSplitAllocation {

	private final String recordId; // "Z "
	private final String exchAcro; // = line.substring(2,5);
	private final String combProdCommCode; // = line.substring(5,15);
	private final String combType; // = line.substring(15,20);
	private final String combConMonth; // = line.substring(20,26);
	private final String combConDayWeekCode; // = line.substring(26,28);
	private final String legNumber; // = line.substring(35,38);
	private final String legRelationship; // = line.substring(38,39);
	private final String legRatio; // = line.substring(39,42);
	private final String legProdCommCode; // = line.substring(42,52);
	private final String legProdType; // = line.substring(52,55);
	private final String legConMonth; // = line.substring(55,61);
	private final String legConDayWeekCode; // = line.substring(61,63);
	private final String legRatioFracPart; // = line.substring(63,67);
	private final String legPriceAvailFlag; // = line.substring(67,68);
	private final String legPriceUsageFlag; // = line.substring(68,70);
	private final String legPrice; // = line.substring(70,77);
	private final String legPriceSign; // = line.substring(77,78);
	
	public DeltaSplitAllocation(String line) {
		
		this.recordId = line.substring(0,2);
		this.exchAcro = line.substring(2,5);
		this.combProdCommCode = line.substring(5,15);
		this.combType = line.substring(15,20);
		this.combConMonth = line.substring(20,26);
		this.combConDayWeekCode = line.substring(26,28);
		this.legNumber = line.substring(35,38);
		this.legRelationship = line.substring(38,39);
		this.legRatio = line.substring(39,42);
		this.legProdCommCode = line.substring(42,52);
		this.legProdType = line.substring(52,55);
		this.legConMonth = line.substring(55,61);
		this.legConDayWeekCode = line.substring(61,63);
		this.legRatioFracPart = line.substring(63,67);
		this.legPriceAvailFlag = line.substring(67,68);
		this.legPriceUsageFlag = line.substring(68,70);
		this.legPrice = line.substring(70,77);
		this.legPriceSign = line.substring(77,78);
		
	}
	
	public String toString() {
		return recordId+","+exchAcro+","+combProdCommCode+","+combType+","+combConMonth+","+combConDayWeekCode+","+legNumber+","+
				legRelationship+","+legRatio+","+legProdCommCode+","+legProdType+","+legConMonth+","+legConDayWeekCode+","+
				legRatioFracPart+","+legPriceAvailFlag+","+legPriceUsageFlag+","+legPrice+","+legPriceSign;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getExchAcro() {
		return exchAcro;
	}

	public String getCombProdCommCode() {
		return combProdCommCode;
	}

	public String getCombType() {
		return combType;
	}

	public String getCombConMonth() {
		return combConMonth;
	}

	public String getCombConDayWeekCode() {
		return combConDayWeekCode;
	}

	public String getLegNumber() {
		return legNumber;
	}

	public String getLegRelationship() {
		return legRelationship;
	}

	public String getLegRatio() {
		return legRatio;
	}

	public String getLegProdCommCode() {
		return legProdCommCode;
	}

	public String getLegProdType() {
		return legProdType;
	}

	public String getLegConMonth() {
		return legConMonth;
	}

	public String getLegConDayWeekCode() {
		return legConDayWeekCode;
	}

	public String getLegRatioFracPart() {
		return legRatioFracPart;
	}

	public String getLegPriceAvailFlag() {
		return legPriceAvailFlag;
	}

	public String getLegPriceUsageFlag() {
		return legPriceUsageFlag;
	}

	public String getLegPrice() {
		return legPrice;
	}

	public String getLegPriceSign() {
		return legPriceSign;
	}
	
}
