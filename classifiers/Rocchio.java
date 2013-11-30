package ir.classifiers;

import java.io.*;
import java.util.*;

import ir.vsr.*;
import ir.utilities.*;

public class Rocchio extends Classifier {

    public boolean neg;

    public Rocchio(boolean n){
        neg = n;
    }

    public void train (List<Example> trainingExamples){
    //add all vectors together

    }

    public boolean test (Example testExample){
        return false;
    }   

    public String getName(){
        return "Rocchio";
    }
}

