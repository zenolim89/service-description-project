package com.kt.dataDao;

import java.util.ArrayList;

import com.kt.dataForms.DictionaryForm;

public class DictionaryList {
	
	private static DictionaryList dictionaryList;
	
	ArrayList<DictionaryForm> dicList = new ArrayList<DictionaryForm>();
	
	private DictionaryList () {
		
	}
	
	public static DictionaryList getInstance()
	{
		if (dictionaryList == null) {
			dictionaryList = new DictionaryList();
		}
		
		return dictionaryList;
	}
	
	public ArrayList<DictionaryForm> getDicList() {
		
		return this.dicList;
	}

}
