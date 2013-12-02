#!/bin/bash


javac KNN.java TestKNN.java Rocchio.java TestRocchio.java 

java ir.classifiers.NaiveBayes 

java ir.classifiers.TestRocchio 
mv Rocchio.data RocchioNormal.data
mv RocchioTrain.data RocchioNormalTrain.data


java ir.classifiers.TestRocchio -neg
mv Rocchio.data RocchioModified.data
mv RocchioTrain.data RocchioModifiedTrain.data


java ir.classifiers.TestKNN -K 1
mv KNN.data KNN_1.data
mv KNNTrain.data KNN_1_Train.data



java ir.classifiers.TestKNN -K 3
mv KNN.data KNN_3.data
mv KNNTrain.data KNN_3_Train.data



java ir.classifiers.TestKNN
mv KNN.data KNN_5.data
mv KNNTrain.data KNN_5_Train.data

