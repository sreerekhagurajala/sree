package com.metricstream.systemi.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.metricstream.systemi.server.common.Controller;

public class ConfigUtilsTest {

	@Before
	public void init() throws Exception
	{		
		Controller controller = Controller.getThis();
		if(controller == null) {
			String input[] = new String[]{"client.config-test.xml"};		
			Controller.create().init(input);			
		}
	}
	
	@Test
	public void isInfoletPaginationNeededTest() {
		Assert.assertEquals("", true, ConfigUtils.isInfoletPaginationNeeded());
	}
	
	@Test
	public void isInfoletColumnSubsetNeededTest() {
		Assert.assertEquals("", true, ConfigUtils.isInfoletColumnSubsetNeeded());
	}
	
	@Test
	public void isInfoletRunLogActiveTest() {
		Assert.assertEquals("", true, ConfigUtils.isInfoletRunLogActive());
	}

	@Test
	public void isAutoIndexingActiveTest() {
		Assert.assertEquals("", true, ConfigUtils.isAutoIndexingActive());
	}
}
