package com.metricstream.systemi.client.servlet.strategy;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import static com.metricstream.util.Check.nvl;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ServerSideValidationStrategyTest {
	private static ServerSideValidationStrategy ssvStrategy = null;
	private static ValidationContext ssvContext = null;
	private static Map<String, Map<String,String>> formLovMap = null;
	
	
	@Test
	public void lovValidationStrategyTest() {
		Object cachedObj = formLovMap.get("LOV_KEY");
		cachedObj = nvl(cachedObj, formLovMap.get("LOV_KEY"));
		ssvStrategy.setCachedObj(cachedObj);
		ssvStrategy.setFieldValue("100000");
		ssvStrategy.setMLOVDelimiter("");
		ssvStrategy.setFieldAsMultiValue(false);
		assertTrue(ssvContext.executeStrategy());
		assertTrue(ssvStrategy.validateStrategy());
	}
	
	@Test
	public void mLovValidationStrategy() {
		Object cachedObj = formLovMap.get("MLOV_KEY");
		cachedObj = nvl(cachedObj, formLovMap.get("LOV_KEY"));
		ssvStrategy.setCachedObj(cachedObj);
		ssvStrategy.setFieldValue("10,20,30");
		ssvStrategy.setMLOVDelimiter(",");
		ssvStrategy.setFieldAsMultiValue(true);
		assertTrue(ssvContext.executeStrategy());
		assertTrue(ssvStrategy.validateStrategy());
	}
	
	@Test
	public void ssvValidationWithCachedObjNull() {
		Object cachedobj = formLovMap.get("KEY");
		ssvStrategy.setCachedObj(cachedobj);
		assertFalse(ssvContext.executeStrategy());
		assertFalse(ssvStrategy.validateStrategy());
	}
	
	@Test
	public void dataTamperedSSV() {
		Object cachedObj = formLovMap.get("LOV_KEY");
		cachedObj = nvl(cachedObj, formLovMap.get("LOV_KEY"));
		ssvStrategy.setCachedObj(cachedObj);
		ssvStrategy.setFieldValue("101010");
		ssvStrategy.setMLOVDelimiter("");
		ssvStrategy.setFieldAsMultiValue(false);
		assertFalse(ssvContext.executeStrategy());
		assertFalse(ssvStrategy.validateStrategy());
	}
	
	@BeforeClass
	public static void setupData() {
		formLovMap = new HashMap<String, Map<String,String>>();
		pupulateFormLovMap(formLovMap);
		ssvStrategy = new ServerSideValidationStrategy();
		ssvStrategy.setServerSideValidationMap(formLovMap);
		ssvContext = formLovMap != null ? new ValidationContext(ssvStrategy) : null;
	}

	private static void pupulateFormLovMap(Map<String, Map<String,String>> formLovMap) {
		Map<String, String> lovMap = new HashMap<String, String>();
		Map<String, String> mLovMap = new HashMap<String, String>();
		lovMap.put("100000", "SYSTEMI");
		lovMap.put("100001", "pfadmin");
		lovMap.put("100002", "appsadmin");
		lovMap.put("100003", "Functional.Expert");
		mLovMap.put("10", "Platform");
		mLovMap.put("20", "AppStudio");
		mLovMap.put("30", "Apps");
		mLovMap.put("40", "PS&PE");
		formLovMap.put("LOV_KEY", lovMap);
		formLovMap.put("MLOV_KEY", mLovMap);
	}

}
