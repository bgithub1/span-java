package com.billybyte.spanjava.recordtypes.expanded;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.spanjava.mongo.span.subtypes.InterCommTierDetail;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.InterCommScanDayWeekTier;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.InterCommScanMonthTier;

public class InterCommScanTiers {

	private final String recordId; // "S "
	private final String combCommCode; // = line.substring(2,8);
	private final String scanSpreadMethodCode; // = line.substring(8,10);
	private final String tierFields; // = line.substring(10,12);
	private final String weightedFutPriceRiskCalcMethod;
	private final List<InterCommTierDetail> interCommScanTierList;
	
	public InterCommScanTiers(String line) {
		
		this.recordId = line.substring(0,2);
		this.combCommCode = line.substring(2,8);
		this.scanSpreadMethodCode = line.substring(8,10);
		this.tierFields = line.substring(10,12);
		this.interCommScanTierList = new ArrayList<InterCommTierDetail>();
		String calcMethodTemp = null;
		
		Integer scanSpreadMethodCodeInt = Integer.parseInt(scanSpreadMethodCode);
		if(scanSpreadMethodCodeInt>2) {

			List<InterCommScanMonthTier> monthTierList = new ArrayList<InterCommScanMonthTier>();
			List<InterCommScanDayWeekTier> dayTierList = new ArrayList<InterCommScanDayWeekTier>();
			List<String> chargeRateList = new ArrayList<String>();
			
			int monthTierRefIndex = 12;
			
			for(int i=0;i<5;i++) {
				if(line.length()>=(monthTierRefIndex+14)) {
					String tierNumber = line.substring(monthTierRefIndex,monthTierRefIndex+2);
					if(tierNumber.trim().length() < 1) continue;
					String startMonth = line.substring(monthTierRefIndex+2,monthTierRefIndex+8);
					String endMonth = line.substring(monthTierRefIndex+8,monthTierRefIndex+14);
					InterCommScanMonthTier interCommScanMonthTier = new InterCommScanMonthTier(tierNumber, startMonth, endMonth);
					monthTierList.add(interCommScanMonthTier);
					monthTierRefIndex = monthTierRefIndex+14;
				}
			}
			
			if(line.length()>=83) {
				calcMethodTemp = line.substring(82,83);
				
				int dayWeekTierRefIndex = 83;
				
				for(int i=0;i<5;i++) {
					if(line.length()>=(dayWeekTierRefIndex+4)) {
						String startDayWeekCode = line.substring(dayWeekTierRefIndex,dayWeekTierRefIndex+2);
						String endDayWeekCode = line.substring(dayWeekTierRefIndex+2,dayWeekTierRefIndex+4);
						InterCommScanDayWeekTier tier = new InterCommScanDayWeekTier(startDayWeekCode, endDayWeekCode);
						dayTierList.add(tier);
						dayWeekTierRefIndex = dayWeekTierRefIndex+4;
					}
				}
				
				int shortOptRefIndex = 103;
				
				for(int i=0;i<5;i++) {
					if(line.length()>=(shortOptRefIndex+7)) {
						String shortOptMinChargeRate = line.substring(shortOptRefIndex,shortOptRefIndex+7);
						chargeRateList.add(shortOptMinChargeRate);
						shortOptRefIndex = shortOptRefIndex+7;
					}
				}
			}
						
			for(int i=0;i<monthTierList.size();i++) {
				InterCommScanMonthTier monthTier = monthTierList.get(i);
				
				String startDay = null;
				String endDay = null;
				String shortOptMinCharge = null;
				
				if(dayTierList.size()>i) {
					startDay = dayTierList.get(i).getStartDayWeekCode();
					endDay = dayTierList.get(i).getEndDayWeekCode();
					
					if(chargeRateList.size()>i) {
						shortOptMinCharge = chargeRateList.get(i);
					}
					
				}
				
				interCommScanTierList.add(
						new InterCommTierDetail(
								Integer.parseInt(monthTier.getTierNumber()), monthTier.getStartMonth(), monthTier.getEndMonth(), 
								startDay, endDay, shortOptMinCharge));
				
			}
			
		}
		
		this.weightedFutPriceRiskCalcMethod = calcMethodTemp;
	}
	
	public String toString() {
		String ret = recordId+","+combCommCode+","+scanSpreadMethodCode+","+tierFields;
		for(InterCommTierDetail tier:interCommScanTierList) {
			ret = ret+","+tier.toString();
		}
		if(weightedFutPriceRiskCalcMethod!=null) ret = ret+","+weightedFutPriceRiskCalcMethod;
		return ret;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public String getScanSpreadMethodCode() {
		return scanSpreadMethodCode;
	}

	public String getTierFields() {
		return tierFields;
	}

	public List<InterCommTierDetail> getInterCommScanTierList() {
		return interCommScanTierList;
	}

	public String getWeightedFutPriceRiskCalcMethod() {
		return weightedFutPriceRiskCalcMethod;
	}
	
}
