package com.metricstream.systemi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

public class WebdavNlpDao {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(WebdavInputDao.class);

	public WebdavNlpDao() {
		/*
		 * This is the default constructor.
		 */
	}

	public int[] processWebdavNlpInsert(Connection con,
			List<WebdavNlpObject> webdavNlpList) throws SQLException {
		LOGGER.info("Getting processWebdavNlpInsert details");
		WebdavNlpObject webdav = new WebdavNlpObject();
		// webdavList = new ArrayList<WebdavInputObject>();
		int[] result = null;
		int trakrId = 0;
		String component = null;
		String description = null;
		String summary = null;
		String categoryNlp = null;
		String subCategoryNlp = null;
		String status = null;
		String resolution = null;

		try {
			LOGGER.debug("Getting processWebdavNlpInsert information");
			String queryWebdavInputInsert = "INSERT INTO NLPCATEGORIES_NEW values(?,?,?,?,?,?,?,?,?)";
			try (PreparedStatement pstmnt = con
					.prepareStatement(queryWebdavInputInsert);) {
				int size = webdavNlpList.size();
				if (webdavNlpList != null) {

					for (int i = 0; i < size; i++) {
						webdav = webdavNlpList.get(i);
						trakrId = webdav.getTrakrId();
						//component = webdav.getComponent();
						//description = webdav.getDescription();
						summary = webdav.getSummary();
						categoryNlp = webdav.getCategory_nlp();
						subCategoryNlp = webdav.getSub_category_nlp();
                        status = webdav.getBug_status();
                        component = webdav.getComponent();
                        resolution = webdav.getResolution();
						pstmnt.setInt(1, trakrId);
						pstmnt.setString(2, component);
						pstmnt.setString(3, status);
						pstmnt.setString(4, resolution);
						pstmnt.setString(5, summary);
						pstmnt.setString(6, "");
						pstmnt.setString(7, categoryNlp);
						pstmnt.setString(8, "");
						pstmnt.setString(9, "N");
						pstmnt.execute();
					}
					// result = pstmnt.executeBatch();
				}

			}
		} catch (SQLException sqe) {
			LOGGER.error(
					"Exception while obtaining Advanced Data Types details",
					sqe);
			throw sqe;
		}
		LOGGER.info("Returning ADT object for Advanced Data Types");
		return result;
	}

	public int[] updateWebdavNlpSubCategory(Connection con,
			List<WebdavNlpObject> webdavNlpList) throws SQLException {
		LOGGER.info("updaingt updateWebdavNlpSubCategory details");
		WebdavNlpObject webdav = null;
		// webdavList = new ArrayList<WebdavInputObject>();
		int[] result = null;
		int trakrId = 0;
		String subCategoryNlp = null;
		int queryresult;

		try {
			LOGGER.debug("updating updateWebdavNlpSubCategory information");
			String queryWebdavnlpupdate = "update nlpcategories_new set nlp_sub_category = ? where bug_id=?";
			try (PreparedStatement pst = con
					.prepareStatement(queryWebdavnlpupdate);) {
				int size = webdavNlpList.size();
				if (webdavNlpList != null) {

					for (int i = 0; i < size; i++) {
						// webdav = new WebdavNlpObject();
						webdav = webdavNlpList.get(i);
						trakrId = webdav.getTrakrId();
						subCategoryNlp = webdav.getCategory_nlp();

						pst.setString(1, subCategoryNlp);
						pst.setInt(2, trakrId);

						// queryresult =
						// pst.addBatch();
						pst.executeUpdate();
						System.out.println("updated trakrId is" + trakrId
								+ "sub_category is" + subCategoryNlp);
					}

					// result = pst.executeBatch();
					System.out.println("updateWebdavNlpSubCategory");
					pst.close();

					// result = pstmnt.executeBatch();
				}

			}
		} catch (SQLException sqe) {
			System.out
					.println("Exception while updating updateWebdavNlpSubCategory"
							+ sqe);
			// throw sqe;
		}
		LOGGER.info("updateWebdavNlpSubCategory");
		return result;
	}

}
