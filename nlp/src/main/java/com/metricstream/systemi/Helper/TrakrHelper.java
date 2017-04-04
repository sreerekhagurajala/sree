package com.metricstream.systemi.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class TrakrHelper {
	private static ResourceBundle rb = null;

	public TrakrHelper(String propertiesFile) {
		rb = ResourceBundle.getBundle(propertiesFile);
	}
	
	//At the time of writing of this class, there were a total of 58 columns in the sourced table "bugs" from mysql
	private static final int bugs_num_col = 58;
	private static final int bugs_act_num_col = 9;
	private static int batch_size = 500;

	static String trunc_table = "delete ";
	static String values = ") values(";
	private static final String trunc_bugs_table = "delete bugs";
	private static final String trunc_bugs_act_table = "delete bugs_activity";

	private static final String pull_bugs_table_data = "SELECT bug_id, assigned_to, bug_file_loc, bug_severity, bug_status, creation_ts, "
			+ "delta_ts, short_desc, op_sys, priority, product_id, rep_platform, reporter, version, component_id,"
			+ "resolution, target_milestone, qa_contact, status_whiteboard, lastdiffed, everconfirmed, "
			+ "reporter_accessible, cclist_accessible, estimated_time, remaining_time, deadline,  alias, "
			+ "cf_product_life_cycle, cf_customized,  cf_type,  cf_customer_support, cf_creator_group, "
			+ "cf_depends_on_status, cf_instance_information_application, cf_customized_yes, cf_justification, "
			+ "cf_resolution_category, cf_rca_category, cf_rca_details, cf_tracking_product, cf_steps_to_reproduce, "
			+ "cf_initial_analysis, cf_preventive_action, cf_cloned_from, cf_platform_version, cf_appstudio_version, "
			+ "cf_expected_due_date, cf_browser_ver, cf_fix_avlbl_in, cf_testcaseid, cf_build, cf_customer, "
			+ "cf_additional_info, cf_hotfix_provided, cf_hotfix_detailed, cf_to_be_retrofited_in, cf_retrofited_in, "
			+ "cf_jiraid FROM devbugs.bugs where cf_customer_support='Yes' and cf_type='Defect' "
			+ "and product_id in (28,155)";
	
	private static final String dump_bugs_table_data ="insert into bugs (bug_id, assigned_to, bug_file_loc,  bug_severity, "
			+ "bug_status, creation_ts, delta_ts, short_desc, op_sys, priority, product_id, rep_platform, reporter, "
			+ "version, component_id, resolution, target_milestone, qa_contact, status_whiteboard, lastdiffed, "
			+ "everconfirmed,  reporter_accessible, cclist_accessible, estimated_time, remaining_time, deadline, "
			+ "alias, cf_product_life_cycle, cf_customized, cf_type, cf_customer_support,  cf_creator_group, "
			+ "cf_depends_on_status, cf_instance_info_appln, cf_customized_yes, cf_justification, "
			+ "cf_resolution_category, cf_rca_category, cf_rca_details, cf_tracking_product, cf_steps_to_reproduce, "
			+ "cf_initial_analysis, cf_preventive_action, cf_cloned_from, cf_platform_version, cf_appstudio_version, "
			+ "cf_expected_due_date, cf_browser_ver, cf_fix_avlbl_in, cf_testcaseid, cf_build, cf_customer, "
			+ "cf_additional_info, cf_hotfix_provided, cf_hotfix_detailed, cf_to_be_retrofited_in, cf_retrofited_in, "
			+ "cf_jiraid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 

	private static final String pull_bugs_activity_table_data = "select id, a.BUG_ID, ATTACH_ID, WHO, BUG_WHEN, "
			+ "FIELDID, ADDED, REMOVED, COMMENT_ID from devbugs.bugs a, devbugs.bugs_activity b "
			+ "where a.bug_id = b.bug_id and a.cf_customer_support='Yes' and a.cf_type='Defect' "
			+ "and a.product_id in (28,155)";

	private static final String dump_bugs_activity_table_data = "insert into BUGS_ACTIVITY(id, BUG_ID, ATTACH_ID, WHO, "
			+ "BUG_WHEN, FIELDID, ADDED, REMOVED, COMMENT_ID) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private Connection getMySQLConnection(){
		Connection connection = null;
		try {
			Class.forName(rb.getString("MY_SQL_JDBC_DRIVER"));
			connection = DriverManager.getConnection(rb.getString("MY_SQL_JDBC_URL"),
					rb.getString("MY_SQL_JDBC_USER"), rb.getString("MY_SQL_JDBC_PASSWORD"));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return connection;
	}

	private Connection getOracleConnection(){
		Connection connection = null;
		try {
			Class.forName(rb.getString("ORACLE_JDBC_DRIVER"));
			connection = DriverManager.getConnection(rb.getString("ORACLE_JDBC_URL"),
					rb.getString("ORACLE_JDBC_USER"), rb.getString("ORACLE_JDBC_PASSWORD"));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return connection;
	}

	public Map<String, Object> dumpDataFromTrakr(){
		Map<String, Object> result = null;
		try {
			System.out.println("~~~~STARTING THE DUMP ACTIVITY");
			result = dumpBugsTable();
			result.putAll(dumpBugsActivityTable());
			System.out.println("~~~~STOPING THE DUMP ACTIVITY");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private Map<String, Object> dumpBugsTable() throws Exception{
		System.out.println("~~~~STARTING DUMP BUGS TABLE");
		Map<String, Object> result = new HashMap<String, Object>();
		int num_rows = 0;
		Connection mySQLConnection = null;
		Statement mySQLStatement = null;
		ResultSet mySQLResultSet = null;
		
		Connection oracleConnection = null;
		PreparedStatement oraclePreparedStatement = null;
		try {
			mySQLConnection = getMySQLConnection();
			mySQLStatement = mySQLConnection.createStatement();
			System.out.println("~~~~STARTING PULL BUGS TABLE");
			mySQLResultSet = mySQLStatement.executeQuery(pull_bugs_table_data);
			System.out.println("~~~~STOPPING PULL BUGS TABLE");
			oracleConnection = getOracleConnection();
			oraclePreparedStatement = oracleConnection.prepareStatement(trunc_bugs_table);
			oraclePreparedStatement.executeQuery();
			System.out.println("~~~~TRUNCATE BUGS TABLE successful");
			oraclePreparedStatement.close();
			oraclePreparedStatement = oracleConnection.prepareStatement(dump_bugs_table_data);
			int i=0;
			System.out.println("~~~~STARTING PERSIST TRAKR DATA");
			for (; mySQLResultSet.next() ; i++) {
				for(int j=1; j<=bugs_num_col; j++){
					oraclePreparedStatement.setObject(j, mySQLResultSet.getObject(j));
				}
				oraclePreparedStatement.addBatch();
				if(i % batch_size == 0) {
					num_rows+= oraclePreparedStatement.executeBatch().length;
				}
			}
			System.out.println("~~~~STOPPING PERSIST TRAKR DATA");
			num_rows+= oraclePreparedStatement.executeBatch().length;
			System.out.println("~~~~BUGS TABLE Result set size from MySql "+i);
			System.out.println("~~~~BUGS TABLE total rows commited to oracle "+num_rows);
			result.put("BUGS_DATE", new java.sql.Date(new java.util.Date().getTime()));
			result.put("BUGS_NUM_ROWS",num_rows);
			result.put("BUGS_STATUS",num_rows>0?"SUCCESS":"FAILURE");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Exception occoured while dumping bugs table "+e);
		} finally {
			try{
			//closing oracle objects
			oraclePreparedStatement.close();
			oracleConnection.close();
			//closing mysql objects
			mySQLResultSet.close();
			mySQLStatement.close();
			mySQLConnection.close();
			} catch (NullPointerException e){}
		}
		System.out.println("~~~~STOPING DUMP BUGS TABLE");
		return result;
	}
	private Map<String, Object> dumpBugsActivityTable() throws Exception{
		System.out.println("~~~~STARTING BUGS_ACTIVITY DUMP");
		Map<String, Object> result = new HashMap<String, Object>();
		int num_rows = 0;
		Connection mySQLConnection = null;
		Statement mySQLStatement = null;
		ResultSet mySQLResultSet = null;
		
		Connection oracleConnection = null;
		PreparedStatement oraclePreparedStatement = null;
		try {
			batch_size = batch_size * 5; //Increasing the batch size as the resultset is small
			System.out.println("~~~~STARTING PULL BUGS ACTIVITY");
			mySQLConnection = getMySQLConnection();
			mySQLStatement = mySQLConnection.createStatement();
			mySQLResultSet = mySQLStatement.executeQuery(pull_bugs_activity_table_data);
			System.out.println("~~~~STOPPING PULL BUGS ACTIVITY");

			oracleConnection = getOracleConnection();
			oraclePreparedStatement = oracleConnection.prepareStatement(trunc_bugs_act_table);
			oraclePreparedStatement.executeQuery();
			System.out.println("~~~~ TRUNCATE BUGS_ACTIVITY TABLE successful");
			oraclePreparedStatement.close();
			oraclePreparedStatement = oracleConnection.prepareStatement(dump_bugs_activity_table_data);
			int i=0;

			System.out.println("~~~~STARTING PERSIST BUGS ACTIVITY");
			for (; mySQLResultSet.next() ; i++) {
				for(int j=1; j<=bugs_act_num_col; j++){
					oraclePreparedStatement.setObject(j, mySQLResultSet.getObject(j));
				}
				oraclePreparedStatement.addBatch();
				if(i % batch_size == 0) {
					num_rows+= oraclePreparedStatement.executeBatch().length;
				}
			}
			System.out.println("~~~~STOPPING PERSIST BUGS ACTIVITY");

			num_rows+= oraclePreparedStatement.executeBatch().length;
			System.out.println("~~~~BUGS ACTIVITY TABLE Result set size from MySql "+i);
			System.out.println("~~~~BUGS ACTIVITY TABLE total rows commited to oracle "+num_rows);
			result.put("BUGS_ACT_DATE", new java.sql.Date(new java.util.Date().getTime()));
			result.put("BUGS_ACT_NUM_ROWS",num_rows);
			result.put("BUGS_ACT_STATUS",num_rows>0?"SUCCESS":"FAILURE");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Exception occoured while dumping bugs activity table "+e);
		} finally {
			//closing oracle objects
			oraclePreparedStatement.close();
			oracleConnection.close();
			//closing mysql objects
			mySQLResultSet.close();
			mySQLStatement.close();
			mySQLConnection.close();
		}
		System.out.println("~~~~COMPLETED BUGS_ACTIVITY DUMP");
		return result;
	}

    public static void main(String[] args) {
    	TrakrHelper trakrHelper = new TrakrHelper("copyTable");
    	trakrHelper.dumpDataFromTrakr();
		System.out.println("aaa");
	}
}

