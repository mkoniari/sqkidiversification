package com.sqki.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sqki.net.diversification.*;
import com.sqki.net.file.ReadAspects;
import com.sqki.net.file.ReadResultFile;

import com.sqki.net.util.AspectsList;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;

import lemurproject.indri.DocumentVector;
import lemurproject.indri.QueryEnvironment;

public class Retrieve {

	/**
	 * @param args
	 */

	String _myIndex;
	int _topicNumber;
	String _resultFile;
	DocumentVector[] documentVector;
	int[] docIDs;
	HashMap<Integer, String[]> docIDmapTermVector = new HashMap<Integer, String[]>();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();
	QueryEnvironment env = new QueryEnvironment();
	ResultList nondiverse= new ResultList();

	public Retrieve(String myIndex, int topicNumber, String resultFile) {

		// TODO Auto-generated constructor stub

		_myIndex = myIndex;
		_topicNumber = topicNumber;
		_resultFile=resultFile;
	}

	private void readResultFile(){
		ReadResultFile readfile = new ReadResultFile(_resultFile,_topicNumber);
		nondiverse=readfile.read();
		
	}
	private void retrieveDocProp() throws Exception {

		env.addIndex(_myIndex);
        DocumentVector[] documentVector;
        String[] names = new String[Main.cuttoff];
        double[] scores=new double[Main.cuttoff];
        int[] ranks= new int[Main.cuttoff];
        
        // Assigning names to array
        for (int i = 0; i < nondiverse.getResultList().size(); i++) {
			names[i]=   nondiverse.getResultList().get(i).getDocName();
			scores[i]=  nondiverse.getResultList().get(i).getScore();
			ranks[i]=   nondiverse.getResultList().get(i).getRank();
		}
        
		
		/*
		 * Create a map for Document IDs and DocNames docIdDs are the document
		 * IDs of the result set
		 */

		docIDs = env.documentIDsFromMetadata("docno", names);
		for (int i = 0; i < docIDs.length; i++) {
			docIDmapName.put(docIDs[i], names[i]);
			docIDmapScore.put(docIDs[i], scores[i]);
			docIDmapRank.put(docIDs[i], ranks[i]);
			ranKmapDocID.put(ranks[i], docIDs[i]);
		}

		/*
		 * Initialise document Vector (Terms and Positions) Document vector
		 * taken from Indri index which is load from query environment
		 */

		documentVector = env.documentVectors(docIDs);
		for (int i = 0; i < documentVector.length; i++) {
			docIDmapTermVector.put(docIDs[i], documentVector[i].stems);

		}

		/*
		 * Generate docIDmapRank and docIDmapScore hashMaps
		 */

		
		/* Check to see docIDmapScore does not contain any fake value such as -1 */
		if (docIDmapScore.containsKey(-1) || docIDmapRank.containsKey(-1)) {
			System.out
					.println("There is problem in assining docId to Score or rank... please check the source code or result file");
			System.exit(1);
		}
		
		env.close();

	}
	
	public ResultList run() throws Exception{
		ResultList finalList= new ResultList();
		readResultFile();
		retrieveDocProp();
		if (Main.divMethod.equalsIgnoreCase("mmr")){
			MMR mmr= new MMR(docIDmapTermVector, docIDmapName, docIDmapScore, docIDmapRank, ranKmapDocID);
			finalList=mmr.run();
		}
		
		if (Main.divMethod.equalsIgnoreCase("xquad")){
			ReadAspects readAspects= new ReadAspects(Main.aspectFile, _topicNumber);
			AspectsList aspectsList = readAspects.read();
			EXQUAD xquad =new EXQUAD(docIDmapTermVector, docIDmapName, docIDmapScore, docIDmapRank, ranKmapDocID,aspectsList); 
			finalList=xquad.run();
		}
		
		return finalList;
	}

	public void checkToWork() throws Exception{
		
		readResultFile();
		retrieveDocProp();
		
		
//		for (int i = 0; i < nondiverse.getResultList().size(); i++) {
//			Result tmpResult= nondiverse.getResultList().get(i);
//			System.err.println(tmpResult.getTopicNumber()+ " " + tmpResult.getNonField()+ " "+tmpResult.getDocName()+" " +tmpResult.getRank()+" "+tmpResult.getScore()+ " "+ tmpResult.getRunID());
//		}
	}
}
