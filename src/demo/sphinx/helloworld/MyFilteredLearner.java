package demo.sphinx.helloworld;

/*
 * A Java class that implements a simple text learner, based on WEKA.
 * To be used with MyFilteredClassifier.java.
 * WEKA is available at: http://www.cs.waikato.ac.nz/ml/weka/
 * Copyright (C) 2013 Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 *
 * This program is free software: you can redistribute it and/or modify
 * it for any purpose.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import graduationinterface.ReEvaluateView;

import java.util.Random;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.*;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.converters.ArffLoader.ArffReader;

import java.io.*;

/**
 * This class implements a simple text learner in Java using WEKA.
 * It loads a text dataset written in ARFF format, evaluates a classifier on it,
 * and saves the learnt model for further use.
 * @author Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 * @see MyFilteredClassifier
 */
public class MyFilteredLearner {

	/**
	 * Object that stores training data.
	 */
	Instances trainData;
	/**
	 * Object that stores the filter
	 */
	StringToWordVector filter;
	/**
	 * Object that stores the classifier
	 */
	FilteredClassifier classifier;
	
	private ReEvaluateView popup; 
	private StringBuilder AllResult = new StringBuilder();
	
		
	public MyFilteredLearner() {
		super();
//		 popup= new ReEvaluateView();
	}

	/**
	 * This method loads a dataset in ARFF format. If the file does not exist, or
	 * it has a wrong format, the attribute trainData is null.
	 * @param fileName The name of the file that stores the dataset.
	 */
	public void loadDataset(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			ArffReader arff = new ArffReader(reader);
			trainData = arff.getData();
			String temp="===== Loaded dataset: " + fileName + " =====";
			System.out.println(temp);
//			popup.appendTextToView(temp+"\n");
			AllResult.append(temp);
			reader.close();
		}
		catch (IOException e) {
			System.out.println("Problem found when reading: " + fileName);
			System.out.println(e);
		}
	}
	
	/**
	 * This method evaluates the classifier. As recommended by WEKA documentation,
	 * the classifier is defined but not trained yet. Evaluation of previously
	 * trained classifiers can lead to unexpected results.
	 */
	public void evaluate() {
		try {

			trainData.setClassIndex(1);
//			trainData.setClassIndex(0);
			filter = new StringToWordVector();
			filter.setAttributeIndices("last");
			classifier = new FilteredClassifier();
			classifier.setFilter(filter);
//			 J48 j48 = new J48();
//			 RandomForest
//			 j48.setUnpruned();        // using an unpruned J48
			classifier.setClassifier(new NaiveBayesMultinomialText());
			Evaluation eval = new Evaluation(trainData);
			eval.crossValidateModel(classifier, trainData, 4, new Random(1));
			
			System.out.println(eval.toSummaryString());
//			popup.appendTextToView(eval.toSummaryString()+"\n");
			AllResult.append(eval.toSummaryString()+"\n");
			System.out.println(eval.toClassDetailsString());
//			popup.appendTextToView(eval.toClassDetailsString()+"\n");
			AllResult.append(eval.toClassDetailsString()+"\n");
			System.out.println("===== Evaluating on filtered (training) dataset done =====");
//			popup.appendTextToView("===== Evaluating on filtered (training) dataset done ====="+"\n");
			AllResult.append("===== Evaluating on filtered (training) dataset done ====="+"\n");
			
		}
		catch (Exception e) {
			System.out.println("Problem found when evaluating");
			System.out.println(e);
		}
	}
	
	/**
	 * This method trains the classifier on the loaded dataset.
	 */
	public void learn() {
		try {
//			System.out.println(trainData.numAttributes());
//			if (trainData.classIndex() == -1)
		    trainData.setClassIndex(1);
			
//			trainData.setClassIndex(0);
			filter = new StringToWordVector();
//			filter.setStemmer(new St);
			filter.setAttributeIndices("last");
			classifier = new FilteredClassifier();
			classifier.setFilter(filter);
//			 J48 j48 = new J48();
//			 j48.setUnpruned(true);        // using an unpruned J48
			classifier.setClassifier(new NaiveBayesMultinomialText());
			classifier.buildClassifier(trainData);
			// Uncomment to see the classifier
			// System.out.println(classifier);
			System.out.println("===== Training on filtered (training) dataset done =====");
//			popup.appendTextToView("===== Training on filtered (training) dataset done ====="+"\n");
			AllResult.append("===== Training on filtered (training) dataset done ====="+"\n");
		}
		catch (Exception e) {
			System.out.println("Problem found when training");
		}
	}
	
	/**
	 * This method saves the trained model into a file. This is done by
	 * simple serialization of the classifier object.
	 * @param fileName The name of the file that will store the trained model.
	 */
	public void saveModel(String fileName) {
		try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(classifier);
            out.close();
 			System.out.println("===== Saved model: " + fileName + " =====");
        } 
		catch (IOException e) {
			System.out.println("Problem found when writing: " + fileName);
		}
	}
	
	/**
	 * Main method. It is an example of the usage of this class.
	 * @param args Command-line arguments: fileData and fileModel.
	 */
	private MyFilteredLearner learner;
	public static void main (String[] args) {
	
		MyFilteredLearner learner;
		
		if (args.length < 2)
			System.out.println("Usage: java MyLearner <fileData> <fileModel>");
		else {
			learner = new MyFilteredLearner();
			learner.loadDataset(args[0]);
			// Evaluation must be done before training
			// More info in: http://weka.wikispaces.com/Use+WEKA+in+your+Java+code
			learner.evaluate();
			learner.learn();
			learner.saveModel(args[1]);
		}
	}
	
	public void trainingClassifier(){
		learner = new MyFilteredLearner();
//		popup = new ReEvaluateView();
//		popup.clearView();
		learner.loadDataset("trainingData.arff");
		// Evaluation must be done before training
		// More info in: http://weka.wikispaces.com/Use+WEKA+in+your+Java+code
		learner.evaluate();
		learner.learn();
//		popup.setString(AllResult);
//		popup.showEvaluationFrame();
	
	}
	
	public void SaveClassifierModel(){
		learner.saveModel("AdaptiveModel.model");
	}
}	