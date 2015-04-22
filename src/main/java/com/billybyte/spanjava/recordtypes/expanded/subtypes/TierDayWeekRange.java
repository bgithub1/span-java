package com.billybyte.spanjava.recordtypes.expanded.subtypes;

public class TierDayWeekRange {

	private final String startDayWeekCode; // = line.substring(80,82);
	private final String endDayWeekCode; // = line.substring(82,84);
	
	public TierDayWeekRange(String startDayWeekCode, String endDayWeekCode) {
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
	
}


