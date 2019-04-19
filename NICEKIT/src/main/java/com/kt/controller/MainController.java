package com.kt.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.Void;
import java.util.ArrayList;
import java.util.List;

import com.kt.controller.exception.NotFoundDomainException;
import com.kt.controller.exception.NotFoundSpecException;
import com.kt.controller.exception.NotFoundTempException;
import com.kt.controller.exception.NotFoundTemplateException;
import com.kt.controller.exception.NotFoundVendorException;
import com.kt.controller.model.ReqAuth;
import com.kt.controller.model.ReqCreateWithTemplate;
import com.kt.controller.model.ReqCreateWithVendor;
import com.kt.controller.model.ReqDeployVendor;
import com.kt.controller.model.ReqSaveTemp;
import com.kt.controller.model.ResCreateWithTemplate;
import com.kt.controller.model.ResCreateWithVendor;
import com.kt.controller.model.ResDeployVendor;
import com.kt.controller.model.ResGetDomain;
import com.kt.controller.model.ResGetSpecList;
import com.kt.controller.model.ResGetTemp;
import com.kt.controller.model.ResGetTemplate;
import com.kt.controller.model.ResGetTemplatePage;
import com.kt.controller.model.ResGetVendor;
import com.kt.controller.model.ResGetVendorPage;
import com.kt.controller.model.ResSaveTemp;
import com.kt.controller.model.ResponseData;
import com.kt.data.model.TempInfo;
import com.kt.dataForms.DicParam;
import com.kt.dataForms.ExcelUploadForm;
import com.kt.dataForms.ResFileUpload;
import com.kt.dataManager.ExcelService;
import com.kt.dataManager.JSONParsingFrom;
import com.kt.dataManager.UtilFile;
import com.kt.serviceManager.CoreService;

@RestController
@RequestMapping("/new")
public class MainController extends BaseController{
	
	@Autowired
	private CoreService coreSvc;
	
	
	/**
	 * 관리자 계정 인증
	 * @param id 아이디
	 * @param pw 비밀번호
	 * @exception
	 * 		Case01 ID 불일치(NOT_FOUND_ACCOUNT)
	 * 		Case02 PW 불일치(NOT_MATCH_PASSWORD)
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public ResponseData<Void> reqAuth(@RequestBody ReqAuth req) {

		System.out.println("ID: " + req.getId());
		System.out.println("PW: " + req.getPw());

		coreSvc.Auth(req.getId(), req.getPw());

		return successResponse();
	}
	
	
	/**
	 * 등록된 도메인 목록 조회
	 * @return
	 * @exception
	 * 		Case01 도메인 목록이 없을 경우(NOT_FOUND_DOMAIN)
	 */
	@RequestMapping(value = "/getDomain", method = RequestMethod.GET)
	public ResponseData<ResGetDomain> getDomain() {
		ResGetDomain result = new ResGetDomain();
		
		List<String> domainList = coreSvc.getDomainList();
		if (domainList == null || domainList.isEmpty()) {
			throw new NotFoundDomainException();
		}

		result.setDomainList(domainList);

		return successResponse(result);
	}

	
	/**
	 * 등록된 3rd 연동규격 목록 조회
	 * @return
	 * @exception
	 * 		Case01 연동규격 목록이 없을 경우(NOT_FOUND_SPEC)
	 */
	@RequestMapping(value = "/getSpecList", method = RequestMethod.GET)
	public ResponseData<ResGetSpecList> getSepcList(@RequestParam String domainName) {
		ResGetSpecList result = new ResGetSpecList();

		List<String> specList = coreSvc.getSepcList(domainName);
		if (specList == null || specList.isEmpty()) {
			throw new NotFoundSpecException();
		}

		result.setSpecList(specList);

		return successResponse(result);
	}

	
	/**
	 * 등록된 템플릿 목록 조회
	 * @return
	 * @exception
	 * 		Case01 템플릿 목록이 없을 경우(NOT_FOUND_TEMPLATE)
	 */
	@RequestMapping(value = "/getTemplate", method = RequestMethod.GET)
	public ResponseData<ResGetTemplate> getTemplate(@RequestParam String domainName) {
		ResGetTemplate result = new ResGetTemplate();

		List<String> templateList = coreSvc.getTemplateList(domainName);
		if (templateList == null || templateList.isEmpty()) {
			throw new NotFoundTemplateException();
		}

		result.setTemplateList(templateList);

		return successResponse(result);
	}
	
	
	/**
	 * 등록된 사업장 목록 조회
	 * @param domianName
	 * @return
	 * @exception
	 * 		Case01 등록된 사업장 목록이 없을 경우(NOT_FOUND_VENDOR)
	 */
	@RequestMapping(value = "/getVendor", method = RequestMethod.GET)
	public ResponseData<ResGetVendor> getVendor(@RequestParam String domainName) {
		ResGetVendor result = new ResGetVendor();

		List<String> vendorList = coreSvc.getVendorList(domainName);
		if (vendorList == null || vendorList.isEmpty()) {
			throw new NotFoundVendorException();
		}

		result.setVendorList(vendorList);

		return successResponse(result);

	}


