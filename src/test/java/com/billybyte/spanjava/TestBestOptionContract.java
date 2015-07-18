package com.billybyte.spanjava;

import java.util.Arrays;
import java.util.List;

import com.billybyte.spanjava.mongo.FromSpanFilesGenerator;
import com.billybyte.spanjava.mongo.span.subtypes.SpanProdId;

import junit.framework.TestCase;

public class TestBestOptionContract extends TestCase {
	private final String mongoIp = "127.0.0.1";
	private final int mongoPort = 27022;
	
	public void test1(){
		
		FromSpanFilesGenerator fsg = new  FromSpanFilesGenerator(mongoIp, mongoPort);
		List<String[]> testItemList = 
				Arrays.asList(new String[][]{
						{"CL","NYM","LO"},
						{"NG","NYM","LN"},
						{"HO","NYM","OH"},
						{"CA","NYM","WA"},
						
				});
		for(String[] testItem : testItemList){
			SpanProdId spi = fsg.getBestOptionContract(testItem[0],testItem[1]);
			assertEquals(testItem[2], spi.getProdCommCode());
		}
	}
}
