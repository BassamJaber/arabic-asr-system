package demo.sphinx.helloworld;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.core.FastVector;


public class Classification {
	private  String trainingFileName = new String("training.arff");
	private  String testingFileName = new String("testing.arff");
	
	public Classification(){
		
	}
	
	public  void J48Classification() throws Exception{

		BufferedReader trainReader = new BufferedReader(new FileReader(
				trainingFileName));
		BufferedReader testReader = new BufferedReader(new FileReader(
				testingFileName));

//		Instances datatrain = new Instances(trainReader);
		
		 Instances train = new Instances(trainReader);
		 Instances test = new Instances(testReader);
		 
		if (train.classIndex() == -1)
			train.setClassIndex(train.numAttributes() - 1);
		 trainReader.close();
		 testReader.close();
		 // filter
		 Remove rm = new Remove();
		
//		 rm.setAttributeIndices("1");  // remove 1st attribute
		 // classifier
		 J48 j48 = new J48();
		 j48.setUnpruned(true);        // using an unpruned J48
		 // meta-classifier
		 FilteredClassifier fc = new FilteredClassifier();
//		 fc.setFilter(rm);
		 fc.setClassifier(j48);
		 // train and make predictions
		 fc.buildClassifier(train);
		 for (int i = 0; i < test.numInstances(); i++) {
		   double pred = fc.classifyInstance(test.instance(i));
		   System.out.print("ID: " + test.instance(i).value(0));
		   System.out.print(", actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
		   System.out.println(", predicted: " + test.classAttribute().value((int) pred));
		 }
	}
	
	
	
	public static void main(String[]args) throws Exception{
		Classification c = new Classification();
		c.J48Classification();
		
		
		BufferedReader datafile = readDataFile("weather.txt");
		 
		Instances data = new Instances(datafile);
		data.setClassIndex(data.numAttributes() - 1);
 
		// Do 10-split cross validation
		Instances[][] split = crossValidationSplit(data, 10);
 
		// Separate split into training and testing arrays
		Instances[] trainingSplits = split[0];
		Instances[] testingSplits = split[1];
 
		// Use a set of classifiers
		Classifier[] models = { 
				new J48(), // a decision tree
				new PART(), 
				new DecisionTable(),//decision table majority classifier
				new DecisionStump() //one-level decision tree
		};
 
		// Run for each model
		for (int j = 0; j < models.length; j++) {
 
			// Collect every group of predictions for current model in a FastVector
			FastVector predictions = new FastVector();
 
			// For each training-testing split pair, train and test the classifier
			for (int i = 0; i < trainingSplits.length; i++) {
				Evaluation validation = classify(models[j], trainingSplits[i], testingSplits[i]);
 
				predictions.appendElements(validation.predictions());
 
				// Uncomment to see the summary for each training-testing pair.
				//System.out.println(models[j].toString());
			}
 
			// Calculate overall accuracy of current classifier on all splits
			double accuracy = calculateAccuracy(predictions);
 
			// Print current classifier's name and accuracy in a complicated,
			// but nice-looking way.
			System.out.println("Accuracy of " + models[j].getClass().getSimpleName() + ": "
					+ String.format("%.2f%%", accuracy)
					+ "\n---------------------------------");
		}
 
	}
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
 
	public static Evaluation classify(Classifier model,
			Instances trainingSet, Instances testingSet) throws Exception {
		Evaluation evaluation = new Evaluation(trainingSet);
 
		model.buildClassifier(trainingSet);
		evaluation.evaluateModel(model, testingSet);
 
		return evaluation;
	}
 
	public static double calculateAccuracy(FastVector predictions) {
		double correct = 0;
 
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
 
		return 100 * correct / predictions.size();
	}
 
	public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];
 
		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}
 
		return split;
	}
 

}

