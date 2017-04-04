package com.metricstream.systemi.server.push;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.infinispan.manager.DefaultCacheManager;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.metricstream.systemi.cache.infinispan.InfinispanImpl;
import com.metricstream.systemi.constant.prt.Property;
import com.metricstream.systemi.interfaces.MSCacheInterface;
import com.metricstream.systemi.sql.SQLHelper;

public class PushFormUtilTest {
	public static DefaultCacheManager manager;
	public static MSCacheInterface<String,Map> cache; 
	static String EGRCP_FORM_ENGINE="EGRCP_FORM_ENGINE";
	static String TEMP_CACHE="TEMP_CACHE";
	
	@BeforeClass
	public static void setUpBeforeClass(){
		Properties cacheProperties = new Properties();
		cacheProperties.put(Property.CACHE_CONFIG_PATH, "ecp-infinispan.xml");
		cache = new InfinispanImpl<String,Map>();
		((InfinispanImpl<String, Map>)cache).init(cacheProperties, null);
	}
	
	@Test
	@Ignore
	public void testGetWFTSConfigValue(){
		assertEquals(true, PushFormUtil.getWFTSConfigValue(Property.WORKFLOW_TRANSACTION_SECURITY_ENABLED));
	}
	
	@Test
	public void testIsWorkFlowTranactionSecurityPerForm(){
		long infoletId = 100;
		String query = "SELECT XML FROM SI_METRICS WHERE METRIC_ID=" + infoletId;
		String enabledXml = "<?xml version=\"1.0\"?><METRIC><FORM_AUTOSAVE_ENABLED>Y</FORM_AUTOSAVE_ENABLED><WORKFLOW_TRANSACTION_SECURITY_ENABLED>Y</WORKFLOW_TRANSACTION_SECURITY_ENABLED></METRIC>";
		String disabledXml = "<?xml version=\"1.0\"?><METRIC><FORM_AUTOSAVE_ENABLED>Y</FORM_AUTOSAVE_ENABLED><WORKFLOW_TRANSACTION_SECURITY_ENABLED>N</WORKFLOW_TRANSACTION_SECURITY_ENABLED></METRIC>";
		String xmlWithOutTag = "<?xml version=\"1.0\"?><METRIC><FORM_AUTOSAVE_ENABLED>Y</FORM_AUTOSAVE_ENABLED></METRIC>";
		
		Connection connectionMock = Mockito.mock(Connection.class);
		Statement statementMock = Mockito.mock(Statement.class);
		ResultSet resultSetMock = Mockito.mock(ResultSet.class);
		try {
			Mockito.when(connectionMock.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY)).thenReturn(statementMock);
			Mockito.when(statementMock.executeQuery(query)).thenReturn(resultSetMock);
			Mockito.when(SQLHelper.getResultSet(connectionMock, query)).thenReturn(resultSetMock);
			/*Making rs.next() returning true for three times 
				1 for testing disable flag
				2 for testing enable flag
				3 for testing without flag*/
			Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
			Mockito.when(resultSetMock.getString("XML")).thenReturn(enabledXml);
		} catch (SQLException e) {
			System.out.println("SQLException occured while mocking resultset in testIsWorkFlowTranactionSecurityPerForm method "+e);
		} catch (Exception e){
			System.out.println("Exception occured while mocking resultset in testIsWorkFlowTranactionSecurityPerForm method "+e);
		}
		assertTrue(PushFormUtil.isWorkFlowTranactionSecurityPerForm(infoletId, connectionMock));
		
		try {
			Mockito.when(resultSetMock.getString("XML")).thenReturn(disabledXml);
		} catch (SQLException e) {
			System.out.println("SQLException occured while mocking resultset in testIsWorkFlowTranactionSecurityPerForm method "+e);
		}
		assertFalse(PushFormUtil.isWorkFlowTranactionSecurityPerForm(infoletId, connectionMock));
		
