package com.sqki.net.similarity;

import java.util.HashMap;

public class JSD {

	String[] _doc1;
	String[] _doc2;

	HashMap<String, Integer> doc1TF= new HashMap<String,Integer>();
	HashMap<String, Integer> doc2TF= new HashMap<String,Integer>();
	//Normalised Vector by total number of term in doc
	HashMap<String, Double> doc1NV= new HashMap<String,Double>();
	HashMap<String, Double> doc2NV= new HashMap<String,Double>();
	HashMap<String, Double> MVector= new HashMap<String,Double>();
	
	
	public JSD(String[] doc1, String[] doc2) {

		// TODO Auto-generated constructor stub

		
		
		_doc1 = clean(doc1);
		_doc2 = clean(doc2);
		
		
		
	}
	
	public double similarity(){
		
		
		
		for (int i = 0; i < _doc1.length; i++) {
			int TF=termFrequency(_doc1,_doc1[i]);
			doc1TF.put(_doc1[i], TF);
			
		}
		
		
		
		for (int i = 0; i < _doc2.length; i++) {
			int TF=termFrequency(_doc2,_doc2[i]);
			doc2TF.put(_doc2[i], TF);
		}
		
		
		//Language model for each document
		for (String key : doc1TF.keySet()) {
		
			double value=doc1TF.get(key)/Double.valueOf(_doc1.length);
			doc1NV.put(key, value);
		}
		
		
		for (String key : doc2TF.keySet()) {
			
			double value=(doc2TF.get(key))/Double.valueOf(_doc2.length);
			doc2NV.put(key, value);
		}
		
		
		
		// M Vector
		
		MVector=createM(doc1NV, doc2NV);
		
		
		
		// Calculate the KL Divergence
		
		double kld_doc1=KLD(MVector,doc1NV);
		
		double kld_doc2=KLD(MVector,doc2NV);
		
		double jsd=0.5*kld_doc1 +0.5*kld_doc2;
		
		return jsd;
	} 
	
	public double KLD(HashMap<String, Double> M, HashMap<String,Double> document){
		
		double sum=0d;
		
		for (String key : document.keySet()){
			
			double part1=document.get(key);
			double part2=document.get(key)/M.get(key);
			//System.err.println( "part 1: "+ part1 + " part 2: "+part2);
			sum=sum+(document.get(key) * (Math.log10(document.get(key)/M.get(key))));
			System.err.println((document.get(key) * (Math.log10(document.get(key)/M.get(key)))));
		}
		
		return sum;
	}

	public HashMap<String, Double> createM(HashMap<String, Double> s1, HashMap<String, Double> s2){
		
		HashMap<String , Double> temp=new HashMap<String, Double>();
		
		for (String key : s1.keySet()) {
			
			double value=-1;
			
			if (s2.containsKey(key)){
				
				value=(s1.get(key)+s2.get(key))/2;
				
				
			}else{
				value=s1.get(key)/2;
				
			}
			
			temp.put(key, value);
			
		}
		
		
		
		for (String key : s2.keySet()) {
			
			double value=-1;
			
			if (s1.containsKey(key)){
				
				value=(s1.get(key)+s2.get(key))/2;
				
				
			}else{
				value=s2.get(key)/2;
				
			}
			
			temp.put(key, value);
		}
		
		return temp;
		
	}
	public int termFrequency(String[] _array, String _term) {
		int _count = 0;
		for (int i = 0; i < _array.length; i++) {
			if (_array[i].equalsIgnoreCase(_term)){
				_count++;
		}
			}
		return _count;
	}

	public String[] clean(String[] doc) {

		String cleanstr = "";
		for (int i = 0; i < doc.length; i++) {

			if (doc[i].contains("[OOV]")) {
				//System.err.println(" FIND OOVVVVVV          ********");
			} else {
				cleanstr = cleanstr + " " + doc[i];
				//System.err.println(doc[i]);
			}
		}
		String[] cleanDoc = cleanstr.split(" ");
		return cleanDoc;
	}
}

