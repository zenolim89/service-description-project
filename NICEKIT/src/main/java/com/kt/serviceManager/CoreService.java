package com.kt.serviceManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import com.datastax.driver.core.Row;
import com.fasterxml.jackson.core.JsonParser;
import com.kt.controller.exception.DbInsertFailedException;
import com.kt.controller.exception.DirectoryAlreadyExistsException;
import com.kt.controller.exception.NotFoundAccountException;
import com.kt.controller.exception.NotFoundDeployVendorException;
import com.kt.controller.exception.NotFoundDirectoryException;
import com.kt.controller.exception.NotFoundPreviewTemplateException;
import com.kt.controller.exception.NotFoundPreviewVendorException;
import com.kt.controller.exception.NotFoundSaveTempException;
import com.kt.controller.exception.NotMatchPasswordException;
import com.kt.controller.exception.SpecAlreadyExistsException;
import com.kt.data.model.TempInfo;
import com.kt.dataDao.Account;
import com.kt.dataDao.DeleteDataTo;
import com.kt.dataDao.InsertDataTo;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.DiscoveredServiceDESC;
import com.kt.dataForms.ReqCreateVendor;
import com.kt.dataForms.ThirdPartyResMsg;
import com.kt.dataManager.JSONParsingFrom;
import com.kt.dataManager.UITemplateController;
import com.kt.service.spec.JsonSpecSvc;
import com.kt.sevice.jsonparser.JsonParserSvc;

@Service
public class CoreService {
	
	
	/**
	 * 로그인
	 * @param id
	 * @param pw
	 * @return
	 * @exception NotFoundAccountException
	 * @exception NotMatchPasswordException
	 */
	public boolean Auth(String id, String pw){
		
		Hashtable<String, String> list = Account.getInstance().getAccountListTable();
		String authPw = list.get(id);
				
		if(authPw == null) {
			throw new NotFoundAccountException();
		}else if(!(authPw.equals(pw))) {
			throw new NotMatchPasswordException();
		}

		return true;
	}
	
	
	/**
	 * 도메인 목록 조회
	 * @return
	 */
	public List<String> getDomainList() {
		SelectDataTo selectTo = new SelectDataTo();
		List<Row> rowList = selectTo.selectDomainList2();
		List<String> domainList = new ArrayList<String>();
		
		for (Row domain : rowList) {
			domainList.add(domain.getString("domainname"));
		}
		
		return domainList;
	}
	
	/**
	 * 	규격 목록 조회
	 * @param domainName
	 * @return
	 */
	public List<String> getSepcList(String domainName) {
		SelectDataTo selectTo = new SelectDataTo();
		List<Row> rowList = selectTo.selectSpecList(domainName);
		List<String> specList = new ArrayList<String>();

		for (Row row : rowList) {
			specList.add(row.getString("specname"));
		}
		
		return specList;
	}
	
	/**
	 * 템플릿 목록 조회
	 * @param domainName
	 * @return
	 */
	public List<String> getTemplateList(String domainName) {
		SelectDataTo selectTo = new SelectDataTo();
		List<Row> rowList = selectTo.selectTemplateList(domainName);
		List<String> list = new ArrayList<String>();
		
		for (Row row : rowList) {
			list.add(row.getString("templatename"));
		}
		
		return list;
	}
	
	/**
	 * 사업장 목록 조회
	 * @param domainName
	 * @return
	 */
	public List<String> getVendorList(String domainName){
		SelectDataTo selectTo = new SelectDataTo();
		List<Row> rowList = selectTo.selectVendorlistInDomain(domainName);
		
		List<String> list = new ArrayList<String>();
		
		for (Row row : rowList) {
			list.add(row.getString("vendorname"));
		}
		
		return list;
	}
	
