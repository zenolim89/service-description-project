package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BaseExcelForm {

	String domainName;
	String domainId;
	String specName;

	String serviceName;
	String invokeType;
	String serviceType;
	String serviceLink;
	String serviceDesc;
	String serviceCode;
	String id;
	String dicInfo;
	JSONArray svcList;
	JSONArray intentInfo;
	JSONArray dicList;
	JSONArray wordList;

	public String getDicInfo() {
		return dicInfo;
	}

	public void setDicInfo(String dicInfo) {
		this.dicInfo = dicInfo;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JSONArray getSvcList() {
		return svcList;
	}

	public void setSvcList(JSONArray svcList) {
		this.svcList = svcList;
	}

	public JSONArray getIntentInfo() {
		return intentInfo;
	}

	public void setIntentInfo(JSONArray intentInfo) {
		this.intentInfo = intentInfo;
	}

	public JSONArray getDicList() {
		return dicList;
	}

	public void setDicList(JSONArray dicList) {
		this.dicList = dicList;
	}

	public JSONArray getWordList() {
		return wordList;
	}

	public void setWordList(JSONArray wordList) {
		this.wordList = wordList;
	}

}
