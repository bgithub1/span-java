package com.billybyte.spanjava.recordtypes.expanded;

public class SecondPhysical {

	private final String recordId; // "82"
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
	private final String arrVal10; // = line.substring(54,59);
	private final String arrValSign10; // = line.substring(59,60);
	private final String arrVal11; // = line.substring(60,65);
	private final String arrValSign11; // = line.substring(65,66);
	private final String arrVal12; // = line.substring(66,71);
	private final String arrValSign12; // = line.substring(71,72);
	private final String arrVal13; // = line.substring(72,77);
	private final String arrValSign13; // = line.substring(77,78);
	private final String arrVal14; // = line.substring(78,83);
	private final String arrValSign14; // = line.substring(83,84);
	private final String arrVal15; // = line.substring(84,89);
	private final String arrValSign15; // = line.substring(89,90);
	private final String arrVal16; // = line.substring(90,95);
	private final String arrValSign16; // = line.substring(95,96);
	private final String spanCompDelta; // = line.substring(96,101);
	private final String spanCompDeltaSign; // = line.substring(101,102);
	private final String impVolAsFrac; // = line.substring(102,110);
	private final String settlePrice; // = line.substring(110,117);
	private final String settlePriceSign; // = line.substring(117,118);
	private final String strikePriceSign; // = line.substring(118,119);
	private final String currentDelta; // = line.substring(119,124);
	private final String currentDeltaSign; // = line.substring(124,125);
	private final String currentDeltaBusDayFlag; // = line.substring(125,126);
	private final String startOfDayPrice; // = line.substring(126,133);
	private final String startOfDayPriceSign; // = line.substring(133,134);
	private final String impVolExp; // = line.substring(134,136);
	private final String impVolExpSign; // = line.substring(136,137);
	private final String contValueFact; // = line.substring(137,151);
	private final String contValueFactExp; // = line.substring(151,153);
	private final String contValueFactExpSign; // = line.substring(153,154);
	private final String strikeValueFactor; // = line.substring(154,168);
	private final String strikeValueFactorExp; // = line.substring(168,170);
	private final String strikeValueFactorExpSign; // = line.substring(170,171);
	
	public SecondPhysical(String line) {
		
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
		this.arrVal10 = line.substring(54,59);
		this.arrValSign10 = line.substring(59,60);
		this.arrVal11 = line.substring(60,65);
		this.arrValSign11 = line.substring(65,66);
		this.arrVal12 = line.substring(66,71);
		this.arrValSign12 = line.substring(71,72);
		this.arrVal13 = line.substring(72,77);
		this.arrValSign13 = line.substring(77,78);
		this.arrVal14 = line.substring(78,83);
		this.arrValSign14 = line.substring(83,84);
		this.arrVal15 = line.substring(84,89);
		this.arrValSign15 = line.substring(89,90);
		this.arrVal16 = line.substring(90,95);
		this.arrValSign16 = line.substring(95,96);
		this.spanCompDelta = line.substring(96,101);
		this.spanCompDeltaSign = line.substring(101,102);
		this.impVolAsFrac = line.substring(102,110);
		this.settlePrice = line.substring(110,117);
		this.settlePriceSign = line.substring(117,118);
		
		String strikePriceSign = null;
		String currentDelta = null; //line.substring(119,124);
		String currentDeltaSign = null; //line.substring(124,125);
		String currentDeltaBusDayFlag = null; //line.substring(125,126);
		String startOfDayPrice = null; //line.substring(126,133);
		String startOfDayPriceSign = null; //line.substring(133,134);
		String impVolExp = null; //line.substring(134,136);
		String impVolExpSign = null; //line.substring(136,137);
		String contValueFact = null; //line.substring(137,151);
		String contValueFactExp = null; //line.substring(151,153);
		String contValueFactExpSign = null; //line.substring(153,154);
		String strikeValueFactor = null; //line.substring(154,168);
		String strikeValueFactorExp = null; //line.substring(168,170);
		String strikeValueFactorExpSign = null; //line.substring(170,171);
		
		if(line.length()>=119) {
			strikePriceSign = line.substring(118,119);
			if(line.length()>=124) {
				currentDelta = line.substring(119,124);
				currentDeltaSign = line.substring(124,125);
				currentDeltaBusDayFlag = line.substring(125,126);
				if(line.length()>126) {
					startOfDayPrice = line.substring(126,133);
					startOfDayPriceSign = line.substring(133,134);
					impVolExp = line.substring(134,136);
					impVolExpSign = line.substring(136,137);
					contValueFact = line.substring(137,151);
					contValueFactExp = line.substring(151,153);
					contValueFactExpSign = line.substring(153,154);
					strikeValueFactor = line.substring(154,168);
					strikeValueFactorExp = line.substring(168,170);
					strikeValueFactorExpSign = line.substring(170,171);
				}
			}
		}
		
		this.strikePriceSign = strikePriceSign;
		this.currentDelta = currentDelta; //line.substring(119,124);
		this.currentDeltaSign = currentDeltaSign; //line.substring(124,125);
		this.currentDeltaBusDayFlag = currentDeltaBusDayFlag; //line.substring(125,126);
		this.startOfDayPrice = startOfDayPrice; //line.substring(126,133);
		this.startOfDayPriceSign = startOfDayPriceSign; //line.substring(133,134);
		this.impVolExp = impVolExp; //line.substring(134,136);
		this.impVolExpSign = impVolExpSign; //line.substring(136,137);
		this.contValueFact = contValueFact; //line.substring(137,151);
		this.contValueFactExp = contValueFactExp; //line.substring(151,153);
		this.contValueFactExpSign = contValueFactExpSign; //line.substring(153,154);
		this.strikeValueFactor = strikeValueFactor; //line.substring(154,168);
		this.strikeValueFactorExp = strikeValueFactorExp; //line.substring(168,170);
		this.strikeValueFactorExpSign = strikeValueFactorExpSign; //line.substring(170,171);
	}
	
	public String toString() {
		String ret = recordId+","+exchAcro+","+commProdCode+","+undCommProdCode+","+prodTypeCode+","+optRightCode+","+
				futConMonth+","+futConDayWeekCode+","+optConMonth+","+optConDayWeekCode+","+optStrikePrice+","+
				arrVal10+","+arrValSign10+","+arrVal11+","+arrValSign11+","+arrVal12+","+arrValSign12+","+
				arrVal13+","+arrValSign13+","+arrVal14+","+arrValSign14+","+arrVal15+","+arrValSign15+","+
				arrVal16+","+arrValSign16+","+spanCompDelta+","+spanCompDeltaSign+","+impVolAsFrac+","+impVolAsFrac+","+
				settlePrice+","+settlePriceSign+","+strikePriceSign+","+currentDelta+","+currentDeltaSign+","+currentDeltaBusDayFlag;
		if(startOfDayPrice!=null) {
			ret = ret+","+startOfDayPrice+","+
					startOfDayPriceSign+","+impVolExp+","+impVolExpSign+","+contValueFact+","+contValueFactExp+","+
					contValueFactExpSign+","+strikeValueFactor+","+strikeValueFactorExp+","+strikeValueFactorExpSign;
		}
		return ret;
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
