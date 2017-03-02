package com.metricstream.systemi.nlp;

import java.io.IOException;

public class SuCategoryTrainer {
 public static void main(String args[]) throws IOException {
  String modelFile = "cat_train.bin";
  String inputFile = "D:\\SreeRekha\\TEXT_ANALYTICS\\TRAKR_TRAINING_DATA\\SUB-COMPONENET-TRAINING-DATA\\ALL_SUB_COMPONENT_TRAINING.txt";

  SubCategoryTrainUtil.trainModel(inputFile, modelFile);

 }
}
