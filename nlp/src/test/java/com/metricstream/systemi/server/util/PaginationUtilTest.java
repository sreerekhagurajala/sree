package com.metricstream.systemi.server.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.metricstream.systemi.server.common.Controller;

public class PaginationUtilTest {
	
	private static final String CLASS_ID = PaginationUtilTest.class.getName();
	
	static String ORIGINAL_QUERY = "select metric_id from si_metrics_t";
	static String PAGINATED_QUERY = "SELECT /*+ FIRST_ROWS */ PAGINATED.* FROM (   SELECT ACTUAL_DATA.*, ROWNUM PAGINATED_RNUM FROM ( "+ ORIGINAL_QUERY +"   ) ACTUAL_DATA WHERE ROWNUM <= 20) PAGINATED WHERE PAGINATED.PAGINATED_RNUM >= 1";
	static String COUNT_QUERY = "SELECT COUNT(*) FROM (" + ORIGINAL_QUERY + ")";
	
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
	
	@Test (expected = IllegalArgumentException.class)
	public void getPaginatedQueryTest() {
		int pageNum = 1;
		int pageSize = 20;
		Assert.assertEquals("", PAGINATED_QUERY, PaginationUtil.getPaginatedQuery(ORIGINAL_QUERY, pageNum, pageSize));
		pageNum = -1;
		pageSize = 20;
		Assert.assertEquals("", null, PaginationUtil.getPaginatedQuery(ORIGINAL_QUERY, pageNum, pageSize));
	}
	
	@Test
	public void getPaginatedQueryTest2() throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PaginationUtil.PAGE_NUMBER, 1);
		map.put(PaginationUtil.PAGE_SIZE, 20);
		Assert.assertEquals("", PAGINATED_QUERY, PaginationUtil.getPaginatedQuery(connection, ORIGINAL_QUERY, map));
	}
	
	@Test
	public void getCountQuery() {
		Assert.assertEquals("", COUNT_QUERY, PaginationUtil.getCountQuery(ORIGINAL_QUERY));
	}
	
	@Test
	public void getCountQuery1() throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PaginationUtil.NEED_COUNT, "true");
		Assert.assertEquals("", COUNT_QUERY, PaginationUtil.getCountQuery(connection, ORIGINAL_QUERY, map));
		map.clear();
		Assert.assertEquals("", null, PaginationUtil.getCountQuery(connection, ORIGINAL_QUERY, map));
	}
	
	@Test
	public void buildPaginationDetailsTest() {
		int pageSize = 20;
		int pageNum = 1;
		boolean countNeeded = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PaginationUtil.PAGE_NUMBER, pageNum);
		map.put(PaginationUtil.PAGE_SIZE, pageSize);
		map.put(PaginationUtil.NEED_COUNT, countNeeded);
		Assert.assertEquals("", map, PaginationUtil.buildPaginationDetails(pageNum, pageSize, countNeeded));
	}

}
