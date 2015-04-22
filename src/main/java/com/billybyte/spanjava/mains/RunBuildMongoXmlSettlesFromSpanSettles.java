package com.billybyte.spanjava.mains;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

import com.billybyte.commonstaticmethods.Dates;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.marketdata.SettlementDataImmute;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.mongo.MongoXml;
import com.billybyte.spanjava.mongo.SpanImpVolDoc;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.SpanSettleDoc;

import com.mongodb.DBCursor;

public class RunBuildMongoXmlSettlesFromSpanSettles {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String beanXmlPathOrFileName = args[0];
		String classNameInPkgOfBeanXmlAsResource = 
				(args.length>1 && args[1].length()>2) ? args[1] : null;
		
		
		Map<String, Object> beanMap = 
				Utils.springGetAllBeans(beanXmlPathOrFileName, classNameInPkgOfBeanXmlAsResource);
		// get mongo instanc  e of span dbs and mongo instance of MongoXml dbs 
		MongoWrapper spanMongo = (MongoWrapper)beanMap.get("spanMongoWrapperBean");
		MongoXml<SettlementDataImmute> settleDb = (MongoXml<SettlementDataImmute>)beanMap.get("mongoXmlSettle");
		MongoXml<BigDecimal> impVolDb = (MongoXml<BigDecimal>)beanMap.get("mongoDocImpVol");
		
		Integer hourOfNewDay = (Integer)beanMap.get("hourOfNewDay");
		Integer minOfNewDay = (Integer)beanMap.get("minOfNewDay");
		Calendar settleDay = Dates.getSettlementDay(Calendar.getInstance(), hourOfNewDay, minOfNewDay) ;
		Long yyyyMmDd = Dates.getYyyyMmDdFromCalendar(settleDay);
		
		DBCursor cur = SpanMongoUtils.getSpanSettleCursor(spanMongo);
		Utils.prtObMess(RunBuildMongoXmlSettlesFromSpanSettles.class,"getting settlements from span file ...");
		Map<String, SettlementDataImmute> settleMap = 
				SpanSettleDoc.getSettlementDataInterfaceMapFromMongoDocs(cur, yyyyMmDd);
		Utils.prtObMess(RunBuildMongoXmlSettlesFromSpanSettles.class,"batch insert of settlements from span file to MongoXml ...");
		
//		if(new Boolean(MessageBox.MessageBoxNoChoices("delete all old MongoXml settles?", "true"))){
//			settleDb.deleteAll();
//		}
		Boolean deletePreviousMongoXmlSettles = 
				(Boolean)beanMap.get("deletePreviousMongoXmlSettles");
		if(deletePreviousMongoXmlSettles){
			settleDb.deleteAll();
			settleDb.batchInsert(settleMap);
		}else{
			settleDb.removeMultipleEntries(settleMap.keySet());
			settleDb.batchInsert(settleMap);
//			settleDb.updateMultipleEntries(settleMap);
			
		}
		
		cur = SpanMongoUtils.getSpanImpVolCursor(spanMongo);
		Utils.prtObMess(RunBuildMongoXmlSettlesFromSpanSettles.class,"getting ImpliedVols from span file ...");
		Map<String , BigDecimal> implVolMap = 
				SpanImpVolDoc.getImpVolFromMongoDocs(cur, yyyyMmDd);
		Utils.prtObMess(RunBuildMongoXmlSettlesFromSpanSettles.class,"batch insert of ImpliedVols from span file to MongoXml ...");

//		if(new Boolean(MessageBox.MessageBoxNoChoices("delete all old ImpliedVols?", "true"))){
//			impVolDb.deleteAll();
//		}
		Boolean deletePreviousMongoXmlImpVols = 
				(Boolean)beanMap.get("deletePreviousMongoXmlImpVols");
		if(deletePreviousMongoXmlImpVols){
			impVolDb.deleteAll();
			impVolDb.batchInsert(implVolMap);
		}else{
			impVolDb.removeMultipleEntries(implVolMap.keySet());
			impVolDb.batchInsert(implVolMap);
		}
		spanMongo.close();
		settleDb.close();
		impVolDb.close();
		
		beanMap=null;
		Utils.prtObMess(RunBuildMongoXmlSettlesFromSpanSettles.class, " finished all builds");
		System.exit(0);
		
	}
	
}
