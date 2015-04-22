package com.billybyte.spanjava.recordtypes.expanded;


public class FirstPhysical {

	private final String recordId; // "81"
	private final String exchAcro; // = line.substring(2,5);
	private final String commProdCode; //= line.substring(5,15);
	private final String undCommProdCode; // = line.substring(15,25);
	private final String prodTypeCode; // = line.substring(25,28);
	private final String optRightCode; // = line.substring(28,29);
	private final String futConMonth; // = line.substring(29,35);
	private final String futConDayWeekCode; // = line.substring(35,37);
	private final String optConMonth; // = line.substring(38,44);
	private final String optConDayWeekCode; // = line.substring(44,46);
	private final String optStrikePrice; // = line.substring(47,54);
	private final String arrVal1; // = line.substring(54,59);
	private final String arrValSign1; // = line.substring(59,60);
	private final String arrVal2; // = line.substring(60,65);
	private final String arrValSign2; // = line.substring(65,66);
	private final String arrVal3; // = line.substring(66,71);
	private final String arrValSign3; // = line.substring(71,72);
	private final String arrVal4; // = line.substring(72,77);
	private final String arrValSign4; // = line.substring(77,78);
	private final String arrVal5; // = line.substring(78,83);
	private final String arrValSign5; // = line.substring(83,84);
	private final String arrVal6; // = line.substring(84,89);
	private final String arrValSign6; // = line.substring(89,90);
	private final String arrVal7; // = line.substring(90,95);
	private final String arrValSign7; // = line.substring(95,96);
	private final String arrVal8; // = line.substring(96,101);
	private final String arrValSign8; // = line.substring(101,102);
	private final String arrVal9; // = line.substring(102,107);
	private final String arrValSign9; // = line.substring(107,108);
	private final String highPrecSettlePrice; // = line.substring(108,122);
	private final String highPrecSettlePriceFlag; // = line.substring(122,123);
	
	
	public FirstPhysical(String line) {
		
		this.recordId = line.substring(0,2);
		this.exchAcro = line.substring(2,5);
		this.commProdCode = line.substring(5,15);
		this.undCommProdCode = line.substring(15,25);
		this.prodTypeCode = line.substring(25,28);
		this.optRightCode = line.substring(28,29);
		this.futConMonth = line.substring(29,35);
		this.futConDayWeekCode = line.substring(35,37);
		this.optConMonth = line.substring(38,44);
		this.optConDayWeekCode = line.substring(44,46);
		this.optStrikePrice = line.substring(47,54);
		this.arrVal1 = line.substring(54,59);
		this.arrValSign1 = line.substring(59,60);
		this.arrVal2 = line.substring(60,65);
		this.arrValSign2 = line.substring(65,66);
		this.arrVal3 = line.substring(66,71);
		this.arrValSign3 = line.substring(71,72);
		this.arrVal4 = line.substring(72,77);
		this.arrValSign4 = line.substring(77,78);
		this.arrVal5 = line.substring(78,83);
		this.arrValSign5 = line.substring(83,84);
		this.arrVal6 = line.substring(84,89);
		this.arrValSign6 = line.substring(89,90);
		this.arrVal7 = line.substring(90,95);
		this.arrValSign7 = line.substring(95,96);
		this.arrVal8 = line.substring(96,101);
		this.arrValSign8 = line.substring(101,102);
		this.arrVal9 = line.substring(102,107);
		this.arrValSign9 = line.substring(107,108);
		
		String highPrecSettlePrice = null;
		String highPrecSettlePriceFlag = null;
		
		if(line.length()>=122) {
			highPrecSettlePrice = line.substring(108,122);
			highPrecSettlePriceFlag = line.substring(122,123);
		}
		
		this.highPrecSettlePrice = highPrecSettlePrice;
		this.highPrecSettlePriceFlag = highPrecSettlePriceFlag;
		
	}
	
	public String toString() {
		return recordId+","+exchAcro+","+commProdCode+","+undCommProdCode+","+prodTypeCode+","+optRightCode+","+futConMonth+","+
				futConDayWeekCode+","+optConMonth+","+optConDayWeekCode+","+optStrikePrice+","+
				arrVal1+","+arrValSign1+","+arrVal2+","+arrValSign2+","+arrVal3+","+arrValSign3+","+
				arrVal4+","+arrValSign4+","+arrVal5+","+arrValSign5+","+arrVal6+","+arrValSign6+","+
				arrVal7+","+arrValSign7+","+arrVal8+","+arrValSign8+","+arrVal9+","+arrValSign9+","+
				highPrecSettlePrice+","+highPrecSettlePriceFlag;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getExchAcro() {
		return exchAcro;
	}

	public String getCommProdCode() {
		return commProdCode;
	}

	public String getUndCommProdCode() {
		return undCommProdCode;
	}

	public String getProdTypeCode() {
		return prodTypeCode;
	}

	public String getOptRightCode() {
		return optRightCode;
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

	public String getOptStrikePrice() {
		return optStrikePrice;
	}

	public String getArrVal1() {
		return arrVal1;
	}

	public String getArrValSign1() {
		return arrValSign1;
	}

	public String getArrVal2() {
		return arrVal2;
	}

	public String getArrValSign2() {
		return arrValSign2;
	}

	public String getArrVal3() {
		return arrVal3;
	}

	public String getArrValSign3() {
		return arrValSign3;
	}

	public String getArrVal4() {
		return arrVal4;
	}

	public String getArrValSign4() {
		return arrValSign4;
	}

	public String getArrVal5() {
		return arrVal5;
	}

	public String getArrValSign5() {
		return arrValSign5;
	}

	public String getArrVal6() {
		return arrVal6;
	}

	public String getArrValSign6() {
		return arrValSign6;
	}

	public String getArrVal7() {
		return arrVal7;
	}

	public String getArrValSign7() {
		return arrValSign7;
	}

	public String getArrVal8() {
		return arrVal8;
	}

	public String getArrValSign8() {
		return arrValSign8;
	}

	public String getArrVal9() {
		return arrVal9;
	}

	public String getArrValSign9() {
		return arrValSign9;
	}

	public String getHighPrecSettlePrice() {
		return highPrecSettlePrice;
	}

	public String getHighPrecSettlePriceFlag() {
		return highPrecSettlePriceFlag;
	}
	
}
