package com.metricstream.systemi.nlp;

import java.io.*;
import java.util.Objects;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class CategoryTrainUtil {

	public static void trainModel(String inputFile, String modelFile)
			throws IOException {
		//Objects.nonNull(inputFile);
		//Objects.nonNull(modelFile);
// need to add additional check to verify both input file and model file are non-empty
		DoccatModel model = null;

		try {

			MarkableFileInputStreamFactory factory = new MarkableFileInputStreamFactory(
					new File(inputFile));
			ObjectStream<String> lineStream = new PlainTextByLineStream(
					factory, "UTF-8");

			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(
					lineStream);

			model = DocumentCategorizerME.train("en", sampleStream,
					TrainingParameters.defaultParams(), new DoccatFactory());

			OutputStream modelOut = null;
			File modelFileTmp = new File(modelFile);
			modelOut = new BufferedOutputStream(new FileOutputStream(
					modelFileTmp));
			model.serialize(modelOut);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
