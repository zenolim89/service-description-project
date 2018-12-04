package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

public class DictionaryForm {
	
	String domainName;
	Hashtable <String, ArrayList<String>> dictionaryList = new Hashtable<String, ArrayList<String>>();
	
	public void setDomainName (String name) {
		this.domainName = name;
	}
	
	public String getDomainName () {
		return this.domainName;
	}
	
	public Hashtable<String, ArrayList<String>> getDictionaryList() {
		return this.dictionaryList;
	}

}
