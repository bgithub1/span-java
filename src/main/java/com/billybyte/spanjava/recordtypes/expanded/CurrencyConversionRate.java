package com.billybyte.spanjava.recordtypes.expanded;

public class CurrencyConversionRate {

	private final String recordId; // "T "
	private final String fromCurrISO; // = line.substring(2,5);
	private final String fromCurrByte; // = line.substring(5,6);
	private final String toCurrISO; // = line.substring(6,9);
	private final String toCurrByte; // = line.substring(9,10);
	private final String convMult; // = line.substring(10,20);
	
	public CurrencyConversionRate(String line) {

		this.recordId = line.substring(0,2);
		this.fromCurrISO = line.substring(2,5);
		this.fromCurrByte = line.substring(5,6);
		this.toCurrISO = line.substring(6,9);
		this.toCurrByte = line.substring(9,10);
		this.convMult = line.substring(10,20);
		
	}
	
	public String toString() {
		return recordId+","+fromCurrISO+","+fromCurrByte+","+toCurrISO+","+toCurrByte+","+convMult;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getFromCurrISO() {
		return fromCurrISO;
	}

	public String getFromCurrByte() {
		return fromCurrByte;
	}

	public String getToCurrISO() {
		return toCurrISO;
	}

	public String getToCurrByte() {
		return toCurrByte;
	}

	public String getConvMult() {
		return convMult;
	}
	
	
}
