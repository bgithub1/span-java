package com.billybyte.spanjava.recordtypes.expanded;

public class SplitAllocation {

	private final String recordId; // "Y "
	private final String combProdCommCode; // = line.substring(2,12);
	private final String optOnCombProdCode; // = line.substring(12,22);
	
	public SplitAllocation(String line) {
		
		this.recordId = line.substring(0,2);
		this.combProdCommCode = line.substring(2,12);
		this.optOnCombProdCode = line.substring(12,22);
				
	}
	
	public String toString() {
		return recordId+","+combProdCommCode+","+optOnCombProdCode;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getCombProdCommCode() {
		return combProdCommCode;
	}

	public String getOptOnCombProdCode() {
		return optOnCombProdCode;
	}
	
}
