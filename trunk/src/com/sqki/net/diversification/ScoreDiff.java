package com.sqki.net.diversification;

import java.util.HashMap;

import com.sqki.net.Main;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;
//import com.sun.java.util.jar.pack.ConstantPool.Entry;

public class ScoreDiff {

	/**
	 * @param args
	 */

	HashMap<Integer, Double> docIDmapDiffScore = new HashMap<Integer, Double>();
	HashMap<Integer, Double> docIDmapRankDiffScore = new HashMap<Integer, Double>();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();

	ResultList diverse = new ResultList();

	public ScoreDiff(HashMap<Integer, String[]> docIDmapTV,
			HashMap<Integer, String> docIDMN, HashMap<Integer, Double> docIDMS,
			HashMap<Integer, Integer> docIDMR,
			HashMap<Integer, Integer> ranKMDID) {

		docIDmapName = docIDMN;
		docIDmapRank = docIDMR;
		ranKmapDocID = ranKMDID;
		docIDmapScore = docIDMS;

	}

	public ResultList run() {

		// Calculating the score difference and sort them
		HashMap<Integer, Integer> docidMapSdrRank = new HashMap<Integer, Integer>();
		
		for (Integer key : docIDmapScore.keySet()) {

			if (docIDmapRank.get(key) == 1) {
				docIDmapDiffScore.put(key, Math.abs(docIDmapScore.get(key)));
				docidMapSdrRank.put(key, 1);
			} else {

				double diffscore = 0d;
				int current_docid = key;
				int current_rank = docIDmapRank.get(key);
				int previous_rank = current_rank - 1;
				int previous_docid = ranKmapDocID.get(previous_rank);
				double current_score = docIDmapScore.get(current_docid);
				double previous_score = docIDmapScore.get(previous_docid);

				diffscore = diff(current_score, previous_score, "rel");
				docIDmapDiffScore.put(key, diffscore);
			}

		}

		
		sorting(docIDmapDiffScore,docidMapSdrRank,2);
		//System.err.println("here ...");
		
		
		// Calculate the Rank Differences Score and sort that

		for (Integer key : docIDmapRank.keySet()) {

			double rankdiffscore = 0d;
			double diff_rank = docidMapSdrRank.get(key);
			double original_rank = docIDmapRank.get(key);
			rankdiffscore = (1 / diff_rank) + (1 / original_rank);
			docIDmapRankDiffScore.put(key, rankdiffscore);

		}

		HashMap<Integer, Integer> docidMapfinalRankSDR = new HashMap<Integer, Integer>();
		
		sorting(docIDmapRankDiffScore,docidMapfinalRankSDR,1);

		
		// Create the final list and return it
		
		for (int i = 1; i < Main.getCuttoff()+1; i++) {
			
			put (FindKeyByValue(docidMapfinalRankSDR,i),i);
		}

		return diverse;
	}
	
	private int FindKeyByValue(HashMap<Integer,Integer> hashmap , int value){
		int docid=-1;
		boolean found=false;
		while (!found){
			
		for (Integer key : hashmap.keySet()) {
			if (hashmap.get(key) == value){
				docid=key;
				found=true;
			}
		}
		}
	
		return docid;
		
	}

	private void put(int docID, int rank) {

		Result tmpresult = new Result();
		tmpresult.setRank(rank);
		tmpresult.setDocName(docIDmapName.get(docID));
		tmpresult.setScore(docIDmapScore.get(docID));
		tmpresult.setTopicNumber(Main.getTopicNumber());
		tmpresult.setDocID(docID);
		tmpresult.setRunID(docIDmapRank.get(docID).toString());
		//removedocID(docID);
		diverse.getResultList().add(tmpresult);

	}

	public void sorting(HashMap<Integer, Double> unsort,HashMap<Integer,Integer> sorted,int rank) {

		// Variable to use in this method
		HashMap<Integer, Double> unsorted = new HashMap<Integer, Double>();
		//unsorted = (HashMap) unsort.clone();
		
		// Deep copy of hashmap
		for (Integer key : unsort.keySet()) {
			double value=unsort.get(key);
			unsorted.put(key, value);
			//System.err.println(key+"   "+unsort.get(key));
		}

		//HashMap<Integer, Integer> sorted = new HashMap<Integer, Integer>();
		
		//int rank = 1;
		
		
		while (!unsorted.isEmpty()) {
		Double max = -1d;
		int docID = -1;
		int topkey = -1;
		Double value=0d;
			//System.err.println("here ..." + unsorted.size());
			for (Integer key : unsorted.keySet()) {
				 value = unsorted.get(key);
				 //System.err.println(value +" **" +unsorted.size());
				if (value > max) {
					max = value;
					topkey = key;
					//System.err.println(value +" " +key);
					
				}

			}

			sorted.put(topkey, rank);
			rank++;
			unsorted.remove(topkey);
			
		}
		
		//return sorted;

	}

	public double diff(double current_score, double previous_score,
			String method) {
		double diffvalue = 0d;

		if (method.equals("rel")) {
			//System.err.println(" Difference is relative difference");
			diffvalue = Math.abs(previous_score - current_score)
					/ Math.abs(previous_score);
		}

		if (method.equals("abs")) {
			//System.err.println(" Difference is absolute difference");
			diffvalue = Math.abs(previous_score - current_score);
		}

		return diffvalue;
	}

}
