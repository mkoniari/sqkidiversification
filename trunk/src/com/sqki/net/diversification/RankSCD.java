package com.sqki.net.diversification;

import java.util.HashMap;
import com.sqki.net.Main;
import com.sqki.net.similarity.Cosine;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;

public class RankSCD {
	int rank=1;
	ResultList diverse = new ResultList();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();
	HashMap<Integer, Double> docIDmapNewScore = new HashMap<Integer, Double>();
	HashMap<Integer, String[]> docIDmapTermVector = new HashMap<Integer, String[]>();
	HashMap<Integer,Integer> docIDmapSortRank= new HashMap<Integer, Integer>();
	HashMap<Integer,Integer> rankSortMapdocID= new HashMap<Integer, Integer>();
	ResultList templist= new ResultList();
	
	
	public RankSCD(HashMap<Integer, String[]> docIDmapTV,
			HashMap<Integer, String> docIDMN, HashMap<Integer, Double> docIDMS,
			HashMap<Integer, Integer> docIDMR,
			HashMap<Integer, Integer> ranKMDID, ResultList tlist){
		
		docIDmapName = docIDMN;
		docIDmapRank = docIDMR;
		ranKmapDocID = ranKMDID;
		docIDmapScore=docIDMS;	
		docIDmapTermVector=docIDmapTV;
		templist=tlist;
		
		
	}
	
	public ResultList run(){
		
		for (int i = 0; i < templist.getResultList().size(); i++) {
			
			int currentDocID=templist.getResultList().get(i).getDocID();
			System.err.println( docIDmapRank.size());
			System.err.println( "index is :" + i + " Rank is : "+ docIDmapRank.get(currentDocID) + " docID :"+currentDocID);
			double newScore=(1/templist.getResultList().get(i).getRank()) +(1/docIDmapRank.get(currentDocID));
			docIDmapNewScore.put(templist.getResultList().get(i).getDocID(), newScore);
			
		}
		
		sorting(docIDmapNewScore);
		
		for (int i = 1; i <= Main.getCuttoff(); i++) {
			
			put (rankSortMapdocID.get(i),i);
		}
		
		return diverse;
		
	}
	
	private void put(int docID, int rank) {
		Result tmpresult = new Result();
		tmpresult.setRank(rank);
		tmpresult.setDocID(docID);
		tmpresult.setDocName(docIDmapName.get(docID));
		tmpresult.setScore(docIDmapScore.get(docID));
		tmpresult.setTopicNumber(Main.getTopicNumber());
		tmpresult.setRunID(docIDmapRank.get(docID).toString());
		//tmpresult.setRunID((docIDmapRank.get(docID).toString()));
		removedocID(docID);
		diverse.getResultList().add(tmpresult);

	}

	private void removedocID(int docid) {
		ranKmapDocID.remove(docIDmapRank.get(docid));
		//docIDmapTermVector.remove(docid);
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
			
			docIDmapSortRank.put(id, rank);
			rankSortMapdocID.put(rank,id);
			rank++;
			unsorted.remove(id);
		}

}
	
}
