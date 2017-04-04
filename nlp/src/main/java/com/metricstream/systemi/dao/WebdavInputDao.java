package com.metricstream.systemi.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;


public class WebdavInputDao {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WebdavInputDao.class);
	public WebdavInputDao() {
		/*
		 * This is the default constructor.
		 */
	}
	
	public List<WebdavInputObject> getWebdavTestInput(Connection con)
			throws SQLException {
		LOGGER.info("Getting WebdavInputObject details");
		WebdavInputObject webdav = null;
		List<WebdavInputObject> webdavList = new ArrayList<WebdavInputObject>();
		try {
			LOGGER.debug("Getting WebdavInputObject information");
			//String queryWebdavInput = "Select TRAKRID,COMPONENT,SUMMARY,DESCRIPTION,STATUS,RCADETAILS,RCACATEGORY From trakr_input";
			
			String queryWebdavInput = "Select B.BUG_ID,B.SHORT_DESC,B.RESOLUTION,B.BUG_STATUS,B.CF_RCA_DETAILS,B.CF_RCA_CATEGORY,C.Name as COMPONENT_NAME From Bugs B, Components C where B.Component_Id = c.id and B.product_id=28 and B.Creation_Ts Between '24-MAR-16' And '23-FEB-17'";
			try (PreparedStatement pstmnt = con.prepareStatement(queryWebdavInput);) {
				//pstmnt.setString(1, "TinyMCE");
				/*pstmnt.setString(2, entityId);
				pstmnt.setString(3, entityType);*/
				try (ResultSet rs = pstmnt.executeQuery();) {
					while (rs.next()) {
						webdav = new WebdavInputObject();
						webdav.setTrakrId(rs.getInt("BUG_ID"));
						webdav.setSummary(rs.getString("SHORT_DESC"));
						webdav.setResolution(rs.getString("RESOLUTION"));
						webdav.setStatus(rs.getString("BUG_STATUS"));
						webdav.setRcaDetails(rs.getString("CF_RCA_DETAILS"));
						webdav.setRcaCategory(rs.getString("CF_RCA_CATEGORY"));
						webdav.setComponent(rs.getString("COMPONENT_NAME"));
						//webdav.setRcaCategory(rs.getString("RCACATEGORY"));						
						webdavList.add(webdav);
					}
				}
			}
		} catch (SQLException sqe) {
			LOGGER.error("Exception while obtaining Advanced Data Types details", sqe);
			throw sqe;
		}
		LOGGER.info("Returning ADT object for Advanced Data Types");
		return webdavList;
	}

}
