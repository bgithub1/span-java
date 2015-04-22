package com.billybyte.spanjava.recordtypes.expanded;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.spanjava.recordtypes.expanded.subtypes.RiskArrayParamBlock;

public class FirstCombComm {

	private final String recordId; // "2 "
	private final String exchAcro; // = line.substring(2,5);
	private final String combCommCode; // = line.substring(6,12);
	private final String riskExp; // = line.substring(12,13);
	private final String perfBondCurrISO; // = line.substring(13,16);
	private final String perfBondCurrCode; // = line.substring(16,17);
	private final String optMargStyle; // = line.substring(17,18);
	private final String limitOptValue; // = line.substring(18,19);
	private final String combMargMethFlag; // = line.substring(19,20);
	private final List<RiskArrayParamBlock> riskArrayParamBlockList;
	
	public FirstCombComm(String line){
		
		this.recordId = line.substring(0,2);
		this.exchAcro = line.substring(2,5);
		this.combCommCode = line.substring(6,12);
		this.riskExp = line.substring(12,13);
		this.perfBondCurrISO = line.substring(13,16);
		this.perfBondCurrCode = line.substring(16,17);
		this.optMargStyle = line.substring(17,18);
		this.limitOptValue = line.substring(18,19);
		this.combMargMethFlag = line.substring(19,20);
		this.riskArrayParamBlockList = new ArrayList<RiskArrayParamBlock>();
		
		int refIndex = 22;
		
		while(line.length()>=(refIndex+15)) {
			String commProdCode = line.substring(refIndex,refIndex+10);
			String conType = line.substring(refIndex+10,refIndex+13);
			String riskArrValDecLoc = line.substring(refIndex+13,refIndex+14);
			String riskArrValDecSign = line.substring(refIndex+14,refIndex+15);
			RiskArrayParamBlock riskArrayParamBlock = 
					new RiskArrayParamBlock(commProdCode, conType, riskArrValDecLoc, riskArrValDecSign);
			this.riskArrayParamBlockList.add(riskArrayParamBlock);
			refIndex = refIndex + 16;
		}
		
	}

	public String getRecordId() {
		return recordId;
	}

	public String getExchAcro() {
		return exchAcro;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public String getRiskExp() {
		return riskExp;
	}

	public String getPerfBondCurrISO() {
		return perfBondCurrISO;
	}

	public String getPerfBondCurrCode() {
		return perfBondCurrCode;
	}

	public String getOptMargStyle() {
		return optMargStyle;
	}

	public String getLimitOptValue() {
		return limitOptValue;
	}

	public String getCombMargMethFlag() {
		return combMargMethFlag;
	}

	public List<RiskArrayParamBlock> getRiskArrayParamBlockList() {
		return riskArrayParamBlockList;
	}
	
	public String toString() {
		
		String ret = recordId+","+exchAcro+","+combCommCode+","+riskExp+","+perfBondCurrISO+","+
				perfBondCurrCode+","+optMargStyle+","+limitOptValue+","+combMargMethFlag;
		for(RiskArrayParamBlock block:riskArrayParamBlockList) {
			ret = ret +","+block.toString();
		}
		
		return ret;

	}

}
