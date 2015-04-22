package com.billybyte.spanjava.mongo.expanded;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ProductSpecsDoc implements MongoDoc {

//	private final String exchAcro; // = line.substring(2,5);
//	private final String prodCommCode; // = line.substring(5,15);
//	private final String prodTypeCode; // = line.substring(15,18);
	private final SpanProdId prodId;
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
	
	public ProductSpecsDoc(String exchAcro, String prodCommCode,
			String prodTypeCode, String prodName, String settlePriceDecLoc,
			String strikePriceDecLoc, String settlePriceAlignCode,
			String strikePriceAlignCode, String contValueFact,
			String standardCabOptValue, String quotePosQtyPerCont,
			String settleCurrISO, String settleCurrByte,
			String priceQuoteMethod, String signForContValueFact,
			String contValueFactMult, String exerciseStyle,
			String prodLongName, String positonalProdIndic,
			String moneyCalcMethod, String valuationMethod,
			String settleMethod, String fxSpotDateCollGainCredRate,
			String fxPreSpotDateCollGainCredRate,
			String fxPreSpotDateNumOfDays, String fxForwardCollGainCredRate,
			String equivPosFactor, String equivPosFactExp,
			String signForEquivPosFactExp, String varTickOptFlag) {
		super();
//		this.exchAcro = exchAcro;
//		this.prodCommCode = prodCommCode;
//		this.prodTypeCode = prodTypeCode;
		this.prodId = new SpanProdId(exchAcro, prodCommCode, prodTypeCode);
		this.prodName = prodName;
		this.settlePriceDecLoc = settlePriceDecLoc;
		this.strikePriceDecLoc = strikePriceDecLoc;
		this.settlePriceAlignCode = settlePriceAlignCode;
		this.strikePriceAlignCode = strikePriceAlignCode;
		this.contValueFact = contValueFact;
		this.standardCabOptValue = standardCabOptValue;
		this.quotePosQtyPerCont = quotePosQtyPerCont;
		this.settleCurrISO = settleCurrISO;
		this.settleCurrByte = settleCurrByte;
		this.priceQuoteMethod = priceQuoteMethod;
		this.signForContValueFact = signForContValueFact;
		this.contValueFactMult = contValueFactMult;
		this.exerciseStyle = exerciseStyle;
		this.prodLongName = prodLongName;
		this.positonalProdIndic = positonalProdIndic;
		this.moneyCalcMethod = moneyCalcMethod;
		this.valuationMethod = valuationMethod;
		this.settleMethod = settleMethod;
		this.fxSpotDateCollGainCredRate = fxSpotDateCollGainCredRate;
		this.fxPreSpotDateCollGainCredRate = fxPreSpotDateCollGainCredRate;
		this.fxPreSpotDateNumOfDays = fxPreSpotDateNumOfDays;
		this.fxForwardCollGainCredRate = fxForwardCollGainCredRate;
		this.equivPosFactor = equivPosFactor;
		this.equivPosFactExp = equivPosFactExp;
		this.signForEquivPosFactExp = signForEquivPosFactExp;
		this.varTickOptFlag = varTickOptFlag;
	}

	public SpanProdId getProdId() {
		return prodId;
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

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
//		doc.put("exchAcro", exchAcro);
//		doc.put("prodCommCode", prodCommCode);
//		doc.put("prodTypeCode", prodTypeCode);
		doc.put("prodId",prodId.getDBObject());
		doc.put("prodName", prodName);
		doc.put("settlePriceDecLoc", settlePriceDecLoc);
		doc.put("strikePriceDecLoc", strikePriceDecLoc);
		doc.put("settlePriceAlignCode", settlePriceAlignCode);
		doc.put("strikePriceAlignCode", strikePriceAlignCode);
		doc.put("contValueFact", contValueFact);
		doc.put("standardCabOptValue", standardCabOptValue);
		doc.put("quotePosQtyPerCont", quotePosQtyPerCont);
		doc.put("settleCurrISO", settleCurrISO);
		doc.put("settleCurrByte", settleCurrByte);
		doc.put("priceQuoteMethod", priceQuoteMethod);
		doc.put("signForContValueFact", signForContValueFact);
		doc.put("contValueFactMult", contValueFactMult);
		doc.put("exerciseStyle", exerciseStyle);
		doc.put("prodLongName", prodLongName);
		doc.put("positonalProdIndic", positonalProdIndic);
		doc.put("moneyCalcMethod", moneyCalcMethod);
		doc.put("valuationMethod", valuationMethod);
		doc.put("settleMethod", settleMethod);
		doc.put("fxSpotDateCollGainCredRate", fxSpotDateCollGainCredRate);
		doc.put("fxPreSpotDateCollGainCredRate", fxPreSpotDateCollGainCredRate);
		doc.put("fxPreSpotDateNumOfDays", fxPreSpotDateNumOfDays);
		doc.put("fxForwardCollGainCredRate", fxForwardCollGainCredRate);
		doc.put("equivPosFactor", equivPosFactor);
		doc.put("equivPosFactExp", equivPosFactExp);
		doc.put("signForEquivPosFactExp", signForEquivPosFactExp);
		doc.put("varTickOptFlag", varTickOptFlag);
		return doc;
	}
	
	public String toString() {
		return this.getDBObject().toString();
	}
	
	
}