	/**
	 * 임시저장 목록 조회
	 * @param domainName
	 * @return
	 */
	public List<TempInfo> getTempList(String domainName){
		SelectDataTo selectTo = new SelectDataTo();
		List<Row> rowList = selectTo.selectTemplistInDomain(domainName);
		
		List<TempInfo> list = new ArrayList<TempInfo>();
		
		for (Row row : rowList) {
			
			TempInfo info = new TempInfo(row.getString("vendorname"), row.getString("specname"));
			list.add(info);
		}
		
		return list;
	}
	
	/**
	 * 사업장을 통한 미리보기 URL 조회
	 * @param vendorName
	 * @return
	 * @exception NotFoundPreviewVendorException 해당 사업장에 대한 미리보기가 존재하지 않는 경우
	 */
	public String getVendorPage(String vendorName) {
		SelectDataTo selectTo = new SelectDataTo();
		List<Row> list = selectTo.selectUseTemplateofVendor(vendorName);
		
		if(list == null) {
			throw new NotFoundPreviewVendorException();
		}
		
		String dirPath = null;
		for (Row row : list)
			dirPath = row.getString("dirpath");
		
		return dirPath;
	}
	
	
	/**
	 * 템플릿을 통한 미리보기 URL 조회
	 * @param templateName
	 * @return
	 * 	@exception NotFoundPreviewTemplateException 해당 템플릿에 대한 미리보기가 존재하지 않는 경우
	 */
	public String getTemplatePage(String templateName) {
		
		SelectDataTo selectTo = new SelectDataTo();
		List<Row> list = selectTo.selectTemplatePreView(templateName);
		
		if(list == null) {
			throw new NotFoundPreviewTemplateException();
		}
		
		String dirPath = null;
		for (Row row : list)
			dirPath = row.getString("dirpath");
		
		return dirPath;
	}
	
	
	/**
	 * 사업장을 통한 서비스 생성
	 * @param domainName
	 * @param vendorName
	 * @param urlPath
	 * @param comUrl
	 * @param testUrl
	 * @return
	 * @exception DirectoryAlreadyExistsException 디렉토리가 이미 존재하는 경우
	 * @exception NotFoundDirectoryException 디렉토리를 찾을수 없을 경우
	 * @exception DbInsertFailedException 데이터베이스 INSERT Failed
	 * @exception SpecAlreadyExistsException 규격이 이미 존재하는 경우
	 * 
	 */
	public String createWithVendor(String domainName, String vendorName, String urlPath, String comUrl, String testUrl) {
		
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		UITemplateController uiController = new UITemplateController();
		InsertDataTo insertToSpecList = new InsertDataTo();
		InsertDataTo insertToTempList = new InsertDataTo();
		ReqCreateVendor vendorInfo = new ReqCreateVendor();
		String resCode;
		
		JSONObject specResObj = insertToSpecList.insertSpecIndexTo(vendorName, domainName);
		String specResult = specResObj.get("resCode").toString();
		String url = "";
		
		if (specResult.equals("200")) {
			JSONObject createResObj = uiController.createWithVendor(vendorName, urlPath, comUrl, testUrl);
			String createResult = createResObj.get("code").toString();
			if(createResult.equals("409")) {
				throw new DirectoryAlreadyExistsException();
			}else if(createResult.equals("400")) {
				throw new NotFoundDirectoryException();
			}else {
				vendorInfo.setDomainName(domainName);
				vendorInfo.setVendorName(vendorName);
				vendorInfo.setSpecName(vendorName);
				vendorInfo.setDirPath(createResObj.get("tempPath").toString());
				resCode = insertToTempList.insertTempToIndexList(vendorInfo);
				
				if(resCode.equals("201")) {
					JSONObject server = parsingFrom.getServerInfo();
					url = "http://" + server.get("serverIp").toString() + ":"
							+ server.get("port").toString() + createResObj.get("tempPath").toString();
				}else {
					throw new DbInsertFailedException();
				}
			}
			
		}else if(specResult.equals("409")) {
			throw new SpecAlreadyExistsException();
		}
		
		return url;
		
	}
	
	
	/**
	 * 템플릿을 이용한 사업장 생성
	 * @param domainName
	 * @param vandor
	 * @param urlPath
	 * @param specName
	 * @return
	 * @exception DirectoryAlreadyExistsException 디렉토리가 이미 존재하는 경우
	 * @exception NotFoundDirectoryException 디렉토리를 찾을수 없을 경우
	 * @exception DbInsertFailedException 데이터베이스 INSERT Failed
	 */
	public String createWithTemplate(String domainName, String vandor, String urlPath, String specName) {
		UITemplateController uiController = new UITemplateController();
		JSONObject createResObj = uiController.createWithTemplate(domainName, vandor, urlPath, specName);
		ReqCreateVendor vendorInfo = new ReqCreateVendor();
		InsertDataTo insertTo = new InsertDataTo();
		String resultCode = createResObj.get("code").toString();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String url = "";
		
		if (resultCode.equals("409")) {
			throw new DirectoryAlreadyExistsException();
		} else if(resultCode.equals("400")) {
			throw new NotFoundDirectoryException();
		}else {
			vendorInfo.setDomainName(domainName);
			vendorInfo.setVendorName(vandor);
			vendorInfo.setSpecName(specName);
			vendorInfo.setDirPath(createResObj.get("tempPath").toString());
			
			String resCode = insertTo.insertTempToIndexList(vendorInfo);
			
			if(resCode.equals("201")) {
				
				JSONObject server = parsingFrom.getServerInfo();
				
				url = "http://" + server.get("serverIp").toString() + ":"
						+ server.get("port").toString() + createResObj.get("tempPath").toString();
			}else {
				throw new DbInsertFailedException();
			}
		}
		
		return url;
		
	}
	
	
	/**
	 * 임시 저장
	 * @param vendorName 사업장명
	 * @param domainName 도메인명
	 * @return
	 * @exception NotFoundSaveTempException 저장 할 사업장 정보를 찾을수 없는 경우
	 */
	public String saveTemp(String vendorName, String domainName) {
		
		SelectDataTo selectTo = new SelectDataTo();
		List<Row> list = selectTo.selectTempInfo(vendorName, domainName);
		
		if(list == null) {
			throw new NotFoundSaveTempException();
		}
		
		String dirPath = null;
		for (Row row : list)
			dirPath = row.getString("dirpath");
		
		return dirPath;
	}
	
	
	/**
	 * 사업장 서비스 배포
	 * @param vendor
	 * @param domain
	 * @return
	 * @exception NotFoundDeployVendorException 배포 할 사업장 정보를 찾을 수 없는 경우
	 * @exception DirectoryAlreadyExistsException 해당 사업장의 디렉토리가 이미 존재하는 경우
	 * @exception NotFoundDirectoryException 복사할 원본 디렉토리를 찾을수 없는 경우
	 */
	public String deployVendor(String vendor, String domain) {
		
		UITemplateController uiController = new UITemplateController();
		ReqCreateVendor vendorInfo = new ReqCreateVendor();
		
		InsertDataTo insertTo = new InsertDataTo();
		SelectDataTo selectTo = new SelectDataTo();
		DeleteDataTo deleteTo = new DeleteDataTo();
		
		List<Row> list = selectTo.selectTempInfo(vendor, domain);
		
		JSONObject resObj = new JSONObject();
		String tempPath = null;
		String specName = null;
		String path = "";
		
		if(list == null) {
			throw new NotFoundDeployVendorException();
		}else {
			for (Row row : list) {
				tempPath = row.getString("dirpath");
				specName = row.getString("specname");
			}
			
			resObj = uiController.moveToVendor(vendor, tempPath);
			String resultCode = resObj.get("code").toString();
			if (resultCode.equals("409")) {
				throw new DirectoryAlreadyExistsException();
			}else if(resultCode.equals("400")) {
				throw new NotFoundDirectoryException();
			}else {
				path = resObj.get("vendorPath").toString();
				vendorInfo.setDomainName(domain);
				vendorInfo.setVendorName(vendor);
				vendorInfo.setSpecName(specName);
				vendorInfo.setDirPath(path);
				
				insertTo.insertVendorToIndexList(vendorInfo);
				deleteTo.deleteRowForTempIndexList(vendor,domain);
				
				return path;
			}
		}
	}
	

