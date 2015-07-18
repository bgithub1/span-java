package com.billybyte.spanjava;

import java.util.Arrays;
import java.util.List;

import com.billybyte.spanjava.mongo.FromSpanFilesGenerator;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;

import junit.framework.TestCase;

public class TestGetUnder extends TestCase{
	private final String mongoIp = "127.0.0.1";
	private final int mongoPort = 27022;

	public void test1(){
		FromSpanFilesGenerator fsg = new  FromSpanFilesGenerator(mongoIp, mongoPort);
		List<String[]> testItemList = 
				Arrays.asList(new String[][]{
						{"LO","NYM","OOF","CL"},
						{"ON","NYM","OOF","NG"},
						{"OH","NYM","OOF","HO"},
						{"WA","NYM","OOC","CA"},
				});
		for(String[] testItem : testItemList){
			SpanProdId optSpanProdId = 
					new SpanProdId(testItem[1], testItem[0], testItem[2]);
			SpanProdId underSpanProdId = fsg.getUnderlyingSpanProdId(optSpanProdId);
			assertEquals(testItem[3],underSpanProdId.getProdCommCode());
		}
	}
}
