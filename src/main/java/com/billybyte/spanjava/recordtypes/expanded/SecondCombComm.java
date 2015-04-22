package com.billybyte.spanjava.recordtypes.expanded;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.spanjava.recordtypes.expanded.subtypes.TierDayWeekRange;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.TierMonthRange;

public class SecondCombComm {

	private final String recordId; // "3 "
	private final String combCommCode; // = line.substring(2,8);
	private final String intraCommSpreadChargeMethCode; // = line.substring(8,10);
	private final List<TierMonthRange> tierMonthRangeList;
	private final String initToMaintRatioMember; // = line.substring(68,72);
	private final String initToMaintRatioHedger; // = line.substring(72,76);
	private final String initToMaintRatioSpec; // = line.substring(76,80);
	private final List<TierDayWeekRange> tierDayWeekRangeList;
	
	public SecondCombComm(String line) {
		
		this.recordId = line.substring(0,2);
		this.combCommCode = line.substring(2,8);
		this.intraCommSpreadChargeMethCode = line.substring(8,10);
		this.tierMonthRangeList = new ArrayList<TierMonthRange>();
		int tierMonthRefIndex = 10;

		this.tierDayWeekRangeList = new ArrayList<TierDayWeekRange>();

		String initToMaintRatioMember = null;
		String initToMaintRatioHedger = null;
		String initToMaintRatioSpec = null;

		if(line.length()>68) {
			for(int i=0;i<4;i++) {
				String tierNumber = line.substring(tierMonthRefIndex,tierMonthRefIndex+2);
				String startMonth = line.substring(tierMonthRefIndex+2,tierMonthRefIndex+8);
				String endMonth = line.substring(tierMonthRefIndex+8,tierMonthRefIndex+14);
				TierMonthRange tierMonthRange = new TierMonthRange(tierNumber, startMonth, endMonth);
				tierMonthRangeList.add(tierMonthRange);
				tierMonthRefIndex = tierMonthRefIndex+14;
			}
			
			initToMaintRatioMember = line.substring(68,72);
			initToMaintRatioHedger = line.substring(72,76);
			initToMaintRatioSpec = line.substring(76,80);

			if(line.length()>80) {
				int dayRefIndex = 80;
				
				while(line.length()>=(dayRefIndex+4)) {
					String startDayWeekCode = line.substring(dayRefIndex, dayRefIndex+2);
					String endDayWeekCode = line.substring(dayRefIndex+2, dayRefIndex+4);
					TierDayWeekRange tierDayWeekRange = new TierDayWeekRange(startDayWeekCode, endDayWeekCode);
					tierDayWeekRangeList.add(tierDayWeekRange);
					dayRefIndex = dayRefIndex+4;
				}
			}
			
		}

		this.initToMaintRatioMember = initToMaintRatioMember;
		this.initToMaintRatioHedger = initToMaintRatioHedger;
		this.initToMaintRatioSpec = initToMaintRatioSpec;

	}
	
	public String toString() {
		String ret = recordId+","+combCommCode+","+intraCommSpreadChargeMethCode; //+","+tierNumber1+","+startMonth1+","+endMonth1+","+
		for(TierMonthRange range:tierMonthRangeList) {
			ret = ret+","+range.toString();
		}
		ret = ret+","+initToMaintRatioMember+","+initToMaintRatioHedger+","+initToMaintRatioSpec;
		for(TierDayWeekRange range:tierDayWeekRangeList) {
			ret = ret+","+range.toString();
		}
		return ret;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public String getIntraCommSpreadChargeMethCode() {
		return intraCommSpreadChargeMethCode;
	}

	public List<TierMonthRange> getTierMonthRangeList() {
		return tierMonthRangeList;
	}

	public String getInitToMaintRatioMember() {
		return initToMaintRatioMember;
	}

	public String getInitToMaintRatioHedger() {
		return initToMaintRatioHedger;
	}

	public String getInitToMaintRatioSpec() {
		return initToMaintRatioSpec;
	}

	public List<TierDayWeekRange> getTierDayWeekRangeList() {
		return tierDayWeekRangeList;
	}
	
}
