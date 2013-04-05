package com.sqki.net.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Cosine {

	String[] _doc1;
	String[] _doc2;

	public Cosine(String[] doc1, String[] doc2) {

		// TODO Auto-generated constructor stub

		_doc1 = clean(doc1);
		_doc2 = clean(doc2);
	}

	public double similarity() {

		double cosineSim = 0d;
		double norm1 = euclideanDist(_doc1);
		double norm2 = euclideanDist(_doc2);
		
		HashMap<String, Integer> doc1vector= new HashMap<String, Integer>();
		HashMap<String, Integer> doc2vector= new HashMap<String, Integer>();
		doc1vector=docVector(_doc1);
		//doc2vector=docVector(_doc2);
		
		
		double sclar=0d;
		for (int i = 0; i < _doc2.length; i++) {
			if (doc1vector.containsKey(_doc2[i])) {
			sclar=sclar+ weight(_doc2, _doc2[i])*doc1vector.get(_doc2[i]);	
			}
			
		}
		
		cosineSim=sclar/(norm1*norm2);
		//System.err.println(cosineSim);
		return cosineSim;
	}
	private HashMap<String,Integer> docVector(String[] doc){
		
		HashMap<String,Integer> vec= new HashMap<String, Integer>();
		
		for (int i = 0; i < doc.length; i++) {
			if(vec.containsKey(doc[i])){
				vec.put(doc[i], vec.get(doc[i])+1);
			}else {
				vec.put(doc[i], 1);
			}
		}
		return vec;
	}
	// The term vector may contain OOV or other unicode which is no possible to handle
	public String[] clean(String[] doc){
		
		String cleanstr=""; 
		for (int i = 0; i < doc.length; i++) {
			if (!doc[i].equals("[OOV]")) cleanstr=cleanstr+" "+doc[i];
		}
		String[] cleanDoc=cleanstr.split(" ");
		return cleanDoc;
	}
	private String[] convertToArray(String doc) {
		String[] sepDoc = doc.split(" ");
		return sepDoc;
	}

	private Double euclideanDist(String[] _docTerms) {
		double _eucDist = 0d;

		for (int i = 0; i < _docTerms.length; i++) {
			_eucDist = _eucDist + Math.pow(weight(_docTerms, _docTerms[i]), 2);
		}
		_eucDist = Math.sqrt(_eucDist);

		return _eucDist;
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

	public double weight(String[] doc, String term) {

		double w=0;
		for (int i = 0; i < doc.length; i++) {
			if (doc[i].equals(term)){
				w++;
			}
			
		}
		return w;
	}

}
