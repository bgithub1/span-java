package com.billybyte.spanjava.recordtypes.expanded.subtypes;

public class InterCommSpreadLegBasic {

	private final String exchAcro;
	private final String reqForScanFlag;
	private final String combCommCode;
	private final String deltaSpreadRatio;
	private final String spreadSide;
	
	public InterCommSpreadLegBasic(String exchAcro, String reqForScanFlag,
			String combCommCode, String deltaSpreadRatio, String spreadSide) {
		super();
		this.exchAcro = exchAcro;
		this.reqForScanFlag = reqForScanFlag;
		this.combCommCode = combCommCode;
		this.deltaSpreadRatio = deltaSpreadRatio;
		this.spreadSide = spreadSide;
	}

	public String getExchAcro() {
		return exchAcro;
	}

	public String getReqForScanFlag() {
		return reqForScanFlag;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public String getDeltaSpreadRatio() {
		return deltaSpreadRatio;
	}

	public String getSpreadSide() {
		return spreadSide;
	}

}
