package com.metricstream.systemi.nlp;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.metricstream.systemi.dao.*;

//import com.metricstream.util.db.*;

public class NlpCategoryDetector {
	static Connection con = null;
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(WebdavInputDao.class);

	// static JDBCConnector connector = new JDBCConnector();

	public NlpCategoryDetector(String propertiesFile) {
		// rb = ResourceBundle.getBundle(propertiesFile);
	}

	/*
	 * public static void main(String[] args) throws SQLException, IOException {
	 * NlpCategoryDetector detector = new NlpCategoryDetector("copyTable");
	 * detector.trainModel(); LOGGER.info("aaaaa"); }
	 */
	public static void main(String[] args) throws SQLException, IOException {

		NlpCategoriesDao nlpDao = new NlpCategoriesDao();
		TrakrInputDao trakrinputDao = new TrakrInputDao();
		String modelFile = "cat_train.bin";
		// String inputFilewebdav =
		// "D:\\SreeRekha\\TEXT_ANALYTICS\\TRAKR_TRAINING_DATA\\FINAL_TRAINING_SETS\\trakr_component_training.txt";
		String inputFileNLP = "/opt/MetricStream/SYSTEMi/Systemi/Resource/Text/trakr_component_training.txt";
		CategoryTrainUtil.trainModel(inputFileNLP, modelFile);
		int[] result = null;
		List<NlpCategoriesObject> NlpListFinal = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			LOGGER.debug("registered the driver");
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@172.27.141.5:34422:orcl",
					"METRICSTREAM", "password");
			/*
			 * Connection con = DriverManager.getConnection(
			 * "jdbc:oracle:thin:@localhost:1521:db11gr2", "M7JANFINAL",
			 * "M7JANFINAL");
			 */
			System.out.println("Established connection");
			LOGGER.debug("Established connection");
			NlpListFinal = trakrinputDao.processTrakrInput(con);

			LOGGER.debug("Obtained the trakr input data in nlpcategorydetector");
			result = nlpDao.processNlpCategoriesInsert(con, NlpListFinal);
			
			LOGGER.debug("successfully inserted records in nlpcategorydetector");

		}

		catch (Exception e) {
			LOGGER.error("Error while performing insert of nlp details", e);
		}

	}

}
