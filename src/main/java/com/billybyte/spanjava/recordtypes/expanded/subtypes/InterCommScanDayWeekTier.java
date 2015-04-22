package com.billybyte.spanjava.recordtypes.expanded.subtypes;

public class InterCommScanDayWeekTier {

	private final String startDayWeekCode; // = line.substring(dayWeekTierRefIndex,dayWeekTierRefIndex+2);
	private final String endDayWeekCode; // = line.substring(dayWeekTierRefIndex+2,dayWeekTierRefIndex+4);
	
	public InterCommScanDayWeekTier(String startDayWeekCode, String endDayWeekCode) {
		super();
		this.startDayWeekCode = startDayWeekCode;
		this.endDayWeekCode = endDayWeekCode;
	}

	public String getStartDayWeekCode() {
		return startDayWeekCode;
	}

	public String getEndDayWeekCode() {
		return endDayWeekCode;
	}
	
	public String toString() {
		return startDayWeekCode+","+endDayWeekCode;
	}
}
