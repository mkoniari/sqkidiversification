package com.sqki.net.diversification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.sqki.net.Main;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;


public class RandomRANK {

	HashMap<Integer, Integer> tempRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> docIDmapRandomRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> randomRankMapDocID = new HashMap<Integer, Integer>();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();
	
	ResultList diverse = new ResultList();

	public RandomRANK(HashMap<Integer, String[]> docIDmapTV,
			HashMap<Integer, String> docIDMN, HashMap<Integer, Double> docIDMS,
			HashMap<Integer, Integer> docIDMR,
			HashMap<Integer, Integer> ranKMDID) {

		docIDmapName = docIDMN;
		docIDmapRank = docIDMR;
		ranKmapDocID = ranKMDID;
		docIDmapScore = docIDMS;

	}
	
	public ResultList run(){
		
		// Generate Random Ranks
		randomRankGenerate(Main.getCuttoff());
		
		for (Integer key : docIDmapRank.keySet()) {
			
			randomRankMapDocID.put(tempRank.get(docIDmapRank.get(key)), key);
		}
		
		for (int i = 1; i < Main.getCuttoff()+1; i++) {
			put(randomRankMapDocID.get(i),i);
		}
		
		
		return diverse;
	}
	
	private void randomRankGenerate(int size){
		
        ArrayList<Integer> list = new ArrayList<Integer>(size);
        for(int i = 1; i <= size; i++) {
            list.add(i);
        }

        Random rand = new Random();
        
        int rank=1;
        while(list.size() > 0) {
            int index = rand.nextInt(list.size());
            tempRank.put(rank, list.remove(index));
            rank++;
            
        }

		
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
}
