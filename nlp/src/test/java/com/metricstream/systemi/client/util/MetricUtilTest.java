package com.metricstream.systemi.client.util;

import java.sql.Connection;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.metricstream.systemi.interfaces.MSCacheInterface;
import com.metricstream.systemi.server.common.Controller;

public class MetricUtilTest {
	
	private static final String CLASS_ID = MetricUtilTest.class.getName();
	
	private Connection connection;

	@Before
	public void init() throws Exception
	{		
		Controller controller = Controller.getThis();
		if(controller == null) {
			String input[] = new String[]{"client.config-test.xml"};		
			Controller.create().init(input);			
		}
		connection = Controller.getConnectionPool().getTransactionalConnection(CLASS_ID);
	}
	
	@After
	public void shutdown() throws Exception{
		Controller.getConnectionPool().returnConnection(connection);
	}
	
	@Test
	public void getMSCacheTest() {
		MSCacheInterface<String, Map> cache = MetricUtil.getMSCache();
		Assert.assertTrue("", (cache instanceof MSCacheInterface));
	}
	
	@Test
	public void getInfoletIdTest() {
		Assert.assertEquals("", 100000, MetricUtil.getInfoletId(connection, "MS - List of Active Infolets"));
		Assert.assertEquals("", -1, MetricUtil.getInfoletId(connection, ""));
	}
	
	
}
