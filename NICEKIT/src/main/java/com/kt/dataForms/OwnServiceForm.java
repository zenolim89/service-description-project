package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

public class OwnServiceForm {
	
	String userAuth;
	String interfaceType; 
	String serviceCode; 
	String refAPI; 
	String intentName;
	String serviceDesc;
	String targetURL;
	String method;
	String dataType;
	String dataDefinition;
	
	ArrayList<ReqDataForm> dataformat = new ArrayList<ReqDataForm>();
	Hashtable<String, ArrayList<String>> refDialog = new Hashtable<String, ArrayList<String>>();
	
	public String getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(String userAuth) {
		this.userAuth = userAuth;
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

	public String getRefAPI() {
		return refAPI;
	}

	public void setRefAPI(String refAPI) {
		this.refAPI = refAPI;
	}

	public String getIntentName() {
		return intentName;
	}

	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public String getTargetURL() {
		return targetURL;
	}

	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataDefinition() {
		return dataDefinition;
	}

	public void setDataDefinition(String dataDefinition) {
		this.dataDefinition = dataDefinition;
	}

	
	// dialog, dataformat, class list

	public ArrayList<ReqDataForm> getDataFormat() {
		return this.dataformat;
	}

	public Hashtable<String, ArrayList<String>> getRefDialog() {
		return this.refDialog;
	}
	
	public void setRefDialog(Hashtable<String, ArrayList<String>> list) {
		this.refDialog = list;
	}

}
