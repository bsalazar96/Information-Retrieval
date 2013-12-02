package ir.classifiers;

import java.io.*;
import java.util.*;

import ir.vsr.*;
import ir.utilities.*;

public class KNN extends Classifier {

	public int k;
	public InvertedIndex index;
	public HashMap<Example, HashMapVector> vectors;

	public KNN (String[] categories, List<Example> examples, int knum){
		k = knum;
		this.categories = categories;
		vectors = new HashMap<Example, HashMapVector>();
		index = new InvertedIndex(examples);
	}

	public void train (List<Example> trainingExamples){
		vectors = new HashMap<Example, HashMapVector>();
		for (Example ex : trainingExamples){
			HashMapVector d = getTFIDFVector(ex.getHashMapVector());
			vectors.put(ex,d);
		}	
	}

	public boolean test (Example testExample){
		HashMapVector d = getTFIDFVector(testExample.getHashMapVector());

		//a tree map of differences and category
		TreeMap<Double,Integer> diff_cat = new TreeMap<Double,Integer>();
		for (Map.Entry<Example, HashMapVector> entry : vectors.entrySet()){
			double s = 1-d.cosineTo(entry.getValue());
			diff_cat.put(s,entry.getKey().getCategory());
		}	

		// keep track of the counts of the number of times a category appears
		// in the top k searches. The index is the category
		double[] counts = new double[categories.length];
		int index = 0;

		for (Map.Entry<Double, Integer> e : diff_cat.entrySet()){
			if (index >= k)
				break;
			else{
				counts[e.getValue()]++;
			}
			index++;
		}

		int guess = argMax(counts);
		return guess == testExample.getCategory();
	}

	public HashMapVector getTFIDFVector (HashMapVector vector){
		double maxTermFreq = vector.maxWeight();
		HashMapVector ans = new HashMapVector();

		for (Map.Entry<String, Weight> entry : vector.entrySet()){
			String token = entry.getKey();
			double tf = entry.getValue().getValue();
            //get the idf for that term
			double idf = index.tokenHash.get(token).idf;
			double tf_idf = tf/maxTermFreq*idf;
			Weight w = new Weight();
			w.setValue(tf_idf);
			ans.hashMap.put(token, w);
		}
		return ans;
	}   

	public String getName(){
		return "KNN";
	}
}

