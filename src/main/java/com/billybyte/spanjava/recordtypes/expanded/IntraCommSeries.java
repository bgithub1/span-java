package com.billybyte.spanjava.recordtypes.expanded;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.spanjava.recordtypes.expanded.subtypes.IntraCommSeriesLeg;

public class IntraCommSeries {

	private final String recordId; // "E "
	private final String combCommCode; // = line.substring(2,8);
	private final String spreadPriority; // = line.substring(8,13);
	private final String chargeRate; // = line.substring(13,20);
	private final List<IntraCommSeriesLeg> intraCommSeriesLegList;
	
	public IntraCommSeries(String line) {
		
		this.recordId = line.substring(0,2);
		this.combCommCode = line.substring(2,8);
		this.spreadPriority = line.substring(8,13);
		this.chargeRate = line.substring(13,20);
		this.intraCommSeriesLegList = new ArrayList<IntraCommSeriesLeg>();
		
		int refIndex = 20;
		
		while(line.length()>=(refIndex+14)) {
			String legMonth = line.substring(refIndex,refIndex+4);
			String legRemainder = line.substring(refIndex+4,refIndex+7);
			String legDeltaPerSpreadRatio = line.substring(refIndex+7,refIndex+13);
			String legMrktSide = line.substring(refIndex+13,refIndex+14);
			IntraCommSeriesLeg intraCommSeriesLeg = new IntraCommSeriesLeg(legMonth, legRemainder, legDeltaPerSpreadRatio, legMrktSide);
			intraCommSeriesLegList.add(intraCommSeriesLeg);
			refIndex = refIndex+14;
		}
		
	}
	
	public String toString() {
		String ret = recordId+","+combCommCode+","+spreadPriority+","+chargeRate;
		for(IntraCommSeriesLeg leg:intraCommSeriesLegList) {
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

	public String getSpreadPriority() {
		return spreadPriority;
	}

	public String getChargeRate() {
		return chargeRate;
	}

	public List<IntraCommSeriesLeg> getIntraCommSeriesLegList() {
		return intraCommSeriesLegList;
	}
	
}
