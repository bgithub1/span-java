package com.billybyte.spanjava.recordtypes.expanded;

public class ExchangeHeader {

	private final String recordId; // "1 "
	private final String exchAcro;
	private final String exchCode;
	
	public ExchangeHeader(String line) {
		this.recordId = line.substring(0,2);
		this.exchAcro = line.substring(2,5);
		this.exchCode = line.substring(7,9);
	}
	
	public String toString() {
		return recordId+","+exchAcro+","+exchCode;
	}

	public String getRecordId() {
		return recordId;
	}
	
	public String getExchAcro() {
		return exchAcro;
	}

	public String getExchCode() {
		return exchCode;
	}
	
}
