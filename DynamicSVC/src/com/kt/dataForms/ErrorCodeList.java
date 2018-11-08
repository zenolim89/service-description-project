package com.kt.dataForms;

import java.util.Hashtable;

public class ErrorCodeList {
	
	Hashtable<String, String> errorCodeList = new Hashtable<String, String>();
	
	public ErrorCodeList () {
		
		errorCodeList.put("authFailed", "4001"); //인증 실패 (비밀번호 오류)
		errorCodeList.put("notFoundAuth", "40002"); //인증 실패 (해당 계정이 없음)
		errorCodeList.put("noUseAPI", "4004"); //사용할 수 있는 API가 없음 (API 오류)
		errorCodeList.put("checkParam", "4000"); //파라미터 오류
		errorCodeList.put("ok", "2000"); //인증 성공 
	}
	
	public Hashtable<String, String> getErrorCodeList () {
		return this.errorCodeList;
	}

}
