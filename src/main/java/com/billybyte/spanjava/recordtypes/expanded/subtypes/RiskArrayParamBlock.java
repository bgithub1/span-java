package com.billybyte.spanjava.recordtypes.expanded.subtypes;

public class RiskArrayParamBlock {

	private final String commProdCode; //1 = line.substring(22,32);
	private final String prodType; //1 = line.substring(32,35);
	private final String riskArrValDecLoc; //1 = line.substring(35,36);
	private final String riskArrValDecSign; //1 = line.substring(36,37);
	
	public RiskArrayParamBlock(String commProdCode, String prodType,
			String riskArrValDecLoc, String riskArrValDecSign) {
		super();
		this.commProdCode = commProdCode;
		this.prodType = prodType;
		this.riskArrValDecLoc = riskArrValDecLoc;
		this.riskArrValDecSign = riskArrValDecSign;
	}

	public String getCommProdCode() {
		return commProdCode;
	}

	public String getProdType() {
		return prodType;
	}

	public String getRiskArrValDecLoc() {
		return riskArrValDecLoc;
	}

	public String getRiskArrValDecSign() {
		return riskArrValDecSign;
	}
	
	public String toString() {
		return commProdCode+","+prodType+","+riskArrValDecLoc+","+riskArrValDecSign;
	}
	
}
