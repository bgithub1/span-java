package com.billybyte.spanjava.recordtypes.expanded.subtypes;

public class IntraCommSeriesLeg {

	private final String legMonth; // = line.substring(20,24);
	private final String legRemainder; // = line.substring(24,27);
	private final String legDeltaPerSpreadRatio; // = line.substring(27,33);
	private final String legMrktSide; // = line.substring(33,34);
	
	public IntraCommSeriesLeg(String legMonth, String legRemainder,	String legDeltaPerSpreadRatio, String legMrktSide) {
		super();
		this.legMonth = legMonth;
		this.legRemainder = legRemainder;
		this.legDeltaPerSpreadRatio = legDeltaPerSpreadRatio;
		this.legMrktSide = legMrktSide;
	}

	public String getLegMonth() {
		return legMonth;
	}

	public String getLegRemainder() {
		return legRemainder;
	}

	public String getLegDeltaPerSpreadRatio() {
		return legDeltaPerSpreadRatio;
	}

	public String getLegMrktSide() {
		return legMrktSide;
	}
	
	public String toString() {
		return legMonth+","+legRemainder+","+legDeltaPerSpreadRatio+","+legMrktSide;
	}
	
}
