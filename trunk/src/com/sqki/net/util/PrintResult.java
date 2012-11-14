package com.sqki.net.util;

public class PrintResult {

	public PrintResult() {
		// TODO Auto-generated constructor stub

	}

	public void print(ResultList resultlist) {

		for (int i = 0; i < resultlist.getResultList().size(); i++) {
			Result tmpResult = resultlist.getResultList().get(i);
			System.out.println(tmpResult.getTopicNumber() + " "
					+ tmpResult.getNonField() + " " + tmpResult.getDocName()
					+ " " + tmpResult.getRank() + " " + tmpResult.getScore()
					+ " " + tmpResult.getRunID());

		}
	
	}

}