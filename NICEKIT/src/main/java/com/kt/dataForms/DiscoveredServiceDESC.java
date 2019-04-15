package com.kt.dataForms;

import org.json.simple.JSONArray;

public class DiscoveredServiceDESC {

	String domainId;

	String interfaceType;
	String serviceCode;
	String dataType;
	String protType;
	String comURL;
	String testURL;
	String method;
	String toUrl;
	String serviceType;

	JSONArray headerInfo;
	JSONArray reqStructure;
	JSONArray reqSpec;
	JSONArray resStructure;
	JSONArray resSpec;
	JSONArray dicList;

	public void setToUrl(String url) {
		this.toUrl = url;
	}

	public String getToUrl() {
		return toUrl;
	}

	public void setServiceType(String type) {
		this.serviceType = type;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setDicList(JSONArray dic) {
		this.dicList = dic;
	}

	public JSONArray getDicList() {
		return dicList;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getProtType() {
		return protType;
	}

	public void setProtType(String protType) {
		this.protType = protType;
	}

	public String getComURL() {
		return comURL;
	}

	public void setComURL(String comURL) {
		this.comURL = comURL;
	}

	public String getTestURL() {
		return testURL;
	}

	public void setTestURL(String testURL) {
		this.testURL = testURL;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public JSONArray getHeaderInfo() {
		return headerInfo;
	}

	public void setHeaderInfo(JSONArray headerInfo) {
		this.headerInfo = headerInfo;
	}

	public JSONArray getReqStructure() {
		return reqStructure;
	}

	public void setReqStructure(JSONArray reqStructure) {
		this.reqStructure = reqStructure;
	}

	public JSONArray getReqSpec() {
		return reqSpec;
	}

	public void setReqSpec(JSONArray reqSpec) {
		this.reqSpec = reqSpec;
	}

	public JSONArray getResStructure() {
		return resStructure;
	}

	public void setResStructure(JSONArray resStructure) {
		this.resStructure = resStructure;
	}

	public JSONArray getResSpec() {
		return resSpec;
	}

	public void setResSpec(JSONArray resSpec) {
		this.resSpec = resSpec;
	}

}
