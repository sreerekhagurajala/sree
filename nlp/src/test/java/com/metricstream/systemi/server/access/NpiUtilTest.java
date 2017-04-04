/**
 * 
 */
package com.metricstream.systemi.server.access;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
/**
 * @author kartik.kumar
 *
 */
public class NpiUtilTest {
	
	//NpiUtil nUtil = null;
	
	Connection connection = null;
	//long x = 123456L;	
	
	Long x = (long) -1;

	@Before
	public void setUp() throws Exception {
		
		//nUtil = new NpiUtil();
		connection = mock(Connection.class);
	}

	@After
	public void tearDown() throws Exception {
		
		//nUtil = null;
		connection = null;
	}

	@Test
	public void testGetRealColumnNamesForAliases() throws SQLException {
		
		//mocking
		//calling
		//verify
		
		String infoletId = "pqr";
		CallableStatement stmt = mockObjects();
		Map<String,NpiColumnMap> mapObj = null;
		
		mapObj = NpiUtil.getRealColumnNamesForAliases(connection, infoletId);
		
		Iterator<String> it =  mapObj.keySet().iterator();
		String key = null;
		NpiColumnMap obj = null;
		
		while(it.hasNext()){
			 key = it.next();
			 obj = mapObj.get(key);
			 //break;
		}
		
		// below two section is verifying
		// here we are verifying actual vs expected
		
		assertEquals(key,"colName");
		assertEquals(obj.getReal(),"real");
		assertEquals(obj.getSeq(),1);
		assertEquals(obj.getAlias(),"alias");
		assertEquals(obj.getResultColumnName(),"colName");
		
		// here we are verifying existing api during call flow

		Mockito.verify(stmt).setLong(1, x);
		Mockito.verify(stmt).setString(2, "pqr");
		Mockito.verify(stmt).registerOutParameter(3, Types.ARRAY, "VARCHAR2_ARRAY");
		Mockito.verify(stmt).registerOutParameter(4, Types.NUMERIC);
		Mockito.verify(stmt).registerOutParameter(5, Types.VARCHAR);
		Mockito.verify(stmt).execute();
		
		//Mockito.verify(stmt,Mockito.atLeastOnce()).execute();
	}
	
	@Test
	public void testHasSetOperations(){
		String sqlStatement = "select * from si_reports_t where report_id=100287";
		boolean expected = false;
		boolean actual = NpiUtil.hasSetOperations(sqlStatement);
		
		assertEquals(expected, actual);
	}
	
	@Test	
	public void testsetOperationsHandledQuery() throws SQLException{		
		String sqlStatemt = "xyz";
		mockObjects();	
		NpiUtil.setOperationsHandledQuery(connection, x, sqlStatemt);
		
	}
	
/*	@Test	
	public void testSetOperationsHandledQuery() throws SQLException{		
		String sqlStatemt = "select * from SI_METRICS_T";
		mockObjects();
		String expected = "select * from SI_METRICS_T";
		String actual = NpiUtil.setOperationsHandledQuery(connection, x, sqlStatemt);
		assertEquals(expected, actual);
	} */
	
	private CallableStatement mockObjects() throws SQLException{

		CallableStatement stmt = mock(CallableStatement.class);
		ResultSet columns = mock(ResultSet.class);

		Array arr = mock(Array.class);

		when(
				connection
						.prepareCall("{call SI_COMMON_SV.GET_REAL_ALIAS_COLUMNS(?,?,?,?,?)}"))
				.thenReturn(stmt);
		when(stmt.getInt(4)).thenReturn(0);
		when(stmt.getArray(3)).thenReturn(arr);
		when(arr.getResultSet()).thenReturn(columns);
		when(columns.next()).thenReturn(true).thenReturn(false);
		when(columns.getString(2))
				.thenReturn("CustomerName~real~alias~colName");

		return stmt;

	}
}
