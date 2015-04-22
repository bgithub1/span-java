package com.billybyte.spanjava.recordtypes.expanded;

public class SecondPhysicalFloat {

	private final String recordId; // "84"
	private final String exchAcro; // = line.substring(2,5);
	private final String commProdCode; // = line.substring(5,15);
	private final String undCommProdCode; // = line.substring(15,25);
	private final String prodTypeCode; // = line.substring(25,28);
	private final String optRightCode; // = line.substring(28,29);
	private final String futConMonth; // = line.substring(29,35);
	private final String futConDayWeekCode; // = line.substring(35,37);
	private final String optConMonth; // = line.substring(38,44);
	private final String optConDayWeekCode; // = line.substring(44,46);
	private final String optStrikePrice; // = line.substring(47,54);
	private final String arrVal10; // = line.substring(54,62);
	private final String arrValSign10; // = line.substring(62,63);
	private final String arrVal11; // = line.substring(63,71);
	private final String arrValSign11; // = line.substring(71,72);
	private final String arrVal12; // = line.substring(72,80);
	private final String arrValSign12; // = line.substring(80,81);
	private final String arrVal13; // = line.substring(81,89);
	private final String arrValSign13; // = line.substring(89,90);
	private final String arrVal14; // = line.substring(90,98);
	private final String arrValSign14; // = line.substring(98,99);
	private final String arrVal15; // = line.substring(99,107);
	private final String arrValSign15; // = line.substring(107,108);
	private final String arrVal16; // = line.substring(108,116);
	private final String arrValSign16; // = line.substring(116,117);
	private final String spanCompDelta; // = line.substring(117,122);
	private final String spanCompDeltaSign; // = line.substring(122,123);
	private final String impVolAsFrac; // = line.substring(123,131);
	private final String settlePrice; // = line.substring(131,138);
	private final String settlePriceSign; // = line.substring(138,139);
	private final String strikePriceSign; // = line.substring(139,140);
	private final String currentDelta; // = line.substring(140,145);
	private final String currentDeltaSign; // = line.substring(145,146);
	private final String currentDeltaBusDayFlag; // = line.substring(146,147);
	private final String startOfDayPrice; // = line.substring(147,154);
	private final String startOfDayPriceSign; // = line.substring(154,155);
	private final String impVolExp; // = line.substring(155,157);
	private final String impVolExpSign; // = line.substring(157,158);
	private final String contValueFact; // = line.substring(158,172);
	private final String contValueFactExp; // = line.substring(172,174);
	private final String contValueFactExpSign; // = line.substring(174,175);
	private final String strikeValueFactor; // = line.substring(175,189);
	private final String strikeValueFactorExp; // = line.substring(189,191);
	private final String strikeValueFactorExpSign; // = line.substring(191,192);

	public SecondPhysicalFloat(String line) {
		
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
		this.arrVal10 = line.substring(54,62);
		this.arrValSign10 = line.substring(62,63);
		this.arrVal11 = line.substring(63,71);
		this.arrValSign11 = line.substring(71,72);
		this.arrVal12 = line.substring(72,80);
		this.arrValSign12 = line.substring(80,81);
		this.arrVal13 = line.substring(81,89);
		this.arrValSign13 = line.substring(89,90);
		this.arrVal14 = line.substring(90,98);
		this.arrValSign14 = line.substring(98,99);
		this.arrVal15 = line.substring(99,107);
		this.arrValSign15 = line.substring(107,108);
		this.arrVal16 = line.substring(108,116);
		this.arrValSign16 = line.substring(116,117);
		this.spanCompDelta = line.substring(117,122);
		this.spanCompDeltaSign = line.substring(122,123);
		this.impVolAsFrac = line.substring(123,131);
		this.settlePrice = line.substring(131,138);
		this.settlePriceSign = line.substring(138,139);
		this.strikePriceSign = line.substring(139,140);
		this.currentDelta = line.substring(140,145);
		this.currentDeltaSign = line.substring(145,146);
		this.currentDeltaBusDayFlag = line.substring(146,147);
		this.startOfDayPrice = line.substring(147,154);
		this.startOfDayPriceSign = line.substring(154,155);
		this.impVolExp = line.substring(155,157);
		this.impVolExpSign = line.substring(157,158);
		this.contValueFact = line.substring(158,172);
		this.contValueFactExp = line.substring(172,174);
		this.contValueFactExpSign = line.substring(174,175);
		this.strikeValueFactor = line.substring(175,189);
		this.strikeValueFactorExp = line.substring(189,191);
		this.strikeValueFactorExpSign = line.substring(191,192);
				
	}
	
