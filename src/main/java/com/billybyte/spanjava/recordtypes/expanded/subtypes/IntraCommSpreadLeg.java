package com.billybyte.spanjava.recordtypes.expanded.subtypes;

public class IntraCommSpreadLeg {

	private final Integer legNumber; // = line.substring(28,30);
	private final Integer tierNumber; // = line.substring(30,32);
	private final Integer deltaPerSpreadRatio; // = line.substring(32,34);
	private final String mrktSide; // = line.substring(34,35);
	
	public IntraCommSpreadLeg(Integer legNumber, Integer tierNumber,
			Integer deltaPerSpreadRatio, String mrktSide) {
		super();
		this.legNumber = legNumber;
		this.tierNumber = tierNumber;
		this.deltaPerSpreadRatio = deltaPerSpreadRatio;
		this.mrktSide = mrktSide;
	}

	public Integer getLegNumber() {
		return legNumber;
	}

	public Integer getTierNumber() {
		return tierNumber;
	}

	public Integer getDeltaPerSpreadRatio() {
		return deltaPerSpreadRatio;
	}

	public String getMrktSide() {
		return mrktSide;
	}
	
	public String toString() {
		return legNumber+","+tierNumber+","+deltaPerSpreadRatio+","+mrktSide;
	}
	
}
