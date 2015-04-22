package com.billybyte.spanjava.recordtypes.expanded;

public class OptionsOnCombinations {

	private final String recordId; // "X "
	private final String combMargMethodCode; // = line.substring(2,3);
	private final String prodCommCode; //= line.substring(3,13);
	private final String priceOffset; // = line.substring(13,20);
	
	public OptionsOnCombinations(String line) {
		
		this.recordId = line.substring(0,2);
		this.combMargMethodCode = line.substring(2,3);
		this.prodCommCode = line.substring(3,13);
		this.priceOffset = line.substring(13,20);
		
	}
	
	public String toString() {
		return recordId+","+combMargMethodCode+","+prodCommCode+","+priceOffset;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getCombMargMethodCode() {
		return combMargMethodCode;
	}

	public String getProdCommCode() {
		return prodCommCode;
	}

	public String getPriceOffset() {
		return priceOffset;
	}
	
}
