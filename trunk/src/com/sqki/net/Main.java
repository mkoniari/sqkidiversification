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
	//MAC Index
	//static String myIndex="/Users/Research/research/Data/FT.Diversity.Index";
	//HPC index

		//static String myIndex="/scratch/sadegh/DATA/TRECDATA/CluewebCatBIndex-StopWordRemoval-WithDoc";
		
		//Lighter HPC index about 167 G
		static String myIndex="/scratch/sadegh/DATA/TRECDATA/CluewebCatBIndex";
	//static String myIndex;
	//on windows
	//static String myIndex="J:\\Sadegh-Personal\\research\\data\\FT.Diversity.Index";
//	static int cuttoff=100;
	static int cuttoff;
	public static int getCuttoff() {
		return cuttoff;
	}
	static int topicNumber;
	static double lambda;
	static String aspectFile="/scratch/sadegh/source/sqkidiversification/aspect/TREC.aspects";
	

	static String resultFile;
	
//	static String divMethod="scorediff";
	static String divMethod="mmr";
	  
		public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
        // Parameter : topic result lambda
		  topicNumber = Integer.parseInt(args[0]);
		  resultFile=args[1];
		  lambda=Double.parseDouble(args[2]);
		  cuttoff=Integer.parseInt(args[3]);
		//  myIndex=args[3];
		  
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
