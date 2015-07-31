package com.billybyte.spanjava.mains;

import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.billybyte.commoninterfaces.QueryInterface;
import com.billybyte.commonstaticmethods.Dates;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.SecDef;
import com.billybyte.marketdata.SecDefQueryAllMarkets;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.helpers.FlattenCombCommDb;
import com.billybyte.spanjava.mongo.helpers.FlattenInterCommSpreadColl;
import com.billybyte.spanjava.parsers.ice.ParseIceSpanFileToMongoSettle;
import com.billybyte.spanjava.utils.FtpFiles;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;

public class RunBuildSettlesImpVolFromSpan {
	public static class Args{
		private final String mongoIp;
		private final Integer mongoPort;
		private final Boolean needsAuth;
		private final String mongoUser;
		private final String mongoPw;
		private final String ftpArgsXmlPath;
		private final String ftpArgsResourceClassName;
		private final List<String> spanFileNames;
		private final Boolean clearSettles;
		private final Boolean clearSpan;
		/**
		 * 
		 * @param mongoIp
		 * @param mongoPort
		 * @param needsAuth
		 * @param mongoUser
		 * @param mongoPw
		 * @param ftpArgsXmlPath
		 * @param ftpArgsResourceClassName
		 * @param spanFileNames
		 * @param clearSettles
		 * @param clearSpan
		 */
		public Args(String mongoIp, Integer mongoPort, Boolean needsAuth,
				String mongoUser, String mongoPw,
				String ftpArgsXmlPath,
				String ftpArgsResourceClassName,
				List<String> spanFileNames,
				Boolean clearSettles,
				Boolean clearSpan ) {
			super();
			this.mongoIp = mongoIp;
			this.mongoPort = mongoPort;
			this.needsAuth = needsAuth;
			this.mongoUser = mongoUser;
			this.mongoPw = mongoPw;
			this.ftpArgsXmlPath = ftpArgsXmlPath;
			this.ftpArgsResourceClassName = ftpArgsResourceClassName;
			this.spanFileNames = spanFileNames;
			this.clearSettles = clearSettles;
			this.clearSpan = clearSpan;
		}
	
