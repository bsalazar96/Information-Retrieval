package ir.vsr;

import java.io.*;
import java.util.*;
import java.lang.*;

import ir.utilities.*;
import ir.classifiers.*;

public class PageRankInvertedIndex extends InvertedIndex {

	public int weight;
	public static HashMap<String, Double> pageRanks = new HashMap<String, Double>();
	;

	public PageRankInvertedIndex(File dirname, short docType, int w) {
		super(dirname, docType, false, false);
		weight = w;
	}

	/**
	* Index the documents in dirFile.
	*/
	protected void indexDocuments() {
		if (!tokenHash.isEmpty() || !docRefs.isEmpty()) {
	// Currently can only index one set of documents when an index is created
			throw new IllegalStateException("Cannot indexDocuments more than once in the same InvertedIndex");
		}
	// Get an iterator for the documents
		DocumentIterator docIter = new DocumentIterator(dirFile, docType, stem);
		System.out.println("Indexing documents in " + dirFile);
		String end = "";
		String name = "";

		while (docIter.hasMoreDocuments()) {
			FileDocument doc = docIter.nextDocument();
			name = doc.file.getName();
			end = name.substring(name.length()-4); // get the last 4 characters to see if it's an .html file
			
			if (doc.file.getName().equals("pageRanks.txt")){
				importHashMap(doc.file);
			}

			//only index the html files
			if (end.equals("html")){
				System.out.print(doc.file.getName() + ",");	
				HashMapVector vector = doc.hashMapVector();
				indexDocument(doc, vector);
			}
		}
		// Now that all documents have been processed, we can calculate the IDF weights for
		// all tokens and the resulting lengths of all weighted document vectors.
		computeIDFandDocumentLengths();
		System.out.println("\nIndexed " + docRefs.size() + " documents with " + size() + " unique terms.");
	}

	protected Retrieval getRetrieval(double queryLength, DocumentReference docRef, double score) {
    // Normalize score for the lengths of the two document vectors
		score = score / (queryLength * docRef.length);
		double rank = pageRanks.get(docRef.file.getName());
    // Add a Retrieval for this document to the result array
		return new Retrieval(docRef, score+rank*weight);
	}


	public static void importHashMap (File f){
		Scanner s = new Scanner("");
		try {
			s = new Scanner(f);
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

		String[] tokens = new String[2];


		while (s.hasNextLine()){
			String temp = s.nextLine();
		//	System.out.println("Line is " + temp);
			tokens = temp.split(" ");
		//	System.out.println("Tokens: " + tokens[0] + " " + tokens[1]);
			pageRanks.put(tokens[0], Double.parseDouble(tokens[1]));
		}
	}


	public static void main(String[] args) {
	// Parse the arguments into a directory name and optional flag
		int weight = 0;
		String dirName = args[args.length - 1];
		short docType = DocumentIterator.TYPE_TEXT;
		boolean stem = false, feedback = false;
		for (int i = 0; i < args.length - 1; i++) {
			String flag = args[i];
			if (flag.equals("-html"))
				docType = DocumentIterator.TYPE_HTML;
			else if (flag.equals("-stem"))
				stem = true;
			else if (flag.equals("-feedback"))
				feedback = true;
			else if (flag.equals("-weight")){
				weight = Integer.parseInt(args[i+1]);
				i++;
			}
			else {
				throw new IllegalArgumentException("Unknown flag: "+ flag);
			}
		}


	// Create an inverted index for the files in the given directory.
		PageRankInvertedIndex index = new PageRankInvertedIndex(new File(dirName), docType, weight);
	// index.print();
	// Interactively process queries to this index.
		index.processQueries();
	}

}