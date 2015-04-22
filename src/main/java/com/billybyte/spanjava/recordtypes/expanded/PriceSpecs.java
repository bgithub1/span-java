package com.billybyte.spanjava.recordtypes.expanded;

public class PriceSpecs {

	private final String recordId; // "P "
	private final String exchAcro; // = line.substring(2,5);
	private final String prodCommCode; // = line.substring(5,15);
	private final String prodTypeCode; // = line.substring(15,18);
	private final String prodName; // = line.substring(18,33);
	private final String settlePriceDecLoc; // = line.substring(33,36);
	private final String strikePriceDecLoc; // = line.substring(36,39);
	private final String settlePriceAlignCode; // = line.substring(39,40);
	private final String strikePriceAlignCode; // = line.substring(40,41);
	private final String contValueFact; // = line.substring(41,55);
	private final String standardCabOptValue; // = line.substring(55,63);
	private final String quotePosQtyPerCont; // = line.substring(63,65);
	private final String settleCurrISO; // = line.substring(65,68);
	private final String settleCurrByte; // = line.substring(68,69);
	private final String priceQuoteMethod; // = line.substring(69,72);
	private final String signForContValueFact; // = line.substring(72,73);
	private final String contValueFactMult; // = line.substring(73,75);
	private final String exerciseStyle; // = line.substring(75,79);
	private final String prodLongName; // = line.substring(79,114);
	private final String positonalProdIndic; // = line.substring(114,115);
	private final String moneyCalcMethod; // = line.substring(115,116);
	private final String valuationMethod; // = line.substring(116,121);
	private final String settleMethod; // = line.substring(121,126);
	private final String fxSpotDateCollGainCredRate; // = line.substring(126,131);
	private final String fxPreSpotDateCollGainCredRate; // = line.substring(131,136);
	private final String fxPreSpotDateNumOfDays; // = line.substring(136,138);
	private final String fxForwardCollGainCredRate; // = line.substring(138,143);
	private final String equivPosFactor; // = line.substring(143,157);
	private final String equivPosFactExp; // = line.substring(157,159);
	private final String signForEquivPosFactExp; // = line.substring(159,160);
	private final String varTickOptFlag; // = line.substring(160,161);

	public PriceSpecs(String line) {
		this.recordId = line.substring(0,2);
		this.exchAcro = line.substring(2,5);
		this.prodCommCode = line.substring(5,15);
		this.prodTypeCode = line.substring(15,18);
		this.prodName = line.substring(18,33);
		this.settlePriceDecLoc = line.substring(33,36);
		this.strikePriceDecLoc = line.substring(36,39);
		this.settlePriceAlignCode = line.substring(39,40);
		this.strikePriceAlignCode = line.substring(40,41);
		this.contValueFact = line.substring(41,55);
		this.standardCabOptValue = line.substring(55,63);
		this.quotePosQtyPerCont = line.substring(63,65);
		this.settleCurrISO = line.substring(65,68);
		this.settleCurrByte = line.substring(68,69);
		this.priceQuoteMethod = line.substring(69,72);
		this.signForContValueFact = line.substring(72,73);
		this.contValueFactMult = line.substring(73,75);
		
		String exerciseStyle = null; //line.substring(75,79);
		String prodLongName = null; //line.substring(79,114);
		String positonalProdIndic = null; //line.substring(114,115);
		String moneyCalcMethod = null; //line.substring(115,116);
		String valuationMethod = null; //line.substring(116,121);
		String settleMethod = null; //line.substring(121,126);
		String fxSpotDateCollGainCredRate = null; //line.substring(126,131);
		String fxPreSpotDateCollGainCredRate = null; //line.substring(131,136);
		String fxPreSpotDateNumOfDays = null; //line.substring(136,138);
		String fxForwardCollGainCredRate = null; //line.substring(138,143);
		String equivPosFactor = null; //line.substring(143,157);
		String equivPosFactExp = null; //line.substring(157,159);
		String signForEquivPosFactExp = null; //line.substring(159,160);
		String varTickOptFlag = null; //line.substring(160,161);
		
		if(line.length()>=79) {
			exerciseStyle = line.substring(75,79);
			if(line.length()>=114) {
				prodLongName = line.substring(79,114);
				positonalProdIndic = line.substring(114,115);
				moneyCalcMethod = line.substring(115,116);
				valuationMethod = line.substring(116,121);
				settleMethod = line.substring(121,126);
				fxSpotDateCollGainCredRate = line.substring(126,131);
				fxPreSpotDateCollGainCredRate = line.substring(131,136);
				fxPreSpotDateNumOfDays = line.substring(136,138);
				fxForwardCollGainCredRate = line.substring(138,143);
				equivPosFactor = line.substring(143,157);
				equivPosFactExp = line.substring(157,159);
				signForEquivPosFactExp = line.substring(159,160);
				varTickOptFlag = line.substring(160,161);
			}
		}
		this.exerciseStyle = exerciseStyle; //line.substring(75,79);
		this.prodLongName = prodLongName; //line.substring(79,114);
		this.positonalProdIndic = positonalProdIndic; //line.substring(114,115);
		this.moneyCalcMethod = moneyCalcMethod; //line.substring(115,116);
		this.valuationMethod = valuationMethod; //line.substring(116,121);
		this.settleMethod = settleMethod; //line.substring(121,126);
		this.fxSpotDateCollGainCredRate = fxSpotDateCollGainCredRate; //line.substring(126,131);
		this.fxPreSpotDateCollGainCredRate = fxPreSpotDateCollGainCredRate; //line.substring(131,136);
		this.fxPreSpotDateNumOfDays = fxPreSpotDateNumOfDays; //line.substring(136,138);
		this.fxForwardCollGainCredRate = fxForwardCollGainCredRate; //line.substring(138,143);
		this.equivPosFactor = equivPosFactor; //line.substring(143,157);
		this.equivPosFactExp = equivPosFactExp; //line.substring(157,159);
		this.signForEquivPosFactExp = signForEquivPosFactExp; //line.substring(159,160);
		this.varTickOptFlag = varTickOptFlag; //line.substring(160,161);
	}
	
