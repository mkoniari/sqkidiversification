package com.sqki.net;

import java.util.HashMap;

import com.sqki.net.util.PrintResult;

import lemurproject.indri.DocumentVector;
import lemurproject.indri.QueryEnvironment;

public class Main {

	/**
	 * @param args
	 */
	
 /*
	 * The initial parameter 
	 */
	//on Macbook
	static String myIndex="/Users/Research/research/Data/FT.Diversity.Index";
	//on windows
	//static String myIndex="J:\\Sadegh-Personal\\research\\data\\FT.Diversity.Index";
	static int cuttoff=100;
	public static int getCuttoff() {
		return cuttoff;
	}
	static int topicNumber;
	static double lambda;
	static String aspectFile="aspect/TREC.aspects";
	

	static String resultFile;
	static String divMethod="scorediff";
	
	  public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
        // Parameter : topic result lambda
		  topicNumber = Integer.parseInt(args[0]);
		  resultFile=args[1];
		  lambda=Double.parseDouble(args[2]);
		  
//		  resultFile="baseline/result.okapi";
//		  topicNumber=303;
		  
		  Retrieve retrieve= new Retrieve(myIndex, topicNumber, resultFile);
		 // retrieve.checkToWork();
		  PrintResult printResult= new PrintResult();
          printResult.print(retrieve.run()); 
           
		 
	}

	// Getter and Setter to use the parameter
	public static int getTopicNumber() {
		return topicNumber;
	}
	public static double getLambda() {
		return lambda;
	}
}
