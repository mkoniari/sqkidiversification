package com.sqki.net.diversification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sqki.net.Main;
import com.sqki.net.similarity.Cosine;
import com.sqki.net.util.AspectsList;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;

public class EXQUAD {

	int rank = 1;
	static double lambda = Main.getLambda();
	ResultList diverse = new ResultList();
	HashMap<Integer, String[]> docIDmapTermVector = new HashMap<Integer, String[]>();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();
	AspectsList aspectsList = new AspectsList();

	public EXQUAD(HashMap<Integer, String[]> docIDmapTV,
			HashMap<Integer, String> docIDMN, HashMap<Integer, Double> docIDMS,
			HashMap<Integer, Integer> docIDMR,
			HashMap<Integer, Integer> ranKMDID, AspectsList _aspectsList) {
		// TODO Auto-generated constructor stub
		docIDmapTermVector = docIDmapTV;
		docIDmapName = docIDMN;
		docIDmapScore = docIDMS;
		docIDmapRank = docIDMR;
		ranKmapDocID = ranKMDID;
		aspectsList = _aspectsList;
	}

	public ResultList run() {
		// What to do with the first rank document in original result list ?
		int docID;
		//put(docID, docIDmapScore.get(docID));
		while (docIDmapScore.size() != 0) {

			HashMap<Integer, Double> tmpList = new HashMap<Integer, Double>();
			List<String> tmpAspects= new ArrayList<String>();
			
			//TODO fix this part to suit xquad this part
			for (Map.Entry<Integer, Double> entry : docIDmapScore.entrySet()) {
				double divscore = 0d;
				docID = entry.getKey();
				divscore = xquadScore(docIDmapScore.get(docID),
						calcMaxDis(docID));
				tmpList.put(docID, divscore);
			}

			docID = findNext(tmpList);
			// System.err.println(docID + "   " + tmpList.get(docID));
			put(docID, tmpList.get(docID));
		}
		return diverse;
	}

	private int findNext(HashMap<Integer, Double> tmpList) {
		// System.err.println("now....." + rank);
		int counter = 1;
		int nextID = 0;
		Double maxScore = -1d;
		// System.err.println("now . ......." + rank);
		for (Map.Entry<Integer, Double> entry : tmpList.entrySet()) {
			// System.err.println(entry.getValue());
			if (counter == 1) {
				nextID = entry.getKey();
				maxScore = entry.getValue();
			}

			if (entry.getValue() > maxScore) {
				maxScore = entry.getValue();
				nextID = entry.getKey();
			}
			counter++;
		}

		return nextID;
	}

	private void put(int docID, double score) {

		Result tmpresult = new Result();
		tmpresult.setRank(rank);
		tmpresult.setDocName(docIDmapName.get(docID));
		// tmpresult.setScore(docIDmapScore.get(docID));
		tmpresult.setScore(score);
		tmpresult.setTopicNumber(Main.getTopicNumber());
		tmpresult.setDocID(docID);
		removedocID(docID);
		diverse.getResultList().add(tmpresult);
		rank++;

	}

	// Calculate maximum dissimilarity of current doc with retrieved docs
	private double calcMaxDis(int docID) {

		double maxDisimilarity = 0d;
		double tmpDisScore = 0d;
		Result tmpresult;
		for (int i = 0; i < diverse.getResultList().size(); i++) {
			tmpresult = diverse.getResultList().get(i);
			tmpDisScore = calcDisimilarity(docID, tmpresult.getDocID());
			if (tmpDisScore > maxDisimilarity)
				maxDisimilarity = tmpDisScore;
		}
		return maxDisimilarity;
	}

	private void removedocID(int docID) {
		// docIDmapTermVector.remove(docID);
		docIDmapName.remove(docID);
		docIDmapRank.remove(docID);
		docIDmapScore.remove(docID);
	}

	private double calcDisimilarity(int docID1, int docID2) {

		double sim = 0d;
		Cosine cosine = new Cosine(docIDmapTermVector.get(docID1),
				docIDmapTermVector.get(docID2));
		sim = cosine.similarity();
		return sim;
	}

	private double xquadScore(double sim, double disim) {

		double score = 0d;
		if (sim < 0d) {
			score = lambda * sim + (1 - lambda) * disim;
		} else {
			score = lambda * sim - (1 - lambda) * disim;
		}
		return score;
	}
}
