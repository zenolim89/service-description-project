package com.kt.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.Void;
import java.util.ArrayList;
import java.util.List;

import com.kt.controller.exception.NotFoundUrlException;
import com.kt.controller.exception.ServiceUnavailableException;
import com.kt.controller.model.ResCreateWithTemplate;
import com.kt.controller.model.ResCreateWithVendor;
import com.kt.controller.model.ResGetDomain;
import com.kt.controller.model.ResGetSpecList;
import com.kt.controller.model.ResGetTemplate;
import com.kt.controller.model.ResGetVendor;
import com.kt.controller.model.ResGetVendorPage;
import com.kt.controller.model.ResponseData;

import com.kt.dataForms.DicParam;
import com.kt.dataForms.ExcelUploadForm;
import com.kt.dataForms.ResFileUpload;
import com.kt.dataManager.ExcelService;
import com.kt.dataManager.UtilFile;

@RestController
@RequestMapping("/new")
public class MainController extends BaseController{
	
	/**
	 * 관리자 계정 인증
	 * @param id 아이디
	 * @param pw 비밀번호
	 * @exception
	 * 		Case01 ID 불일치
	 * 		Case02 PW 불일치
	 */
	@RequestMapping(value="/auth", method=RequestMethod.POST)
	public ResponseData<Void> reqAuth(@RequestParam String id, 
															@RequestParam String pw){
		
		System.out.println("ID: " + id);
		System.out.println("PW: " + pw);
		
		//TODO SERA Controller 로그인 관련 프로세스
		
		
		return successResponse();
	}
	
	
	/**
	 * 등록된 도메인 목록 조회
	 * @return
	 * @exception
	 * 		Case01 도메인 목록이 없을 경우
	 */
	@RequestMapping(value="/getDomain", method=RequestMethod.GET)
	public ResponseData<ResGetDomain> getDomain(){
		ResGetDomain result = new ResGetDomain();
		
		//TODO SERA Controller get Domain 관련 프로세스
		
		return successResponse(result);
	}

	
	/**
	 * 등록된 3rd 연동규격 목록 조회
	 * @return
	 * @exception
	 * 		Case01 연동규격 목록이 없을 경우
	 */
	@RequestMapping(value="/getSpecList", method=RequestMethod.GET)
	public ResponseData<ResGetSpecList> getSepcList(){
		ResGetSpecList result = new ResGetSpecList();
		
		//TODO SERA Controller get SpecList 관련 프로세스
		
		return successResponse(result);
	}

	
	/**
	 * 등록된 템플릿 목록 조회
	 * @return
	 * @exception
	 * 		Case01 템플릿 목록이 없을 경우
	 */
	@RequestMapping(value="/getTemplate", method=RequestMethod.GET)
	public ResponseData<ResGetTemplate> getTemplate(){
		ResGetTemplate result = new ResGetTemplate();
		
		//TODO SERA Controller get Template 관련 프로세스
		
		return successResponse(result);
	}
	

	/**
	 * 신규 사업장 폴더 생성 및 기존 템플릿 복사
	 * @param domianName	도메인명
	 * @param vendor		신규사업장명
	 * @param urlPath		기존 템플릿 경로
	 * @return
	 * @exception
	 * 		Case01 해당 도메인을 찾을수 없는 경우
	 * 		Case02 해당 Vendor를 찾을 수 없는 경우
	 * 		Case03 urlPath를 찾을 수 없는 경우
	 */
	@RequestMapping(value="/CreateWithTemplate", method=RequestMethod.POST)
	public ResponseData<ResCreateWithTemplate> createWithTemplate(@RequestParam String domianName,
																										@RequestParam String vendor,
																										@RequestParam String urlPath){
		ResCreateWithTemplate result = new ResCreateWithTemplate();
		
		//TODO SERA Controller ResCreateWithTemplate 관련 프로세스
		
		return successResponse(result);
		
	}
	
	
	/**
	 * 등록된 사업장 목록 조회
	 * @param domianName
	 * @return
	 * @exception
	 * 		Case01 해당 도메인을 찾을수 없을 경우
	 * 		Case02 등록된 사업장 목록이 없을 경우
	 */
	@RequestMapping(value="/getVendor", method=RequestMethod.GET)
	public ResponseData<ResGetVendor> getVendor(@RequestParam String domianName){
		ResGetVendor result = new ResGetVendor();
		
		//TODO SERA Controller getVendor 관련 프로세스
		
		return successResponse(result);
		
	}
	
	
	/**
	 * 사업장 서비스 화면 미리보기
	 * @param domianName 도메인명
	 * @param vendor	사업장명
	 * @return
	 * @exception
	 * 		Case01 해당 도메인을 찾을수 없을 경우
	 *	 	Case02 해당 사업장을 찾을수 없을 경우
	 * 
	 */
	@RequestMapping(value="/getVendorPage", method=RequestMethod.GET)
	public ResponseData<ResGetVendorPage> getVendorPage(@RequestParam String domianName,
																					@RequestParam String vendor){
		ResGetVendorPage result = new ResGetVendorPage();
		
		//TODO SERA Controller getVendorPage 관련 프로세스
		
		return successResponse(result);
		
	}
	

	/**
	 * 신규 사업장 폴더 생성 및 기존 사업장 파일 복사
	 * @param domianName
	 * @param vendor
	 * @param urlPath
	 * @return
	 * @exception
	 * 		Case01 해당 도메인을 찾을수 없는 경우
	 * 		Case02 해당 Vendor를 찾을 수 없는 경우
	 * 		Case03 urlPath를 찾을 수 없는 경우
	 */
	@RequestMapping(value="/CreateWithVendor", method=RequestMethod.POST)
	public ResponseData<ResCreateWithVendor> createWithVendor(@RequestParam String domianName,
																										@RequestParam String vendor,
																										@RequestParam String urlPath){
		ResCreateWithVendor result = new ResCreateWithVendor();
		
		//TODO SERA Controller CreateWithVendor 관련 프로세스
		
		return successResponse(result);
		
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
			@RequestParam String domainName,
			@RequestParam String domainId,
			@RequestParam String specName,
			@RequestParam("uploadFile") MultipartFile uploadFile){
		
		
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
		
		for(ExcelUploadForm e : list) {
			ResFileUpload res = new ResFileUpload();
			res.setServiceName(e.getServiceName());
			res.setServiceCode(e.getServiceCode());
			res.setServiceDesc(e.getServiceDesc());
			res.setServiceType(e.getServiceType());
			res.setInvokeType(e.getInvokeType());
			res.setIntentName(e.getIntentInfo());
			res.setIsRegistered(e.isRegistered());
			
			/* dicNameList, wordList 매핑 */
			if(e.getDicList() != null) {
				StringBuilder sbDic = new StringBuilder();
				StringBuilder sbWord = new StringBuilder();
				
				List<DicParam> dicList = e.getDicList();
				for(DicParam item : dicList) {
					if(sbDic.length() > 0) sbDic.append("<br>");
					if(sbWord.length() > 0) sbWord.append("<br>");
					
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
}
