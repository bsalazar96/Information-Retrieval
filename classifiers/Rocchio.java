package ir.classifiers;

import java.io.*;
import java.util.*;

import ir.vsr.*;
import ir.utilities.*;

public class Rocchio extends Classifier {

    public boolean neg;
    public int numCategories;
    public HashMap<Integer, HashMapVector> prototype; 
    public int numExamples;
    public InvertedIndex index; 

    public Rocchio(String[] categories, boolean n, List<Example> examples){
        neg = n;
        this.categories = categories;
        this.numCategories = categories.length;
        prototype = new HashMap<Integer, HashMapVector>();
        index = new InvertedIndex(examples);
    }

    public void initializePrototype(){
        for (int i =0; i< numCategories; i++){
            HashMapVector p = new HashMapVector();
            prototype.put(i, p);
        }
    }

    public void train (List<Example> trainingExamples){
        numExamples = trainingExamples.size();
        //maps the category to its prototype vector
        initializePrototype();

        for (Example ex : trainingExamples){
            HashMapVector pi = prototype.get(ex.getCategory());
            HashMapVector toAdd = getTFIDFVector(ex.getHashMapVector());
            pi.add(toAdd);
        }

        if (neg){
            for (Example e : trainingExamples){
                for (Map.Entry<Integer,HashMapVector> entry: prototype.entrySet()){
                    if (e.getCategory() != entry.getKey()){
                        entry.getValue().subtract(getTFIDFVector(e.getHashMapVector()));
                    }
                }
            }
        }
    }


    public boolean test (Example testExample){
        HashMapVector d = testExample.getHashMapVector();
        d = getTFIDFVector(d);
        double m = -2.0;
        double s = 0.0;
        int guess = 1; //default guess
        HashMapVector p = new HashMapVector();
        for (int i = 0; i < numCategories; i++){
            // get the prototype vector
            p = prototype.get(i);

            s = d.cosineTo(p);
            // if this protype is a better match
            // update the guess
            if (s > m){
                m = s;
                guess = i;
            }

        }   

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
        return "Rocchio";
    }


}

