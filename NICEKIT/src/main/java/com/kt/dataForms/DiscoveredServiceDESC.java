package com.kt.dataForms;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONArray;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DiscoveredServiceDESC {

	private String domainId;

	private String interfaceType;
	private String serviceCode;
	private String dataType;
	private String protType;
	private String comURL;
	private String testURL;
	private String method;
	private String toUrl;
	private String serviceType;

	private String strHeaderInfo;
	private String strReqStructure;
	private String strReqSpec;
	private String strResStructure;
	private String strResSpec;
	private String strDicList;
	
	private List<HttpParam> header;
	
//	JSONArray headerInfo;
//	JSONArray reqStructure;
//	JSONArray reqSpec;
//	JSONArray resStructure;
//	JSONArray resSpec;
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

	public List<HttpParam> getHeader() {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			header = mapper.readValue(strHeaderInfo, new TypeReference<List<HttpParam>>() {});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return header;
	}


//	public JSONArray getReqStructure() {
//		return reqStructure;
//	}
//
//	public void setReqStructure(JSONArray reqStructure) {
//		this.reqStructure = reqStructure;
//	}
//
//	public JSONArray getReqSpec() {
//		return reqSpec;
//	}
//
//	public void setReqSpec(JSONArray reqSpec) {
//		this.reqSpec = reqSpec;
//	}
//
//	public JSONArray getResStructure() {
//		return resStructure;
//	}
//
//	public void setResStructure(JSONArray resStructure) {
//		this.resStructure = resStructure;
//	}
//
//	public JSONArray getResSpec() {
//		return resSpec;
//	}
//
//	public void setResSpec(JSONArray resSpec) {
//		this.resSpec = resSpec;
//	}
//
	
	//++++++++++++++++++++++++++++++++
	
	


	public String getStrHeaderInfo() {
		return strHeaderInfo;
	}

	public void setStrHeaderInfo(String strHeaderInfo) {
		this.strHeaderInfo = strHeaderInfo;
	}

	public String getStrReqStructure() {
		return strReqStructure;
	}

	public void setStrReqStructure(String strReqStructure) {
		this.strReqStructure = strReqStructure;
	}

	public String getStrReqSpec() {
		return strReqSpec;
	}

	public void setStrReqSpec(String strReqSpec) {
		this.strReqSpec = strReqSpec;
	}

	public String getStrResStructure() {
		return strResStructure;
	}

	public void setStrResStructure(String strResStructure) {
		this.strResStructure = strResStructure;
	}

	public String getStrResSpec() {
		return strResSpec;
	}

	public void setStrResSpec(String strResSpec) {
		this.strResSpec = strResSpec;
	}

	public String getStrDicList() {
		return strDicList;
	}

	public void setStrDicList(String strDicList) {
		this.strDicList = strDicList;
	}
	
	
	//++++++++++++++++++++++++++++++++

}
