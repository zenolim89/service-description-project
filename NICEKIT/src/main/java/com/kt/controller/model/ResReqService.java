package com.kt.controller.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ResReqService {
	
	private JSONArray data;
	private JSONObject source;
	
	
	public JSONArray getData() {
		return data;
	}
	public void setData(JSONArray data) {
		this.data = data;
	}
	public JSONObject getSource() {
		return source;
	}
	public void setSource(JSONObject source) {
		this.source = source;
	}
}
