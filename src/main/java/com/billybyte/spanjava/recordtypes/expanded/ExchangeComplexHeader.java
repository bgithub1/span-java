package com.billybyte.spanjava.recordtypes.expanded;

public class ExchangeComplexHeader {

	private final String recordId; // "0 "
	private final String exchComplex; // = line.substring(2,8);
	private final String businessDate; // = line.substring(8,16);
	private final String siFlag; // = line.substring(16,17);
	private final String fileId; // = line.substring(17,19);
	private final String businessTime; // = line.substring(19, 23);
	private final String fileCreationDate; // = line.substring(23, 31);
	private final String fileCreationTime; // = line.substring(31, 35);
	private final String fileFormat; // = line.substring(35, 37);
	private final String grossNetMarginIndic; // = line.substring(37, 38);
	private final String limitOptValueFlag; // = line.substring(38, 39);
	private final String businessFunc; // = line.substring(39, 44);
	private final String clearingCustCode; // = line.substring(50, 51);
	
	public ExchangeComplexHeader(String line) {
		
		this.recordId = line.substring(0,2);
		this.exchComplex = line.substring(2,8);
		this.businessDate = line.substring(8,16);
		this.siFlag = line.substring(16,17);
		this.fileId = line.substring(17,19);
		this.businessTime = line.substring(19, 23);
		this.fileCreationDate = line.substring(23, 31);
		this.fileCreationTime = line.substring(31, 35);
		this.fileFormat = line.substring(35, 37);
		this.grossNetMarginIndic = line.substring(37, 38);
		this.limitOptValueFlag = line.substring(38, 39);
		this.businessFunc = line.substring(39, 44);
		this.clearingCustCode = line.substring(50, 51);
		
	}
	
	public String toString() {
		return recordId+","+exchComplex+","+businessDate+","+siFlag+","+fileId+","+businessTime+","+fileCreationDate+","+fileCreationTime+
				","+fileFormat+","+grossNetMarginIndic+","+limitOptValueFlag+","+businessFunc+","+clearingCustCode;
	}

	public String getRecordId() {
		return recordId;
	}
	
	public String getExchComplex() {
		return exchComplex;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public String getSiFlag() {
		return siFlag;
	}

	public String getFileId() {
		return fileId;
	}

	public String getBusinessTime() {
		return businessTime;
	}

	public String getFileCreationDate() {
		return fileCreationDate;
	}

	public String getFileCreationTime() {
		return fileCreationTime;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public String getGrossNetMarginIndic() {
		return grossNetMarginIndic;
	}

	public String getLimitOptValueFlag() {
		return limitOptValueFlag;
	}

	public String getBusinessFunc() {
		return businessFunc;
	}

	public String getClearingCustCode() {
		return clearingCustCode;
	}
	
}