	public String toString() {
		return recordId+","+exchAcro+","+commProdCode+","+undCommProdCode+","+prodTypeCode+","+optRightCode+","+futConMonth+","+
				futConDayWeekCode+","+optConMonth+","+optConDayWeekCode+","+optStrikePrice+","+
				arrVal10+","+arrValSign10+","+arrVal11+","+arrValSign11+","+arrVal12+","+arrValSign12+","+
				arrVal13+","+arrValSign13+","+arrVal14+","+arrValSign14+","+arrVal15+","+arrValSign15+","+
				arrVal16+","+arrValSign16+","+
				spanCompDelta+","+spanCompDeltaSign+","+impVolAsFrac+","+impVolAsFrac+","+settlePrice+","+settlePriceSign+","+
				strikePriceSign+","+currentDelta+","+currentDeltaSign+","+currentDeltaBusDayFlag+","+startOfDayPrice+","+
				startOfDayPriceSign+","+impVolExp+","+impVolExpSign+","+contValueFact+","+contValueFactExp+","+
				contValueFactExpSign+","+strikeValueFactor+","+strikeValueFactorExp+","+strikeValueFactorExpSign;
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

	public String getArrVal10() {
		return arrVal10;
	}

	public String getArrValSign10() {
		return arrValSign10;
	}

	public String getArrVal11() {
		return arrVal11;
	}

	public String getArrValSign11() {
		return arrValSign11;
	}

	public String getArrVal12() {
		return arrVal12;
	}

	public String getArrValSign12() {
		return arrValSign12;
	}

	public String getArrVal13() {
		return arrVal13;
	}

	public String getArrValSign13() {
		return arrValSign13;
	}

	public String getArrVal14() {
		return arrVal14;
	}

	public String getArrValSign14() {
		return arrValSign14;
	}

	public String getArrVal15() {
		return arrVal15;
	}

	public String getArrValSign15() {
		return arrValSign15;
	}

	public String getArrVal16() {
		return arrVal16;
	}

	public String getArrValSign16() {
		return arrValSign16;
	}

	public String getSpanCompDelta() {
		return spanCompDelta;
	}

	public String getSpanCompDeltaSign() {
		return spanCompDeltaSign;
	}

	public String getImpVolAsFrac() {
		return impVolAsFrac;
	}

	public String getSettlePrice() {
		return settlePrice;
	}

	public String getSettlePriceSign() {
		return settlePriceSign;
	}

	public String getStrikePriceSign() {
		return strikePriceSign;
	}

	public String getCurrentDelta() {
		return currentDelta;
	}

	public String getCurrentDeltaSign() {
		return currentDeltaSign;
	}

	public String getCurrentDeltaBusDayFlag() {
		return currentDeltaBusDayFlag;
	}

	public String getStartOfDayPrice() {
		return startOfDayPrice;
	}

	public String getStartOfDayPriceSign() {
		return startOfDayPriceSign;
	}

	public String getImpVolExp() {
		return impVolExp;
	}

	public String getImpVolExpSign() {
		return impVolExpSign;
	}

	public String getContValueFact() {
		return contValueFact;
	}

	public String getContValueFactExp() {
		return contValueFactExp;
	}

	public String getContValueFactExpSign() {
		return contValueFactExpSign;
	}

	public String getStrikeValueFactor() {
		return strikeValueFactor;
	}

	public String getStrikeValueFactorExp() {
		return strikeValueFactorExp;
	}

	public String getStrikeValueFactorExpSign() {
		return strikeValueFactorExpSign;
	}
	
}
