package ir.classifiers;

import java.util.*;

/**
 * Wrapper class to test NaiveBayes classifier using 10-fold CV.
 * Running it with -debug option gives very detailed output
 *
 * @author Sugato Basu
 */

public class TestKNN {
  /**
   * A driver method for testing the Rocchio classifier using
   * 10-fold cross validation.
   *
   * @param args a list of command-line arguments.  Specifying "-debug"
   *             will provide detailed output
   */
  public static void main(String args[]) throws Exception {
    String dirName = "/u/mooney/ir-code/corpora/yahoo-science/";
    String[] categories = {"bio", "chem", "phys"};
    System.out.println("Loading Examples from " + dirName + "...");
    List<Example> examples = new DirectoryExamplesConstructor(dirName, categories).getExamples();
    System.out.println("Initializing KNN classifier...");
    KNN KC;
    boolean neg = false;

    int k = 5;
    if (args.length > 1 && args[0].equals("-K"))
        k = Integer.parseInt(args[1]);
    
    KC = new KNN (categories, examples, k);

    // Perform 10-fold cross validation to generate learning curve
    CVLearningCurve cvCurve = new CVLearningCurve(KC, examples);
    cvCurve.run();
  }
}
