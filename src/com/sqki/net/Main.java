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
	
	//HOMEPC INDEX
	//static String myIndex="/home/sqki/Dropbox/Research/IR/DATA/SAMPLE_CLUEWEB/index";
	
	//HPC index
 	static String myIndex="/scratch/sadegh/DATA/TRECDATA/CluewebCatBIndex-StopWordRemoval-WithDoc";
		
	//Lighter HPC index about 167 G
	//static String myIndex="/scratch/sadegh/DATA/TRECDATA/CluewebCatBIndex";
	//static String myIndex;
	//on windows
	//static String myIndex="J:\\Sadegh-Personal\\research\\data\\FT.Diversity.Index";
	//static int cuttoff=100;
	static int cuttoff;
	
	
	
	static int topicNumber;
	static double lambda;
	static int windowSize;
	
	static String aspectFile="/scratch/sadegh/source/sqkidiversification/aspect/TREC.aspects";
	
	static String query;
	
	static String resultFile;
	
//	static String divMethod="scorediff";
//	//static String divMethod="scorediffRank";
//	static String divMethod="scorediffMean";
//	//static String divMethod="RankSCD";
// 	static String divMethod="mmr";
	static String divMethod="correlation";
//	static String divMethod="xquad";
//	static String divMethod="windowMMR";
// New Methods
//   static String divMethod="rankscoredifference";
//   static String divMethod="RandomRank";
   
	
	  
	  
		public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
			System.err.println(" Diversification Method is : "+ divMethod);
			
			
		/*
		 * Instruction to user 
		 * arg 1 : topic number
		 * arg 2 : result file 
		 * arg 3 : lambda value
		 * arg 4 : cutt off
		 * arg 5 : query String	
		 * arg 6: window size for correlation and window MMR
		 */
			
        // Parameter : topic result lambda
		  topicNumber = Integer.parseInt(args[0]);
		  resultFile=args[1];
		  lambda=Double.parseDouble(args[2]);
		  cuttoff=Integer.parseInt(args[3]);
		  query=args[4];
		  windowSize=Integer.parseInt(args[5]);
		//  myIndex=args[3];
		  
//		  resultFile="baseline/result.okapi";
//		  topicNumber=303;
		  System.err.println(" Result file is :  "+ resultFile +"  Topic Number is : "+ topicNumber);
		  Retrieve retrieve= new Retrieve(myIndex, topicNumber, resultFile);
		 // retrieve.checkToWork();
		  PrintResult printResult= new PrintResult();
          printResult.print(retrieve.run()); 
           
		 
	}

	// Getter and Setter to use the parameter
	public static String getQuery() {
			return query;
	}
	public static int getTopicNumber() {
		return topicNumber;
	}
	public static double getLambda() {
		return lambda;
	}

	public static int getCuttoff() {
		return cuttoff;
	}

	public static void setCuttoff(int cuttoff) {
		Main.cuttoff = cuttoff;
		System.err.println(cuttoff);
	}
	
}
