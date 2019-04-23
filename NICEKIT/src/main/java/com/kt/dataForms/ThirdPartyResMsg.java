package com.kt.dataForms;

import org.json.simple.JSONArray;

public class ThirdPartyResMsg {
	
	private JSONArray data;
	private String source;
	
	
	public JSONArray getData() {
		return data;
	}
	public void setData(JSONArray data) {
		this.data = data;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

}
