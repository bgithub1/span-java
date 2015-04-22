package com.billybyte.spanjava.mongo.helpers;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.billybyte.commonstaticmethods.LoggingUtils;
import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.mongo.MongoDoc;
import com.billybyte.mongo.MongoWrapper;
import com.billybyte.spanjava.mongo.SpanMongoUtils;
import com.billybyte.spanjava.mongo.span.CombCommProdIdDoc;
import com.billybyte.spanjava.mongo.span.FlatCombCommProdIdDoc;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;

public class FlattenCombCommDb {

	private final MongoWrapper m;
	private final Integer batchSize;
	
	private final LoggingUtils logger = new LoggingUtils(this.getClass());

	public FlattenCombCommDb(MongoWrapper m, Integer batchSize) {
		this.m = m;
		this.batchSize = batchSize;
	}
	
	public static void main(String[] args) {
		
		String mongoIp = args[0];
		Integer mongoPort = Integer.parseInt(args[1]);
		
		Gson gson = new Gson();
		
		int batchSize = 5000;
		
		try {
			
			Utils.prt("Starting...");
			
			MongoWrapper m = new MongoWrapper(mongoIp, mongoPort);
			
			DB sourceDb = m.getDB(SpanMongoUtils.COMB_COMM_DB);
			DBCollection sourceColl = sourceDb.getCollection(SpanMongoUtils.COMB_COMM_CL);

			DB destDb = m.getDB(SpanMongoUtils.FLAT_COMB_COMM_DB);
			DBCollection destColl = destDb.getCollection(SpanMongoUtils.FLAT_COMB_COMM_CL);

			destColl.remove(new BasicDBObject());
			
			DBCursor cursor = sourceColl.find();

			List<MongoDoc> uploadList = new ArrayList<MongoDoc>();
			try {
				
				while(cursor.hasNext()) {
					CombCommProdIdDoc doc = gson.fromJson(cursor.next().toString(), CombCommProdIdDoc.class);
					String combCommCode = doc.getCombCommCode().trim();
					
					for(SpanProdId id:doc.getProdIdSet()) {
						uploadList.add(new FlatCombCommProdIdDoc(id, combCommCode));
					}
					
				}
				
			} finally {
				
				SpanMongoUtils.batchInsert(destColl, uploadList, batchSize);
				
				cursor.close();
				
				Utils.prt("finished");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
	}
	
	public void flatten() {
				
		Gson gson = new Gson();
				
		try {
			
			logger.info("Starting...");
						
			DB sourceDb = m.getDB(SpanMongoUtils.COMB_COMM_DB);
			DBCollection sourceColl = sourceDb.getCollection(SpanMongoUtils.COMB_COMM_CL);

			DB destDb = m.getDB(SpanMongoUtils.FLAT_COMB_COMM_DB);
			DBCollection destColl = destDb.getCollection(SpanMongoUtils.FLAT_COMB_COMM_CL);

			destColl.remove(new BasicDBObject());
			
			DBCursor cursor = sourceColl.find();

			List<MongoDoc> uploadList = new ArrayList<MongoDoc>();
			try {
				
				while(cursor.hasNext()) {
					CombCommProdIdDoc doc = gson.fromJson(cursor.next().toString(), CombCommProdIdDoc.class);
					String combCommCode = doc.getCombCommCode().trim();
					
					for(SpanProdId id:doc.getProdIdSet()) {
						uploadList.add(new FlatCombCommProdIdDoc(id, combCommCode));
					}
					
				}
				
			} finally {
				
				SpanMongoUtils.batchInsert(destColl, uploadList, batchSize);
				
				cursor.close();
				
				logger.info("finished");
			}
			
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
}
