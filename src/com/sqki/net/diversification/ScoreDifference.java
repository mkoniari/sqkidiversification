package com.sqki.net.diversification;

import java.math.BigDecimal;
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

public class ScoreDifference {
	
	int rank=1;
	//static double lambda=Main.getLambda();
	ResultList diverse = new ResultList();
	//HashMap<Integer, String[]> docIDmapTermVector = new HashMap<Integer, String[]>();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();
	HashMap<Integer,Double> docIDmapDiffScore=new HashMap<Integer, Double>();
	HashMap<Integer,Integer> docIDmapDiffRankSort=new HashMap<Integer, Integer>();
	HashMap<Integer,Integer> docIDmapDiffRank=new HashMap<Integer, Integer>();
	HashMap<Integer,Integer> ranKmapdocIDDiff=new HashMap<Integer, Integer>();
	
    public ScoreDifference(HashMap<Integer, String[]> docIDmapTV,
			HashMap<Integer, String> docIDMN, HashMap<Integer, Double> docIDMS,
			HashMap<Integer, Integer> docIDMR,
			HashMap<Integer, Integer> ranKMDID){
		//TODO Constructor
		
		//docIDmapTermVector = docIDmapTV;
		docIDmapName = docIDMN;
		
		docIDmapRank = docIDMR;
		ranKmapDocID = ranKMDID;
		
		docIDmapScore = normalise(docIDMS);
		//docIDmapScore=docIDMS;	
    }
	
	
	public ResultList run(){
	
		
		for (Integer i: docIDmapScore.keySet()){
		    double dif=0.0d;
			if (docIDmapRank.get(i) == 1) {
			    dif=Math.abs(docIDmapScore.get(i)*100);
				
			}
			if (docIDmapRank.get(i) > 1) {
				int prevDocRank=docIDmapRank.get(i)-1;
				int prevDocID=ranKmapDocID.get(prevDocRank);
				
			    dif=diff(i,prevDocID);
			}
			
			docIDmapDiffScore.put(i, dif);
		}
		
		sorting(docIDmapDiffScore);
		
//		for (Integer i: docIDmapDiffRankSort.keySet()){
//			put(i,docIDmapDiffRankSort.get(i));
//			
//		}
		
		for (int i = 1; i <= Main.getCuttoff(); i++) {
			
			put (ranKmapdocIDDiff.get(i),i);
		}
		return diverse;
	}
    
	private void put(int docID,int rank){
		
		
		Result tmpresult= new Result();
		tmpresult.setRank(rank);
		tmpresult.setDocName(docIDmapName.get(docID));
		tmpresult.setScore(docIDmapScore.get(docID));
		tmpresult.setTopicNumber(Main.getTopicNumber());
		tmpresult.setDocID(docID);
		tmpresult.setRunID(docIDmapRank.get(docID).toString());
		removedocID(docID);
		diverse.getResultList().add(tmpresult);
		
	}
    public double diff(int docID, int docIDprevious){
    	
    	double diff=0d;
    	
    	diff=reldiff(Math.abs(docIDmapScore.get(docID)),Math.abs(docIDmapScore.get(docIDprevious)));
    	
    	
    	return diff;
    }
    
    private void removedocID(int docid) {
    	ranKmapDocID.remove(docIDmapRank.get(docid));
    	//docIDmapTermVector.remove(docid);
		docIDmapName.remove(docid);
		docIDmapRank.remove(docid);
		docIDmapScore.remove(docid);
		
		}
    
  
    
    public void sorting(HashMap<Integer, Double> unsorted){
		
    	int rank=1;
    	while (!unsorted.isEmpty()){
    		double max=-1d;
    		int id=-1;
    		for (Integer i: unsorted.keySet()){
    	        if (unsorted.get(i)>max){
    	        	max=unsorted.get(i);
    	        	id=i;
    	        }
    		}
    	   // System.err.println(id + "...."+ rank+ "......"+max);
    		docIDmapDiffRankSort.put(id, rank);
    		ranKmapdocIDDiff.put(rank, id);
    		rank++;
    		unsorted.remove(id);
    	}
    	
    	
    	
    }
    
    public double absdiff(double fscore,double sscore){
		double diff=0.0d;
		diff=Math.abs(fscore-sscore);
		//System.err.println(diff);
		return diff;
	}
	
	public double reldiff(double fscore,double sscore){
		double diff=0.0d;
		diff=Math.abs((fscore-sscore)/sscore);
		return diff;
		
	}
	
	private HashMap<Integer, Double> normalise(HashMap<Integer, Double> docIDMS) {
		// TODO Auto-generated method stub
		HashMap<Integer,Double> normalScore= new HashMap<Integer,Double>();
		
		
		double maxScore=docIDMS.get(ranKmapDocID.get(1));
		double minScore=docIDMS.get(ranKmapDocID.get(ranKmapDocID.size()));
		double normalizeScore=0d;
		if(maxScore > 0){
			normalScore=docIDMS;
			
		} else {
		
		//System.err.println(maxScore+" *"+ minScore + " * " + ranKmapDocID.size());
		// add loop on 
		Iterator it = docIDMS.entrySet().iterator();

		while (it.hasNext()) {

		Map.Entry entry = (Map.Entry) it.next();

		Integer key = (Integer)entry.getKey();

		double val = (Double)entry.getValue();

		normalizeScore=(val-minScore)/(maxScore-minScore);
		normalScore.put(key, normalizeScore);

		}
		
		}
		
		return normalScore;
	}

}
