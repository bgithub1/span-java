package com.billybyte.spanjava.recordtypes.expanded;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.billybyte.spanjava.mongo.span.subtypes.InterCommSpreadLeg;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.InterCommSpreadLegBasic;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.SpreadCreditRateLeg;
import com.billybyte.spanjava.recordtypes.expanded.subtypes.TierNumberLeg;

public class InterCommSpreads {

	private final String recordId; // "6 "
	private final String commGrpCode; // = line.substring(2,5);
	private final String spreadPriority; // = line.substring(5,9);
	private final String spreadCreditRate; // = line.substring(9,16);
	private final List<InterCommSpreadLeg> interCommSpreadLegList;
	private final String interSpreadMethodCode; // = line.substring(88,90);
	private final String targetExchAcro; // = line.substring(90,93);
	private final String targetLegReqFlag; // = line.substring(93,94);
	private final String targetCombCommCode; // = line.substring(94,100);
	private final String creditCalcMethod; // = line.substring(100,101);
//	private final List<TierNumberLeg> tierNumberLegList;
	private final String spreadGroupFlag; // = line.substring(109,110);
	private final String targetLegDeltaSpreadRatio; // = line.substring(110,117);
	private final String minNumOfLegsReq; // = line.substring(117,121);
	private final String spreadCreditRateDefSepFlag; // = line.substring(121,122);
//	private final List<SpreadCreditRateLeg> spreadCreditRateLegList;
	private final String regStatusEligibilityCode; // = line.substring(150,151);
	
	public InterCommSpreads(String line) {
		
		this.recordId = line.substring(0,2);
		this.commGrpCode = line.substring(2,5);
		this.spreadPriority = line.substring(5,9);
		this.spreadCreditRate = line.substring(9,16);

		int spreadLegRefIndex = 16;
		
		List<InterCommSpreadLegBasic> basicLegList = new ArrayList<InterCommSpreadLegBasic>();
		List<TierNumberLeg> tierNumberLegList = new ArrayList<TierNumberLeg>();
		List<SpreadCreditRateLeg> spreadCreditRateLegList = new ArrayList<SpreadCreditRateLeg>();
		
		for(int i=0;i<4;i++) {
			if(line.length()>=(spreadLegRefIndex+18)) {
				String exchAcroLeg = line.substring(spreadLegRefIndex,spreadLegRefIndex+3);
				if(exchAcroLeg.trim().length()<1) continue;
				String requiredForScanningBasedSpreadFlag = line.substring(spreadLegRefIndex+3, spreadLegRefIndex+4);
				String combCommCode = line.substring(spreadLegRefIndex+4,spreadLegRefIndex+10);
				String deltaSpreadRatio = line.substring(spreadLegRefIndex+10,spreadLegRefIndex+17);
				String spreadSide = line.substring(spreadLegRefIndex+17,spreadLegRefIndex+18);
				InterCommSpreadLegBasic leg = 
						new InterCommSpreadLegBasic(exchAcroLeg, requiredForScanningBasedSpreadFlag, combCommCode, deltaSpreadRatio, spreadSide);
				basicLegList.add(leg);
				spreadLegRefIndex = spreadLegRefIndex+18;
			}
		}
		
		this.interSpreadMethodCode = line.substring(88,90);
		this.targetExchAcro = line.substring(90,93);
		this.targetLegReqFlag = line.substring(93,94);
		this.targetCombCommCode = line.substring(94,100);
		this.creditCalcMethod = line.substring(100,101);
		
		int tierNumberRefIndex = 101;

		for(int i=0;i<4;i++){
			if(line.length()>=(tierNumberRefIndex+2)) {
				String tierNumberLeg = line.substring(tierNumberRefIndex,tierNumberRefIndex+2);
				if(tierNumberLeg.trim().length()>0) tierNumberLegList.add(new TierNumberLeg(tierNumberLeg));
				tierNumberRefIndex = tierNumberRefIndex+2;
			}
		}
		
		String spreadGroupFlagTemp = null;
		String targetLegDeltaSpreadRatioTemp = null;
		String minNumOfLegsReq = null;
		String spreadCreditRateDefSepFlag = null;
		String reqStatusEligibilityCode = null;

		if(line.length()>=(110)) {
			spreadGroupFlagTemp = line.substring(109,110);

			if(line.length()>=(117)) {
				targetLegDeltaSpreadRatioTemp = line.substring(110,117);
				minNumOfLegsReq = line.substring(117,121);

				if(line.length()>=122) {
					spreadCreditRateDefSepFlag = line.substring(121,122);
					
					int creditRateLegRefIndex = 122;
					while(line.length()>=(creditRateLegRefIndex+7)) {
						String spreadCreditRateLeg = line.substring(creditRateLegRefIndex,creditRateLegRefIndex+7);
						spreadCreditRateLegList.add(new SpreadCreditRateLeg(spreadCreditRateLeg));
						creditRateLegRefIndex = creditRateLegRefIndex+7;
					}
					if(line.length()>=151) reqStatusEligibilityCode = line.substring(150,151);

				}
			}
		}

		this.spreadGroupFlag = spreadGroupFlagTemp;
		this.targetLegDeltaSpreadRatio = targetLegDeltaSpreadRatioTemp; 
		this.minNumOfLegsReq = minNumOfLegsReq;
		this.spreadCreditRateDefSepFlag = spreadCreditRateDefSepFlag;
		this.regStatusEligibilityCode = reqStatusEligibilityCode;
		
		this.interCommSpreadLegList = new ArrayList<InterCommSpreadLeg>();

//		List<InterCommSpreadLegBasic> basicLegList = new ArrayList<InterCommSpreadLegBasic>();
//		List<TierNumberLeg> tierNumberLegList = new ArrayList<TierNumberLeg>();
//		List<SpreadCreditRateLeg> spreadCreditRateLegList = new ArrayList<SpreadCreditRateLeg>();

		for(int i=0;i<basicLegList.size();i++) {
			InterCommSpreadLegBasic basic = basicLegList.get(i);
			
			Integer tierNumber;
			try {
				tierNumber = (tierNumberLegList.size()>i) ? Integer.parseInt(tierNumberLegList.get(i).getTierNumberLeg()) : null;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				break;
			}
			String creditRate = (spreadCreditRateLegList.size()>i) ? spreadCreditRateLegList.get(i).getSpreadCreditRateLeg() : null;
			
			String deltaSpreadRatioString = basic.getDeltaSpreadRatio();
			BigDecimal deltaSpreadRatio = new BigDecimal(deltaSpreadRatioString.substring(0,3)+"."+deltaSpreadRatioString.substring(3));
			
			InterCommSpreadLeg fullLeg = new InterCommSpreadLeg(
					basic.getExchAcro(), basic.getReqForScanFlag(), basic.getCombCommCode(), 
					deltaSpreadRatio, basic.getSpreadSide(), tierNumber, creditRate);
			
			interCommSpreadLegList.add(fullLeg);
			
		}
		
		if(interSpreadMethodCode.compareTo("04")==0) {
			interCommSpreadLegList.add(
					new InterCommSpreadLeg(
							targetExchAcro, targetLegReqFlag, targetCombCommCode, new BigDecimal("1.0000"), "T", null, null));
		}

		
	}
	
