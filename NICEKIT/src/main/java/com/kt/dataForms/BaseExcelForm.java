package com.kt.dataForms;

import org.json.simple.JSONArray;

public class BaseExcelForm {

	String domainName; // 도메인명
	String domainId; // 도메인아이디
	String specName; // 규격명

	String serviceName; // 서비스명
	String invokeType; // 인보크타입
	String serviceType; // 서비스타입
	String serviceLink; // 연결링크
	String serviceDesc; // 서비스개요
	String serviceCode; // 서비스 코드
	String id; // 인텐트 아이디
	JSONArray svcList; // 서비스 리스트
	JSONArray intentInfo; // 인텐트 정보
	JSONArray dicList; // 사전 리스트
	JSONArray wordList; // 어휘 리스트

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
