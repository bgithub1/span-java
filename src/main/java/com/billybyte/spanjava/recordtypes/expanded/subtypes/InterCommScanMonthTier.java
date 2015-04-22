package com.billybyte.spanjava.recordtypes.expanded.subtypes;

public class InterCommScanMonthTier {

	private final String tierNumber; // = line.substring(12,14);
	private final String startMonth; // = line.substring(14,20);
	private final String endMonth; // = line.substring(20,26);
	
	public InterCommScanMonthTier(String tierNumber, String startMonth, String endMonth) {
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
