/*
 * SYSTEMi Copyright (c) 2000, 2014, MetricStream Inc and/or its affiliates. All rights reserved.
 * @author Shyam Vytla(shyamvytla@metricstream.com)
 * created December 16, 2014
 * $Id: ReportsutilsTest.java 2014-12-16 06:17:48Z bar.team $
 *
 */
package com.metricstream.systemi.client.servlet.servant;

//import static com.metricstream.systemi.client.servlet.servant.ServantTest.getConnection;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;

import com.metricstream.systemi.server.common.Controller;

public class ReportsutilsTest {
	private static final String CLASS_ID = ReportsutilsTest.class.getName();

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

	private Connection getConnection() {
		return connection;
	}
	
	@Test
	public void isReportOwnerTest(){
		long reportId = 100000;
		String userId = "100000";
		boolean isOwner = Reportsutils.isReportOwner(getConnection(), reportId, userId);

		System.out.println(CLASS_ID + ": Is the user- " +userId + "owner for the report- " +reportId+ "," + isOwner);
	}
}