	/**
	 * 
	 * @param keySpace
	 * @param tableName
	 * @param intentName
	 * @param property
	 * @param word
	 */
	public ThirdPartyResMsg executeService(String keySpace, String tableName, String intentName, String property, String word) {
		
		keySpace = "vendorsvcks";
		property = "발화어휘";
		System.out.println("[DEBUG] 수신된 인텐트명: " + intentName + " 요청된 어휘: " + word + " 서비스 사업장 구분자:" + tableName);
		
		SelectDataTo selectTo = new SelectDataTo();
		DiscoveredServiceDESC desc = selectTo.selectServiceInfo(keySpace, tableName, intentName);
		
		JsonSpecSvc svc = new JsonSpecSvc();
		JSONObject temp  = svc.createReqFormat(desc.getStrReqStructure(), desc.getStrReqSpec(), word, "발화 어휘");
		
		String word_id = "";
		if(word.equals("을숙도")){
			word_id = "2507834";
		}else if(word.equals("영동난계")) {
			word_id = "1118680";
		}else if(word.equals("가락몰")) {
			word_id = "2509701";
		}else if(word.equals("부여국악")) {
			word_id = "1258353";
		}
			
		
		JSONObject param  = svc.createReqFormat(temp.toString(), desc.getStrReqSpec(), word_id, "설정 값");
		System.out.println(param.toString());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json;charset=utf-8");
		//HttpEntity entity = new HttpEntity(headers);
		
		StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro"); /*URL*/

        try {
		
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+param.get("ServiceKey").toString());/*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(param.get("numOfRows").toString(), "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(param.get("pageNo").toString(), "UTF-8")); /*현재 페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode(param.get("MobileOS").toString(), "UTF-8")); /*IOS(아이폰),AND(안드로이드),WIN(원도우폰),ETC*/
        urlBuilder.append("&" + URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode(param.get("MobileApp").toString(), "UTF-8")); /*서비스명=어플명*/
        urlBuilder.append("&" + URLEncoder.encode("contentTypeId","UTF-8") + "=" + URLEncoder.encode(param.get("contentTypeId").toString(), "UTF-8")); /*관광타입(관광지, 숙박 등) ID*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode(param.get("_type").toString(), "UTF-8")); /*기본정보 조회여부*/
        urlBuilder.append("&" + URLEncoder.encode("contentId","UTF-8") + "=" + URLEncoder.encode(param.get("contentId").toString(), "UTF-8")); /*콘텐츠ID*/
		
        URL url = new URL(urlBuilder.toString());
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        
        conn.setRequestProperty("Content-type", "application/json;charset=utf-8");
        BufferedReader rd;

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();

        conn.disconnect();
        
        JsonSpecSvc jsonSpecSvc =new JsonSpecSvc();
        List<Map<String, String>> _msg = jsonSpecSvc.selectResMsg(sb.toString(), desc.getStrResSpec() , "설정 값");

        JsonParserSvc jsonParserSvc = new JsonParserSvc();
        
        JSONArray jsonMsg = jsonParserSvc.getJsonArrayFromList(_msg);
        
        ThirdPartyResMsg resMsg = new ThirdPartyResMsg();
        resMsg.setSource(sb.toString());
        resMsg.setData(jsonMsg);
        
        return resMsg;
		
        }catch (IOException e) {
			e.printStackTrace();
		}
        return null;
        }
}
