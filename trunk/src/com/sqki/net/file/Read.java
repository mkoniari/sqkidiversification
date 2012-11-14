package com.sqki.net.file;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;

public class Read {

	/**
	 * @param args
	 */
	
	String _resultFile;
	int _topicNumber;
	public Read(String resultFile,int topicNumber) {
		// TODO Auto-generated constructor stub
		_resultFile=resultFile;
		_topicNumber=topicNumber;
	}
	
	
	public ResultList read() {

		String name;
		double score;
		int rank;
		ResultList resultList= new ResultList();
	
		try {
			FileInputStream fstream = new FileInputStream(_resultFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] words = strLine.split(" ");

				if (_topicNumber == Integer.parseInt(words[0])) {
					
					Result result= new Result();
					result.setTopicNumber(_topicNumber);
					result.setRank(Integer.parseInt(words[3]));
					result.setDocName(words[2].trim());
					result.setScore(Double.valueOf(words[4].trim()).doubleValue());
					checkTopicNumber(result.getDocName(), result.getScore());
					resultList.add(result);
				}
			}
			
			in.close();
		} catch (Exception e) {
			e.getMessage();
		}
	 return resultList;
	}

	public void checkTopicNumber(String name, double score) {
		if (name == null ) {
			System.out
					.println("Your topic number does not exist in result file. Please check ");
			System.out
					.println("result file and topic number and run it again ");
			System.exit(1);
		}
	}
}