	/**
	 * 임시저장된 사업장 목록 조회
	 * @param domainName
	 * @return
	 * @exception 
	 * 		Case01 편집할 사업장이 없을 경우(NOT_FOUND_TEMP)
	 */
	@RequestMapping(value = "/getTemp", method = RequestMethod.GET)
	public ResponseData<ResGetTemp> getTemp(@RequestParam String domainName) {
		ResGetTemp result = new ResGetTemp();
		List<TempInfo> tempList = coreSvc.getTempList(domainName);

		if (tempList == null || tempList.isEmpty()) {
			throw new NotFoundTempException();
		}

		result.setTempList(tempList);

		return successResponse(result);
	}
	
	/**
	 * 사업장 서비스 화면 미리보기
	 * @param domianName 도메인명
	 * @param vendorName 사업장명
	 * @return
	 * @exception 
	 * 		Case01 해당 사업장에 대한 미리보기가 존재하지 않는 경우(NOT_FOUND_PREVIEW_VENDOR)
	 * 
	 */
	@RequestMapping(value = "/getVendorPage", method = RequestMethod.GET)
	public ResponseData<ResGetVendorPage> getVendorPage(@RequestParam String vendorName) {
		ResGetVendorPage result = new ResGetVendorPage();

		String dirPath = coreSvc.getVendorPage(vendorName);

		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONObject server = parsingFrom.getServerInfo();
		String path = "http://" + server.get("serverIp") + ":" + server.get("port") + dirPath;

		result.setUrlPath(path);
		
		return successResponse(result);

	}
	
	
	/**
	 * 템플릿 서비스 화면 미리보기
	 * @param domainName
	 * @param templateName
	 * @return
	 * @exception 
	 * 		Case01 해당 템플릿에 대한 미리보기가 존재하지 않는 경우(NOT_FOUND_PREVIEW_TEMPLATE)
	 */
	@RequestMapping(value = "/getTemplatePage", method = RequestMethod.GET)
	public ResponseData<ResGetTemplatePage> getTemplatePage(@RequestParam String domainName,
			@RequestParam String templateName) {
		ResGetTemplatePage result = new ResGetTemplatePage();

		String dirPath = coreSvc.getTemplatePage(templateName);

		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONObject server = parsingFrom.getServerInfo();
		String path = "http://" + server.get("serverIp") + ":" + server.get("port") + dirPath;

		result.setUrlPath(path);

		return successResponse(result);

	}
	

	/**
	 * 신규 사업장 폴더 생성 및 기존 사업장 파일 복사
	 * @param domianName
	 * @param vendorName
	 * @param urlPath
	 * @return
	 * @exception
	 * 		Case01 이미 존재한 디렉토리가 있는 경우(DIRECTORY_ALREADY_EXISTS)
	 * 		Case02 디렉토리를 찾지 못할 경우(NOT_FOUND_DIRECTORY)
	 * 		Case03 데이터베이스 INSERT Failed(DB_INSERT_FAILED)
	 * 		Case04 이미 규격이 존재 하는 경우(SPEC_ALREADY_EXISTS)
	 * 
	 */
	@RequestMapping(value="/CreateWithVendor", method=RequestMethod.POST)
	public ResponseData<ResCreateWithVendor> createWithVendor(@RequestBody ReqCreateWithVendor req){
		ResCreateWithVendor result = new ResCreateWithVendor();
		
		String path = coreSvc.createWithVendor(req.getDomianName(), 
																	req.getVendor(), 
																	req.getUrlPath(), 
																	req.getComUrl(), 
																	req.getTestUrl());
		result.setUrlPath(path);
		return successInsertResponse(result);
		
	}
	
	
	/**
	 * 신규 사업장 폴더 생성 및 기존 템플릿 복사
	 * @param domianName	도메인명
	 * @param vendorName		신규사업장명
	 * @param urlPath		기존 템플릿 경로
	 * @return
	 * @exception
	 * 		Case01 디렉토리가 이미 존재하는 경우(DIRECTORY_ALREADY_EXISTS)
	 * 		Case02 복사할 원본 디렉토리가 존재하지 않는 경우(NOT_FOUND_DIRECTORY)
	 * 		Case03 데이터베이트 Insert 오류(DB_INSERT_FAILED)
	 */
	@RequestMapping(value="/CreateWithTemplate", method=RequestMethod.POST)
	public ResponseData<ResCreateWithTemplate> createWithTemplate(@RequestBody ReqCreateWithTemplate req){
		ResCreateWithTemplate result = new ResCreateWithTemplate();
		
		String path = coreSvc.createWithTemplate(req.getDomainName(), 
																		req.getVendorName(), 
																		req.getUrlPath(), 
																		req.getSpecName());
		
		result.setUrlPath(path);
		
		return successInsertResponse(result);
		
	}
	
	
	/**
	 * 신규 서비스 명세 파일 업로드
	 * @param request
	 * @param domainName
	 * @param domainId
	 * @param specName
	 * @param uploadFile
	 * @return
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public ResponseData<List<ResFileUpload>> fileUpload(MultipartHttpServletRequest request,
			@RequestParam String domainName, @RequestParam String domainId, @RequestParam String specName,
			@RequestParam("uploadFile") MultipartFile uploadFile) {

		System.out.println("RewardController reAddProCtrl uploadFile : " + uploadFile);
		// System.out.println("RewardController reAddProCtrl reward : " + reward);

		// UtilFile 객체 생성
		UtilFile utilFile = new UtilFile();

		// 파일 업로드 결과값을 path로 받아온다(이미 fileUpload() 메소드에서 해당 경로에 업로드는 끝났음)
		String uploadPath = utilFile.fileUpload(request, uploadFile);

		// System.out.println("RewardController reAddProCtrl n : " + n);
		System.out.println("RewardController reAddProCtrl uploadPath : " + uploadPath);

		/* 업로드 엑셀파일 파서 */
		ExcelService excelSvc = new ExcelService();
		List<ExcelUploadForm> list = excelSvc.excelUpload(uploadPath, domainName, domainId, specName);

