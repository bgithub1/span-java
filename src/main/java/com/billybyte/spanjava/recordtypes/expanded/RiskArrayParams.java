package com.billybyte.spanjava.recordtypes.expanded;

public class RiskArrayParams {

	private final String recordId; // "B "
	private final String exchAcro; // = line.substring(2,5);
	private final String commCode; // = line.substring(5,15);
	private final String prodTypeCode; // = line.substring(15,18);
	private final String futConMonth; // = line.substring(18,24);
	private final String futConDayWeekCode; // = line.substring(24,26);
	private final String optConMonth; // = line.substring(27,33);
	private final String optConDayWeekCode; // = line.substring(33,35);
	private final String baseVol; // = line.substring(36,44);
	private final String volScanRange; // = line.substring(44,52);
	private final String futPriceScanRange; // = line.substring(52,57);
	private final String extremeMoveMult; // = line.substring(57,62);
	private final String extremeMoveCovFrac; // = line.substring(62,67);
	private final String intRate; // = line.substring(67,72);
	private final String timeToExpiration; // = line.substring(72,79);
	private final String lookaheadTime; // = line.substring(79,85);
	private final String deltaScalingFactor; // = line.substring(85,91);
	private final String settleDate; // = line.substring(91,99);
	private final String undCommCode; // = line.substring(99,109);
	private final String pricingModel; // = line.substring(109,111);
	private final String divYield; // = line.substring(111,119);
	private final String optExpRefPriceFlag; // = line.substring(119,120);
	private final String optExpRefPrice; // = line.substring(120,127);
	private final String optExpRefPriceSign; // = line.substring(127,128);
	private final String swapValueFactor; // = line.substring(128,142);
	private final String swapValueFactorExp; // = line.substring(142,144);
	private final String swapValueFactSign; // = line.substring(144,145);
	private final String baseVolExp; // = line.substring(145,147);
	private final String baseVolExpSign; // = line.substring(147,148);
	private final String volScanRangeExp; // = line.substring(148,150);
	private final String volScanRangeExpSign; // = line.substring(150,151);
	private final String discountFactor; // = line.substring(151,163);
	private final String volScanRangeQuotMeth; // = line.substring(163,164);
	private final String priceScanRangeQuotMeth; // = line.substring(164,165);
	private final String futPriceScanRangeExp; // = line.substring(165,167);
	private final String futPriceScanRangeExpSign; // = line.substring(167,168);

	public RiskArrayParams(String line) {
		
		this.recordId = line.substring(0,2);
		this.exchAcro = line.substring(2,5);
		this.commCode = line.substring(5,15);
		this.prodTypeCode = line.substring(15,18);
		this.futConMonth = line.substring(18,24);
		this.futConDayWeekCode = line.substring(24,26);
		this.optConMonth = line.substring(27,33);
		this.optConDayWeekCode = line.substring(33,35);
		this.baseVol = line.substring(36,44);
		this.volScanRange = line.substring(44,52);
		this.futPriceScanRange = line.substring(52,57);
		this.extremeMoveMult = line.substring(57,62);
		this.extremeMoveCovFrac = line.substring(62,67);
		this.intRate = line.substring(67,72);
		this.timeToExpiration = line.substring(72,79);
		this.lookaheadTime = line.substring(79,85);
		this.deltaScalingFactor = line.substring(85,91);
		this.settleDate = line.substring(91,99);
		this.undCommCode = line.substring(99,109);
		this.pricingModel = line.substring(109,111);
		this.divYield = line.substring(111,119);
		
		String futPriceScanRangeExp = null;
		String futPriceScanRangeExpSign = null;
		
		if(line.length()>=120) {
			this.optExpRefPriceFlag = line.substring(119,120);
			this.optExpRefPrice = line.substring(120,127);
			this.optExpRefPriceSign = line.substring(127,128);
			this.swapValueFactor = line.substring(128,142);
			this.swapValueFactorExp = line.substring(142,144);
			this.swapValueFactSign = line.substring(144,145);
			this.baseVolExp = line.substring(145,147);
			this.baseVolExpSign = line.substring(147,148);
			this.volScanRangeExp = line.substring(148,150);
			this.volScanRangeExpSign = line.substring(150,151);
			this.discountFactor = line.substring(151,163);
			this.volScanRangeQuotMeth = line.substring(163,164);
			this.priceScanRangeQuotMeth = line.substring(164,165);
			
			if(line.length()>=168) {
				futPriceScanRangeExp = line.substring(165,167);
				futPriceScanRangeExpSign = line.substring(167,168);
			}		
		} else {
			this.optExpRefPriceFlag = null; //line.substring(119,120);
			this.optExpRefPrice = null; //line.substring(120,127);
			this.optExpRefPriceSign = null; //line.substring(127,128);
			this.swapValueFactor = null; //line.substring(128,142);
			this.swapValueFactorExp = null; //line.substring(142,144);
			this.swapValueFactSign = null; //line.substring(144,145);
			this.baseVolExp = null; //line.substring(145,147);
			this.baseVolExpSign = null; //line.substring(147,148);
			this.volScanRangeExp = null; //line.substring(148,150);
			this.volScanRangeExpSign = null; //line.substring(150,151);
			this.discountFactor = null; //line.substring(151,163);
			this.volScanRangeQuotMeth = null; //line.substring(163,164);
			this.priceScanRangeQuotMeth = null; //line.substring(164,165);			

		}
		
		this.futPriceScanRangeExp = futPriceScanRangeExp; //line.substring(165,167);
		this.futPriceScanRangeExpSign = futPriceScanRangeExpSign; //line.substring(167,168);


		
	}
	
