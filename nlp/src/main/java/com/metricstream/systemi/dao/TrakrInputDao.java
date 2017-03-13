package com.metricstream.systemi.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.metricstream.systemi.nlp.CategoryDetectorUtil;


public class TrakrInputDao {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WebdavInputDao.class);
	public TrakrInputDao() {
		/*
		 * This is the default constructor.
		 */
	}
	
	public List<TrakrInputObject> getTrakrInputToCategorize(Connection con)
			throws SQLException {
		LOGGER.info("Getting WebdavInputObject details");
		TrakrInputObject trakrinput = null;
		List<TrakrInputObject> trakrInputList = new ArrayList<TrakrInputObject>();
		try {
			LOGGER.debug("Getting WebdavInputObject information");
			//String queryTrakrInput = "Select B.BUG_ID,B.SHORT_DESC,B.RESOLUTION,B.BUG_STATUS,B.CF_RCA_DETAILS,B.CF_RCA_CATEGORY,C.Name as COMPONENT_NAME From Bugs B, Components C where B.Component_Id = c.id and B.product_id=28";
			String queryTrakrInput = "Select B.BUG_ID,B.SHORT_DESC,B.RESOLUTION,B.BUG_STATUS,B.CF_RCA_DETAILS,B.CF_RCA_CATEGORY,C.Name as COMPONENT_NAME From Bugs B, Components C where B.Component_Id = c.id and B.product_id=28";
			try (PreparedStatement pstmnt = con.prepareStatement(queryTrakrInput);) {
				try (ResultSet rs = pstmnt.executeQuery();) {
					while (rs.next()) {
						trakrinput = new TrakrInputObject();
						/*trakrinput.setTrakrId(rs.getInt("BUG_ID"));
						trakrinput.setSummary(rs.getString("SHORT_DESC"));
						trakrinput.setResolution(rs.getString("RESOLUTION"));
						trakrinput.setStatus(rs.getString("BUG_STATUS"));
						trakrinput.setRcaDetails(rs.getString("CF_RCA_DETAILS"));
						trakrinput.setRcaCategory(rs.getString("CF_RCA_CATEGORY"));
						trakrinput.setComponent(rs.getString("COMPONENT_NAME"));*/
						trakrinput.setTrakrId(rs.getInt("BUG_ID"));
						trakrinput.setSummary(rs.getString("SHORT_DESC"));
						trakrinput.setResolution(rs.getString("RESOLUTION"));
						trakrinput.setStatus(rs.getString("BUG_STATUS"));
						trakrinput.setRcaDetails(rs.getString("CF_RCA_DETAILS"));
						trakrinput.setRcaCategory(rs.getString("CF_RCA_CATEGORY"));
						trakrinput.setComponent(rs.getString("COMPONENT_NAME"));
						trakrInputList.add(trakrinput);
					}
				}
			}
		} catch (SQLException sqe) {
			LOGGER.error("Exception while obtaining Advanced Data Types details", sqe);
			throw sqe;
		}
		LOGGER.info("Returning ADT object for Advanced Data Types");
		return trakrInputList;
	}
	
	public static List<NlpCategoriesObject> processTrakrInput(Connection con) {
		CategoryDetectorUtil detector = null;
		String modelFile = "cat_train.bin";
		String resultcategory = "";
		String summary = "";
		String resultSubCategory = "";
		String resolution = "";
		TrakrInputObject trakrInputObj = new TrakrInputObject();
		NlpCategoriesObject NlpObject = null;
		TrakrInputDao trakrdao = new TrakrInputDao();
		List<TrakrInputObject> trakrList = new ArrayList<TrakrInputObject>();
		List<NlpCategoriesObject> nlpList = new ArrayList<NlpCategoriesObject>();
		List<NlpCategoriesObject> nlpListfinal = new ArrayList<NlpCategoriesObject>();

		try {
			/*Class.forName("oracle.jdbc.driver.OracleDriver");
			LOGGER.info("registered the driver");
			Connection conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@172.27.141.5:34422:orcl",
					"METRICSTREAM", "password");
			System.out.println("Established connection");*/
			// con = connector.getConnection();
			detector = new CategoryDetectorUtil(modelFile);
			trakrList = trakrdao.getTrakrInputToCategorize(con);

			int size = trakrList.size();
			if (trakrList != null) {
				for (int i = 0; i < size; i++) {
					trakrInputObj = trakrList.get(i);
					NlpObject = new NlpCategoriesObject();
					summary = trakrInputObj.getSummary();
					resolution = trakrInputObj.getResolution();
					if (summary != null || !summary.isEmpty()) {
						resultcategory = detector.getCategory(summary);
					}
					NlpObject.setBugId(trakrInputObj.getTrakrId());
					NlpObject.setComponent(trakrInputObj.getComponent());
					NlpObject.setShortDesc(trakrInputObj.getDescription());
					NlpObject.setBugStatus(trakrInputObj.getStatus());
					NlpObject.setNlpCategory(resultcategory);
					NlpObject.setNlpSubCategory(resultSubCategory);
					NlpObject.setResolution(resolution);

					nlpList.add(NlpObject);

				}

			}

			nlpListfinal = nlpList;
		} catch (Exception e) {
			LOGGER.error("Exception while inserting webdavnlp details", e);
		}

		return nlpListfinal;
	}


}
