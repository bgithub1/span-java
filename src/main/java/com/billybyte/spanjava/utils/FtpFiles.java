package com.billybyte.spanjava.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.billybyte.commonstaticmethods.CollectionsStaticMethods;
import com.billybyte.commonstaticmethods.Dates;
import com.billybyte.commonstaticmethods.Utils;

public class FtpFiles {
	/**
	 * 
	 * @author bperlman1
	 *
	 */
	public static class FtpArgs{
		private final String yyyyMmDdMast;
		private final int hourOfNewDay;
		private final int minOfNewDay;
		private final List<String[]> ftpFileNameTemplateList ;
		private final String zipFileDestFolder;
		private final String unZipFileDestFolder;
		/**
		 * 
		 * @param yyyyMmDdMast   like "yyyyMmDd" it's the string that you replace with the real date in the strings
		 * 			that are contained in the list below;
		 * @param ftpFileNameTemplateList  list of string arrays with 2 elements 
		 * 			 like {"ftp://ftp.cmegroup.com/span/data/ice/","ice.yyyyMmDd.pa5"}
		 * @param hourOfNewDay like 18
		 * @param minOfNewDay like 01 or 30
		 */
		public FtpArgs(
				String yyyyMmDdMast, 
				List<String[]> ftpFileNameTemplateList,
				int hourOfNewDay,
				int minOfNewDay,
				String zipFileDestFolder,
				String unZipFileDestFolder) {
			super();
			this.yyyyMmDdMast = yyyyMmDdMast;
			this.ftpFileNameTemplateList = ftpFileNameTemplateList;
			this.hourOfNewDay = hourOfNewDay;
			this.minOfNewDay = minOfNewDay;
			this.zipFileDestFolder = zipFileDestFolder;
			this.unZipFileDestFolder = unZipFileDestFolder;
		}
	
	}
	
	
	private final String argsXmlFileName;
	private final Class<?> argsXmlResourceClass;
	
	public FtpFiles(String argsXmlFileName, String argsXmlResourceClassName){
		this.argsXmlFileName = argsXmlFileName;
 		try {
			argsXmlResourceClass = argsXmlResourceClassName!=null ? 
					Class.forName(argsXmlResourceClassName) : null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			throw Utils.IllArg(FtpFiles.class, e1.getMessage());
		}
	}
	
