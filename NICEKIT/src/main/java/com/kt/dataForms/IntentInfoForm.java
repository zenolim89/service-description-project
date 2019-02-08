package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.simple.JSONArray;

public class IntentInfoForm {
	
	int seqNum;
	String intentName;
	String desc;
	JSONArray arr;
	
	
	public int getSeqNum() {
		return seqNum;
	}
	
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}
	
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
