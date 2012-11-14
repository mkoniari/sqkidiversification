package com.sqki.net.similarity;

public class BM25 {

	double k1 = 1.2d;
	double k3 = 7d;
	double b = 0.75d;
	String[] doc1;
	String[] doc2;
	
	public BM25(String[] _doc1, String[] _doc2) {
		
		// TODO Auto-generated constructor stub
		doc1=clean(_doc1);
		doc2=clean(_doc2);
	}

	
	public double similarity(){
		
		double okapiScore = 0d;
		double IDF = 0d;
		int qdf = 0; // query document Frequency
		double _termWeight = 0d;
		int qtf = 0;// query Term Frequency

		for (int i = 0; i < doc1.length; i++) {
			
			IDF = inverseDF(doc1, doc1[i]);
			qdf = termFreq(doc1, doc1[i]);
			qtf = termFreq(doc2, doc2[i]);
		
			_termWeight = (((k3 + 1) * qtf) / (k3 + qtf));
			okapiScore = _termWeight * okapiScore + IDF
					* ((qdf * (k1 + 1)) / (qdf + k1));

		}
		return okapiScore;
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
	public int termFreq(String[] _array, String _term) {
		int _count = 0;
		for (int i = 0; i < _array.length; i++) {
			if (_array[i].equals(_term))
				_count++;
		}
		return _count;
	}

	public double inverseDF(String[] docTerms, String _term) {
		double IDF = 0d;

		IDF = Math.log((1 - 1 + 0.5) / (1 + 0.5));

		return IDF;
	}
}
