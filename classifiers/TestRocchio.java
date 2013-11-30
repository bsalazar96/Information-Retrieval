package ir.classifiers;

import java.util.*;

/**
 * Wrapper class to test NaiveBayes classifier using 10-fold CV.
 * Running it with -debug option gives very detailed output
 *
 * @author Sugato Basu
 */

public class TestRocchio {
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
    System.out.println("Initializing Rocchio classifier...");
    
    boolean neg = false;

    //System.out.println("What is args[0]? " + args[0]);
    if (args[0] != null && args[0].equals("-neg"))
        neg = true;
    Rocchio RC = new Rocchio (neg);

    // Perform 10-fold cross validation to generate learning curve
    CVLearningCurve cvCurve = new CVLearningCurve(RC, examples);
    cvCurve.run();
  }
}
