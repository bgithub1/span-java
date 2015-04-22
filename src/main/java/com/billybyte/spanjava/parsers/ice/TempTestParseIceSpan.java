package com.billybyte.spanjava.parsers.ice;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.resources.SpanUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class TempTestParseIceSpan {
	public static void main(String[] args) {
//		String folder = "/Users/bperlman1/dropbox/jrtr/deployment_3_0/meteorlaunch/bsr/xml/db/unzipfolder/";
//		String fileName = "ice.20131004.pa5";
		String folder = "/Users/bperlman1/Downloads/";
		String fileName = "IPE1004F.SP5";
		String spanFileName = folder+fileName;
		
		try {
			
			MongoWrapper m = new MongoWrapper("127.0.0.1",27022);
			DB db = m.getDB(SpanMongoUtils.SETTLE_DB);
			
			DBCollection coll = db.getCollection(SpanMongoUtils.SETTLE_CL);
			
			
			Calendar beforeTime = Calendar.getInstance();
			ParseIceSpanFileToMongoSettle spanParser = new ParseIceSpanFileToMongoSettle(new FileReader(spanFileName), coll);
			
			@SuppressWarnings("unchecked")
			Map<String,String> prodConvMap = Utils.getXmlData(Map.class, SpanUtils.class, "iceProdConvMap.xml");
			
			
			Utils.prt("started processing span file...");
			
			spanParser.processSpan(true, prodConvMap);
			
			Calendar afterTime = Calendar.getInstance();

			Utils.prt("took "+(afterTime.getTimeInMillis()-beforeTime.getTimeInMillis())+" ms");
			
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
