package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

public class OwnServiceList {
	
	private static OwnServiceList ownServiceList;
	
	String authId;
	String serviceCode;
	String serviceDesc;
	Hashtable<String, String> dataformat = new Hashtable<String, String>();
	Hashtable<String, String> refDialog = new Hashtable<String, String>();
	
	ArrayList<OwnServiceList> ownList = new ArrayList<OwnServiceList>();
	
	private OwnServiceList () {
		
	}
	
	public static OwnServiceList getInstance ()
	{
		if (ownServiceList == null) {
			ownServiceList = new OwnServiceList();
		}
		
		return ownServiceList;
	}
	
	public void setServiceDesc (String desc) {
		this.serviceDesc = desc;
	}
	
	public String getServiceDesc () {
		return this.serviceDesc;
	}
	
	public void setAuthId (String id) {
		this.authId = id;
	}
	
	public String getAuthId () {
		return this.authId;
	}
	
	public void setServiceCode (String code) {
		this.serviceCode = code;
	}
	
	public String getServiceCode () {
		return this.serviceCode;
	}
	
	public Hashtable<String, String> getDataFormat() {
		return this.dataformat;
	}
	
	public Hashtable<String, String> getRefDialog() {
		return this.refDialog;
	}
	
	public ArrayList<OwnServiceList> getOwnList() {
		return this.ownList;
	}
}
