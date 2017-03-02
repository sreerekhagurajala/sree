package com.metricstream.systemi.nlp;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.metricstream.systemi.dao.*;

public class NlpSubCategoryDetector {
	static Connection con = null;
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(WebdavInputDao.class);

	public static void main(String args[]) throws IOException,
			ClassNotFoundException, SQLException {
		NlpCategoriesDao nlpDao = new NlpCategoriesDao();
		TrakrInputDao trakrinputDao = new TrakrInputDao();

		String modelFile = "cat_train.bin";
		// String inputFileNLP =
		// "D:\\SreeRekha\\TEXT_ANALYTICS\\TRAKR_TRAINING_DATA\\FINAL_TRAINING_SETS\\trakr_sub_component_training.txt";
		String inputFileNLP = "/opt/MetricStream/SYSTEMi/Systemi/Resource/Text/trakr_sub_component_training.txt";
		CategoryTrainUtil.trainModel(inputFileNLP, modelFile);

		int[] result = null;
		List<NlpCategoriesObject> NlpListFinal = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			LOGGER.info("registered the driver");
			/*
			 * Connection con = DriverManager.getConnection(
			 * "jdbc:oracle:thin:@localhost:1521:db11gr2", "M7JANFINAL",
			 * "M7JANFINAL");
			 */
			/*
			 * Connection con = DriverManager.getConnection(
			 * "jdbc:oracle:thin:@msi-l1008:1521:platformdev", "MS_7_DATA",
			 * "MS_7_DATA");
			 */

			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@172.27.141.5:34422:orcl",
					"METRICSTREAM", "password");
			LOGGER.info("Established connection");

			NlpListFinal = trakrinputDao.processTrakrInput(con);
			LOGGER.debug("Obtained the trakr input data in nlpsubcategorydetector");
			result = nlpDao.updateNlpSubCategory(con, NlpListFinal);
			LOGGER.debug("successfully inserted records in nlpsubcategorydetector");
			// System.out.println("the result is"+result.toString());
		}

		catch (Exception e) {
			LOGGER.error("Error while performing insert of webdavnlp details",
					e);
		}

		// add other methods also over here

	}

}
