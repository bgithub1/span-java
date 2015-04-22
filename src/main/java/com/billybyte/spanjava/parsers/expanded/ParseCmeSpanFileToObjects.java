package com.billybyte.spanjava.parsers.expanded;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.spanjava.recordtypes.expanded.CombinedCommGroups;
import com.billybyte.spanjava.recordtypes.expanded.CurrencyConversionRate;
import com.billybyte.spanjava.recordtypes.expanded.DailyAdjustment;
import com.billybyte.spanjava.recordtypes.expanded.DeltaSplitAllocation;
import com.billybyte.spanjava.recordtypes.expanded.ExchangeComplexHeader;
import com.billybyte.spanjava.recordtypes.expanded.ExchangeHeader;
import com.billybyte.spanjava.recordtypes.expanded.FirstCombComm;
import com.billybyte.spanjava.recordtypes.expanded.FirstPhysical;
import com.billybyte.spanjava.recordtypes.expanded.FirstPhysicalFloat;
import com.billybyte.spanjava.recordtypes.expanded.InterCommScanTiers;
import com.billybyte.spanjava.recordtypes.expanded.InterCommSpreads;
import com.billybyte.spanjava.recordtypes.expanded.IntraCommSeries;
import com.billybyte.spanjava.recordtypes.expanded.IntraCommTiers;
import com.billybyte.spanjava.recordtypes.expanded.OptionsOnCombinations;
import com.billybyte.spanjava.recordtypes.expanded.PriceSpecs;
import com.billybyte.spanjava.recordtypes.expanded.RiskArrayParams;
import com.billybyte.spanjava.recordtypes.expanded.SecondCombComm;
import com.billybyte.spanjava.recordtypes.expanded.SecondPhysical;
import com.billybyte.spanjava.recordtypes.expanded.SecondPhysicalFloat;
import com.billybyte.spanjava.recordtypes.expanded.SplitAllocation;
import com.billybyte.spanjava.recordtypes.expanded.ThirdCombComm;
import com.billybyte.spanjava.utils.CmeSpanUtils;
import com.billybyte.spanjava.utils.CmeSpanUtils.RecordType;

public class ParseCmeSpanFileToObjects {

	private final BufferedReader br;
	
	public ParseCmeSpanFileToObjects(Reader reader) {
		this.br = new BufferedReader(reader);
	}

	public static void main(String[] args) {
		
		String fileName = "cme.20130128.c.pa2";
		
		try {
			
			Calendar beforeTime = Calendar.getInstance();
			ParseCmeSpanFileToObjects spanParser = new ParseCmeSpanFileToObjects(new FileReader(fileName));
			
			Boolean newEnum = spanParser.processSpan();

			Calendar afterTime = Calendar.getInstance();

			Utils.prt(newEnum.toString()+", took "+(afterTime.getTimeInMillis()-beforeTime.getTimeInMillis())+" ms");
			
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Boolean processSpan() throws IOException {
		
		Boolean ret = true;
		
		Set<String> badEnumSet = new HashSet<String>();
		List<Exception> eList = new ArrayList<Exception>();
		
		int lineNum = 0;
					
		while(br.ready()) {
			
			String line = br.readLine();
			Utils.prt(lineNum+" ("+line.length()+"): "+line);

			String recIdString = line.substring(0, 2);

			String enumName = RecordType.getEnumNameByValue(recIdString);
			if(enumName!=null) {
				RecordType recId = RecordType.valueOf(enumName);
				
				switch(recId) {
				
					case EXCHANGE_COMPLEX_HEADER:
						ExchangeComplexHeader ech = new ExchangeComplexHeader(line);
						Utils.prt(ech.toString());
						break;
					case CURR_CONV_RATE:
						CurrencyConversionRate ccr = new CurrencyConversionRate(line);
						Utils.prt(ccr.toString());
						break;
					case EXCHANGE_HEADER:
						ExchangeHeader eh = new ExchangeHeader(line);
						Utils.prt(eh.toString());
						break;
					case PRICE_SPECS:
						PriceSpecs ps = new PriceSpecs(line);
						Utils.prt(ps.toString());
						break;
					case FIRST_COMB_COMM:
						FirstCombComm fcc = new FirstCombComm(line);
						Utils.prt(fcc.toString());
						break;
					case SECOND_COMB_COMM:
						SecondCombComm scc = new SecondCombComm(line);
						Utils.prt(scc.toString());
						break;
					case THIRD_COMB_CORR:
						ThirdCombComm tcc = new ThirdCombComm(line);
						Utils.prt(tcc.toString());
						break;
					case TIERED_SCANNED:
						InterCommScanTiers icst = new InterCommScanTiers(line);
						Utils.prt(icst.toString());
						break;
					case INTRA_COMM_SERIES:
						IntraCommSeries ics = new IntraCommSeries(line);
						Utils.prt(ics.toString());
						break;
					case INTRA_COMM_TIERS:
						IntraCommTiers ict = new IntraCommTiers(line);
						Utils.prt(ict.toString());
						break;
					case INTER_COMM_SPREADS:
						InterCommSpreads icsp = new InterCommSpreads(line);
						Utils.prt(icsp.toString());
						break;
					case RISK_ARRAY_PARAMS:
						RiskArrayParams rap = new RiskArrayParams(line);
						Utils.prt(rap.toString());
						break;
					case DAILY_ADJ:
						DailyAdjustment da = new DailyAdjustment(line);
						Utils.prt(da.toString());
						break;
					case OPT_ON_COMB:
						OptionsOnCombinations ooc = new OptionsOnCombinations(line);
						Utils.prt(ooc.toString());
						break;
					case SPLIT_ALLOC:
						SplitAllocation sa = new SplitAllocation(line);
						Utils.prt(sa.toString());
						break;
					case DELTA_SPLIT_ALLOC:
						DeltaSplitAllocation dsa = new DeltaSplitAllocation(line);
						Utils.prt(dsa.toString());
						break;
					case COMB_COMM_GRPS:
						CombinedCommGroups ccg = new CombinedCommGroups(line);
						Utils.prt(ccg.toString());
						break;
					case FIRST_PHYS_REC:
						FirstPhysical fp = new FirstPhysical(line);
						Utils.prt(fp.toString());
						break;
					case SECOND_PHYS_REC:
						SecondPhysical sp = new SecondPhysical(line);
						Utils.prt(sp.toString());
						break;
					case FIRST_PHYS_REC_FLOAT:
						FirstPhysicalFloat fpf = new FirstPhysicalFloat(line);
						Utils.prt(fpf.toString());
						break;
					case SECOND_PHYS_REC_FLOAT:
						SecondPhysicalFloat spf = new SecondPhysicalFloat(line);
						Utils.prt(spf.toString());
						break;
					default:
						Utils.prtErr("MAKE METHOD TO HANDLE THIS ENUM: "+recId);
						badEnumSet.add(recId.toString());
						ret = false;
						break;
				}
			} else {
				Utils.prtErr("Not able to parse enum from: "+recIdString);
				ret = false;
				badEnumSet.add(recIdString);
			}
			
			lineNum++;
		}

		Utils.prt("*************************************************************************************");
		Utils.prt("Finished processing: "+lineNum+" lines");
		for(String err:badEnumSet) {
			Utils.prtErr(err);
		}
		Utils.prt("Exception list - "+eList.size());
		return ret;
	}
	

}
