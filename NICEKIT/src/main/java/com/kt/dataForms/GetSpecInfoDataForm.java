package com.kt.dataForms;

import org.json.simple.JSONArray;

public class GetSpecInfoDataForm {
	
	String serviceName;
	String invokeType;
	String serviceType;
	String serviceLink;
	String serviceDesc;
	String serviceCode;
	String intenName;
	JSONArray wordList;
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getInvokeType() {
		return invokeType;
	}
	public void setInvokeType(String invokeType) {
		this.invokeType = invokeType;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceLink() {
		return serviceLink;
	}
	public void setServiceLink(String serviceLink) {
		this.serviceLink = serviceLink;
	}
	public String getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getIntenName() {
		return intenName;
	}
	public void setIntenName(String intenName) {
		this.intenName = intenName;
	}
	public JSONArray getWordList() {
		return wordList;
	}
	public void setWordList(JSONArray wordList) {
		this.wordList = wordList;
	}
	
	
	

}
