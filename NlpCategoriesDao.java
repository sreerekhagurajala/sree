package com.metricstream.systemi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

public class NlpCategoriesDao {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(NlpCategoriesDao.class);

	public NlpCategoriesDao() {
		/*
		 * This is the default constructor.
		 */
	}

	public List<NlpCategoriesObject> getNlpCategories(Connection con)
			throws SQLException {
		LOGGER.info("Getting NlpCategoriesObject details");
		NlpCategoriesObject nlpobj = null;
		List<NlpCategoriesObject> nlpObjList = new ArrayList<NlpCategoriesObject>();
		try {
			LOGGER.debug("Getting NlpCategoriesObject information");
			String queryNlpCategories = "Select BUG_ID,COMPONENT,BUG_STATUS,SHORT_DESC,LONG_DESC,RESOLUTION,NLP_CATEGORY,NLP_SUB_CATEGORY,TS_UPDATED From nlpcategories";
			try (PreparedStatement pstmnt = con
					.prepareStatement(queryNlpCategories);) {
				// pstmnt.setString(1, "TinyMCE");
				/*
				 * pstmnt.setString(2, entityId); pstmnt.setString(3,
				 * entityType);
				 */
				try (ResultSet rs = pstmnt.executeQuery();) {
					while (rs.next()) {
						nlpobj = new NlpCategoriesObject();
						nlpobj.setBugId(rs.getInt("BUG_ID"));
						nlpobj.setComponent(rs.getString("COMPONENT"));
						nlpobj.setBugStatus(rs.getString("BUG_STATUS"));
						nlpobj.setShortDesc(rs.getString("SHORT_DESC"));
						nlpobj.setLongDesc((rs.getString("LONG_DESC")));
						nlpobj.setResolution(rs.getString("RESOLUTION"));
						nlpobj.setNlpCategory(rs.getString("NLP_CATEGORY"));
						nlpobj.setNlpSubCategory("NLP_SUB_CATEGORY");
						nlpobj.setTsUpdated("TS_UPDATED");
						nlpObjList.add(nlpobj);
					}
				}
			}
		} catch (SQLException sqe) {
			LOGGER.error("Exception while obtaining NLPCATEGORIES", sqe);
			throw sqe;
		}
		LOGGER.info("Returning nlpObjList object for NLPCATEGORIES");
		return nlpObjList;
	}

	public int[] processNlpCategoriesInsert(Connection con,
			List<NlpCategoriesObject> NlpCategoriesList) throws SQLException {
		LOGGER.info("Getting processNlpCategoriesInsert details");
		NlpCategoriesObject nlpObject = new NlpCategoriesObject();
		// webdavList = new ArrayList<WebdavInputObject>();
		int[] result = null;
		try {
			LOGGER.debug("Getting processNlpCategoriesInsert information");
			String queryWebdavInputInsert = "INSERT INTO NLPCATEGORIES_TEST values(?,?,?,?,?,?,?,?,?)";
			try (PreparedStatement pstmnt = con
					.prepareStatement(queryWebdavInputInsert);) {
				int size = NlpCategoriesList.size();
				if (NlpCategoriesList != null) {

					for (int i = 0; i < size; i++) {
						nlpObject = NlpCategoriesList.get(i);
						pstmnt.setInt(1, nlpObject.getBugId());
						pstmnt.setString(2, nlpObject.getComponent());
						pstmnt.setString(3, nlpObject.getBugStatus());
						pstmnt.setString(4, nlpObject.getResolution());
						pstmnt.setString(5, nlpObject.getShortDesc());
						pstmnt.setString(6, "");
						pstmnt.setString(7, nlpObject.getNlpCategory());
						pstmnt.setString(8, "");
						pstmnt.setString(9, "N");
						pstmnt.execute();
					}
				}

			}
		} catch (SQLException sqe) {
			LOGGER.error(
					"Exception while processing processNlpCategoriesInsert",
					sqe);
			throw sqe;
		}
		LOGGER.info("Returning processNlpCategoriesInsert");
		return result;
	}

	public int[] updateNlpSubCategory(Connection con,
			List<NlpCategoriesObject> NlpCategoriesList) throws SQLException {
		LOGGER.info("updaingt updateNlpSubCategory details");
		NlpCategoriesObject nlpObject = null;
		// webdavList = new ArrayList<WebdavInputObject>();
		int[] result = null;
		int trakrId = 0;
		String subCategoryNlp = null;
		int queryresult;

		try {
			LOGGER.debug("updating updateNlpSubCategory information");
			String queryWebdavnlpupdate = "update nlpcategories_test set nlp_sub_category = ? where bug_id=?";
			try (PreparedStatement pst = con
					.prepareStatement(queryWebdavnlpupdate);) {
				int size = NlpCategoriesList.size();
				if (NlpCategoriesList != null) {

					for (int i = 0; i < size; i++) {
						nlpObject = NlpCategoriesList.get(i);
						trakrId = nlpObject.getBugId();
						subCategoryNlp = nlpObject.getNlpCategory();
						pst.setString(1, subCategoryNlp);
						pst.setInt(2, trakrId);
						pst.executeUpdate();
						LOGGER.debug("updated trakrId is" + trakrId
								+ "sub_category is" + subCategoryNlp);
					}
					LOGGER.debug("updateNlpSubCategory");
					pst.close();

				}

			}
		} catch (SQLException sqe) {
			LOGGER.debug("Exception while updating updateNlpSubCategory"
					+ sqe);
		}
		LOGGER.debug("successfully updated updateNlpSubCategory");
		return result;
	}

}