		@SuppressWarnings("unused")
		private static void writeXmlExample(String filePath){
			String mongoIp = "127.0.0.1";
			Integer mongoPort = 27017;
			Boolean needsAuth = false;
			String mongoUser = null; // use null if needsAuth is false
			String mongoPw = null;  // use null if needsAuth is false
			String ftpArgsXmlPath = "./ftpArgs.xml";
			String ftpArgsResourceClassName = null;
			Args args = new Args(mongoIp, mongoPort, needsAuth, mongoUser, mongoPw, ftpArgsXmlPath, ftpArgsResourceClassName,null,null,null);
			try {
				Utils.writeToXml(args, filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	public static void main(String[] args) {

		int k = 0;
		String argsXmlFileName = args[k];
		k+=1;
		String argsResourceClassName = args.length>k ? args[k] : null;
		k+=1;
		String[] runBuildMongoXmlSettlesFromSpanSettles_args = 
				args.length>k ? Arrays.copyOfRange(args, k, k+2) : null;
		
		Class<?> argsXmlResourceClass=null;
		if(argsResourceClassName!=null && argsResourceClassName.compareTo("")>0){
			try {
				argsXmlResourceClass = Class.forName(argsResourceClassName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		Args xsArgs  = Utils.getXmlData(Args.class, argsXmlResourceClass, argsXmlFileName);

		// get other args like mongIp,port etc
		String mongoIp = xsArgs.mongoIp;
		Integer mongoPort = xsArgs.mongoPort;
		Boolean needsAuth = xsArgs.needsAuth;
		String mongoUser = xsArgs.mongoUser;
		String mongoPw = xsArgs.mongoPw;
		Boolean clearSpan = xsArgs.clearSpan;
		Boolean clearSettles = xsArgs.clearSettles;
		
		// get ftp files
		List<String> spanFileNames = null;
		if(xsArgs.spanFileNames==null){
			String ftpArgsPath = xsArgs.ftpArgsXmlPath;
			String ftpArgsResourcePath = xsArgs.ftpArgsResourceClassName;
			FtpFiles ftpFiles = 
					new FtpFiles(ftpArgsPath, ftpArgsResourcePath);
			
			spanFileNames = ftpFiles.unZipFromFtp();
		}else{
			spanFileNames = xsArgs.spanFileNames;
		}
		
		

		try {

			@SuppressWarnings("unchecked")
//			Map<String,String> convMap = Utils.getXmlData(Map.class, SpanUtils.class, "spanConvMap.xml");
			Map<String,String> convMap = (Map<String,String>)Utils.getXmlData(Map.class, null, "spanConvMap.xml");

			@SuppressWarnings("unchecked")
//			Map<String,String> iceConvMap = Utils.getXmlData(Map.class, SpanUtils.class, "iceProdConvMap.xml");
			Map<String,String> iceConvMap = (Map<String,String>)Utils.getXmlData(Map.class, null, "iceProdConvMap.xml");

			// build cme and nyb to dbs
			MongoWrapper m = new MongoWrapper(mongoIp,mongoPort);
			
			Calendar startTime = Calendar.getInstance();
			
			Utils.prtObMess(RunBuildSettlesImpVolFromSpan.class,"Starting span build process");
			
			ProcessSpanFileToDb spanDbBuilder = new ProcessSpanFileToDb(m, needsAuth, mongoUser, mongoPw);
			
			
			if(clearSpan==null || clearSpan){
				spanDbBuilder.clearCollections();
			}

			String iceFile = null; // assume no ice
			for(String spanFileName:spanFileNames){
				if(spanFileName.contains("ice") || spanFileName.toLowerCase().contains("ipe")){
					iceFile = spanFileName;
				}else{
					spanDbBuilder.processSpan(spanFileName);
					Utils.prtObMess(RunBuildSettlesImpVolFromSpan.class,"Finished processing span file: " + spanFileName);
				}
			}
			
			

			String[] flattenArgs = new String[]{mongoIp, mongoPort.toString()};
			
			FlattenCombCommDb.main(flattenArgs);
			FlattenInterCommSpreadColl.main(flattenArgs);
			
			// build span array db to settle db

			List<DB> dbList = new ArrayList<DB>();
			
			DB priceSpecDb = m.getDB(SpanMongoUtils.PRICE_SPEC_DB);
			dbList.add(priceSpecDb);
			DB spanSettleDb = m.getDB(SpanMongoUtils.SETTLE_DB);
			dbList.add(spanSettleDb);
			DB spanArrayDb = m.getDB(SpanMongoUtils.ARRAY_DB);
			dbList.add(spanArrayDb);
			DB spanImpVolDb = m.getDB(SpanMongoUtils.IMP_VOL_DB);
			dbList.add(spanImpVolDb);
			
			if(needsAuth) {
				for(DB db:dbList) {
					if(!db.authenticate(mongoUser, mongoPw.toCharArray())){
						Utils.prtObErrMess(RunBuildSettlesImpVolFromSpan.class, "Unable to authorize connection to db: "+db.getName());
					}
				}
			}
			
			Utils.prtObMess(RunBuildSettlesImpVolFromSpan.class,"Generated settlement arrays from span DBs");
			
			DBCollection priceSpecColl = priceSpecDb.getCollection(SpanMongoUtils.PRICE_SPEC_CL);
			DBCollection settleColl = spanSettleDb.getCollection(SpanMongoUtils.SETTLE_CL);
			DBCollection spanArrayColl = spanArrayDb.getCollection(SpanMongoUtils.ARRAY_CL);
			DBCollection impVolColl = spanImpVolDb.getCollection(SpanMongoUtils.IMP_VOL_CL);

			QueryInterface<String,SecDef> sdQuery = new SecDefQueryAllMarkets();
//			QueryInterface<String,SecDef> sdQuery = new SecDefQuerySpanMongo(null,null,true);
			
			RunGenerateSettlesFromSpanArrayDb settleFromSpanDbBuilder = 
					new RunGenerateSettlesFromSpanArrayDb(priceSpecColl, spanArrayColl, settleColl, impVolColl, sdQuery);

			if(clearSettles==null || clearSettles){
				settleFromSpanDbBuilder.clearSettleAndImpVolCollection();
			}
//			DBObject searchObj = new BasicDBObject();
//			searchObj.put("contractId.prodId.prodCommCode", "LO4");
			settleFromSpanDbBuilder.processSpan(true, convMap,null);
			
			Utils.prtObMess(RunBuildSettlesImpVolFromSpan.class,"Finished building cme and nyb settles from span dbs, starting to build settles from Ice span file");
			
			// build ice settles to db			
			if(iceFile!=null) {
				ParseIceSpanFileToMongoSettle iceSettleBuilder = new ParseIceSpanFileToMongoSettle(new FileReader(iceFile), settleColl);
				
				iceSettleBuilder.processSpan(true, iceConvMap);
			}

			Utils.prtObMess(RunBuildSettlesImpVolFromSpan.class,"Finished building ice settles, mapping CME implied vols to corresponding ICE products");
			
			String[] impVolArgs = new String[]{
					mongoIp,mongoPort.toString(),needsAuth.toString(),mongoUser,mongoPw};
//					args[0], args[1], args[2], args[3], args[4]};
			RunBuildIceImpVolsFromCmeProds.main(impVolArgs);
			
			Utils.prtObMess(RunBuildSettlesImpVolFromSpan.class,"Finished building vols for Ice/CME associated products, building future vols from option chains");
			
			FindFutImpVolFromOptChain.insertImpVolsForFutures(settleColl, impVolColl);
			
			Calendar endTime = Calendar.getInstance();

			// get odd product settlements and implied vols for stuff like
			//  LO1 thru LO5, etc.
			Calendar dateOfSpan = Dates.getSettlementDay(Calendar.getInstance(), 16, 10);
			RunBuildSecDefsFromSpanArrays oddProductBuilder = 
					new RunBuildSecDefsFromSpanArrays(mongoIp, mongoPort, "prodIdMap.xml", dateOfSpan);
			oddProductBuilder.process(true, true);
			
			if(runBuildMongoXmlSettlesFromSpanSettles_args!=null){
				RunBuildMongoXmlSettlesFromSpanSettles.main(runBuildMongoXmlSettlesFromSpanSettles_args);
			}


			
			Utils.prtObMess(RunBuildSettlesImpVolFromSpan.class,"Finished - took "+(endTime.getTimeInMillis()-startTime.getTimeInMillis())/1000+" secs");
//			System.exit(0);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
}
