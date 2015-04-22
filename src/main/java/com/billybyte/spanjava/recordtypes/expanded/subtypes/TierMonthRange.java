package com.billybyte.spanjava.recordtypes.expanded.subtypes;

public class TierMonthRange {

	private final String tierNumber; // = line.substring(10,12);
	private final String startMonth; // = line.substring(12,18);
	private final String endMonth; // = line.substring(18,24);

	public TierMonthRange(String tierNumber, String startMonth, String endMonth) {
		super();
		this.tierNumber = tierNumber;
		this.startMonth = startMonth;
		this.endMonth = endMonth;
	}

	public String getTierNumber() {
		return tierNumber;
	}

	public String getStartMonth() {
		return startMonth;
	}

	public String getEndMonth() {
		return endMonth;
	}
	
	public String toString() {
		return tierNumber+","+startMonth+","+endMonth;
	}
	
}
