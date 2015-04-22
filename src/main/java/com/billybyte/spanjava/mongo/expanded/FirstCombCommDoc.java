package com.billybyte.spanjava.mongo.expanded;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.mongo.MongoDoc;
import com.billybyte.spanjava.mongo.expanded.subdocs.RiskArrayParamBlockDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class FirstCombCommDoc implements MongoDoc {

	private final String exchAcro;
	private final String combCommCode;
	private final String riskExp;
	private final String perfBondCurrISO;
	private final String perfBondCurrCode;
	private final String optMarginStyleMethod;
	private final String limitOptVal;
	private final String combMargMethodFlag;
	private final List<RiskArrayParamBlockDoc> riskArrayParamBlockList;

	public FirstCombCommDoc(String exchAcro, String combCommCode,
			String riskExp, String perfBondCurrISO, String perfBondCurrCode,
			String optMarginStyleMethod, String limitOptVal,
			String combMargMethodFlag,
			List<RiskArrayParamBlockDoc> riskArrayParamBlockList) {
		super();
		this.exchAcro = exchAcro;
		this.combCommCode = combCommCode;
		this.riskExp = riskExp;
		this.perfBondCurrISO = perfBondCurrISO;
		this.perfBondCurrCode = perfBondCurrCode;
		this.optMarginStyleMethod = optMarginStyleMethod;
		this.limitOptVal = limitOptVal;
		this.combMargMethodFlag = combMargMethodFlag;
		this.riskArrayParamBlockList = riskArrayParamBlockList;
	}

	public String getExchAcro() {
		return exchAcro;
	}

	public String getCombCommCode() {
		return combCommCode;
	}

	public String getRiskExp() {
		return riskExp;
	}

	public String getPerfBondCurrISO() {
		return perfBondCurrISO;
	}

	public String getPerfBondCurrCode() {
		return perfBondCurrCode;
	}

	public String getOptMarginStyleMethod() {
		return optMarginStyleMethod;
	}

	public String getLimitOptVal() {
		return limitOptVal;
	}

	public String getCombMargMethodFlag() {
		return combMargMethodFlag;
	}

	public List<RiskArrayParamBlockDoc> getRiskArrayParamBlockList() {
		return riskArrayParamBlockList;
	}

	@Override
	public DBObject getDBObject() {
		DBObject doc = new BasicDBObject();
		doc.put("exchAcro", exchAcro);
		doc.put("combCommCode", combCommCode);
		doc.put("riskExp", riskExp);
		doc.put("perfBondCurrISO", perfBondCurrISO);
		doc.put("perfBondCurrCode", perfBondCurrCode);
		doc.put("optMarginStyleMethod", optMarginStyleMethod);
		doc.put("limitOptVal", limitOptVal);
		doc.put("combMargMethodFlag", combMargMethodFlag);
		List<DBObject> riskArrayParamBlockDBObjList = new ArrayList<DBObject>();
		for(RiskArrayParamBlockDoc subDoc:this.riskArrayParamBlockList) {
			riskArrayParamBlockDBObjList.add(subDoc.getDBObject());
		}
		doc.put("riskArrayParamBlockList", riskArrayParamBlockDBObjList);
		return doc;
	}
	
}
