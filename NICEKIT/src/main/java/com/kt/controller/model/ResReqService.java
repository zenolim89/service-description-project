package com.kt.controller.model;

import org.json.simple.JSONObject;

public class ResReqService {
	
	private JSONObject data;
	private JSONObject source;
	
	
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
	public JSONObject getSource() {
		return source;
	}
	public void setSource(JSONObject source) {
		this.source = source;
	}
}
