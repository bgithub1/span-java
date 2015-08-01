package com.billybyte.spanjava;

import java.util.Map;

import com.billybyte.commonstaticmethods.Utils;
import com.billybyte.spanjava.resources.SpanUtils;

import junit.framework.TestCase;

public class TestResourcesInPackages extends TestCase{
	@SuppressWarnings("unchecked")
	public void test1(){
		Map<String,String> bestOptSymbolMap = Utils.getXmlData(Map.class, SpanUtils.class, "bestOptSymbolMap.xml");
		assertTrue(bestOptSymbolMap!=null && bestOptSymbolMap.size()>0);
		Map<String,String> iceCmeAssocMap = Utils.getXmlData(Map.class, SpanUtils.class, "iceToCmeProdConvMap.xml");
		assertTrue(iceCmeAssocMap!=null && iceCmeAssocMap.size()>0);

	}
}