	public String toString() {
		String ret = recordId+","+commGrpCode+","+spreadPriority+","+spreadCreditRate;
		for(InterCommSpreadLeg leg:interCommSpreadLegList) {
			ret = ret+","+leg.toString();
		}
		ret = ret+","+interSpreadMethodCode+","+targetExchAcro+","+targetLegReqFlag+","+
				targetCombCommCode+","+creditCalcMethod+","+regStatusEligibilityCode;
//		for(TierNumberLeg leg:tierNumberLegList) {
//			ret = ret+","+leg.toString();
//		}
//		ret = ret+","+spreadGroupFlag+","+targetLegDeltaSpreadRatio+","+minNumOfLegsReq+","+spreadCreditRateDefSepFlag;
//		for(SpreadCreditRateLeg leg:spreadCreditRateLegList) {
//			ret = ret+","+leg.toString();
//		}
		return ret;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getCommGrpCode() {
		return commGrpCode;
	}

	public String getSpreadPriority() {
		return spreadPriority;
	}

	public String getSpreadCreditRate() {
		return spreadCreditRate;
	}

	public List<InterCommSpreadLeg> getInterCommSpreadLegList() {
		return interCommSpreadLegList;
	}

	public String getInterSpreadMethodCode() {
		return interSpreadMethodCode;
	}

	public String getTargetExchAcro() {
		return targetExchAcro;
	}

	public String getTargetLegReqFlag() {
		return targetLegReqFlag;
	}

	public String getTargetCombCommCode() {
		return targetCombCommCode;
	}

	public String getCreditCalcMethod() {
		return creditCalcMethod;
	}

//	public List<TierNumberLeg> getTierNumberLegList() {
//		return tierNumberLegList;
//	}

	public String getSpreadGroupFlag() {
		return spreadGroupFlag;
	}

	public String getTargetLegDeltaSpreadRatio() {
		return targetLegDeltaSpreadRatio;
	}

	public String getMinNumOfLegsReq() {
		return minNumOfLegsReq;
	}

	public String getSpreadCreditRateDefSepFlag() {
		return spreadCreditRateDefSepFlag;
	}

//	public List<SpreadCreditRateLeg> getSpreadCreditRateLegList() {
//		return spreadCreditRateLegList;
//	}

	public String getRegStatusEligibilityCode() {
		return regStatusEligibilityCode;
	}
	
}