		try {
			Mockito.when(resultSetMock.getString("XML")).thenReturn(xmlWithOutTag);
		} catch (SQLException e) {
			System.out.println("SQLException occured while mocking resultset in testIsWorkFlowTranactionSecurityPerForm method "+e);
		}
		assertFalse(PushFormUtil.isWorkFlowTranactionSecurityPerForm(infoletId, connectionMock));
	}
	@Test
	public void testClearPushFormCachePerForm(){
		String cacheKey = "SID:"+100000+":ID:"+100001+":PID:"+100002+":UID:"+100005;
		cache.put(cacheKey, new HashMap<String,String>(), MSCacheInterface.EGRCP_FORM_ENGINE);
		assertTrue(PushFormUtil.clearPushFormCachePerForm(cache, MSCacheInterface.EGRCP_FORM_ENGINE, cacheKey));
//		assertFalse(PushFormUtil.clearPushFormCachePerForm(cache, MSCacheInterface.EGRCP_FORM_ENGINE, cacheKey));
	}
	
	@Test
	public void testClearPushFormCachePerUser(){
		String sessionId = "100000";
		String cacheKey = "SID:"+sessionId+":ID:"+100001+":PID:"+100002+":UID:"+100005;
		
		cache.put(cacheKey, new HashMap<String,String>(), MSCacheInterface.EGRCP_FORM_ENGINE);
		assertTrue(PushFormUtil.clearPushFormCachePerUser(cache, EGRCP_FORM_ENGINE, cacheKey, sessionId));
	}
	
	@Test
	public void testConstructWFTSAccessMapKey() {
		int fieldSeq = 10;
		String fieldValue = "My Audit (17-Mar-2015)";
		String fieldName = "AUDIT_NAME";

		// Testing Secure access with mrId 0
		assertEquals(fieldSeq + PushFormUtil.MS_WORKFLOW_DELIM + fieldValue,
				PushFormUtil.constructWFTSAccessMapKey(fieldSeq, fieldName,
						fieldSeq + PushFormUtil.MS_WORKFLOW_DELIM + fieldValue,
						PushFormUtil.WFTS_SECURE_ACCESS, 0));

		// Testing Secure access with mrId 100
		assertEquals(fieldSeq + PushFormUtil.MS_WORKFLOW_DELIM + fieldValue,
				PushFormUtil.constructWFTSAccessMapKey(fieldSeq, fieldName,
						fieldSeq + PushFormUtil.MS_WORKFLOW_DELIM + fieldValue,
						PushFormUtil.WFTS_SECURE_ACCESS, 100));

		// Testing Secure access field with tampered value
		assertEquals("", PushFormUtil.constructWFTSAccessMapKey(fieldSeq,
				fieldName, fieldSeq + fieldValue,
				PushFormUtil.WFTS_SECURE_ACCESS, 100));

		// Testing Secure access field with tampered value, but it was throwing
		// ArrayIndexOutOfBoundException. Needs to correct the actual method
		// logic not to throw exception
		/*
		 * assertEquals("", PushFormUtil.constructWFTSAccessMapKey(fieldSeq,
		 * fieldName, fieldSeq, PushFormUtil.WFTS_SECURE_ACCESS, 100));
		 */

		// Testing Readonly access with mrId 0
		assertEquals(fieldSeq + PushFormUtil.MS_WORKFLOW_DELIM + fieldName,
				PushFormUtil.constructWFTSAccessMapKey(fieldSeq, fieldName,
						fieldSeq + PushFormUtil.MS_WORKFLOW_DELIM + fieldValue,
						PushFormUtil.WFTS_READONLY_ACCESS, 0));

		// Testing Readonly access with mrId 100
		assertEquals(fieldSeq + PushFormUtil.MS_WORKFLOW_DELIM + fieldValue,
				PushFormUtil.constructWFTSAccessMapKey(fieldSeq, fieldName,
						fieldSeq + PushFormUtil.MS_WORKFLOW_DELIM + fieldValue,
						PushFormUtil.WFTS_READONLY_ACCESS, 100));
	}
	
	
	
	@Test
	public void testConstructPushformCacheKey(){
		long pushFormId = 100;
		long procId = 101;
		long userId = 102;
		String sessionId = "103";
		String cacheKey = "SID:"+sessionId+":ID:"+pushFormId+":PID:"+procId+":UID:"+userId;
		assertEquals(cacheKey,PushFormUtil.constructPushformCacheKey(pushFormId, procId, userId, sessionId));
	}
	
	@Test
	public void testIsSecureFiled(){
		Map<Integer, Integer> map = null;
		assertFalse(PushFormUtil.isSecureFiled(1,map));
		
		map = new HashMap<Integer, Integer>();
		assertFalse(PushFormUtil.isSecureFiled(1,map));
		
		map.put(1, PushFormUtil.WFTS_SECURE_ACCESS);
		assertTrue(PushFormUtil.isSecureFiled(1,map));
		
		map.put(1, PushFormUtil.WFTS_READONLY_ACCESS);
		assertFalse(PushFormUtil.isSecureFiled(1,map));
	}
	
	@Test
	public void testIsReadOnlyFiled(){
		Map<Integer, Integer> map = null;
		assertFalse(PushFormUtil.isReadOnlyFiled(1,map));
		
		map = new HashMap<Integer, Integer>();
		assertFalse(PushFormUtil.isReadOnlyFiled(2,map));
		
		map.put(2, PushFormUtil.WFTS_READONLY_ACCESS);
		assertTrue(PushFormUtil.isReadOnlyFiled(2,map));
		
		map.put(2, PushFormUtil.WFTS_SECURE_ACCESS);
		assertFalse(PushFormUtil.isReadOnlyFiled(2,map));
	}
	
	@Test
	public void testGetServerSideValidationMapFromCache() {
		long infoletId=100;
		long procId=101;
		long userId = 102;
		String sessionId = "103";
		Map ssvMap = null;
		
		PushFormUtil.putFormLovMapInToCache(cache, ssvMap, infoletId, procId, userId, sessionId, false);
		assertTrue(PushFormUtil.getFormLovMapFromCache(cache, infoletId, procId, userId, sessionId, false).isEmpty());
		
		ssvMap  = new HashMap();
		PushFormUtil.putFormLovMapInToCache(cache, ssvMap, infoletId, procId, userId, sessionId, false);
		assertTrue(PushFormUtil.getFormLovMapFromCache(cache, infoletId, procId, userId, sessionId, false).isEmpty());
		
		ssvMap.put("KEY", "VALUE");
		assertEquals("VALUE",PushFormUtil.getFormLovMapFromCache(cache, infoletId, procId, userId, sessionId, false).get("KEY"));
	}
	
	@Test
	public void testGetWFTSMapFromCache(){
		long infoletId=100;
		long procId=101;
		long userId = 102;
		String sessionId = "103";
		Map wftsMap = null;
		
		PushFormUtil.putWFTSMapInToCache(cache, wftsMap, infoletId, procId, userId, sessionId);
		assertTrue(PushFormUtil.getWFTSMapFromCache(cache, infoletId, procId, userId, sessionId).isEmpty());
		
		wftsMap  = new HashMap();
		PushFormUtil.putWFTSMapInToCache(cache, wftsMap, infoletId, procId, userId, sessionId);
		assertTrue(PushFormUtil.getWFTSMapFromCache(cache, infoletId, procId, userId, sessionId).isEmpty());
		
		wftsMap.put("KEY", "VALUE");
		assertEquals("VALUE",PushFormUtil.getWFTSMapFromCache(cache, infoletId, procId, userId, sessionId).get("KEY"));
	}
	
	@Test
	public void testGetRecNumInMapFromCache(){
		long infoletId=100;
		long procId=101;
		long userId = 102;
		String sessionId = "103";
		Map recNumMap = null;
		
		PushFormUtil.putRecNumMapInToCache(cache, recNumMap, infoletId, procId, userId, sessionId);
		assertTrue(PushFormUtil.getRecNumInMapFromCache(cache, infoletId, procId, userId, sessionId).isEmpty());
		
		recNumMap  = new HashMap();
		PushFormUtil.putRecNumMapInToCache(cache, recNumMap, infoletId, procId, userId, sessionId);
		assertTrue(PushFormUtil.getRecNumInMapFromCache(cache, infoletId, procId, userId, sessionId).isEmpty());
		
		recNumMap.put("KEY", "VALUE");
		assertEquals("VALUE",PushFormUtil.getRecNumInMapFromCache(cache, infoletId, procId, userId, sessionId).get("KEY"));
	}
	
	@Test
	public void testSetMultiRowCountInWFTSCache(){
		long infoletId=100;
		long procId=101;
		long userId = 102;
		String sessionId = "103";
		long multiRegionId = 104;
		Map<String,Integer> wftsMap  = new HashMap<String,Integer>();
		assertFalse(PushFormUtil.setMultiRowCountInWFTSCache(cache, infoletId, procId, userId, multiRegionId, 0, sessionId));
		
		assertFalse(PushFormUtil.setMultiRowCountInWFTSCache(cache, infoletId, procId, userId, multiRegionId, 10, sessionId));
		
		PushFormUtil.putWFTSMapInToCache(cache, wftsMap, infoletId, procId, userId, sessionId);
		assertFalse(PushFormUtil.setMultiRowCountInWFTSCache(cache, infoletId, procId, userId, multiRegionId, 0, sessionId));
		
		wftsMap.put("mr_"+multiRegionId, 3);
		assertTrue(PushFormUtil.setMultiRowCountInWFTSCache(cache, infoletId, procId, userId, multiRegionId, 0, sessionId));
		
		wftsMap.put("mr_"+multiRegionId, 3);
		assertFalse(PushFormUtil.setMultiRowCountInWFTSCache(cache, infoletId, procId, userId, multiRegionId, 3, sessionId));
	}
	
	@Test
	public void testGetOrigRowSeqObj(){
		String origRowSeqJSONStr = "{\"origRowSeq2dArr\":[[\"100005\"],[\"4\",\"3\",\"1\",\"2\"],[\"100006\"],[\"3\",\"2\",\"1\"]]}";
		Map origRowSeqMap = PushFormUtil.getOrigRowSeqObj(origRowSeqJSONStr);
		
		assertNull(origRowSeqMap.get(100));

		assertEquals(4,((int[])(origRowSeqMap.get(100005)))[0]);
		assertEquals(3,((int[])(origRowSeqMap.get(100005)))[1]);
		assertEquals(1,((int[])(origRowSeqMap.get(100005)))[2]);
		assertEquals(2,((int[])(origRowSeqMap.get(100005)))[3]);
		
		assertEquals(3,((int[])(origRowSeqMap.get(100006)))[0]);
		assertEquals(1,((int[])(origRowSeqMap.get(100006)))[2]);
	}
	
	@Test
	public void testGetSeq(){
		assertEquals(8,PushFormUtil.getSeq(2, 3, 3));
	}

	@Test
	public void testGetColSeq(){
		assertEquals(2,PushFormUtil.getColSeq(8, 3));
	}

	@Test
	public void testGetRowSeq(){
		assertEquals(3,PushFormUtil.getRowSeq(8, 3));
	}
	
	@Test
	public void testGetMaskedXml(){
		//String xml = "<?xml version=\"1.0\"?><METRIC><FORM_AUTOSAVE_ENABLED>Y</FORM_AUTOSAVE_ENABLED><SNAPSHOT_ENABLED>N</SNAPSHOT_ENABLED></METRIC>";
		String xml1 = "<?xml version=\"1.0\"?>"
				+"<METRIC ID=\"100006\" NAME=\"Active Users Infolet\" INSTANCE=\"11144\" NOTIFY=\"NO\" SOURCE=\"100000\" MODE=\"DEMAND\"><SQL URL=\"jdbc:oracle:thin:@msi-l1014.metricstream.com:1521:orcl\"" 
				+"USER=\"TRUNK\" PSWD=\"TRUNK\" CONNECTIONS=\"25\">"
				+"select user_name,user_id,first_name,last_name, creation_date from si_users_t where (end_date is null or end_date &gt; sysdate)</SQL></METRIC>";
		String xml = "<?xml version=\"1.0\"?><METRIC ID=\"100035\" INSTANCE=\"11146\" PROCESS=\"1381\" SOURCE=\"100000\" MODE=\"STORE\" NOTIFY=\"NO\"><RESULT RUN_DATE=\"2015-03-08 01:02:35.037 +0530\">"
				+"<META><FIELD NAME=\"UserName\" TYPE=\"STRING\" ORDER=\"1\"/><FIELD NAME=\"LOV_MR1\" TYPE=\"STRING\" ORDER=\"2\"/><FIELD NAME=\"LOV_MR2\" TYPE=\"STRING\" ORDER=\"3\"/><FIELD NAME=\"LOV1_MR1\" TYPE=\"STRING\" ORDER=\"4\"/><FIELD NAME=\"Dropdown_NMR\" TYPE=\"STRING\" ORDER=\"5\"/></META>"
				+"<ROWDATA ORDER=\"1\" GROUP=\"100012\">b5phanib6100002neb6100000</ROWDATA><ROWDATA ORDER=\"2\" GROUP=\"100013\">b5phaninb6100007nb6100000</ROWDATA></RESULT></METRIC>";
		assertNull(PushFormUtil.getMaskedXml(xml, "FORM_AUTOSAVE_ENABLED"));
	}
	
	@Ignore
	@Test
	public void testIsSnapshotFeatureEnabled(){
		assertTrue(PushFormUtil.isSnapshotFeatureEnabled());
	}
	
	
}