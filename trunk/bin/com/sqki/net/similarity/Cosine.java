package com.sqki.net.similarity;

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

		cosineSim = (2) / (norm1 * norm2);

		for (int i = 0; i < _doc1.length; i++) {
			for (int j = 0; j < _doc2.length; j++) {
				if (_doc1[i].equalsIgnoreCase(_doc2[j])) {
					cosineSim = cosineSim
							+ (weight(_doc1, _doc1[i]) * weight(_doc2, _doc2[j]));
					break;
				}
			}
		}

		return cosineSim;
	}
	// The term vector may contain OOV or other unicode which is no possible to handle
	public String[] clean(String[] doc){
		
		String cleanstr=""; 
		for (int i = 0; i < doc.length; i++) {
			cleanstr=cleanstr+" "+doc[i];
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
			if (_array[i].equals(_term))
				_count++;
		}
		return _count;
	}

	public double weight(String[] doc, String term) {

		return 1d;
	}

}
