package com.kt.dataForms;

import java.util.ArrayList;

public class ReqDataFormatForService {
	
	String key;
	String value;
	
	ArrayList<ReqDataFormatForService> reqList = new ArrayList<ReqDataFormatForService>();

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ArrayList<ReqDataFormatForService> getReqList() {
		return reqList;
	}

}
