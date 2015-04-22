package com.billybyte.spanjava.recordtypes.expanded;

public class ThirdCombComm {

	private final String recordId; // "4 "
	private final String combCommCode; // = line.substring(2,8);
	private final String delivChargeMethodCode; // = line.substring(8,10);
	private final String numOfConMonths; // = line.substring(10,12);
	private final String monthNum1; // = line.substring(12,14);
	private final String conMonth1; // = line.substring(14,20);
	private final String chargeRatePerDeltaBySpread1; // = line.substring(20,27);
	private final String chargeRatePerDeltaByOutright1; // = line.substring(27,34);
	private final String monthNum2; // = line.substring(34,36);
	private final String conMonth2; // = line.substring(36,42);
	private final String chargeRatePerDeltaBySpread2; // = line.substring(42,49);
	private final String chargeRatePerDeltaByOutright2; // = line.substring(49,56);
	private final String spotCommCode; // = line.substring(10,20);
	private final String basisRiskChargeRate; // = line.substring(20,27);
	private final String shortOptMinChargeRate; // = line.substring(62,69);
	private final String riskMainBondAdjFactMember; // = line.substring(69,72);
	private final String riskMainBondAdjFactHedger; // = line.substring(72,75);
	private final String riskMainBondAdjFactSpec; // = line.substring(75,78);
	private final String shortOptMinCalcMethod; // = line.substring(78,79);
	
	public ThirdCombComm(String line) {
		
		this.recordId = line.substring(0,2);
		this.combCommCode = line.substring(2,8);
		this.delivChargeMethodCode = line.substring(8,10);
				
		Integer delivChargeMethodCodeInt = Integer.parseInt(delivChargeMethodCode);
		if(delivChargeMethodCodeInt==10) {
			this.numOfConMonths = line.substring(10,12);
			this.monthNum1 = line.substring(12,14);
			this.conMonth1 = line.substring(14,20);
			this.chargeRatePerDeltaBySpread1 = line.substring(20,27);
			this.chargeRatePerDeltaByOutright1 = line.substring(27,34);
			this.monthNum2 = line.substring(34,36);
			this.conMonth2 = line.substring(36,42);
			this.chargeRatePerDeltaBySpread2 = line.substring(42,49);
			this.chargeRatePerDeltaByOutright2 = line.substring(49,56);
			this.spotCommCode = null; 
			this.basisRiskChargeRate = null;
			
		} else if(delivChargeMethodCodeInt==11) {
			this.spotCommCode = line.substring(10,20);
			this.basisRiskChargeRate = line.substring(20,27);
			this.numOfConMonths = null;
			this.monthNum1 = null;
			this.conMonth1 = null;
			this.chargeRatePerDeltaBySpread1 = null;
			this.chargeRatePerDeltaByOutright1 = null;
			this.monthNum2 = null;
			this.conMonth2 = null;
			this.chargeRatePerDeltaBySpread2 = null;
			this.chargeRatePerDeltaByOutright2 = null;
		} else {
			this.spotCommCode = null;
			this.basisRiskChargeRate = null;
			this.numOfConMonths = null;
			this.monthNum1 = null;
			this.conMonth1 = null;
			this.chargeRatePerDeltaBySpread1 = null;
			this.chargeRatePerDeltaByOutright1 = null;
			this.monthNum2 = null;
			this.conMonth2 = null;
			this.chargeRatePerDeltaBySpread2 = null;
			this.chargeRatePerDeltaByOutright2 = null;
		}
		
		this.shortOptMinChargeRate = line.substring(62,69);
		this.riskMainBondAdjFactMember = line.substring(69,72);
		this.riskMainBondAdjFactHedger = line.substring(72,75);
		this.riskMainBondAdjFactSpec = line.substring(75,78);
		this.shortOptMinCalcMethod = line.substring(78,79);
	}
	
	public String toString() {
		
		String ret = recordId+","+combCommCode+","+delivChargeMethodCode;
		Integer delivChargeMethodCodeInt = Integer.parseInt(delivChargeMethodCode);
		if(delivChargeMethodCodeInt==10) {
			ret = ret+","+numOfConMonths+","+
					monthNum1+","+conMonth1+","+chargeRatePerDeltaBySpread1+","+chargeRatePerDeltaByOutright1+","+
					monthNum2+","+conMonth2+","+chargeRatePerDeltaBySpread2+","+chargeRatePerDeltaByOutright2;
		} else if(delivChargeMethodCodeInt==11) {
			ret = ret+","+spotCommCode+","+basisRiskChargeRate;
		} 

		ret = ret+","+shortOptMinChargeRate+","+riskMainBondAdjFactMember+","+riskMainBondAdjFactHedger+","+
				riskMainBondAdjFactSpec+","+shortOptMinCalcMethod;

		return ret;
		
	}

	public String getRecordId() {
		return recordId;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public String getDelivChargeMethodCode() {
		return delivChargeMethodCode;
	}

	public String getNumOfConMonths() {
		return numOfConMonths;
	}

	public String getMonthNum1() {
		return monthNum1;
	}

	public String getConMonth1() {
		return conMonth1;
	}

	public String getChargeRatePerDeltaBySpread1() {
		return chargeRatePerDeltaBySpread1;
	}

	public String getChargeRatePerDeltaByOutright1() {
		return chargeRatePerDeltaByOutright1;
	}

	public String getMonthNum2() {
		return monthNum2;
	}

	public String getConMonth2() {
		return conMonth2;
	}

	public String getChargeRatePerDeltaBySpread2() {
		return chargeRatePerDeltaBySpread2;
	}

	public String getChargeRatePerDeltaByOutright2() {
		return chargeRatePerDeltaByOutright2;
	}

	public String getSpotCommCode() {
		return spotCommCode;
	}

	public String getBasisRiskChargeRate() {
		return basisRiskChargeRate;
	}

	public String getShortOptMinChargeRate() {
		return shortOptMinChargeRate;
	}

	public String getRiskMainBondAdjFactMember() {
		return riskMainBondAdjFactMember;
	}

	public String getRiskMainBondAdjFactHedger() {
		return riskMainBondAdjFactHedger;
	}

	public String getRiskMainBondAdjFactSpec() {
		return riskMainBondAdjFactSpec;
	}

	public String getShortOptMinCalcMethod() {
		return shortOptMinCalcMethod;
	}
	
}
