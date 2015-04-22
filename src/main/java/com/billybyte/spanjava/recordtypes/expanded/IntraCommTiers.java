package com.billybyte.spanjava.recordtypes.expanded;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.spanjava.recordtypes.expanded.subtypes.IntraCommSpreadLeg;

public class IntraCommTiers {

	private final String recordId; // "C "
	private final String combCommCode; // = line.substring(2,8);
	private final String intraCommSpreadMethodCode; // = line.substring(8,10);
	private final String spreadPriority; // = line.substring(10,12);
	private final String numOfLegs; // = line.substring(12,14);
	private final String chargeRate; // = line.substring(14,21);
	private final List<IntraCommSpreadLeg> intraCommSpreadLegList;
	
	public IntraCommTiers(String line) {
		
		this.recordId = line.substring(0,2);
		this.combCommCode = line.substring(2,8);
		this.intraCommSpreadMethodCode = line.substring(8,10);
		this.spreadPriority = line.substring(10,12);
		this.numOfLegs = line.substring(12,14);
		this.chargeRate = line.substring(14,21);
		this.intraCommSpreadLegList = new ArrayList<IntraCommSpreadLeg>();
		
		int refIndex = 21;
		
		while(line.length()>=(refIndex+7)) {
			Integer legNumber = Integer.parseInt(line.substring(refIndex,refIndex+2));
			Integer tierNumber = Integer.parseInt(line.substring(refIndex+2,refIndex+4));
			Integer deltaPerSpreadRatio = Integer.parseInt(line.substring(refIndex+4,refIndex+6));
			String mrktSide = line.substring(refIndex+6,refIndex+7);
			IntraCommSpreadLeg intraCommSpreadLeg = new IntraCommSpreadLeg(legNumber, tierNumber, deltaPerSpreadRatio, mrktSide);
			intraCommSpreadLegList.add(intraCommSpreadLeg);
			refIndex = refIndex+7;
			
		}
		
//		String legNumber1 = line.substring(21,23);
//		String tierNumber1 = line.substring(23,25);
//		String deltaPerSpreadRatio1 = line.substring(25,27);
//		String mrktSide1 = line.substring(27,28);
//		if(line.length()>35) {
//			String legNumber2 = line.substring(28,30);
//			String tierNumber2 = line.substring(30,32);
//			String deltaPerSpreadRatio2 = line.substring(32,34);
//			String mrktSide2 = line.substring(34,35);
//			prtLine = prtLine+legNumber2+","+tierNumber2+","+deltaPerSpreadRatio2+","+mrktSide2;

	}
	
	public String toString() {
		String ret = recordId+","+combCommCode+","+intraCommSpreadMethodCode+","+spreadPriority+","+numOfLegs+","+chargeRate;
		for(IntraCommSpreadLeg leg:intraCommSpreadLegList) {
			ret = ret+","+leg.toString();
		}
		return ret;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public String getIntraCommSpreadMethodCode() {
		return intraCommSpreadMethodCode;
	}

	public String getSpreadPriority() {
		return spreadPriority;
	}

	public String getNumOfLegs() {
		return numOfLegs;
	}

	public String getChargeRate() {
		return chargeRate;
	}

	public List<IntraCommSpreadLeg> getIntraCommSpreadLegList() {
		return intraCommSpreadLegList;
	}
	
}
