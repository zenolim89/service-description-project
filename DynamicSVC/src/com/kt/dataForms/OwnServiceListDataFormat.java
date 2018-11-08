package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

public class OwnServiceListDataFormat {
	
	//intent, dictionary 추가 방안 고민 필
	
	String authId;
	String serviceCode;
	Hashtable<String, String> dataformat = new Hashtable<String, String>();
	
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
	
	public void setDataFormat (ReqDataFormatForService format) {
		
		ArrayList<ReqDataFormatForService> data = format.getReqList();
		
		for (int i=0; i < data.size(); i ++ ) {
			
			this.dataformat.put(data.get(i).getKey(), data.get(i).getValue());
		}		
		
	}
	
	public Hashtable<String, String> getDataFormat() {
		return this.dataformat;
	}

}
