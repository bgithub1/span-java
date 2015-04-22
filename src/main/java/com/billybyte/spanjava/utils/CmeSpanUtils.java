package com.billybyte.spanjava.utils;

public class CmeSpanUtils {

	public enum RecordType {
		EXCHANGE_COMPLEX_HEADER("0 "), EXCHANGE_HEADER("1 "),
		FIRST_COMB_COMM("2 "), SECOND_COMB_COMM("3 "), THIRD_COMB_CORR("4 "),
		FIRST_PHYS_REC("81"), SECOND_PHYS_REC("82"), FIRST_PHYS_REC_FLOAT("83"), SECOND_PHYS_REC_FLOAT("84"),
		PRICE_SPECS("P "), RISK_ARRAY_PARAMS("B "), COMM_REDEF("R "),
		INTRA_COMM_TIERS("C "), INTRA_COMM_SERIES("E "), TIERED_SCANNED("S "), 
		COMB_COMM_GRPS("5 "), INTER_COMM_SPREADS("6 "), PHYS_DEBT_SEC("9 "), OPT_ON_COMB("X "),
		DAILY_ADJ("V "), SPLIT_ALLOC("Y "), DELTA_SPLIT_ALLOC("Z "), CURR_CONV_RATE("T ");
		
		private final String recId;
		
		RecordType(String recId){
			this.recId = recId;
		}
		
		public static String getEnumNameByValue(String value) {
			String enumValue = null;
			for(RecordType type:RecordType.values()) {
				if(value.compareTo(type.toString())==0){
					return type.name();
				}
			}
			return enumValue;
		}
		
		public String toString() {
			return this.recId;
		}
	}
	
}
