package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

public class SubValueArrKeyValeTypeFormat {

	String key;
	ArrayList<Hashtable<String, String>> varObjList = new ArrayList<Hashtable<String, String>>();

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public ArrayList<Hashtable<String, String>> getVarObjList() {
		return this.varObjList;
	}
}
