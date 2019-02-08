package com.kt.dataForms;

import java.util.ArrayList;

public class BaseDictionarySet {
	
	String dicName;
	ArrayList<String> wordList = new ArrayList<String>();
	
	public void setDicName (String name) {
		this.dicName = name;
	}
	
	public String getDicName () {
		return dicName;
	}
	
	public ArrayList<String> getWordList () {
		return wordList;
	}

}
