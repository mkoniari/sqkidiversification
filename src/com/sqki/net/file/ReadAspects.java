package com.sqki.net.file;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.sqki.net.util.Aspect;
import com.sqki.net.util.AspectsList;
import com.sqki.net.util.Result;
import com.sqki.net.util.ResultList;

public class ReadAspects {

	String _aspectFile;
	int _topicNumber;
	
	public ReadAspects(String aspectFile,int topicNumber) {
				// TODO Auto-generated constructor stub
		//_aspectFile=resultFile;
		_topicNumber=topicNumber;
	}
	public AspectsList read() {

		
		AspectsList aspectsList= new AspectsList();
		aspectsList.setTopicNumber(_topicNumber);
		try {
			FileInputStream fstream = new FileInputStream(_aspectFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] words = strLine.split(":");
				Aspect aspect = new Aspect();
				if (_topicNumber == Integer.parseInt(words[0])) {
				  aspect.setAspect(words[1]);
				  aspect.setType(words[2]);
				  aspectsList.getAspect().add(aspect);
					
				}
			}
			
			in.close();
		} catch (Exception e) {
			e.getMessage();
		}
		// TODO fix this null return
		return aspectsList;
	}

	
	}
	

