package com.kt.commonUtils;

import org.json.simple.JSONObject;

public class Constants {
	/** 카산드라 DB 관련 */
	public static final String CASSANDRA_PROPERTY_HOST = "222.107.124.9";

	public static final String CASSANDRA_KEYSPACE_COMMON = "commonks";
	public static final String CASSANDRA_TABLE_DOMAINLIST = "domainlist";
	public static final String CASSANDRA_TABLE_DOMAINSVCLIST = "domainservicelist";
	public static final String CASSANDRA_TABLE_INTENTINFO = "intentinfo";
	public static final String CASSANDRA_TABLE_SPECINDEXLIST = "specindexlist";
	public static final String CASSANDRA_TABLE_TEMPLATEINDEXLIST = "templateindexlist";
	public static final String CASSANDRA_TABLE_TEMPINDEXLIST = "tempindexlist";

	public static final String CASSANDRA_KEYSPACE_DOMAIN = "domainks";
	public static final String CASSANDRA_TABLE_RESORT = "resort";

	public static final String CASSANDRA_KEYSPACE_VENDOR = "vendorsvcks";
	public static final String CASSANDRA_TABLE_VENDORINDEXLIST = "vendorindexlist";

	/** 외부 폴더 경로 */
	public static final String EXTERNAL_FOLDER_REALPATH = "/opt/nicekit";
	public static final String EXTERNAL_FOLDER_URLPATH_SAMPLE = "/docbase/sample";
	public static final String EXTERNAL_FOLDER_URLPATH_TEMP = "/docbase/temp";
	public static final String EXTERNAL_FOLDER_URLPATH_TEMPLATE = "/docbase/template";
	public static final String EXTERNAL_FOLDER_URLPATH_VENDORS = "/docbase/vendors";
	
	
	/** 예외처리 코드 */
	public static final String[] NOT_FOUND_ACCOUNT 						= new String[]{"401", "해당 계정을 찾을 수 없습니다."};
	public static final String[] NOT_MATCH_PASSWORD 					= new String[]{"402", "비밀번호가 일치하지 않습니다."};
	
	public static final String[] NOT_FOUND_DOMAIN 						= new String[]{"404", "도메인 정보를 찾을 수 없습니다."};
	public static final String[] NOT_FOUND_SPEC 								= new String[]{"405", "해당 도메인에 사용가능한 규격이 없습니다"};
	public static final String[] NOT_FOUND_TEMPLATE 					= new String[]{"406", "해당 도메인에 사용가능한 UI 템플릿이 없습니다"};
	public static final String[] NOT_FOUND_VENDOR 						= new String[]{"407", "해당 도메인에 등록된 사업장 정보가 없습니다."};
	public static final String[] NOT_FOUND_URL 								= new String[]{"408", "URL 정보를 찾을수 없습니다."};
	public static final String[] NOT_FOUND_DIRECTORY 					= new String[]{"409", "복사할 원본 디렉토리가 존재하지 않습니다"};
	public static final String[] NOT_FOUND_PREVIEW_VENDOR 		= new String[]{"410", "해당 사업자에 대한 미리보기가 없습니다"};
	public static final String[] NOT_FOUND_PREVIEW_TEMPLATE 	= new String[]{"411", "해당 템플릿에 대한 미리보기가 없습니다"};
	public static final String[] NOT_FOUND_TEMP 							= new String[]{"412", "편집 할 사업장 정보가 없습니다"};
	public static final String[] NOT_FOUND_SAVE_TEMP 					= new String[]{"413", "저장 할 사업장 정보가 없습니다"};
	public static final String[] NOT_FOUND_DEPLOY_VENDOR  		= new String[]{"414", "배포 할 사업장 정보가 없습니다"};
	
	public static final String[] SERVICE_UNAVAILABLE 					= new String[]{"500", "서비스를 처리할수 없음"};

	public static final String[] INVALID_URL 									= new String[]{"601", "유효하지 않은 URL"};
	
	public static final String[] DIRECTORY_ALREADY_EXISTS 			= new String[]{"701", "요청하신 사업장의 디렉토리가 이미 존재합니다"};
	public static final String[] SPEC_ALREADY_EXISTS 					= new String[]{"702", "요청하신 규격이 이미 등록되어 있습니다"};
	
	public static final String[] DB_INSERT_FAILED 							= new String[]{"801", "데이터베이스 삽입 실패하였습니다."};
	

	
	



}
