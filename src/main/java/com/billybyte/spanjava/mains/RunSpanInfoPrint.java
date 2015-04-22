package com.billybyte.spanjava.mains;


import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlType;

import com.billybyte.commonstaticmethods.CollectionsStaticMethods;
import com.billybyte.commonstaticmethods.JaxbStuff;
import com.billybyte.commonstaticmethods.Reflection;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.SettlementDataImmute;
import com.billybyte.spanjava.generated.Alias;
import com.billybyte.spanjava.generated.CalDef;
import com.billybyte.spanjava.generated.ClearingOrg;
import com.billybyte.spanjava.generated.Definitions;
import com.billybyte.spanjava.generated.Exchange;
import com.billybyte.spanjava.generated.Fut;
import com.billybyte.spanjava.generated.FutPf;
import com.billybyte.spanjava.generated.Holidays;
import com.billybyte.spanjava.generated.OocPf;
import com.billybyte.spanjava.generated.OofPf;
import com.billybyte.spanjava.generated.Opt;
import com.billybyte.spanjava.generated.PointInTime;
import com.billybyte.spanjava.generated.Series;
import com.billybyte.spanjava.generated.SpanFile;
import com.billybyte.spanjava.resources.SpanUtils;
/**
 * Print Information for the exchange span xml file like cme.20121012.s.cust.spn.xml.
 * These files are found at locations like:
 * 			ftp://ftp.cmegroup.com/span/data/cme/xml/   or
 * 			ftp://ftp.cmegroup.com/span/data/ice/xml/
 * @author bperlman1
 *
 */
public class RunSpanInfoPrint {
	public enum ExchPfListType{
		futPf,oofPf,oocPf
	}
	private static String DEFAULT_EXCH = "NYM";
	private static String DEFAULT_PRODUCT = "CL";
	
	
	/**
	 * 
	 * @param args - args[0] = a file like 
	 * "/Users/bperlman1/Downloads/cme.20120613.s.cust.spn.xml"
	 * 				args[1] if present is an exchange like NYM
	 * 				args[2] if present is a product like "CL"
	 * 
	 * You can use VIM to open the actual xml file, which is huge.
	 * To facilate searching the file using "find", search for the first <exchange>
	 * 	tag, and then search for </exchange> tags.  You will then see the beginning
	 * 	and the end of exchange records.  You can play the same trick for any other 
	 * 	level.
	 * 
	 * For example, if you wanted to find the G3 3 M CSO FIN contract for Natural Gas
	 *   (3 month cso), you would search for <exchange>, then search for <exch>NYM,
	 *   then search for <oocpf>, then search for the <series> tag,and then search for
	 *   <pe> tag that matches the monthyear that you want.  If you want the
	 *   2013 V/F cso, then search for a <pe> of 201310.  Each strike for each put/call
	 *   will be in a an <opt> tag.
	 * 
	 */
		public static void main(String[] args) {
			int i=0;
			String spanXmlFilePath = args[i];
			i+=1;
			ExchPfListType listType = (args==null || args.length<i+1)?ExchPfListType.futPf:ExchPfListType.valueOf(args[i]);
			i+=1;
			String 	exchangeString = (args==null || args.length<i+1)?DEFAULT_EXCH:args[i];
			i+=1;
			String productString =  (args==null || args.length<i+1)?DEFAULT_PRODUCT:args[i];
			
			
			boolean printHolidays = true;
			SpanFile spanFile;
			try {
				spanFile = JaxbStuff.getFromJaxb(SpanFile.class, spanXmlFilePath);
				Definitions defs = spanFile.getDefinitions();
				PointInTime pit = spanFile.getPointInTime();
				ClearingOrg clearOrg = pit.getClearingOrg();
				List<Exchange> eList = clearOrg.getExchange();
				for(Exchange exch:eList){
					Utils.prt("exch: "+exch.getExch());
					if(exch.getExch().compareTo(exchangeString)==0){
						switch(listType){
						case futPf:
							List<FutPf> futPfList = exch.getFutPf();
							for(FutPf pf: futPfList){
								if(pf.getPfCode().compareTo(productString)==0){
									String[] propNames = 
											pf.getClass().getAnnotation(XmlType.class).propOrder();
									Utils.prt(
											Arrays.toString(propNames));
									SpanUtils.printProductInfo( CollectionsStaticMethods.listFromArray(new Object[]{pf}),
											propNames);
									List<Fut> futList = pf.getFut();
									XmlType xmlType = Fut.class.getAnnotation(XmlType.class);
									Utils.prt(Arrays.toString(xmlType.propOrder()));
									Reflection.printListObjects((List)futList, xmlType.propOrder());
//									for(Fut fut:futList){
//										Utils.prt(fut.getPe()+","+fut.getLdot()+","+fut.getP()+","+fut.getScanRate().getPriceScan()+","+fut.getScanRate().getVolScan());
//									}
								}
							}
							break;
						case oocPf:
							List<OocPf> oocPfList = exch.getOocPf();
//							printProductInfo( (List)oocPfList);
							for(OocPf pf: oocPfList){
								if(pf.getPfCode().compareTo(productString)!=0){
									continue;
								}
								String[] propNames = 
										pf.getClass().getAnnotation(XmlType.class).propOrder();
								Utils.prt(
										Arrays.toString(propNames));
								SpanUtils.printProductInfo( 
										CollectionsStaticMethods.listFromArray(
										new Object[]{pf}),propNames);
								SpanUtils.printOptionSeriesValues(Opt.class.getAnnotation(XmlType.class), pf.getSeries());
							}
							break;
						case oofPf:
							List<OofPf> oofPfList = exch.getOofPf();
//							printProductInfo( (List)oofPfList);
							for(OofPf pf: oofPfList){
								if(pf.getPfCode().compareTo(productString)!=0){
									continue;
								}
								String[] propNames = 
											pf.getClass().getAnnotation(XmlType.class).propOrder();
									Utils.prt(
											Arrays.toString(propNames));
									SpanUtils.printProductInfo( 
											CollectionsStaticMethods.listFromArray(
											new Object[]{pf}),propNames);
								SpanUtils.printOptionSeriesValues(Opt.class.getAnnotation(XmlType.class), pf.getSeries());
							}

							break;
						default:
							break;
						
						}
					}
				}
				if(printHolidays){
					for(CalDef cd:defs.getCalDef()){
						Utils.prt("holiday description:" +cd.getDescription().toString());
						Holidays holidays = cd.getHolidays();
						for(BigInteger holiday:holidays.getDt()){
							Utils.prt("holiday: "+holiday.toString());
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
}
