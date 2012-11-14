package com.sqki.net.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResultList  {

	List<Result> ResultList= new ArrayList<Result>();

	public List<Result> getResultList() {
		return ResultList;
	}

	public void setResultList(List<Result> resultList) {
		ResultList = resultList;
	}

	public void add(Result result){
		ResultList.add(result);
		
	}
	
}