	public List<String> unZipFromFtp(FtpArgs ftpArgs) {

		int hourOfNewDay = ftpArgs.hourOfNewDay;
		int minOfNewDay = ftpArgs.minOfNewDay;
		
		Calendar c = Dates.getSettlementDay(Calendar.getInstance(), hourOfNewDay, minOfNewDay);
		
		String yyyyMmDdActual = Dates.getYyyyMmDdFromCalendar(c).toString();
		// *********** new code 20131007 ***************
		String yyyyAct = yyyyMmDdActual.substring(0, 4);
		String mmAct = yyyyMmDdActual.substring(4, 6);
		String ddAct = yyyyMmDdActual.substring(6, 8);
		// *********** end new code 20131007 ***************
		String yyyyMmDdMast = ftpArgs.yyyyMmDdMast;
		// *********** new code 20131007 ***************
		String yyyyMast = yyyyMmDdMast.substring(0, 4);
		String mmMast = yyyyMmDdMast.substring(4, 6);
		String ddMast = yyyyMmDdMast.substring(6, 8);
		// *********** end new code 20131007 ***************
		
		List<String> ret = new ArrayList<String>();
		for(String[] ftpPathTemplate : ftpArgs.ftpFileNameTemplateList){
			// this code is replace on 20131007 ************
//			String ftpUrlFirstPart = addSlash(ftpPathTemplate[0]);
			// end this code is replace on 20131007 ************
			
			// *********** new code 20131007 ***************
			String p0 = ftpPathTemplate[0].replace(yyyyMast, yyyyAct);
			p0 = p0.replace(mmMast, mmAct);
			p0 = p0.replace(ddMast, ddAct);
			String ftpUrlFirstPart = p0;
			// first create source Ftp address

			String p1 = ftpPathTemplate[1].replace(yyyyMast, yyyyAct);
			p1 = p1.replace(mmMast, mmAct);
			p1 = p1.replace(ddMast, ddAct);
			// *********** new code 20131007 ***************
			// this code is replace on 20131007 ************
//			String ftpUrlSecondPart= ftpPathTemplate[1].replace(yyyyMmDdMast, yyyyMmDdActual);
			String ftpUrlSecondPart= p1;
			// end this code is replace on 20131007 ************
			String fullFtpPath= ftpUrlFirstPart + ftpUrlSecondPart + ".zip";
			// next create destination folder path
			String zipFileDestPath = addSlash(ftpArgs.zipFileDestFolder) + ftpUrlSecondPart+ ".zip" ;
			
			
			Utils.prt(" *************** downloading ftp file "+fullFtpPath+" **************");
			Utils.copyFile(fullFtpPath, zipFileDestPath);
			Utils.prt(" *************** unziping ftp file "+fullFtpPath+ " to " + zipFileDestPath + " **************");
			try {
				ZipFile zp = new ZipFile(zipFileDestPath);
				Enumeration<? extends ZipEntry> enumer = zp.entries();
				while(enumer.hasMoreElements()){
					ZipEntry entry = enumer.nextElement();
				       if(entry.isDirectory()) {
				           // Assume directories are stored parents first then children.
				           System.err.println("Extracting directory: " + entry.getName());
				           // This is not robust, just for demonstration purposes.
				           (new File(entry.getName())).mkdir();
				           continue;
				         }

				         Utils.prtObMess(this.getClass(),"Extracting file: " + entry.getName());
				         String destFilePath = addSlash(ftpArgs.unZipFileDestFolder)+entry.getName();
				         Utils.copyFile(zp.getInputStream(entry),
				        		 destFilePath);
				         ret.add(destFilePath);  
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			
		}
		return ret;
		
	}
	
	public  List<String> unZipFromFtp() {
 		
		FtpArgs xsArgs = Utils.getXmlData(FtpArgs.class, argsXmlResourceClass, argsXmlFileName);
		
		return unZipFromFtp(xsArgs);
		
	}
	
	private static String addSlash(String folder){
		String ret = folder;
		String endChar = folder.substring(folder.length()-1,folder.length()); 
		if(endChar.compareTo("/")!=0){
			ret = ret+"/";
		}
		return ret;
	}
	
	public static void main(String[] args) {
		int i = 0;
		String ftpArgsPath = args[i];
		i+=1;
		String ftpArgsResourceClass = (args!=null && args.length>i) ? args[i] : null;
		i+=1;
		boolean writeExampleFtp = (args!=null && args.length>i) ?
				new Boolean(args[i]) :
					false;
				
		if(writeExampleFtp){
			String[][] ftpFileNameTemplateArray = {
					{"ftp://ftp.cmegroup.com/span/data/cme/","cme.YYYYMMDD.s.pa2"},
					{"ftp://ftp.cmegroup.com/span/data/nyb/","nyb.YYYYMMDD.s.pa2"},
					{"ftp://ftp.cmegroup.com/span/data/ice/","ice.YYYYMMDD.pa5"},
			};
			List<String[]> ftpFileNameTemplateList = CollectionsStaticMethods.listFromArray(ftpFileNameTemplateArray);
			String yyyyMmDdMast = "YYYYMMDD";
			int hourOfNewDay = 18;
			int minOfNewDay = 10;
			String zipFileDestFolder = "./";
			String unZipFileDestFolder = "./";
			FtpArgs ftpArgs = new FtpArgs(
					yyyyMmDdMast, ftpFileNameTemplateList, 
					hourOfNewDay, minOfNewDay, 
					zipFileDestFolder, unZipFileDestFolder);
			
			try {
				Utils.writeToXml(ftpArgs, "./ftpArgs.xml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// get ftp
		FtpFiles ftpFiles = 
				new FtpFiles(ftpArgsPath, ftpArgsResourceClass);
		ftpFiles.unZipFromFtp();		
	}
}
