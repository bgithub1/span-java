package com.billybyte.spanjava.recordtypes.ice;

import java.util.Arrays;

public class RiskArrayRecord {

	private final String recordType;
	private final String strikePrice;
	private final String contractType;
	private final String lotSize;
	private final String settlementPrice;
	private final String compositeDelta;
	private final String[] lossArray;
	
	private final static String COMMA = ",";
	
	public RiskArrayRecord(String line) {
		
		String recordType;
		String strikePrice;
		String contractType;
		String lotSize;
		String settlementPrice;
		String compositeDelta;
		this.lossArray = new String[16];
		
		if(line.contains(COMMA)){
			String[] lineSplit = line.split(COMMA);
			recordType = lineSplit[0];
			strikePrice = lineSplit[1];
			contractType = lineSplit[2];
			lotSize = lineSplit[3];
			settlementPrice = lineSplit[4];
			compositeDelta = lineSplit[5];
			
			for(int i=6;i<23;i++){
				lossArray[i-6] = lineSplit[i];
			}
			
		} else {
			recordType = line.substring(0,2);
			strikePrice = line.substring(2,10);
			contractType = line.substring(10,12);
			lotSize = line.substring(12,17);
			settlementPrice = line.substring(17,25);
			compositeDelta = line.substring(25,34);
			
			int refIndex = 34;
			for(int i=0;i<16;i++) {
				lossArray[i] = line.substring(refIndex,refIndex+7);
				refIndex = refIndex + 7;
			}
		}
		
		this.recordType = recordType;
		this.strikePrice = strikePrice;
		this.contractType = contractType;
		this.lotSize = lotSize;
		this.settlementPrice = settlementPrice;
		this.compositeDelta = compositeDelta;
		
	}

	public String getRecordType() {
		return recordType;
	}

	public String getStrikePrice() {
		return strikePrice;
	}

	public String getContractType() {
		return contractType;
	}

	public String getLotSize() {
		return lotSize;
	}

	public String getSettlementPrice() {
		return settlementPrice;
	}

	public String getCompositeDelta() {
		return compositeDelta;
	}

	public String[] getLossArray() {
		return lossArray;
	}
	
	public String toString() {
		return 	recordType+","+strikePrice+","+contractType+","+lotSize+","+
				settlementPrice+","+compositeDelta+Arrays.toString(lossArray);
	}
	
}