	public String toString() {
		String ret = recordId+","+exchAcro+","+prodCommCode+","+prodTypeCode+","+prodName+","+settlePriceDecLoc+","+strikePriceDecLoc+","+
				settlePriceAlignCode+","+strikePriceAlignCode+","+contValueFact+","+standardCabOptValue+","+quotePosQtyPerCont+
				","+settleCurrISO+","+settleCurrByte+","+priceQuoteMethod+","+signForContValueFact+","+contValueFactMult+","+
				exerciseStyle; 

		if(prodLongName!=null) ret = ret+","+prodLongName+","+positonalProdIndic+","+moneyCalcMethod+","+valuationMethod+","+
				settleMethod+","+fxSpotDateCollGainCredRate+","+fxPreSpotDateCollGainCredRate+","+fxPreSpotDateNumOfDays+","+
				fxForwardCollGainCredRate+","+equivPosFactor+","+equivPosFactExp+","+signForEquivPosFactExp+","+varTickOptFlag;
		
		return ret;
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

	public String getProdTypeCode() {
		return prodTypeCode;
	}

	public String getProdName() {
		return prodName;
	}

	public String getSettlePriceDecLoc() {
		return settlePriceDecLoc;
	}

	public String getStrikePriceDecLoc() {
		return strikePriceDecLoc;
	}

	public String getSettlePriceAlignCode() {
		return settlePriceAlignCode;
	}

	public String getStrikePriceAlignCode() {
		return strikePriceAlignCode;
	}

	public String getContValueFact() {
		return contValueFact;
	}

	public String getStandardCabOptValue() {
		return standardCabOptValue;
	}

	public String getQuotePosQtyPerCont() {
		return quotePosQtyPerCont;
	}

	public String getSettleCurrISO() {
		return settleCurrISO;
	}

	public String getSettleCurrByte() {
		return settleCurrByte;
	}

	public String getPriceQuoteMethod() {
		return priceQuoteMethod;
	}

	public String getSignForContValueFact() {
		return signForContValueFact;
	}

	public String getContValueFactMult() {
		return contValueFactMult;
	}

	public String getExerciseStyle() {
		return exerciseStyle;
	}

	public String getProdLongName() {
		return prodLongName;
	}

	public String getPositonalProdIndic() {
		return positonalProdIndic;
	}

	public String getMoneyCalcMethod() {
		return moneyCalcMethod;
	}

	public String getValuationMethod() {
		return valuationMethod;
	}

	public String getSettleMethod() {
		return settleMethod;
	}

	public String getFxSpotDateCollGainCredRate() {
		return fxSpotDateCollGainCredRate;
	}

	public String getFxPreSpotDateCollGainCredRate() {
		return fxPreSpotDateCollGainCredRate;
	}

	public String getFxPreSpotDateNumOfDays() {
		return fxPreSpotDateNumOfDays;
	}

	public String getFxForwardCollGainCredRate() {
		return fxForwardCollGainCredRate;
	}

	public String getEquivPosFactor() {
		return equivPosFactor;
	}

	public String getEquivPosFactExp() {
		return equivPosFactExp;
	}

	public String getSignForEquivPosFactExp() {
		return signForEquivPosFactExp;
	}

	public String getVarTickOptFlag() {
		return varTickOptFlag;
	}

}
