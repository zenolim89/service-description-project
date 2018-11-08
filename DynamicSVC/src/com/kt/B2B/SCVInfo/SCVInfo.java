package com.kt.B2B.SCVInfo;

public class SCVInfo {
	
	private static int TOUCH = 1;
	private static int VOICE = 2;
	private static int REMOCON = 3;
	
	// (jdlee) setting SCV
	private int InterfaceType;
	private String SCVCode;
	private String ActionID;
	
	private String RefAPI;
	private String SCVDescription;
	private String SCVFunctionName;
	
	// (jdlee) java objclass to json 
	private String TargetURL;
	private String MethodType;
	private String DataType;
	private String Dataformat;
	private String DataBody;
	
	
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
	
	public String getActionID() {
		return ActionID;
	}
	
	public void setActionID(String actionID) {
		ActionID = actionID;
	}
	
	public String getRefAPI() {
		return RefAPI;
	}
	
	public void setRefAPI(String refAPI) {
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
	
	public String getMethodType() {
		return MethodType;
	}
	
	public void setMethodType(String methodType) {
		MethodType = methodType;
	}
	
	public String getDataType() {
		return DataType;
	}
	
	public void setDataType(String dataType) {
		DataType = dataType;
	}
	
	public String getDataformat() {
		return Dataformat;
	}
	
	public void setDataformat(String dataformat) {
		Dataformat = dataformat;
	}
	
	public String getDataBody() {
		return DataBody;
	}
	
	public void setDataBody(String dataBody) {
		DataBody = dataBody;
	}
	
}