	public String toString() {
		String ret = recordId+","+ exchAcro+","+commCode+","+prodTypeCode+","+futConMonth+","+futConDayWeekCode+","+optConMonth+","+
				optConDayWeekCode+","+baseVol+","+volScanRange+","+futPriceScanRange+","+extremeMoveMult+","+
				extremeMoveCovFrac+","+intRate+","+timeToExpiration+","+lookaheadTime+","+deltaScalingFactor+","+
				settleDate+","+undCommCode+","+pricingModel+","+divYield+","+optExpRefPriceFlag+","+optExpRefPriceFlag+","+
				optExpRefPrice+","+optExpRefPriceSign+","+swapValueFactor+","+swapValueFactorExp+","+swapValueFactSign+","+
				baseVolExp+baseVolExpSign+","+volScanRangeExp+","+volScanRangeExpSign+","+discountFactor+","+
				volScanRangeQuotMeth+","+priceScanRangeQuotMeth;
		if(futPriceScanRangeExp!=null) {
			ret = ret+","+futPriceScanRangeExp+","+futPriceScanRangeExpSign;
		}
		return ret;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getExchAcro() {
		return exchAcro;
	}

	public String getCommCode() {
		return commCode;
	}

	public String getProdTypeCode() {
		return prodTypeCode;
	}

	public String getFutConMonth() {
		return futConMonth;
	}

	public String getFutConDayWeekCode() {
		return futConDayWeekCode;
	}

	public String getOptConMonth() {
		return optConMonth;
	}

	public String getOptConDayWeekCode() {
		return optConDayWeekCode;
	}

	public String getBaseVol() {
		return baseVol;
	}

	public String getVolScanRange() {
		return volScanRange;
	}

	public String getFutPriceScanRange() {
		return futPriceScanRange;
	}

	public String getExtremeMoveMult() {
		return extremeMoveMult;
	}

	public String getExtremeMoveCovFrac() {
		return extremeMoveCovFrac;
	}

	public String getIntRate() {
		return intRate;
	}

	public String getTimeToExpiration() {
		return timeToExpiration;
	}

	public String getLookaheadTime() {
		return lookaheadTime;
	}

	public String getDeltaScalingFactor() {
		return deltaScalingFactor;
	}

	public String getSettleDate() {
		return settleDate;
	}

	public String getUndCommCode() {
		return undCommCode;
	}

	public String getPricingModel() {
		return pricingModel;
	}

	public String getDivYield() {
		return divYield;
	}

	public String getOptExpRefPriceFlag() {
		return optExpRefPriceFlag;
	}

	public String getOptExpRefPrice() {
		return optExpRefPrice;
	}

	public String getOptExpRefPriceSign() {
		return optExpRefPriceSign;
	}

	public String getSwapValueFactor() {
		return swapValueFactor;
	}

	public String getSwapValueFactorExp() {
		return swapValueFactorExp;
	}

	public String getSwapValueFactSign() {
		return swapValueFactSign;
	}

	public String getBaseVolExp() {
		return baseVolExp;
	}

	public String getBaseVolExpSign() {
		return baseVolExpSign;
	}

	public String getVolScanRangeExp() {
		return volScanRangeExp;
	}

	public String getVolScanRangeExpSign() {
		return volScanRangeExpSign;
	}

	public String getDiscountFactor() {
		return discountFactor;
	}

	public String getVolScanRangeQuotMeth() {
		return volScanRangeQuotMeth;
	}

	public String getPriceScanRangeQuotMeth() {
		return priceScanRangeQuotMeth;
	}

	public String getFutPriceScanRangeExp() {
		return futPriceScanRangeExp;
	}

	public String getFutPriceScanRangeExpSign() {
		return futPriceScanRangeExpSign;
	}
	
}
