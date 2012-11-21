package com.sqki.net.diversification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.sqki.net.Main;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;

public class ScoreDifference {
	
	int rank=1;
	static double lambda=Main.getLambda();
	ResultList diverse = new ResultList();
	HashMap<Integer, String[]> docIDmapTermVector = new HashMap<Integer, String[]>();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();
	HashMap<Integer,Double> docIDmapDiffScore=new HashMap<Integer, Double>();
	
	
	public ScoreDifference(HashMap<Integer, String[]> docIDmapTV,
			HashMap<Integer, String> docIDMN, HashMap<Integer, Double> docIDMS,
			HashMap<Integer, Integer> docIDMR,
			HashMap<Integer, Integer> ranKMDID){
		//TODO Constructor
		
		docIDmapTermVector = docIDmapTV;
		docIDmapName = docIDMN;
		docIDmapScore = docIDMS;
		docIDmapRank = docIDMR;
		ranKmapDocID = ranKMDID;
	}
	
	
	public ResultList run(){
		
		for (int i = 1; i < docIDmapScore.size(); i++) {
			
			if (i == 1) {
				Result result= new Result();
				int docId=ranKmapDocID.get(1);
				result.setDocID(docId);
				result.setDocName(docIDmapName.get(docId));
				result.setRank(1);
				result.setTopicNumber(Main.getTopicNumber());
				result.setScore(docIDmapScore.get(docId));
			}else {
				Result result= new Result();
				int docIdpervious=ranKmapDocID.get(i-1);
				int docId=ranKmapDocID.get(i);
				result.setDocID(docId);
				result.setDocName(docIDmapName.get(docId));
				result.setRank(i);
				result.setTopicNumber(Main.getTopicNumber());
				result.setScore(docIDmapScore.get(docId));
			}
		}
		Collections.sort(list);
		
		return diverse;
	}
    
	private void put(int docID){
		
		
	}
    public double diff(int docID, int docIDprevious){
    	
    	double diff=absdiff(docIDmapScore.get(docID),docIDmapScore.get(docIDprevious));
    	
    	
    	return diff;
    }
    
    public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);
            
        LinkedHashMap sortedMap = 
            new LinkedHashMap();
        
        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();
            
            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();
                
                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String)key, (Double)val);
                    break;
                }

            }

        }
        return sortedMap;
    }
    
    public double absdiff(double fscore,double sscore){
		double diff=0.0d;
		diff=Math.abs(fscore-sscore);
		return diff;
	}
	
	public double reldiff(double fscore,double sscore){
		double diff=0.0d;
		diff=Math.abs((fscore-sscore)/sscore);
		return diff;
		
	}
}
