package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

public class ReqDataForm {
	
//	{
//	"keyName":"userToken", "valueName":"token", "superVar":"false", "type":"JSONArray", "userDefine":"false", "subArrType":"false"
//	"subArr": "false"
//	 },
//	
//	"keyName":"serviceValue", "valueNAme":"NE", "superVar":"userToke", "type":"JSONObject", "userDefine":"true", "subArrType":"object",
//	"subArr": [{"key1":"value1", "key2":"value"}]
//	},
//	{
//	"keyName":"serviceValue", "valueNAme":"NE", "superVar":"userToke", "type":"JSONObject", "userDefine":"true", "subArrType":"string",
//	"subArr": ["key1", "key2"]
//	}
	
	String keyName;
	String valueName;
	String isSubField;
	String superVar;
	String jsonType;
	String isUserDefine;
	String subArrType;
	Hashtable<String, String> subArrObjectType = new Hashtable<String, String>();
	ArrayList<String> subArrStringType = new ArrayList<String>();
	
	public String getJsonType() {
		return jsonType;
	}
	
	public void setJsonType(String type) {
		this.jsonType = type;
	}
	
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	public String getIsSubField() {
		return isSubField;
	}
	public void setIsSubField(String isSubField) {
		this.isSubField = isSubField;
	}
	public String getSuperVar() {
		return superVar;
	}
	public void setSuperVar(String superVar) {
		this.superVar = superVar;
	}
	public String getIsUserDefine() {
		return isUserDefine;
	}
	public void setIsUserDefine(String isUserDefine) {
		this.isUserDefine = isUserDefine;
	}
	public String getSubArrType() {
		return subArrType;
	}
	public void setSubArrType(String subArrType) {
		this.subArrType = subArrType;
	}
	public Hashtable<String, String> getSubArrObjectType() {
		return subArrObjectType;
	}
	public ArrayList<String> getSubArrStringType() {
		return subArrStringType;
	}
	
	
	

	
}
