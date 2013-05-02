package com.sqki.net.diversification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sqki.net.Main;
import com.sqki.net.similarity.Cosine;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;

public class ScoreDifferenceMean {

	/*
	 * Define Hash Maps and variable which are useful
	 */
	int rank = 1;
	ResultList diverse = new ResultList();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();
	HashMap<Integer, Double> docIDmapDiffScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapDiffRankSort = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> docIDmapDiffRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapdocIDDiff = new HashMap<Integer, Integer>();
	HashMap<Integer, Double> templist = new HashMap<Integer, Double>();
	HashMap<Integer, String[]> docIDmapTermVector = new HashMap<Integer, String[]>();
	
	/* Constructor */
	public ScoreDifferenceMean(HashMap<Integer, String[]> docIDmapTV,
			HashMap<Integer, String> docIDMN, HashMap<Integer, Double> docIDMS,
			HashMap<Integer, Integer> docIDMR,
			HashMap<Integer, Integer> ranKMDID) {

		docIDmapName = docIDMN;
		docIDmapRank = docIDMR;
		ranKmapDocID = ranKMDID;
		// Use to normalise score between 0 and 1. The top rank one will get 1
		// and the bottom rank one will get 0
		// docIDmapScore = normalise(docIDMS);
		docIDmapScore = docIDMS;
		//docIDmapTermVector=docIDmapTV;
	}

	/* Main method to run this diversification approach */
	public ResultList run() {

		/* New Hashmap by new value are created */
		for (Integer i : docIDmapScore.keySet()) {
			double dif = 0.0d;
			if (docIDmapRank.get(i) == 1) {
				dif = Math.abs(docIDmapScore.get(i));
				docIDmapDiffRankSort.put(i, 1);
				ranKmapdocIDDiff.put(1, i);
				
				
			}
			if (docIDmapRank.get(i) > 1) {
				int prevDocRank = docIDmapRank.get(i) - 1;
				int prevDocID = ranKmapDocID.get(prevDocRank);
				//Cosine cosine=new Cosine(docIDmapTermVector.get(i),docIDmapTermVector.get(prevDocID));
				//dif = diff(i, prevDocID)*cosine.similarity();
				dif=diff(i,prevDocID);
				docIDmapDiffScore.put(i, dif);
			}

			
		}

		/* Finding bigger differences than mean */
		/* This is happen to be greedy */

		while (!docIDmapDiffScore.isEmpty()) {

			double mn = mean(docIDmapDiffScore);
			int grSc = 0;
			outerloop:
			for (Integer i : docIDmapDiffScore.keySet()) {
				if (docIDmapDiffScore.get(i) >= mn) {
					templist.put(i, docIDmapDiffScore.get(i));
					sorting(templist);
					break outerloop;
				}
			}
			 
			 
			
		}

		System.err.println(docIDmapDiffRankSort.size());
		
		for (int i = 1; i < Main.getCuttoff() + 1; i++) {
			put(ranKmapdocIDDiff.get(i), i);
		}

		return diverse;
	}

	private void put(int docID, int rank) {

		Result tmpresult = new Result();
		tmpresult.setRank(rank);
		tmpresult.setDocName(docIDmapName.get(docID));
		tmpresult.setScore(docIDmapScore.get(docID));
		tmpresult.setTopicNumber(Main.getTopicNumber());
		tmpresult.setDocID(docID);
		tmpresult.setRunID(docIDmapRank.get(docID).toString());
		removedocID(docID);
		diverse.getResultList().add(tmpresult);

	}

	public double mean(HashMap<Integer, Double> list) {
		double meanScore = 0d;
		for (Integer i : list.keySet()) {
			meanScore = meanScore + list.get(i);
		}
		return meanScore / list.size();

	}

	public double diff(int docID, int docIDprevious) {

		double difference = 0d;

		difference = reldiff(Math.abs(docIDmapScore.get(docID)),
				Math.abs(docIDmapScore.get(docIDprevious)));

		return difference;
	}

	private void removedocID(int docid) {
		ranKmapDocID.remove(docIDmapRank.get(docid));
		docIDmapTermVector.remove(docid);
		docIDmapName.remove(docid);
		docIDmapRank.remove(docid);
		docIDmapScore.remove(docid);

	}

	public void sorting(HashMap<Integer, Double> unsorted) {

		int rank = docIDmapDiffRankSort.size()+1;
		while (!unsorted.isEmpty()) {
			double max = -1d;
			int id = -1;
			for (Integer i : unsorted.keySet()) {
				if (unsorted.get(i) > max) {
					max = unsorted.get(i);
					id = i;
				}
			}

			docIDmapDiffRankSort.put(id, rank);
			ranKmapdocIDDiff.put(rank, id);
			rank++;
			docIDmapDiffScore.remove(id);
			unsorted.remove(id);
		}

	}

	public double absdiff(double fscore, double sscore) {
		double diff = 0.0d;
		diff = Math.abs(fscore - sscore);
		// System.err.println(diff);
		return diff;
	}

	public double reldiff(double fscore, double sscore) {
		double diff = 0.0d;
		diff = Math.abs((fscore - sscore) / sscore);
		return diff;

	}

	private HashMap<Integer, Double> normalise(HashMap<Integer, Double> docIDMS) {
		// TODO Auto-generated method stub
		HashMap<Integer, Double> normalScore = new HashMap<Integer, Double>();

		double maxScore = docIDMS.get(ranKmapDocID.get(1));
		double minScore = docIDMS.get(ranKmapDocID.get(ranKmapDocID.size()));
		double normalizeScore = 0d;

		// System.err.println(maxScore+" *"+ minScore + " * " +
		// ranKmapDocID.size());
		// add loop on
		Iterator it = docIDMS.entrySet().iterator();

		while (it.hasNext()) {

			Map.Entry entry = (Map.Entry) it.next();

			Integer key = (Integer) entry.getKey();

			double val = (Double) entry.getValue();

			normalizeScore = new BigDecimal((val - minScore)
					/ (maxScore - minScore)).doubleValue();
			// System.err.println(normalizeScore);
			normalScore.put(key, normalizeScore);

		}

		return normalScore;
	}

}
