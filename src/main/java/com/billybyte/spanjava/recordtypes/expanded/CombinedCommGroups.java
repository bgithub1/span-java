package com.billybyte.spanjava.recordtypes.expanded;

import java.util.ArrayList;
import java.util.List;

import com.billybyte.spanjava.recordtypes.expanded.subtypes.CombCommCode;

public class CombinedCommGroups {

	private final String recordId; // "5 "
	private final String combCommGrpCode; // = line.substring(2,5);
	private final List<CombCommCode> combCommCodeList;
	
	public CombinedCommGroups(String line) {
		
		this.recordId = line.substring(0,2);
		this.combCommGrpCode = line.substring(2,5);
		this.combCommCodeList = new ArrayList<CombCommCode>();
		
		int refIndex = 12;
		
		while(line.length()>(refIndex+6)) {
			String combCommCode = line.substring(refIndex,refIndex+6);
			combCommCodeList.add(new CombCommCode(combCommCode));
			refIndex = refIndex+6;
		}
	}
	
	public String toString() {
		String ret = recordId+","+combCommGrpCode;
		for(CombCommCode code:combCommCodeList) {
			ret = ret+","+code.toString();
		}
		return ret;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getCombCommGrpCode() {
		return combCommGrpCode;
	}

	public List<CombCommCode> getCombCommCodeList() {
		return combCommCodeList;
	}
	
}
