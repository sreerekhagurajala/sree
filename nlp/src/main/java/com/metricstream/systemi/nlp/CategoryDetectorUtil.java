package com.metricstream.systemi.nlp;

import java.io.FileInputStream;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

public class CategoryDetectorUtil {
	private InputStream inputStream;
	private DoccatModel docCatModel;
	private DocumentCategorizerME myCategorizer;

	public CategoryDetectorUtil(String modelFile) {
		// Objects.nonNull(modelFile);
		initModel(modelFile);
	}

	private void initModel(String modelFile) {
		try {
			inputStream = new FileInputStream(modelFile);
			docCatModel = new DoccatModel(inputStream);
			myCategorizer = new DocumentCategorizerME(docCatModel);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public String getCategory(String text) {
		double[] outcomes = myCategorizer.categorize(text);
		String category = myCategorizer.getBestCategory(outcomes);
		return category;
	}

}
