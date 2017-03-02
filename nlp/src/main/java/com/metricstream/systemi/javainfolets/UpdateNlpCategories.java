/*package com.metricstream.systemi.javainfolets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metricstream.systemi.dao.NlpCategoriesDao;
import com.metricstream.systemi.dao.NlpCategoriesObject;
import com.metricstream.systemi.dao.WebdavNlpDao;
import com.metricstream.systemi.dao.WebdavNlpObject;
import com.metricstream.systemi.nlp.NlpSubCategoryDetector;

public class UpdateNlpCategories {

	final static String CLASS_ID = "UpdateNlpCategories";
	static final int NUM_COLUMNS = 9;
	final static Logger logger = LoggerFactory
			.getLogger(UpdateNlpCategories.class);

	
	 * public static void main(String[] args) { GetNlpCategories nlpcat = new
	 * GetNlpCategories(); nlpcat.getNlpCategories(); System.out.println("aaa");
	 * }
	 
	public static String[][] InsertNlpCategories() {

		List<WebdavNlpObject> webdavNlpListFinal = null;
		WebdavNlpDao nlpDao = new WebdavNlpDao();
		// public static void main(String args[]){
		String[][] result = null;
		int[] result1 = null;
		try {

			// JDBCConnector connector = new JDBCConnector();
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("registered the driver");
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:db11gr2", "M7FEB",
					"M7FEB");
			NlpSubCategoryDetector nlpdetector = new NlpSubCategoryDetector();
			//webdavNlpListFinal = nlpdetector.processWebdav();
			result1 = nlpDao.processWebdavNlpInsert(con, webdavNlpListFinal);

			logger.info("successfully completed u of records into NLPCATEGORIES:::::");
			NlpCategoriesDao nlpobj = new NlpCategoriesDao();
			logger.info("Fetching all the NLPCATEGORIES TABLE DETAILS::::::");
			List<NlpCategoriesObject> nlpList = nlpobj.getNlpCategories(con);
			logger.info("NLPCATEGORIES count::::::" + nlpList.size());
			Iterator itr = nlpList.iterator();
			ArrayList<String[]> output = new ArrayList<>();
			while (itr.hasNext()) {
				String[] columnResult = new String[NUM_COLUMNS];
				NlpCategoriesObject nlpCategoriesObject = (NlpCategoriesObject) itr
						.next();
				columnResult[0] = String
						.valueOf(nlpCategoriesObject.getBugId());
				columnResult[1] = String.valueOf(nlpCategoriesObject
						.getBugStatus());
				columnResult[2] = String.valueOf(nlpCategoriesObject
						.getComponent());
				columnResult[3] = String.valueOf(nlpCategoriesObject
						.getShortDesc());
				columnResult[4] = String.valueOf(nlpCategoriesObject
						.getLongDesc());
				columnResult[5] = String.valueOf(nlpCategoriesObject
						.getResolution());
				columnResult[6] = String.valueOf(nlpCategoriesObject
						.getNlpCategory());
				columnResult[7] = String.valueOf(nlpCategoriesObject
						.getNlpSubCategory());
				columnResult[8] = String.valueOf(nlpCategoriesObject
						.getTsUpdated());
				output.add(columnResult);
			}
			result = new String[output.size()][NUM_COLUMNS];
			for (int i = 0; i < output.size(); i++) {
				for (int j = 0; j < NUM_COLUMNS; j++) {
					result[i][j] = ((String[]) output.get(i))[j];
				}
			}

		} catch (Exception ex) {
			logger.debug(CLASS_ID, "Error in obtaining NLP DATA", ex);
		}

		System.out.println("the categories obtained are" + result.length);
		// return result;
		return result;

	}

}
*/