		List<ResFileUpload> array = new ArrayList<ResFileUpload>();

		for (ExcelUploadForm e : list) {
			ResFileUpload res = new ResFileUpload();
			res.setServiceName(e.getServiceName());
			res.setServiceCode(e.getServiceCode());
			res.setServiceDesc(e.getServiceDesc());
			res.setServiceType(e.getServiceType());
			res.setInvokeType(e.getInvokeType());
			res.setIntentName(e.getIntentInfo());
			res.setIsRegistered(e.isRegistered());

			/* dicNameList, wordList 매핑 */
			if (e.getDicList() != null) {
				StringBuilder sbDic = new StringBuilder();
				StringBuilder sbWord = new StringBuilder();

				List<DicParam> dicList = e.getDicList();
				for (DicParam item : dicList) {
					if (sbDic.length() > 0)
						sbDic.append("<br>");
					if (sbWord.length() > 0)
						sbWord.append("<br>");

					sbDic.append(item.getDicName());
					sbWord.append(item.getWordList());
				}
				res.setDicNameList(sbDic.toString());
				res.setWordList(sbWord.toString());
			}

			array.add(res);
		}

		return successResponse(array);

	}
	
	
	/**
	 * 편집 중 임시저장
	 * @param domianName	도메인명
	 * @param vendorName		신규사업장명
	 * @return
	 * @exception
	 * 		Case01 저장 할 사업장 정보를 찾을수 없는 경우(NOT_FOUND_SAVE_TEMP)
	 */	
	@RequestMapping(value = "/tempSave", method = RequestMethod.POST)
	public ResponseData<ResSaveTemp> saveTemp(@RequestBody ReqSaveTemp req) {
		ResSaveTemp result = new ResSaveTemp();

		String dirPath = coreSvc.saveTemp(req.getVenderName(), req.getDomainName());

		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONObject server = parsingFrom.getServerInfo();
		String path = "http://" + server.get("serverIp") + ":" + server.get("port") + dirPath;

		result.setUrlPath(path);

		return successResponse(result);

	}

	/**
	 * 서비스 배포
	 * @param domianName	도메인명
	 * @param vendorName		신규사업장명
	 * @return
	 * @exception
	 * 		Case01 배포 할 사업장 정보를 찾을 수 없는 경우(NOT_FOUND_DEPLOY_VENDOR)
	 * 		Case02 해당 사업장의 디렉토리가 이미 존재하는 경우(DIRECTORY_ALREADY_EXISTS)
	 * 		Case03 복사할 원본 디렉토리를 찾을수 없는 경우(NOT_FOUND_DIRECTORY)
	 */	
	@RequestMapping(value = "/deployVendor", method = RequestMethod.POST)
	public ResponseData<ResDeployVendor> deployVendor(@RequestBody ReqDeployVendor req) {
		ResDeployVendor result = new ResDeployVendor();

		String dirPath = coreSvc.deployVendor(req.getVenderName(), req.getDomainName());
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONObject server = parsingFrom.getServerInfo();

		String path = "http://" + server.get("serverIp").toString() + ":" + server.get("port").toString() + dirPath;

		result.setUrlPath(path);

		return successResponse(result);

	}
	
	
	
	
	
	
}
