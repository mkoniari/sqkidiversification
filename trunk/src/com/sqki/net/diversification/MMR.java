package com.sqki.net.diversification;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.sqki.net.Main;
import com.sqki.net.similarity.Cosine;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;

public class MMR {

	int rank=1;
	static double lambda=Main.getLambda();
	ResultList diverse = new ResultList();
	HashMap<Integer, String[]> docIDmapTermVector = new HashMap<Integer, String[]>();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();
	
	
	
	public MMR(HashMap<Integer, String[]> docIDmapTV,
			HashMap<Integer, String> docIDMN, HashMap<Integer, Double> docIDMS,
			HashMap<Integer, Integer> docIDMR,
			HashMap<Integer, Integer> ranKMDID) {
		// TODO Auto-generated constructor stub

		docIDmapTermVector = docIDmapTV;
		docIDmapName = docIDMN;
		docIDmapRank = docIDMR;
		ranKmapDocID = ranKMDID;
		docIDmapScore = normalise(docIDMS);
		
		
	}
    
	public ResultList run() {
    	// Initialise first rank document
		int docID = ranKmapDocID.get(1);
		put(docID,docIDmapScore.get(docID));
	   //Iteration to find the next suitable document
		while (docIDmapScore.size() != 0){
			
			HashMap<Integer,Double> tmpList= new HashMap<Integer, Double>();
			
			for (Map.Entry<Integer, Double> entry : docIDmapScore.entrySet()) {
				double divscore=0d;
				docID=entry.getKey();
				// For calculating the score using initial retrieval score
				divscore=mmrScore(docIDmapScore.get(docID), calcMaxDis(docID));
				//For Calculating the similarity and dissimilarity based on Cosine
				//divscore=mmrScore(calcSimilarity(Main.getQuery(), docID), calcMaxDis(docID));
				tmpList.put(docID, divscore);	
			}
			
			docID=findNext(tmpList);
			//System.err.println(docID + "   " + tmpList.get(docID));
			put(docID,tmpList.get(docID));
			
		}
		
		return diverse;
	}
	private int findNext(HashMap<Integer,Double> tmpList){
		//System.err.println("now....." + rank);
		int counter=1;
		int nextID=0;
		Double maxScore= -1d;
		//System.err.println("now . ......." + rank); 
		for (Map.Entry<Integer, Double> entry : tmpList.entrySet()) {
			//System.err.println(entry.getValue());
			if (counter==1) {
				nextID=entry.getKey();
				maxScore=entry.getValue();
			}
			
			if (entry.getValue() > maxScore) {
				maxScore=entry.getValue();
				nextID=entry.getKey();
			}
			counter++;
		}
       
		return nextID;
	}
	private void put(int docID,double score){
		
		Result tmpresult= new Result();
		tmpresult.setRank(rank);
		tmpresult.setDocName(docIDmapName.get(docID));
		//tmpresult.setScore(docIDmapScore.get(docID));
		tmpresult.setScore(score);
		tmpresult.setTopicNumber(Main.getTopicNumber());
		tmpresult.setDocID(docID);
		removedocID(docID);
		diverse.getResultList().add(tmpresult);
		
		//System.err.println("Score : " + score + " :: " + rank );
		rank++;
	
	}
	// Calculate maximum dissimilarity of current doc with retrieved docs
	private double calcMaxDis(int docID){
		
		double maxDisimilarity=0d;
		double tmpDisScore=0d;
		Result tmpresult;
		for (int i = 0; i < diverse.getResultList().size(); i++) {
			tmpresult=diverse.getResultList().get(i);
			tmpDisScore=calcDisimilarity(docID, tmpresult.getDocID());
		    if (tmpDisScore > maxDisimilarity) maxDisimilarity=tmpDisScore;
		}
		return maxDisimilarity;
	}
	private void removedocID(int docID) {
		//docIDmapTermVector.remove(docID);
		docIDmapName.remove(docID);
		docIDmapRank.remove(docID);
		docIDmapScore.remove(docID);
	}
	private double calcDisimilarity(int docID1, int docID2){
		
		double sim=0d;
		Cosine cosine= new Cosine(docIDmapTermVector.get(docID1), docIDmapTermVector.get(docID2));
		sim=cosine.similarity();
		return sim;
	}
	
   private double calcSimilarity(String q, int docID2){
		
		double sim=0d;
		String[] query = q.split(" ");
		Cosine cosine= new Cosine(query, docIDmapTermVector.get(docID2));
		sim=cosine.similarity();
		return sim;
	}
	private double mmrScore(double sim, double disim) {
		
		//System.err.println("lambda Value is : " + lambda);
		double score = 0d;
		
			score = lambda * sim - (1 - lambda) * disim;
		
		//System.err.println("sim : " + sim + " dis : "+ disim);
		return score;
	}
	
	private HashMap<Integer, Double> normalise(HashMap<Integer, Double> docIDMS) {
		// TODO Auto-generated method stub
		HashMap<Integer,Double> normalScore= new HashMap<Integer,Double>();
		
		double maxScore=docIDMS.get(ranKmapDocID.get(1));
		double minScore=docIDMS.get(ranKmapDocID.get(ranKmapDocID.size()));
		double normalizeScore=0d;
		
		System.err.println(maxScore+" *"+ minScore + " * " + ranKmapDocID.size());
		// add loop on 
		Iterator it = docIDMS.entrySet().iterator();

		while (it.hasNext()) {

		Map.Entry entry = (Map.Entry) it.next();

		Integer key = (Integer)entry.getKey();

		double val = (Double)entry.getValue();

		normalizeScore=(val-minScore)/(maxScore-minScore);
		normalScore.put(key, normalizeScore);

		}
		
		//DEBUG
		it = normalScore.entrySet().iterator();

		while (it.hasNext()) {

		Map.Entry entry = (Map.Entry) it.next();

		Integer key = (Integer)entry.getKey();

		double val = (Double)entry.getValue();

		System.err.println("ID : " + key + "  score : "+ val);

		}
		
		return normalScore;
	}

}
