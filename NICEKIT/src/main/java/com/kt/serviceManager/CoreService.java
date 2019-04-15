package com.kt.serviceManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.Row;
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
import com.kt.dataDao.Account;
import com.kt.dataDao.DeleteDataTo;
import com.kt.dataDao.InsertDataTo;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.ReqCreateVendor;
import com.kt.dataManager.JSONParsingFrom;
import com.kt.dataManager.UITemplateController;

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
	public List<String> getTempList(String domainName){
		SelectDataTo selectTo = new SelectDataTo();
		List<Row> rowList = selectTo.selectTemplistInDomain(domainName);
		
		List<String> list = new ArrayList<String>();
		
		for (Row row : rowList) {
			list.add(row.getString("vendorname"));
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
		JSONObject createResObj = uiController.createWithTemplate(vandor, urlPath, specName);
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
	

}
