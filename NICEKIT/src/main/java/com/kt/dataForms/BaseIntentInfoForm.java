package com.kt.dataForms;

import org.json.simple.JSONArray;

public class BaseIntentInfoForm {

	String intentName;
	String desc;
	JSONArray arr;

	public String getIntentName() {
		return intentName;
	}

	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public JSONArray getArr() {
		return arr;
	}

	public void setArr(JSONArray arr) {
		this.arr = arr;
	}

}
