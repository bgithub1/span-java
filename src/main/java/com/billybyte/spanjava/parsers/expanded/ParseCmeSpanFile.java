package com.billybyte.spanjava.parsers.expanded;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.spanjava.utils.CmeSpanUtils;
import com.billybyte.spanjava.utils.CmeSpanUtils.RecordType;

public class ParseCmeSpanFile {

	private final BufferedReader br;
	
	public ParseCmeSpanFile(Reader reader) {
		this.br = new BufferedReader(reader);
	}
	
	public static void main(String[] args) {
		
		String fileName = "cme.20130128.c.pa2";
		
		try {
			
			Calendar beforeTime = Calendar.getInstance();
			ParseCmeSpanFile spanParser = new ParseCmeSpanFile(new FileReader(fileName));
			
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

//			try {
				String recIdString = line.substring(0, 2);
				
				String enumName = RecordType.getEnumNameByValue(recIdString);
				if(enumName!=null) {
					RecordType recId = RecordType.valueOf(enumName);
					
					switch(recId) {
					
						case EXCHANGE_COMPLEX_HEADER:
							parseExchCompHeader(line);
							break;
						case CURR_CONV_RATE:
							parseCurrConvRate(line);
							break;
						case EXCHANGE_HEADER:
							parseExchHeader(line);
							break;
						case PRICE_SPECS:
							parsePriceSpecs(line);
							break;
						case FIRST_COMB_COMM:
							parseFirstCombComm(line);
							break;
						case SECOND_COMB_COMM:
							parseSecondCombComm(line);
							break;
						case THIRD_COMB_CORR:
							parseThirdCombComm(line);
							break;
						case TIERED_SCANNED:
							parseTieredScanned(line);
							break;
						case INTRA_COMM_SERIES:
							parseIntraCommSeries(line);
							break;
						case INTRA_COMM_TIERS:
							parseIntraCommTiers(line);
							break;
						case INTER_COMM_SPREADS:
							parseInterCommSpreads(line);
							break;
						case RISK_ARRAY_PARAMS:
							parseRiskArrayParams(line);
							break;
						case DAILY_ADJ:
							parseDailyAdj(line);
							break;
						case OPT_ON_COMB:
							parseOptOnComb(line);
							break;
						case SPLIT_ALLOC:
							parseSplitAlloc(line);
							break;
						case DELTA_SPLIT_ALLOC:
							parseDeltaSplitAlloc(line);
							break;
						case COMB_COMM_GRPS:
							parseCombCommGrps(line);
							break;
						case FIRST_PHYS_REC:
							parseFirstPhysRec(line);
							break;
						case SECOND_PHYS_REC:
							parseSecondPhysRec(line);
							break;
						case FIRST_PHYS_REC_FLOAT:
							parseFirstPhysRecFloat(line);
							break;
						case SECOND_PHYS_REC_FLOAT:
							parseSecondPhysRecFloat(line);
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
//			} catch (Exception e){
//				Utils.prtObErrMess(this.getClass(), "Exception while parsing line "+lineNum);
//				e.printStackTrace();
//				eList.add(e);
//			}
			
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
	
	private void parseExchCompHeader(String line) {
		
		String exchComplex = line.substring(2,8);
		String businessDate = line.substring(8,16);
		String siFlag = line.substring(16,17);
		String fileId = line.substring(17,19);
		String businessTime = line.substring(19, 23);
		String fileCreationDate = line.substring(23, 31);
		String fileCreationTime = line.substring(31, 35);
		String fileFormat = line.substring(35, 37);
		String grossNetMarginIndic = line.substring(37, 38);
		String limitOptValueFlag = line.substring(38, 39);
		String businessFunc = line.substring(39, 44);
		String clearingCustCode = line.substring(50, 51);
		
		Utils.prt(exchComplex+","+businessDate+","+siFlag+","+fileId+","+businessTime+","+fileCreationDate+","+fileCreationTime+
				","+fileFormat+","+grossNetMarginIndic+","+limitOptValueFlag+","+businessFunc+","+clearingCustCode);
		
	}
	
	private void parseExchHeader(String line) {
		
		String exchAcro = line.substring(2,5);
		String exchCode = line.substring(7,9);
		
		Utils.prt(exchAcro+","+exchCode);
		
	}
	
	private void parsePriceSpecs(String line) {

//	Commented out code below will work for PARIS-EXPANDED formatted span file
//		String exchAcro = line.substring(2,5);
//		String prodCommCode = line.substring(5,17);
//		String prodTypeCode = line.substring(17,22);
//		String prodName = line.substring(22,37);
//		String settlePriceDecLoc = line.substring(37,40);
//		String strikePriceDecLoc = line.substring(40,43);
//		String settlePriceAlignCode = line.substring(43,44);
//		String strikePriceAlignCode = line.substring(44,45);
//		String contValueFact = line.substring(45,59);
//		String standardCabOptValue = line.substring(59,67);
//		String quotePosQtyPerCont = line.substring(67,69);
//		String settleCurrISO = line.substring(69,72);
//		String settleCurrByte = line.substring(72,73);
//		String priceQuoteMethod = line.substring(73,76);
//		String exerciseStyle = line.substring(76,80);
//		String volScanRangeQuoteMeth = line.substring(80,81);
//		String priceScanRangeQuoteMeth = line.substring(81,82);
//		String priceScanRangeValuType = line.substring(82,83);
//		String valuationMethod = line.substring(83,88);

		String exchAcro = line.substring(2,5);
		String prodCommCode = line.substring(5,15);
		String prodTypeCode = line.substring(15,18);
		String prodName = line.substring(18,33);
		String settlePriceDecLoc = line.substring(33,36);
		String strikePriceDecLoc = line.substring(36,39);
		String settlePriceAlignCode = line.substring(39,40);
		String strikePriceAlignCode = line.substring(40,41);
		String contValueFact = line.substring(41,55);
		String standardCabOptValue = line.substring(55,63);
		String quotePosQtyPerCont = line.substring(63,65);
		String settleCurrISO = line.substring(65,68);
		String settleCurrByte = line.substring(68,69);
		String priceQuoteMethod = line.substring(69,72);
		String signForContValueFact = line.substring(72,73);
		String contValueFactMult = line.substring(73,75);
		String exerciseStyle = line.substring(75,79);
		String prodLongName = line.substring(79,114);
		String positonalProdIndic = line.substring(114,115);
		String moneyCalcMethod = line.substring(115,116);
		String valuationMethod = line.substring(116,121);
		String settleMethod = line.substring(121,126);
		String fxSpotDateCollGainCredRate = line.substring(126,131);
		String fxPreSpotDateCollGainCredRate = line.substring(131,136);
		String fxPreSpotDateNumOfDays = line.substring(136,138);
		String fxForwardCollGainCredRate = line.substring(138,143);
		String equivPosFactor = line.substring(143,157);
		String equivPosFactExp = line.substring(157,159);
		String signForEquivPosFactExp = line.substring(159,160);
		String varTickOptFlag = line.substring(160,161);

		Utils.prt(exchAcro+","+prodCommCode+","+prodTypeCode+","+prodName+","+settlePriceDecLoc+","+strikePriceDecLoc+","+
				settlePriceAlignCode+","+strikePriceAlignCode+","+contValueFact+","+standardCabOptValue+","+quotePosQtyPerCont+
				","+settleCurrISO+","+settleCurrByte+","+priceQuoteMethod+","+signForContValueFact+","+contValueFactMult+","+
				exerciseStyle+","+prodLongName+","+positonalProdIndic+","+moneyCalcMethod+","+valuationMethod+","+
				settleMethod+","+fxSpotDateCollGainCredRate+","+fxPreSpotDateCollGainCredRate+","+fxPreSpotDateNumOfDays+","+
				fxForwardCollGainCredRate+","+equivPosFactor+","+equivPosFactExp+","+signForEquivPosFactExp+","+varTickOptFlag);
		
	}
	
	private void parseFirstCombComm(String line){
	
		String exchAcro = line.substring(2,5);
		String combCommCode = line.substring(6,12);
		String riskExp = line.substring(12,13);
		String perfBondCurrISO = line.substring(13,16);
		String perfBondCurrCode = line.substring(16,17);
		String optMargStyle = line.substring(17,18);
		String limitOptValue = line.substring(18,19);
		String combMargMethFlag = line.substring(19,20);
		String commProdCode1 = line.substring(22,32);
		String conType1 = line.substring(32,35);
		String riskArrValDecLoc1 = line.substring(35,36);
		String riskArrValDecSign1 = line.substring(36,37);
		
		String prtLine = exchAcro+","+combCommCode+","+riskExp+","+perfBondCurrISO+","+
				perfBondCurrCode+","+optMargStyle+","+limitOptValue+","+combMargMethFlag+","+
				commProdCode1+","+conType1+","+riskArrValDecLoc1+","+riskArrValDecSign1;

		if(line.length()>53){
			
			String commProdCode2 = line.substring(38,48);
			String conType2 = line.substring(48,51);
			String riskArrValDecLoc2 = line.substring(51,52);
			String riskArrValDecSign2 = line.substring(52,53);
			prtLine = prtLine+","+commProdCode2+","+conType2+","+riskArrValDecLoc2+","+riskArrValDecSign2;

			if(line.length()>69) {
				String commProdCode3 = line.substring(54,64);
				String conType3 = line.substring(64,67);
				String riskArrValDecLoc3 = line.substring(67,68);
				String riskArrValDecSign3 = line.substring(68,69);
				prtLine = prtLine+","+commProdCode3+","+conType3+","+riskArrValDecLoc3+","+riskArrValDecSign3;

				if(line.length()>85) {
					String commProdCode4 = line.substring(70,80);
					String conType4 = line.substring(80,83);
					String riskArrValDecLoc4 = line.substring(83,84);
					String riskArrValDecSign4 = line.substring(84,85);
					prtLine = prtLine+","+commProdCode4+","+conType4+","+riskArrValDecLoc4+","+riskArrValDecSign4;
					
					if(line.length()>101) {
						String commProdCode5 = line.substring(86,96);
						String conType5 = line.substring(96,99);
						String riskArrValDecLoc5 = line.substring(99,100);
						String riskArrValDecSign5 = line.substring(100,101);
						prtLine = prtLine+","+commProdCode5+","+conType5+","+riskArrValDecLoc5+","+riskArrValDecSign5;
						if(line.length()>117) {
							String commProdCode6 = line.substring(102,112);
							String conType6 = line.substring(112,115);
							String riskArrValDecLoc6 = line.substring(115,116);
							String riskArrValDecSign6 = line.substring(116,117);
							prtLine = prtLine+","+commProdCode6+","+conType6+","+riskArrValDecLoc6+","+riskArrValDecSign6;
						}
					}
				}
			}
		}
		
		Utils.prt(prtLine);
		
	}
	
	private void parseSecondCombComm(String line) {
		
		String combCommCode = line.substring(2,8);
		String intraCommSpreadChargeMethCode = line.substring(8,10);
		String tierNumber1 = line.substring(10,12);
		String startMonth1 = line.substring(12,18);
		String endMonth1 = line.substring(18,24);
		String tierNumber2 = line.substring(24,26);
		String startMonth2 = line.substring(26,32);
		String endMonth2 = line.substring(32,38);
		String tierNumber3 = line.substring(38,40);
		String startMonth3 = line.substring(40,46);
		String endMonth3 = line.substring(46,52);
		String tierNumber4 = line.substring(52,54);
		String startMonth4 = line.substring(54,60);
		String endMonth4 = line.substring(60,66);
		String initToMaintRatioMember = line.substring(68,72);
		String initToMaintRatioHedger = line.substring(72,76);
		String initToMaintRatioSpec = line.substring(76,80);
		String prtLine = combCommCode+","+intraCommSpreadChargeMethCode+","+tierNumber1+","+startMonth1+","+endMonth1+","+
				tierNumber2+","+startMonth2+","+endMonth2+","+
				tierNumber3+","+startMonth3+","+endMonth3+","+
				tierNumber4+","+startMonth4+","+endMonth4+","+
				initToMaintRatioMember+","+initToMaintRatioHedger+","+initToMaintRatioSpec;

		if(line.length()>84) {
			String startDayWeekCode1 = line.substring(80,82);
			String endDayWeekCode1 = line.substring(82,84);
			prtLine = prtLine+startDayWeekCode1+","+endDayWeekCode1;

			if(line.length()>88) {
				String startDayWeekCode2 = line.substring(84,86);
				String endDayWeekCode2 = line.substring(86,88);
				prtLine = prtLine+startDayWeekCode2+","+endDayWeekCode2;				
				
				if(line.length()>92) {
					String startDayWeekCode3 = line.substring(88,90);
					String endDayWeekCode3 = line.substring(90,92);
					prtLine = prtLine+startDayWeekCode3+","+endDayWeekCode3;									
					
					if(line.length()>96) {
						String startDayWeekCode4 = line.substring(92,94);
						String endDayWeekCode4 = line.substring(94,96);
						prtLine = prtLine+startDayWeekCode4+","+endDayWeekCode4;
					}
				}
			}
			
		}
				
		Utils.prt(prtLine);
		
	}
	
	private void parseThirdCombComm(String line) {
		
		String combCommCode = line.substring(2,8);
		String delivChargeMethodCode = line.substring(8,10);
		
		String prtLine = combCommCode+","+delivChargeMethodCode;
		
		Integer delivChargeMethodCodeInt = Integer.parseInt(delivChargeMethodCode);
		if(delivChargeMethodCodeInt==10) {
			String numOfConMonths = line.substring(10,12);
			String monthNum1 = line.substring(12,14);
			String conMonth1 = line.substring(14,20);
			String chargeRatePerDeltaBySpread1 = line.substring(20,27);
			String chargeRatePerDeltaByOutright1 = line.substring(27,34);
			String monthNum2 = line.substring(34,36);
			String conMonth2 = line.substring(36,42);
			String chargeRatePerDeltaBySpread2 = line.substring(42,49);
			String chargeRatePerDeltaByOutright2 = line.substring(49,56);
			
			prtLine = prtLine+numOfConMonths+","+
					monthNum1+","+conMonth1+","+chargeRatePerDeltaBySpread1+","+chargeRatePerDeltaByOutright1+","+
					monthNum2+","+conMonth2+","+chargeRatePerDeltaBySpread2+","+chargeRatePerDeltaByOutright2;
			
		} else if(delivChargeMethodCodeInt==11) {
			String spotCommCode = line.substring(10,20);
			String basisRiskChargeRate = line.substring(20,27);
			
			prtLine = prtLine+","+spotCommCode+","+basisRiskChargeRate;
		}
		
		String shortOptMinChargeRate = line.substring(62,69);
		String riskMainBondAdjFactMember = line.substring(69,72);
		String riskMainBondAdjFactHedger = line.substring(72,75);
		String riskMainBondAdjFactSpec = line.substring(75,78);
		String shortOptMinCalcMethod = line.substring(78,79);
		
		prtLine = prtLine+","+shortOptMinChargeRate+","+riskMainBondAdjFactMember+","+riskMainBondAdjFactHedger+","+
				riskMainBondAdjFactSpec+","+shortOptMinCalcMethod;
		
		Utils.prt(prtLine);
	}
	
	private void parseCurrConvRate(String line) {

		String fromCurrISO = line.substring(2,5);
		String fromCurrByte = line.substring(5,6);
		String toCurrISO = line.substring(6,9);
		String toCurrByte = line.substring(9,10);
		String convMult = line.substring(10,20);
		
		String devConvMult = convMult.substring(0,4)+"."+convMult.substring(4);
		
		BigDecimal bdConvMult = new BigDecimal(devConvMult);
		
		Utils.prt(fromCurrISO+","+fromCurrByte+","+toCurrISO+","+toCurrByte+","+convMult+","+bdConvMult);
	}
	
	private void parseTieredScanned(String line) {
		
		String combCommCode = line.substring(2,8);
		String scanSpreadMethodCode = line.substring(8,10);
		String tierFields = line.substring(10,12);
		
		String prtLine = combCommCode+","+scanSpreadMethodCode+","+tierFields;
		
		Integer scanSpreadMethodCodeInt = Integer.parseInt(scanSpreadMethodCode);
		if(scanSpreadMethodCodeInt>2) {
			
			String tierNumber1 = line.substring(12,14);
			String startMonth1 = line.substring(14,20);
			String endMonth1 = line.substring(20,26);
			prtLine = prtLine+","+tierNumber1+","+startMonth1+","+endMonth1;
			
			if(line.length()>40) {
				String tierNumber2 = line.substring(26,28);
				String startMonth2 = line.substring(28,34);
				String endMonth2 = line.substring(34,40);
				prtLine = prtLine+","+tierNumber2+","+startMonth2+","+endMonth2;
				
				if(line.length()>54) {
					String tierNumber3 = line.substring(40,42);
					String startMonth3 = line.substring(42,48);
					String endMonth3 = line.substring(48,54);
					prtLine = prtLine+","+tierNumber3+","+startMonth3+","+endMonth3;
					
					if(line.length()>68) {
						String tierNumber4 = line.substring(54,56);
						String startMonth4 = line.substring(56,62);
						String endMonth4 = line.substring(62,68);
						prtLine = prtLine+","+tierNumber4+","+startMonth4+","+endMonth4;
						
						if(line.length()>82) {
							String tierNumber5 = line.substring(68,70);
							String startMonth5 = line.substring(70,76);
							String endMonth5 = line.substring(76,82);
							prtLine = prtLine+","+tierNumber5+","+startMonth5+","+endMonth5;
							
							if(line.length()>87) {
								String weightedFutPriceRiskCalcMethod = line.substring(82,83);
								
								String startDayWeekCode1 = line.substring(83,85);
								String endDayWeekCode1 = line.substring(85,87);
								prtLine = prtLine+","+weightedFutPriceRiskCalcMethod+","+
										startDayWeekCode1+","+endDayWeekCode1;
								
								if(line.length()>91) {
									String startDayWeekCode2 = line.substring(87,89);
									String endDayWeekCode2 = line.substring(89,91);
									prtLine = prtLine+","+startDayWeekCode2+","+endDayWeekCode2;
									
									if(line.length()>95) {
										String startDayWeekCode3 = line.substring(91,93);
										String endDayWeekCode3 = line.substring(93,95);
										prtLine = prtLine+","+startDayWeekCode3+","+endDayWeekCode3;

										if(line.length()>99) {
											String startDayWeekCode4 = line.substring(95,97);
											String endDayWeekCode4 = line.substring(97,99);
											prtLine = prtLine+","+startDayWeekCode4+","+endDayWeekCode4;
											
											if(line.length()>103) {
												String startDayWeekCode5 = line.substring(99,101);
												String endDayWeekCode5 = line.substring(101,103);
												prtLine = prtLine+","+startDayWeekCode5+","+endDayWeekCode5;

												if(line.length()>110) {
													String shortOptMinCharge1 = line.substring(103,110);
													prtLine = prtLine+","+shortOptMinCharge1;
													if(line.length()>117) {
														String shortOptMinCharge2 = line.substring(110,117);
														prtLine = prtLine+","+shortOptMinCharge2;
														if(line.length()>124) {
															String shortOptMinCharge3 = line.substring(117,124);
															prtLine = prtLine+","+shortOptMinCharge3;
															if(line.length()>131) {
																String shortOptMinCharge4 = line.substring(124,131);
																prtLine = prtLine+","+shortOptMinCharge4;
																if(line.length()>138) {
																	String shortOptMinCharge5 = line.substring(131,138);
																	prtLine = prtLine+","+shortOptMinCharge5;
																}
															}
														}
													}
												}
											}
										}
									}
								}										
							}
						}
					}
				}
			}
			
		}
		
		Utils.prt(prtLine);
		
	}
	
	private void parseIntraCommSeries(String line) {
		
		String combCommCode = line.substring(2,8);
		String spreadPriority = line.substring(8,13);
		String chargeRate = line.substring(13,20);
		String legMonth1 = line.substring(20,24);
		String legRemainder1 = line.substring(24,27);
		String legDeltaPerSpreadRatio1 = line.substring(27,33);
		String legMrktSide1 = line.substring(33,34);
		
		String prtLine = combCommCode+","+spreadPriority+","+chargeRate+","+
				legMonth1+","+legRemainder1+","+legDeltaPerSpreadRatio1+","+legMrktSide1;

		if(line.length()>48) {
			String legMonth2 = line.substring(34,38);
			String legRemainder2 = line.substring(38,41);
			String legDeltaPerSpreadRatio2 = line.substring(41,47);
			String legMrktSide2 = line.substring(47,48);
			
			prtLine = prtLine+legMonth2+","+legRemainder2+","+legDeltaPerSpreadRatio2+","+legMrktSide2; 
			if(line.length()>62) {
				String legMonth3 = line.substring(48,52);
				String legRemainder3 = line.substring(52,55);
				String legDeltaPerSpreadRatio3 = line.substring(55,61);
				String legMrktSide3 = line.substring(61,62);
				prtLine = prtLine+legMonth3+","+legRemainder3+","+legDeltaPerSpreadRatio3+","+legMrktSide3; 
				
				if(line.length()>76) {
					String legMonth4 = line.substring(62,66);
					String legRemainder4 = line.substring(66,69);
					String legDeltaPerSpreadRatio4 = line.substring(69,75);
					String legMrktSide4 = line.substring(75,76);
					prtLine = prtLine+legMonth4+","+legRemainder4+","+legDeltaPerSpreadRatio4+","+legMrktSide4; 
				}
			}
		}
				
		Utils.prt(prtLine);
		
	}
	
	private void parseIntraCommTiers(String line) {
		
		String combCommCode = line.substring(2,8);
		String intraCommSpreadMethodCode = line.substring(8,10);
		String spreadPriority = line.substring(10,12);
		String numOfLegs = line.substring(12,14);
		String chargeRate = line.substring(14,21);
		String legNumber1 = line.substring(21,23);
		String tierNumber1 = line.substring(23,25);
		String deltaPerSpreadRatio1 = line.substring(25,27);
		String mrktSide1 = line.substring(27,28);
		
		String prtLine = combCommCode+","+intraCommSpreadMethodCode+","+spreadPriority+","+numOfLegs+","+chargeRate+","+
				legNumber1+","+tierNumber1+","+deltaPerSpreadRatio1+","+mrktSide1;
		
		if(line.length()>35) {
			String legNumber2 = line.substring(28,30);
			String tierNumber2 = line.substring(30,32);
			String deltaPerSpreadRatio2 = line.substring(32,34);
			String mrktSide2 = line.substring(34,35);
			prtLine = prtLine+legNumber2+","+tierNumber2+","+deltaPerSpreadRatio2+","+mrktSide2;

			if(line.length()>42) {
				String legNumber3 = line.substring(35,37);
				String tierNumber3 = line.substring(37,39);
				String deltaPerSpreadRatio3 = line.substring(39,41);
				String mrktSide3 = line.substring(41,42);
				prtLine = prtLine+legNumber3+","+tierNumber3+","+deltaPerSpreadRatio3+","+mrktSide3;

				if(line.length()>49) {
					String legNumber4 = line.substring(42,44);
					String tierNumber4 = line.substring(44,46);
					String deltaPerSpreadRatio4 = line.substring(46,48);
					String mrktSide4 = line.substring(48,49);
					prtLine = prtLine+legNumber4+","+tierNumber4+","+deltaPerSpreadRatio4+","+mrktSide4;
					
					if(line.length()>56) {
						String legNumber5 = line.substring(49,51);
						String tierNumber5 = line.substring(51,53);
						String deltaPerSpreadRatio5 = line.substring(53,55);
						String mrktSide5 = line.substring(55,56);
						prtLine = prtLine+legNumber5+","+tierNumber5+","+deltaPerSpreadRatio5+","+mrktSide5;
						
						if(line.length()>63) {
							String legNumber6 = line.substring(56,58);
							String tierNumber6 = line.substring(58,60);
							String deltaPerSpreadRatio6 = line.substring(60,62);
							String mrktSide6 = line.substring(62,63);
							prtLine = prtLine+legNumber6+","+tierNumber6+","+deltaPerSpreadRatio6+","+mrktSide6;
							
							if(line.length()>70) {
								String legNumber7 = line.substring(63,65);
								String tierNumber7 = line.substring(65,67);
								String deltaPerSpreadRatio7 = line.substring(67,69);
								String mrktSide7 = line.substring(69,70);
								prtLine = prtLine+legNumber7+","+tierNumber7+","+deltaPerSpreadRatio7+","+mrktSide7;
								
								if(line.length()>77) {
									String legNumber8 = line.substring(70,72);
									String tierNumber8 = line.substring(72,74);
									String deltaPerSpreadRatio8 = line.substring(74,76);
									String mrktSide8 = line.substring(76,77);
									prtLine = prtLine+legNumber8+","+tierNumber8+","+deltaPerSpreadRatio8+","+mrktSide8;
								}
							}
						}
					}
					
				}
			}
		}
		Utils.prt(prtLine);
	}
	
	private void parseRiskArrayParams(String line) {
		
		String exchAcro = line.substring(2,5);
		String commCode = line.substring(5,15);
		String prodTypeCode = line.substring(15,18);
		String futConMonth = line.substring(18,24);
		String futConDayWeekCode = line.substring(24,26);
		String optConMonth = line.substring(27,33);
		String optConDayWeekCode = line.substring(33,35);
		String baseVol = line.substring(36,44);
		String volScanRange = line.substring(44,52);
		String futPriceScanRange = line.substring(52,57);
		String extremeMoveMult = line.substring(57,62);
		String extremeMoveCovFrac = line.substring(62,67);
		String intRate = line.substring(67,72);
		String timeToExpiration = line.substring(72,79);
		String lookaheadTime = line.substring(79,85);
		String deltaScalingFactor = line.substring(85,91);
		String settleDate = line.substring(91,99);
		String undCommCode = line.substring(99,109);
		String pricingModel = line.substring(109,111);
		String divYield = line.substring(111,119);
		String optExpRefPriceFlag = line.substring(119,120);
		String optExpRefPrice = line.substring(120,127);
		String optExpRefPriceSign = line.substring(127,128);
		String swapValueFactor = line.substring(128,142);
		String swapValueFactorExp = line.substring(142,144);
		String swapValueFactSign = line.substring(144,145);
		String baseVolExp = line.substring(145,147);
		String baseVolExpSign = line.substring(147,148);
		String volScanRangeExp = line.substring(148,150);
		String volScanRangeExpSign = line.substring(150,151);
		String discountFactor = line.substring(151,163);
		String volScanRangeQuotMeth = line.substring(163,164);
		String priceScanRangeQuotMeth = line.substring(164,165);

		String prtLine = exchAcro+","+commCode+","+prodTypeCode+","+futConMonth+","+futConDayWeekCode+","+optConMonth+","+
				optConDayWeekCode+","+baseVol+","+volScanRange+","+futPriceScanRange+","+extremeMoveMult+","+
				extremeMoveCovFrac+","+intRate+","+timeToExpiration+","+lookaheadTime+","+deltaScalingFactor+","+
				settleDate+","+undCommCode+","+pricingModel+","+divYield+","+optExpRefPriceFlag+","+optExpRefPriceFlag+","+
				optExpRefPrice+","+optExpRefPriceSign+","+swapValueFactor+","+swapValueFactorExp+","+swapValueFactSign+","+
				baseVolExp+baseVolExpSign+","+volScanRangeExp+","+volScanRangeExpSign+","+discountFactor+","+
				volScanRangeQuotMeth+","+priceScanRangeQuotMeth;

		if(line.length()>168) {
			String futPriceScanRangeExp = line.substring(165,167);
			String futPriceScanRangeExpSign = line.substring(167,168);
			prtLine = prtLine+","+futPriceScanRangeExp+","+futPriceScanRangeExpSign;
		}
		
		Utils.prt(prtLine);
	}
	
	private void parseDailyAdj(String line) {
		
		String exchAcro = line.substring(2,5);
		String prodCommCode = line.substring(5,15);
		String futConMonth = line.substring(15,21);
		String futConDayWeekCode = line.substring(21,23);
		String businessDate = line.substring(23,31);
		String dailyAdjRate = line.substring(31,44);
		String dailyAdjRateSign = line.substring(44,45);
		String dailyAdjPremDiscFlag = line.substring(45,46);
		String cumulAdjRate = line.substring(46,59);
		String cumulRateSign = line.substring(59,60);
		String cumulRatePremDiscFlag = line.substring(60,61);
		String shortRateFlag = line.substring(61,62);
		String longPosValMaintRate = line.substring(62,65);
		String shortPosValMaintRate = line.substring(65,68);
		String resetLongMarginPriceFlag = line.substring(68,69);
		String resetLongDownThreshhold = line.substring(69,72);
		String resetLongUpThreshhold = line.substring(72,75);
		String resetShortMarginPriceFlag = line.substring(75,76);
		String resetShortDownThreshhold = line.substring(76,79);
		String resetShortUpThreshhold = line.substring(79,82);
		String valueMaintProdClass = line.substring(82,88);
		
		Utils.prt(exchAcro+","+prodCommCode+","+futConMonth+","+futConDayWeekCode+","+businessDate+","+dailyAdjRate+","+
				dailyAdjRateSign+","+dailyAdjPremDiscFlag+","+cumulAdjRate+","+cumulRateSign+","+cumulRatePremDiscFlag+","+
				shortRateFlag+","+longPosValMaintRate+","+shortPosValMaintRate+","+resetLongMarginPriceFlag+","+
				resetLongDownThreshhold+","+resetLongUpThreshhold+","+resetShortMarginPriceFlag+","+
				resetShortDownThreshhold+","+resetShortUpThreshhold+","+valueMaintProdClass);
		
	}
	
	private void parseOptOnComb(String line) {
		
		String combMargMethodCode = line.substring(2,3);
		String prodCommCode = line.substring(3,13);
		String priceOffset = line.substring(13,20);
		
		Utils.prt(combMargMethodCode+","+prodCommCode+","+priceOffset);
		
	}
	
	private void parseSplitAlloc(String line) {
		
		String combProdCommCode = line.substring(2,12);
		String optOnCombProdCode = line.substring(12,22);
		
		Utils.prt(combProdCommCode+","+optOnCombProdCode);
		
	}
	
	private void parseDeltaSplitAlloc(String line) {
		
		String exchAcro = line.substring(2,5);
		String combProdCommCode = line.substring(5,15);
		String combType = line.substring(15,20);
		String combConMonth = line.substring(20,26);
		String combConDayWeekCode = line.substring(26,28);
		String legNumber = line.substring(35,38);
		String legRelationship = line.substring(38,39);
		String legRatio = line.substring(39,42);
		String legProdCommCode = line.substring(42,52);
		String legProdType = line.substring(52,55);
		String legConMonth = line.substring(55,61);
		String legConDayWeekCode = line.substring(61,63);
		String legRatioFracPart = line.substring(63,67);
		String legPriceAvailFlag = line.substring(67,68);
		String legPriceUsageFlag = line.substring(68,70);
		String legPrice = line.substring(70,77);
		String legPriceSign = line.substring(77,78);
		
		Utils.prt(exchAcro+","+combProdCommCode+","+combType+","+combConMonth+","+combConDayWeekCode+","+legNumber+","+
				legRelationship+","+legRatio+","+legProdCommCode+","+legProdType+","+legConMonth+","+legConDayWeekCode+","+
				legRatioFracPart+","+legPriceAvailFlag+","+legPriceUsageFlag+","+legPrice+","+legPriceSign);
		
	}
	
	private void parseCombCommGrps(String line) {
		
		String combCommGrpCode = line.substring(2,5);
		String combCommCode1 = line.substring(12,18);
		String prtLine = combCommGrpCode+","+combCommCode1;
		if(line.length()>24) {
			String combCommCode2 = line.substring(18,24);
			prtLine = prtLine+","+combCommCode2;
			if(line.length()>30) {
				String combCommCode3 = line.substring(24,30);
				prtLine = prtLine+","+combCommCode3;
				if(line.length()>36) {
					String combCommCode4 = line.substring(30,36);
					prtLine = prtLine+","+combCommCode4;
					if(line.length()>42) {
						String combCommCode5 = line.substring(36,42);
						prtLine = prtLine+","+combCommCode5;
						if(line.length()>48) {
							String combCommCode6 = line.substring(42,48);
							prtLine = prtLine+","+combCommCode6;
							if(line.length()>54) {
								String combCommCode7 = line.substring(48,54);
								prtLine = prtLine+","+combCommCode7;
								if(line.length()>60) {
									String combCommCode8 = line.substring(54,60);
									prtLine = prtLine+","+combCommCode8;
									if(line.length()>66) {
										String combCommCode9 = line.substring(60,66);
										prtLine = prtLine+","+combCommCode9;
										if(line.length()>72) {
											String combCommCode10 = line.substring(66,72);
											prtLine = prtLine+","+combCommCode10;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		Utils.prt(prtLine);
	}
	
	private void parseFirstPhysRec(String line) {
		
		String exchAcro = line.substring(2,5);
		String commProdCode = line.substring(5,15);
		String undCommProdCode = line.substring(15,25);
		String prodTypeCode = line.substring(25,28);
		String optRightCode = line.substring(28,29);
		String futConMonth = line.substring(29,35);
		String futConDayWeekCode = line.substring(35,37);
		String optConMonth = line.substring(38,44);
		String optConDayWeekCode = line.substring(44,46);
		String optStrikePrice = line.substring(47,54);
		String arrVal1 = line.substring(54,59);
		String arrValSign1 = line.substring(59,60);
		String arrVal2 = line.substring(60,65);
		String arrValSign2 = line.substring(65,66);
		String arrVal3 = line.substring(66,71);
		String arrValSign3 = line.substring(71,72);
		String arrVal4 = line.substring(72,77);
		String arrValSign4 = line.substring(77,78);
		String arrVal5 = line.substring(78,83);
		String arrValSign5 = line.substring(83,84);
		String arrVal6 = line.substring(84,89);
		String arrValSign6 = line.substring(89,90);
		String arrVal7 = line.substring(90,95);
		String arrValSign7 = line.substring(95,96);
		String arrVal8 = line.substring(96,101);
		String arrValSign8 = line.substring(101,102);
		String arrVal9 = line.substring(102,107);
		String arrValSign9 = line.substring(107,108);
		String highPrecSettlePrice = line.substring(108,122);
		String highPrecSettlePriceFlag = line.substring(122,123);
		
		Utils.prt(exchAcro+","+commProdCode+","+undCommProdCode+","+prodTypeCode+","+optRightCode+","+futConMonth+","+
				futConDayWeekCode+","+optConMonth+","+optConDayWeekCode+","+optStrikePrice+","+
				arrVal1+","+arrValSign1+","+
				arrVal2+","+arrValSign2+","+
				arrVal3+","+arrValSign3+","+
				arrVal4+","+arrValSign4+","+
				arrVal5+","+arrValSign5+","+
				arrVal6+","+arrValSign6+","+
				arrVal7+","+arrValSign7+","+
				arrVal8+","+arrValSign8+","+
				arrVal9+","+arrValSign9+","+
				highPrecSettlePrice+","+highPrecSettlePriceFlag);
		
	}
	
	private void parseSecondPhysRec(String line) {
		
		String exchAcro = line.substring(2,5);
		String commProdCode = line.substring(5,15);
		String undCommProdCode = line.substring(15,25);
		String prodTypeCode = line.substring(25,28);
		String optRightCode = line.substring(28,29);
		String futConMonth = line.substring(29,35);
		String futConDayWeekCode = line.substring(35,37);
		String optConMonth = line.substring(38,44);
		String optConDayWeekCode = line.substring(44,46);
		String optStrikePrice = line.substring(47,54);
		String arrVal10 = line.substring(54,59);
		String arrValSign10 = line.substring(59,60);
		String arrVal11 = line.substring(60,65);
		String arrValSign11 = line.substring(65,66);
		String arrVal12 = line.substring(66,71);
		String arrValSign12 = line.substring(71,72);
		String arrVal13 = line.substring(72,77);
		String arrValSign13 = line.substring(77,78);
		String arrVal14 = line.substring(78,83);
		String arrValSign14 = line.substring(83,84);
		String arrVal15 = line.substring(84,89);
		String arrValSign15 = line.substring(89,90);
		String arrVal16 = line.substring(90,95);
		String arrValSign16 = line.substring(95,96);
		String spanCompDelta = line.substring(96,101);
		String spanCompDeltaSign = line.substring(101,102);
		String impVolAsFrac = line.substring(102,110);
		String settlePrice = line.substring(110,117);
		String settlePriceSign = line.substring(117,118);
		String strikePriceSign = line.substring(118,119);
		String currentDelta = line.substring(119,124);
		String currentDeltaSign = line.substring(124,125);
		String currentDeltaBusDayFlag = line.substring(125,126);
		
		String prtLine = exchAcro+","+commProdCode+","+undCommProdCode+","+prodTypeCode+","+optRightCode+","+futConMonth+","+
				futConDayWeekCode+","+optConMonth+","+optConDayWeekCode+","+optStrikePrice+","+
				arrVal10+","+arrValSign10+","+
				arrVal11+","+arrValSign11+","+
				arrVal12+","+arrValSign12+","+
				arrVal13+","+arrValSign13+","+
				arrVal14+","+arrValSign14+","+
				arrVal15+","+arrValSign15+","+
				arrVal16+","+arrValSign16+","+
				spanCompDelta+","+spanCompDeltaSign+","+impVolAsFrac+","+impVolAsFrac+","+settlePrice+","+settlePriceSign+","+
				strikePriceSign+","+currentDelta+","+currentDeltaSign+","+currentDeltaBusDayFlag;
		
		if(line.length()>126) {
			String startOfDayPrice = line.substring(126,133);
			String startOfDayPriceSign = line.substring(133,134);
			String impVolExp = line.substring(134,136);
			String impVolExpSign = line.substring(136,137);
			String contValueFact = line.substring(137,151);
			String contValueFactExp = line.substring(151,153);
			String contValueFactExpSign = line.substring(153,154);
			String strikeValueFactor = line.substring(154,168);
			String strikeValueFactorExp = line.substring(168,170);
			String strikeValueFactorExpSign = line.substring(170,171);
			
			prtLine = prtLine+","+startOfDayPrice+","+
					startOfDayPriceSign+","+impVolExp+","+impVolExpSign+","+contValueFact+","+contValueFactExp+","+
					contValueFactExpSign+","+strikeValueFactor+","+strikeValueFactorExp+","+strikeValueFactorExpSign;
		}
		
		Utils.prt(prtLine);
		
	}
	
	private void parseFirstPhysRecFloat(String line) {
		
		String exchAcro = line.substring(2,5);
		String commProdCode = line.substring(5,15);
		String undCommProdCode = line.substring(15,25);
		String prodTypeCode = line.substring(25,28);
		String optRightCode = line.substring(28,29);
		String futConMonth = line.substring(29,35);
		String futConDayWeekCode = line.substring(35,37);
		String optConMonth = line.substring(38,44);
		String optConDayWeekCode = line.substring(44,46);
		String optStrikePrice = line.substring(47,54);
		String arrVal1 = line.substring(54,62);
		String arrValSign1 = line.substring(62,63);
		String arrVal2 = line.substring(63,71);
		String arrValSign2 = line.substring(71,72);
		String arrVal3 = line.substring(72,80);
		String arrValSign3 = line.substring(80,81);
		String arrVal4 = line.substring(81,89);
		String arrValSign4 = line.substring(89,90);
		String arrVal5 = line.substring(90,98);
		String arrValSign5 = line.substring(98,99);
		String arrVal6 = line.substring(99,107);
		String arrValSign6 = line.substring(107,108);
		String arrVal7 = line.substring(108,116);
		String arrValSign7 = line.substring(116,117);
		String arrVal8 = line.substring(117,125);
		String arrValSign8 = line.substring(125,126);
		String arrVal9 = line.substring(126,134);
		String arrValSign9 = line.substring(134,135);
		
		Utils.prt(exchAcro+","+commProdCode+","+undCommProdCode+","+prodTypeCode+","+optRightCode+","+futConMonth+","+
				futConDayWeekCode+","+optConMonth+","+optConDayWeekCode+","+optStrikePrice+","+
				arrVal1+","+arrValSign1+","+
				arrVal2+","+arrValSign2+","+
				arrVal3+","+arrValSign3+","+
				arrVal4+","+arrValSign4+","+
				arrVal5+","+arrValSign5+","+
				arrVal6+","+arrValSign6+","+
				arrVal7+","+arrValSign7+","+
				arrVal8+","+arrValSign8+","+
				arrVal9+","+arrValSign9);
		
	}
	
	private void parseSecondPhysRecFloat(String line) {
		
		String exchAcro = line.substring(2,5);
		String commProdCode = line.substring(5,15);
		String undCommProdCode = line.substring(15,25);
		String prodTypeCode = line.substring(25,28);
		String optRightCode = line.substring(28,29);
		String futConMonth = line.substring(29,35);
		String futConDayWeekCode = line.substring(35,37);
		String optConMonth = line.substring(38,44);
		String optConDayWeekCode = line.substring(44,46);
		String optStrikePrice = line.substring(47,54);
		String arrVal10 = line.substring(54,62);
		String arrValSign10 = line.substring(62,63);
		String arrVal11 = line.substring(63,71);
		String arrValSign11 = line.substring(71,72);
		String arrVal12 = line.substring(72,80);
		String arrValSign12 = line.substring(80,81);
		String arrVal13 = line.substring(81,89);
		String arrValSign13 = line.substring(89,90);
		String arrVal14 = line.substring(90,98);
		String arrValSign14 = line.substring(98,99);
		String arrVal15 = line.substring(99,107);
		String arrValSign15 = line.substring(107,108);
		String arrVal16 = line.substring(108,116);
		String arrValSign16 = line.substring(116,117);
		String spanCompDelta = line.substring(117,122);
		String spanCompDeltaSign = line.substring(122,123);
		String impVolAsFrac = line.substring(123,131);
		String settlePrice = line.substring(131,138);
		String settlePriceSign = line.substring(138,139);
		String strikePriceSign = line.substring(139,140);
		String currentDelta = line.substring(140,145);
		String currentDeltaSign = line.substring(145,146);
		String currentDeltaBusDayFlag = line.substring(146,147);
		String startOfDayPrice = line.substring(147,154);
		String startOfDayPriceSign = line.substring(154,155);
		String impVolExp = line.substring(155,157);
		String impVolExpSign = line.substring(157,158);
		String contValueFact = line.substring(158,172);
		String contValueFactExp = line.substring(172,174);
		String contValueFactExpSign = line.substring(174,175);
		String strikeValueFactor = line.substring(175,189);
		String strikeValueFactorExp = line.substring(189,191);
		String strikeValueFactorExpSign = line.substring(191,192);
		
		Utils.prt(exchAcro+","+commProdCode+","+undCommProdCode+","+prodTypeCode+","+optRightCode+","+futConMonth+","+
				futConDayWeekCode+","+optConMonth+","+optConDayWeekCode+","+optStrikePrice+","+
				arrVal10+","+arrValSign10+","+
				arrVal11+","+arrValSign11+","+
				arrVal12+","+arrValSign12+","+
				arrVal13+","+arrValSign13+","+
				arrVal14+","+arrValSign14+","+
				arrVal15+","+arrValSign15+","+
				arrVal16+","+arrValSign16+","+
				spanCompDelta+","+spanCompDeltaSign+","+impVolAsFrac+","+impVolAsFrac+","+settlePrice+","+settlePriceSign+","+
				strikePriceSign+","+currentDelta+","+currentDeltaSign+","+currentDeltaBusDayFlag+","+startOfDayPrice+","+
				startOfDayPriceSign+","+impVolExp+","+impVolExpSign+","+contValueFact+","+contValueFactExp+","+
				contValueFactExpSign+","+strikeValueFactor+","+strikeValueFactorExp+","+strikeValueFactorExpSign);
		
	}
	
	private void parseInterCommSpreads(String line) {
		
		String commGrpCode = line.substring(2,5);
		String spreadPriority = line.substring(5,9);
		String spreadCreditRate = line.substring(9,16);
		String exchAcroLeg1 = line.substring(16,19);
		String requiredForScanningBasedSpreadFlag1 = line.substring(19, 20);
		String combCommCode1 = line.substring(20,26);
		String deltaSpreadRatio1 = line.substring(26,33);
		String spreadSide1 = line.substring(33,34);
		String exchAcroLeg2 = line.substring(34,37);
		String requiredForScanningBasedSpreadFlag2 = line.substring(37,38);
		String combCommCode2 = line.substring(38,44);
		String deltaSpreadRatio2 = line.substring(44,51);
		String spreadSide2 = line.substring(51,52);
		String exchAcroLeg3 = line.substring(52,55);
		String requiredForScanningBasedSpreadFlag3 = line.substring(55,56);
		String combCommCode3 = line.substring(56,62);
		String deltaSpreadRatio3 = line.substring(62,69);
		String spreadSide3 = line.substring(69,70);
		String exchAcroLeg4 = line.substring(70,73);
		String requiredForScanningBasedSpreadFlag4 = line.substring(73,74);
		String combCommCode4 = line.substring(74,80);
		String deltaSpreadRatio4 = line.substring(80,87);
		String spreadSide4 = line.substring(87,88);
		String interSpreadMethodCode = line.substring(88,90);
		String targetExchAcro = line.substring(90,93);
		String targetLegReqFlag = line.substring(93,94);
		String targetCombCommCode = line.substring(94,100);
		String creditCalcMethod = line.substring(100,101);

		String prtLine = commGrpCode+","+spreadPriority+","+spreadCreditRate+","+
				exchAcroLeg1+","+requiredForScanningBasedSpreadFlag1+","+combCommCode1+","+deltaSpreadRatio1+","+spreadSide1+","+
				exchAcroLeg2+","+requiredForScanningBasedSpreadFlag2+","+combCommCode2+","+deltaSpreadRatio2+","+spreadSide2+","+
				exchAcroLeg3+","+requiredForScanningBasedSpreadFlag3+","+combCommCode3+","+deltaSpreadRatio3+","+spreadSide3+","+
				exchAcroLeg4+","+requiredForScanningBasedSpreadFlag4+","+combCommCode4+","+deltaSpreadRatio4+","+spreadSide4+","+
				interSpreadMethodCode+","+targetExchAcro+","+targetLegReqFlag+","+targetCombCommCode+","+creditCalcMethod;

//				tierNumberLeg1+","+tierNumberLeg2+","+tierNumberLeg3+","+tierNumberLeg4+","+
//				spreadGroupFlag;

		if(line.length()>103) {
			String tierNumberLeg1 = line.substring(101,103);
			prtLine = prtLine+","+tierNumberLeg1;
			
			if(line.length()>105) {
				String tierNumberLeg2 = line.substring(103,105);
				prtLine = prtLine+","+tierNumberLeg2;

				if(line.length()>107) {
					String tierNumberLeg3 = line.substring(105,107);
					prtLine = prtLine+","+tierNumberLeg3;

					if(line.length()>109) {
						String tierNumberLeg4 = line.substring(107,109);
						prtLine = prtLine+","+tierNumberLeg4;

						if(line.length()>110) {
							String spreadGroupFlag = line.substring(109,110);
							prtLine = prtLine+","+spreadGroupFlag;

							if(line.length()>117) {
								String targetLegDeltaSpreadRatio = line.substring(110,117);
								String minNumOfLegsReq = line.substring(117,121);
								
								prtLine = prtLine+","+targetLegDeltaSpreadRatio+","+minNumOfLegsReq;
								if(line.length()>122) {
									String spreadCreditRateDefSepFlag = line.substring(121,122);
									String spreadCreditRateLeg1 = line.substring(122,129);
									String spreadCreditRateLeg2 = line.substring(129,136);
									String spreadCreditRateLeg3 = line.substring(136,143);
									String spreadCreditRateLeg4 = line.substring(143,150);
									String regStatusEligibilityCode = line.substring(150,151);
									prtLine = prtLine+","+spreadCreditRateDefSepFlag+","+
											spreadCreditRateLeg1+","+spreadCreditRateLeg2+","+spreadCreditRateLeg3+","+spreadCreditRateLeg4+","+
											regStatusEligibilityCode;
								}
							}
						}
					}
				}
			}
		}

		Utils.prt(prtLine);
		
	}
}
