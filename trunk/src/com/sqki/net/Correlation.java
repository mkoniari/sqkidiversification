package com.sqki.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sqki.net.similarity.Cosine;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;

public class Correlation {

	/**
	 * @param args
	 */
	
	int rank=1;
	static double lambda=Main.getLambda();
	ResultList diverse = new ResultList();
	HashMap<Integer, String[]> docIDmapTermVector = new HashMap<Integer, String[]>();
	HashMap<Integer, String> docIDmapName = new HashMap<Integer, String>();
	HashMap<Integer, Double> docIDmapScore = new HashMap<Integer, Double>();
	HashMap<Integer, Integer> docIDmapRank = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> ranKmapDocID = new HashMap<Integer, Integer>();
	final int wsz=Main.windowSize;
	
	public Correlation(HashMap<Integer, String[]> docIDmapTV,
			HashMap<Integer, String> docIDMN, HashMap<Integer, Double> docIDMS,
			HashMap<Integer, Integer> docIDMR,
			HashMap<Integer, Integer> ranKMDID){
		
		docIDmapTermVector = docIDmapTV;
		docIDmapName = docIDMN;
		docIDmapRank = docIDMR;
		ranKmapDocID = ranKMDID;
		docIDmapScore = normalise(docIDMS);
		//docIDmapScore=docIDMS;
		
	}
	private String[] windowString(String query, int docID, int windowSize) {

		      
		String[] doc = docIDmapTermVector.get(docID);
		String finalDoc = " ";
		String[] q = query.split(" ");

		for (int i = 0; i < doc.length; i++) {
			//System.err.println(doc[i]);
			for (int j = 0; j < q.length; j++) {
					
				if (q[j].equalsIgnoreCase(doc[i])) {
					//System.err.println("*");
					System.err.println(docID+" "+q[j] +" "+ doc[i]);
					int min=i - windowSize;
					int max=i + windowSize + 1;
					if (min<0) min=0;
					if (max>doc.length) max=doc.length-1;
					for (int k = min; k < max; k++) {

						if (!doc[k].equals(null)) {
							finalDoc = finalDoc + " "+ doc[k];
						}
					}
				}
			}

		}
			System.err.println("final doc is : " +finalDoc);
		return finalDoc.split(" ");
	}

	public void run(){
		
		// Initialise first rank document
				
				
				String docName;
				double docScorediff;
				double docSim;
				int documentID;
				Cosine cosine;
				String[] doc1;
				String[] doc2;
				
				//System.err.println(docIDmapName.size());
				//System.out.println(Main.topicNumber +" Q0 "+ docIDmapName.get(ranKmapDocID.get(1))+" 1 "+ docIDmapScore.get(ranKmapDocID.get(1))+" 1  Correlation");
				for (int i = 2; i < Main.cuttoff+1; i++) {
					
					//TODO This one should print score similarity between two 
					//documents and the difference between two documents
					//System.err.println(ranKmapDocID.size());
					documentID=ranKmapDocID.get(i);
					docScorediff=Math.abs(docIDmapScore.get(documentID)-docIDmapScore.get(ranKmapDocID.get(i-1)));
					// For Orginal Document Similarity Check
				//	doc1=docIDmapTermVector.get(documentID);
				//	doc2=docIDmapTermVector.get(ranKmapDocID.get(i-1));
					// For Windows Based Similarity Check
					doc1=windowString(Main.getQuery(),documentID,wsz);
					doc2=windowString(Main.getQuery(),ranKmapDocID.get(i-1),wsz);
					
					cosine= new Cosine(doc1, doc2);
					
					docSim=cosine.similarity();
					
					System.out.println(Main.topicNumber +" Q0 "+ docIDmapName.get(ranKmapDocID.get(i))+" "+i+" "+ docScorediff+" "+docSim +"  Correlation");
					
				}
				
					
					
				
		
	}
	private void removedocID(int docID) {
		//docIDmapTermVector.remove(docID);
		docIDmapName.remove(docID);
		docIDmapRank.remove(docID);
		docIDmapScore.remove(docID);
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
	private HashMap<Integer, Double> normalise(HashMap<Integer, Double> docIDMS) {
		// TODO Auto-generated method stub
		HashMap<Integer,Double> normalScore= new HashMap<Integer,Double>();
		
		double maxScore=docIDMS.get(ranKmapDocID.get(1));
		double minScore=docIDMS.get(ranKmapDocID.get(ranKmapDocID.size()));
		double normalizeScore=0d;
		
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
		
		
		
		return normalScore;
	}


}
