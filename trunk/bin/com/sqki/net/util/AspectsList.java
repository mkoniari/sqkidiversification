package com.sqki.net.util;

import java.util.ArrayList;
import java.util.List;

public class AspectsList {
	
	private int topicNumber;
	private List<Aspect> aspect= new ArrayList<Aspect>();
	
	public int getTopicNumber() {
		return topicNumber;
	}
	public void setTopicNumber(int topicNumber) {
		this.topicNumber = topicNumber;
	}
	public List<Aspect> getAspect() {
		return aspect;
	}
	public void setAspect(List<Aspect> aspect) {
		this.aspect = aspect;
	}

}
