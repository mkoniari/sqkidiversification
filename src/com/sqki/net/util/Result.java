package com.sqki.net.util;

import java.math.BigDecimal;

public class Result {

	private int topicNumber;
	private String nonField="Q0";
	private String docName;
	private int rank;
	private double score;
	private String runID="SQKIFRM";
	private int docID;
	public int getDocID() {
		return docID;
	}
	public void setDocID(int docID) {
		this.docID = docID;
	}
	public int getTopicNumber() {
		return topicNumber;
	}
	public void setTopicNumber(int topicNumber) {
		this.topicNumber = topicNumber;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	public String getNonField() {
		return nonField;
	}
	public void setNonField(String nonField) {
		this.nonField = nonField;
	}
	public String getRunID() {
		return runID;
	}
	public void setRunID(String runID) {
		this.runID = runID;
	}
	
}
