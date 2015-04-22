package com.billybyte.spanjava.recordtypes.expanded;

public class DailyAdjustment {

	private final String recordId; // "V ";
	private final String exchAcro; // = line.substring(2,5);
	private final String prodCommCode; // = line.substring(5,15);
	private final String futConMonth; // = line.substring(15,21);
	private final String futConDayWeekCode; // = line.substring(21,23);
	private final String businessDate; // = line.substring(23,31);
	private final String dailyAdjRate; // = line.substring(31,44);
	private final String dailyAdjRateSign; // = line.substring(44,45);
	private final String dailyAdjPremDiscFlag; // = line.substring(45,46);
	private final String cumulAdjRate; // = line.substring(46,59);
	private final String cumulRateSign; // = line.substring(59,60);
	private final String cumulRatePremDiscFlag; // = line.substring(60,61);
	private final String shortRateFlag; // = line.substring(61,62);
	private final String longPosValMaintRate; // = line.substring(62,65);
	private final String shortPosValMaintRate; // = line.substring(65,68);
	private final String resetLongMarginPriceFlag; // = line.substring(68,69);
	private final String resetLongDownThreshhold; // = line.substring(69,72);
	private final String resetLongUpThreshhold; // = line.substring(72,75);
	private final String resetShortMarginPriceFlag; // = line.substring(75,76);
	private final String resetShortDownThreshhold; // = line.substring(76,79);
	private final String resetShortUpThreshhold; // = line.substring(79,82);
	private final String valueMaintProdClass; // = line.substring(82,88);
	
	public DailyAdjustment(String line) {
		
		this.recordId = line.substring(0,2);
		this.exchAcro = line.substring(2,5);
		this.prodCommCode = line.substring(5,15);
		this.futConMonth = line.substring(15,21);
		this.futConDayWeekCode = line.substring(21,23);
		this.businessDate = line.substring(23,31);
		this.dailyAdjRate = line.substring(31,44);
		this.dailyAdjRateSign = line.substring(44,45);
		this.dailyAdjPremDiscFlag = line.substring(45,46);
		this.cumulAdjRate = line.substring(46,59);
		this.cumulRateSign = line.substring(59,60);
		this.cumulRatePremDiscFlag = line.substring(60,61);
		this.shortRateFlag = line.substring(61,62);
		this.longPosValMaintRate = line.substring(62,65);
		this.shortPosValMaintRate = line.substring(65,68);
		this.resetLongMarginPriceFlag = line.substring(68,69);
		this.resetLongDownThreshhold = line.substring(69,72);
		this.resetLongUpThreshhold = line.substring(72,75);
		this.resetShortMarginPriceFlag = line.substring(75,76);
		this.resetShortDownThreshhold = line.substring(76,79);
		this.resetShortUpThreshhold = line.substring(79,82);
		this.valueMaintProdClass = line.substring(82,88);
				
	}
	
	public String toString() {
		
		return recordId+","+exchAcro+","+prodCommCode+","+futConMonth+","+futConDayWeekCode+","+businessDate+","+dailyAdjRate+","+
				dailyAdjRateSign+","+dailyAdjPremDiscFlag+","+cumulAdjRate+","+cumulRateSign+","+cumulRatePremDiscFlag+","+
				shortRateFlag+","+longPosValMaintRate+","+shortPosValMaintRate+","+resetLongMarginPriceFlag+","+
				resetLongDownThreshhold+","+resetLongUpThreshhold+","+resetShortMarginPriceFlag+","+
				resetShortDownThreshhold+","+resetShortUpThreshhold+","+valueMaintProdClass;
		
	}

	public String getRecordId() {
		return recordId;
	}

	public String getExchAcro() {
		return exchAcro;
	}

	public String getProdCommCode() {
		return prodCommCode;
	}

	public String getFutConMonth() {
		return futConMonth;
	}

	public String getFutConDayWeekCode() {
		return futConDayWeekCode;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public String getDailyAdjRate() {
		return dailyAdjRate;
	}

	public String getDailyAdjRateSign() {
		return dailyAdjRateSign;
	}

	public String getDailyAdjPremDiscFlag() {
		return dailyAdjPremDiscFlag;
	}

	public String getCumulAdjRate() {
		return cumulAdjRate;
	}

	public String getCumulRateSign() {
		return cumulRateSign;
	}

	public String getCumulRatePremDiscFlag() {
		return cumulRatePremDiscFlag;
	}

	public String getShortRateFlag() {
		return shortRateFlag;
	}

	public String getLongPosValMaintRate() {
		return longPosValMaintRate;
	}

	public String getShortPosValMaintRate() {
		return shortPosValMaintRate;
	}

	public String getResetLongMarginPriceFlag() {
		return resetLongMarginPriceFlag;
	}

	public String getResetLongDownThreshhold() {
		return resetLongDownThreshhold;
	}

	public String getResetLongUpThreshhold() {
		return resetLongUpThreshhold;
	}

	public String getResetShortMarginPriceFlag() {
		return resetShortMarginPriceFlag;
	}

	public String getResetShortDownThreshhold() {
		return resetShortDownThreshhold;
	}

	public String getResetShortUpThreshhold() {
		return resetShortUpThreshhold;
	}

	public String getValueMaintProdClass() {
		return valueMaintProdClass;
	}
	
}
