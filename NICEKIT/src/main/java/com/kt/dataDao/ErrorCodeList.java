package com.kt.dataDao;

import java.util.Hashtable;

public class ErrorCodeList {
	
	Hashtable<String, String> errorCodeList = new Hashtable<String, String>();
	
	public ErrorCodeList () {
		
		errorCodeList.put("2001", "정상적으로 서비스가 등록되었습니다"); //인증 실패 (비밀번호 오류)
		errorCodeList.put("4004", "해당 경로를 찾을 수 없습니다"); //인증 실패 (해당 계정이 없음)
		 
	}
	
	public Hashtable<String, String> getErrorCodeList () {
		return this.errorCodeList;
	}

}
