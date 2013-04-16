//TODO needs to check the algorithm for correct working.The algorithm does not works correctly.

/*
 * Rank Score Difference approach. It combine the rank position of original ranking and score
 * difference ranking to build a new ranking system using a technique called COSUMup.
 * 
 * @see Rank Score Difference
 * @author sadegh kharazmi
 * 
 */
package com.sqki.net.diversification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.sqki.net.Main;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;

public class RankScoreDifference {

	int rank = 1;
	// static double lambda=Main.getLambda();
	ResultList diverse = new ResultList();
	HashMap<Integer, String[]> docIDmapTermVector = new HashMap<Integer, String[]>();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();
	
	//Second Use
	HashMap<Integer, Integer> docIDmapRankAPP = new HashMap<Integer, Integer>();
	HashMap<Integer, Double> docIDmapRankDiffScoreAPP = new HashMap<Integer, Double>();
	HashMap<Integer, String> docIDmapNameAPP = new HashMap<Integer, String>();

	ScoreDifference sdff;
	
	//Needs in sorting
	HashMap<Integer, Double> docIDmapRankDiffScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapDiffRankSort = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapdocIDRankDiff = new HashMap<Integer, Integer>();
	

	public RankScoreDifference(HashMap<Integer, String[]> docIDmapTV,
			HashMap<Integer, String> docIDMN, HashMap<Integer, Double> docIDMS,
			HashMap<Integer, Integer> docIDMR,
			HashMap<Integer, Integer> ranKMDID) {
		// TODO Constructor

		docIDmapTermVector = docIDmapTV;
		docIDmapName = docIDMN;
		docIDmapRank = docIDMR;
		
		ranKmapDocID = ranKMDID;
		docIDmapScore = docIDMS;
		
		sdff= new ScoreDifference(docIDmapTV, docIDMN, docIDMS, docIDMR, ranKMDID);

		
	}

	public ResultList run() {
		
		//DEBUG
		
		
		
		
		//Initializing a new object
		
		
		
		 Iterator it = docIDmapRank.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        Integer key = (Integer)pairs.getKey();
				Integer val = (Integer)pairs.getValue();
				//System.err.println(key + " " + val);
				docIDmapRankAPP.put(key, val);
		        
			//	it.remove(); // avoids a ConcurrentModificationException
		    }
		
		    it = docIDmapName.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        Integer key = (Integer)pairs.getKey();
				String val = (String)pairs.getValue();
				//System.err.println(key + " " + val);
				docIDmapNameAPP.put(key, val);
		        
			//	it.remove(); // avoids a ConcurrentModificationException
		    }
		  
		    
		    ResultList result= new ResultList();
		    result=sdff.run();
		  
		    
		    
		    for (int i = 0; i < result.getResultList().size(); i++) {
				
				int docid= result.getResultList().get(i).getDocID();
				//System.err.println(docid);
				double nscore=(1d/docIDmapRankAPP.get(docid))+(1d/result.getResultList().get(i).getRank());
				System.err.println(1d/docIDmapRankAPP.get(docid));
				docIDmapRankDiffScore.put(docid,nscore);
				
			}
		    
		
		
//		HashMap<Integer, Double> docIDmapDiffScore = new HashMap<Integer, Double>();
//		
//		HashMap<Integer, Integer> docIDmapDiffRank = new HashMap<Integer, Integer>();
		
		
		
		
		sorting(docIDmapRankDiffScore);
		
		for (int i = 1; i <= Main.getCuttoff(); i++) {
			
			put (ranKmapdocIDRankDiff.get(i),i);
		}
		
		return diverse;
		
	}

	
	
	

	private void put(int docID, int rank) {
		Result tmpresult = new Result();
		tmpresult.setRank(rank);
		tmpresult.setDocID(docID);
		tmpresult.setDocName(docIDmapNameAPP.get(docID));
		
		tmpresult.setScore(docIDmapRankDiffScoreAPP.get(docID));
		tmpresult.setTopicNumber(Main.getTopicNumber());

		tmpresult.setRunID("Rank.Diff");
		//removedocID(docID);
		diverse.getResultList().add(tmpresult);

	}

	private void removedocID(int docid) {
		ranKmapDocID.remove(docIDmapRank.get(docid));
		docIDmapTermVector.remove(docid);
		docIDmapName.remove(docid);
		docIDmapRank.remove(docid);
		docIDmapScore.remove(docid);

	}

	public void sorting(HashMap<Integer, Double> unsorted) {

		int rank = 1;
		while (!unsorted.isEmpty()) {
			double max = -1d;
			int id = -1;
			for (Integer i : unsorted.keySet()) {
				if (unsorted.get(i) > max) {
					max = unsorted.get(i);
					id = i;
				}
			}
			// System.err.println(id + "...."+ rank+ "......"+max);
			docIDmapDiffRankSort.put(id, rank);
			ranKmapdocIDRankDiff.put(rank, id);
			docIDmapRankDiffScoreAPP.put(id, unsorted.get(id));
			rank++;
			unsorted.remove(id);
		}

	}

}
