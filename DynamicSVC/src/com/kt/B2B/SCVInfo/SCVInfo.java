package com.kt.B2B.SCVInfo;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SCVInfo {
	
	private static int TOUCH = 1;
	private static int VOICE = 2;
	private static int REMOCON = 3;
	
	// (jdlee) setting SCVInfo 
	private int InterfaceType;
	private String SCVCode;
	private VoiceIntents Intent;
	
	private ArrayList<String> RefAPI;
	private String SCVDescription;
	private String SCVFunctionName;
	
	// (jdlee) java objclass to json format for creating full json 
	private String TargetURL;
	private ArrayList<String> MethodType;
	private ArrayList<String> DataType;
	private ArrayList<String> Dataformat;
	private JSONObject DataBody; 
	
		
	public SCVInfo() {
		super();
		
	}
	
	public SCVInfo(int interfaceType, String sCVCode, VoiceIntents intent, ArrayList<String> refAPI,
			String sCVDescription, String sCVFunctionName, String targetURL, ArrayList<String> methodType,
			ArrayList<String> dataType, ArrayList<String> dataformat, JSONObject dataBody) {
		super();
		InterfaceType = interfaceType;
		SCVCode = sCVCode;
		Intent = intent;
		RefAPI = refAPI;
		SCVDescription = sCVDescription;
		SCVFunctionName = sCVFunctionName;
		TargetURL = targetURL;
		MethodType = methodType;
		DataType = dataType;
		Dataformat = dataformat;
		DataBody = dataBody;
	}

	// (jdlee) get/set method
	public int getInterfaceType() {
		return InterfaceType;
	}
	
	public void setInterfaceType(String interfaceType) {
		
		interfaceType = interfaceType.toLowerCase();
		
		switch(interfaceType) {
		
		case "touch":
			InterfaceType = TOUCH;
			break;
		case "voice":
			InterfaceType = VOICE;
			break;
		case "remocon":
			InterfaceType = REMOCON;
			break;
		default:
			InterfaceType = 0;
		}
	}

	public String getSCVCode() {
		return SCVCode;
	}

	public void setSCVCode(String sCVCode) {
		SCVCode = sCVCode;
	}

	public VoiceIntents getIntent() {
		return Intent;
	}

	public void setIntent(VoiceIntents intent) {
		Intent = intent;
	}

	public ArrayList<String> getRefAPI() {
		return RefAPI;
	}

	public void setRefAPI(ArrayList<String> refAPI) {
		RefAPI = refAPI;
	}

	public String getSCVDescription() {
		return SCVDescription;
	}

	public void setSCVDescription(String sCVDescription) {
		SCVDescription = sCVDescription;
	}

	public String getSCVFunctionName() {
		return SCVFunctionName;
	}

	public void setSCVFunctionName(String sCVFunctionName) {
		SCVFunctionName = sCVFunctionName;
	}

	public String getTargetURL() {
		return TargetURL;
	}

	public void setTargetURL(String targetURL) {
		TargetURL = targetURL;
	}

	public ArrayList<String> getMethodType() {
		return MethodType;
	}

	public void setMethodType(ArrayList<String> methodType) {
		MethodType = methodType;
	}

	public ArrayList<String> getDataType() {
		return DataType;
	}

	public void setDataType(ArrayList<String> dataType) {
		DataType = dataType;
	}

	public ArrayList<String> getDataformat() {
		return Dataformat;
	}

	public void setDataformat(ArrayList<String> dataformat) {
		Dataformat = dataformat;
	}

	public JSONObject getDataBody() {
		return DataBody;
	}

	public void setDataBody(JSONObject dataBody) {
		DataBody = dataBody;
	}
	
}
