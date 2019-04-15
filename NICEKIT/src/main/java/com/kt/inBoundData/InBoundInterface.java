package com.kt.inBoundData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.datastax.driver.core.Row;
import com.kt.dataDao.InsertDataTo;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.DicParam;
import com.kt.dataForms.ExcelUploadForm;
import com.kt.dataForms.ResFileUpload;
import com.kt.dataManager.ExcelService;
import com.kt.dataManager.JSONParsingFrom;
import com.kt.dataManager.JSONSerializerTo;
import com.kt.dataManager.UtilFile;

@RestController
@RequestMapping("")
public class InBoundInterface {
	// Logger instance
	private static final Logger logger = Logger.getLogger(InBoundInterface.class);

	// load index page
	// see also SpringDispatcher-servlet.xml
	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("/view/index");

		return mv;
		// return new ModelAndView("index");
	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 15. 오후 7:14:13] desc : 등록기에서 도메인 추가 시 사용
	 * @version :
	 * @param :
	 * @return : JSONObject
	 * @throws :
	 * @see :
	 */
	@RequestMapping(value = "/setDomain", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public JSONObject setDomain(@RequestParam String domainName) {

		InsertDataTo insertTo = new InsertDataTo();
		JSONObject resObj = new JSONObject();

		Boolean res = insertTo.insertDomainList(domainName);

		if (res == true) {

			resObj.put("resCode", "201");
			resObj.put("resMsg", "성공");

		} else if (res == false) {
			resObj.put("resCode", "409");
			resObj.put("resMsg", "동일한 도메인 이름이 존재합니다");
		} else {
			resObj.put("resCode", "400");
			resObj.put("resMsg", "잘못된 접근 입니다.");
		}

		return resObj;

	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 15. 오후 7:19:05] desc : 등록기에서 사용하는 규격 생성요청 응대
	 *         API; 먼저 도메인 이름으로 해당 도메인 이름이 도메인 리스트 테이블에 존재하는지 여부 확인 후 신규 규격을 위한 테이블을
	 *         생성
	 * @version :
	 * @param :
	 * @return : JSONObject
	 * @throws :
	 * @see :
	 */
	@RequestMapping(value = "/setSpec", method = RequestMethod.GET)
	public JSONObject setSpec(@RequestParam String domainName, @RequestParam String specName) {

		SelectDataTo selectTo = new SelectDataTo();
		InsertDataTo insertTo = new InsertDataTo();

		

		String ks = "commonks";

		JSONObject resObj = insertTo.insertSpecIndexTo(specName, domainName);

		return resObj;

	}

	// @RequestMapping(value = "/downloadSample", method = RequestMethod.POST)
	// public Workbook downloadSample (InputStream body) {
	//
	// JSONParsingFrom parsingFrom = new JSONParsingFrom();
	// String bf = null;
	// String response = "";
	// Workbook res = new XSSFWorkbook();
	//
	// BufferedReader in = new BufferedReader(new InputStreamReader(body));
	// try {
	// while ((bf = in.readLine()) != null) {
	// response += bf;
	// }
	//
	//// res = parsingFrom.setDomainDictionary(response);
	//
	// } catch (Exception e) {
	//
	// res.put("resCode", "4000");
	// res.put("resMsg", e.getMessage());
	//
	// response = e.getMessage().toString();
	// System.out.println(response);
	// }
	//
	// return res;
	//
	// }

	/**
	 * @author : "Minwoo Ryu" [2019. 2. 12. 오후 12:21:07] desc : 요청된 서비스를 처리하기 위한
	 *         인터페이스, 현재는 Auth부분을 별도의 parm형태로 받아 id 값을 확인하여 해당 서비스 벤터를 검색 Demo 이후 수정
	 *         필요
	 * @version :
	 * @param :
	 * @return : String
	 * @throws :
	 * @see :
	 * 
	 * @param intentName
	 * @param word
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/reqService", method = RequestMethod.GET)
	public ModelAndView reqService(@RequestParam String intentName, @RequestParam String word,
			@RequestParam String name) {

		SelectDataTo selectTo = new SelectDataTo();
		JSONObject res = new JSONObject();
		ModelAndView mv = new ModelAndView("jsonView");

		String keySpace = "vendersvcks";

		System.out.println("[DEBUG] 수신된 인텐트명: " + intentName + " 요청된 어휘: " + word + " 서비스 사업장 구분자:" + name);

		res = selectTo.selectMatchingService(intentName, word, name, keySpace);
		if (res.containsKey("serviceType")) {
			if ((res.get("serviceType").toString()).equals("RetriveATChangeView")) {

				// mv.setViewName("/template" + res.get("toUrl").toString());
				// mv.addObject("resCode", res.get("resCode"));
				// mv.addObject("resMsg", res.get("resMsg"));

				Map<String, String> map = new HashMap<String, String>();
				map.put("resCode", res.get("resCode").toString());
				map.put("resMsg", res.get("resMsg").toString());
				map.put("resUrl", res.get("toUrl").toString());
				mv.addObject("obj", map);
			}
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("resCode", res.get("resCode").toString());
			map.put("resMsg", res.get("resMsg").toString());
			map.put("resUrl", "none");
			mv.addObject("obj", map);
		}

		return mv;

	}

	@RequestMapping(value = "/svcCode", method = RequestMethod.POST)
	public JSONObject getServiceCode(InputStream body) {

		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();

		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}

			res = parsingFrom.getServiceCodeTo(response);

		} catch (Exception e) {

			res.put("resCode", "500");
			res.put("resMsg", e.getMessage());

			response = e.getMessage().toString();
			System.out.println(response);
		}

		return res;
	}

	// request domain list
	@RequestMapping(value = "/getDomain", method = RequestMethod.GET)
	public JSONObject getDomain() {

		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		SelectDataTo selectTo = new SelectDataTo();

		JSONObject res = new JSONObject();

		res = selectTo.selectDomainList();

		return res;
	}

	// request intentNameList
	@RequestMapping(value = "/getIntentList", method = RequestMethod.GET)
	public JSONObject getIntentNameList() {

		JSONSerializerTo serializerTo = new JSONSerializerTo();
		JSONObject res = new JSONObject();
		try {

			res = serializerTo.resDomanIntentNameList();

		} catch (ParseException e) {

			res.put("resCode", "4000");
			res.put("resMsg", e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	// registration domain intent information
	@RequestMapping(value = "/setDictionary", method = RequestMethod.POST)
	public JSONObject setDicList(InputStream body) {

		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();

		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}

			res = parsingFrom.setDomainDictionary(response);

		} catch (Exception e) {

			res.put("resCode", "4000");
			res.put("resMsg", e.getMessage());

			response = e.getMessage().toString();
			System.out.println(response);
		}

		return res;

	}

	// getDicInfo
	@RequestMapping(value = "/getDictionary", method = RequestMethod.POST)
	public JSONObject getDic(InputStream body) {
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}
			res = parsingFrom.getDictionaryList(response);
		} catch (Exception e) {
			response = e.getMessage().toString();
		}
		return res;
	}

	// setAuth
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public JSONObject reqAuth(InputStream body) {
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String bf = null;
		String response = "";
		JSONObject res = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}
			// send auth information to parser
			res = parsingFrom.setAuth(response);
		} catch (Exception e) {
			response = e.getMessage().toString();
		}
		System.out.print("[DEBUG: 인증처리 후 결과]: " + res + "\n");
		return res;
	}

	// service registration for domain
	@RequestMapping(value = "/domainRegistration", method = RequestMethod.POST)
	public JSONObject regiForDomainService(InputStream body) {

		JSONParsingFrom parsingFrom = new JSONParsingFrom();

		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}
			// send registration to parser
			res = parsingFrom.setDomainService(response);
		} catch (Exception e) {

			res.put("code", "4000");
			res.put("message", e.getMessage());

			response = e.getMessage().toString();

		}
		return res;
	}

	// service registration for vender
	@RequestMapping(value = "/venderRegistration", method = RequestMethod.POST)
	public JSONObject regiForVanerderService(InputStream body) {

		JSONParsingFrom parsingFrom = new JSONParsingFrom();

		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();
		BufferedReader in = new BufferedReader(new InputStreamReader(body));

		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}

			res = parsingFrom.setVenderService(response);
		} catch (Exception e) {

			res.put("code", "4000");
			res.put("message", e.getMessage());

			response = e.getMessage().toString();

		}

		return res;
	}

	// service create for vender
	@RequestMapping(value = "/venderServiceCreation", method = RequestMethod.POST)
	public JSONObject createVenderService(InputStream body) {

		JSONParsingFrom parsingFrom = new JSONParsingFrom();

		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();

		BufferedReader in = new BufferedReader(new InputStreamReader(body));

		try {
			while ((bf = in.readLine()) != null) {
				response += bf;

			}

			// send response to a specific method in parsingfrom
		} catch (Exception e) {

			res.put("code", "4000");
			res.put("resMsg", e.getMessage());
		}

		return res;

	}

	@RequestMapping(value = "/<add method name here>", method = RequestMethod.PUT)
	public String putSomething(@RequestBody String request,
			@RequestParam(value = "version", required = false, defaultValue = "1") int version) {
		if (logger.isDebugEnabled()) {
			logger.debug("Start putSomething");
			logger.debug("data: '" + request + "'");
		}
		String response = null;
		try {
			switch (version) {
			case 1:
				if (logger.isDebugEnabled())
					logger.debug("in version 1");
				// TODO: add your business logic here
				response = "Response from Spring RESTful Webservice : " + request;
				break;
			default:
				throw new Exception("Unsupported version: " + version);
			}
		} catch (Exception e) {
			response = e.getMessage().toString();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("result: '" + response + "'");
			logger.debug("End putSomething");
		}
		return response;
	}

	@RequestMapping(value = "/<add method name here>", method = RequestMethod.GET)
	public String getSomething(@RequestParam(value = "request") String request,
			@RequestParam(value = "version", required = false, defaultValue = "1") int version) {

		if (logger.isDebugEnabled()) {
			logger.debug("Start getSomething");
			logger.debug("data: '" + request + "'");
		}

		String response = null;

		try {
			switch (version) {
			case 1:
				if (logger.isDebugEnabled())
					logger.debug("in version 1");
				// TODO: add your business logic here
				response = "Response from Spring RESTful Webservice : " + request;

				break;
			default:
				throw new Exception("Unsupported version: " + version);
			}
		} catch (Exception e) {
			response = e.getMessage().toString();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("result: '" + response + "'");
			logger.debug("End getSomething");
		}
		return response;
	}

	@RequestMapping(value = "/<add method name here>", method = RequestMethod.DELETE)
	public void deleteSomething(@RequestBody String request,
			@RequestParam(value = "version", required = false, defaultValue = "1") int version) {
		if (logger.isDebugEnabled()) {
			logger.debug("Start putSomething");
			logger.debug("data: '" + request + "'");
		}
		String response = null;
		try {
			switch (version) {
			case 1:
				if (logger.isDebugEnabled())
					logger.debug("in version 1");
				// TODO: add your business logic here
				break;
			default:
				throw new Exception("Unsupported version: " + version);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("result: '" + response + "'");
			logger.debug("End putSomething");
		}
	}

	@RequestMapping(value = "/xlsxDown", method = RequestMethod.POST)
	public JSONObject xlsxDown(InputStream body, HttpServletRequest request) {
		
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String filePath = "/resources/download";
		String realPath = request.getSession().getServletContext().getRealPath(filePath);
		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}
			res = parsingFrom.setExcelForm(response, realPath, filePath);
		} catch (Exception e) {
			res.put("code", "4000");
			res.put("message", e.getMessage());
			response = e.getMessage().toString();
		}
		return res;
	}

	// request vendor list
	/**
	 * @author : "Minwoo Ryu" [2019. 3. 18. 오후 3:23:15] desc : 생성기로부터 사업장 리스트 요청 시
	 *         해당 동작을 수행하기 위한 API 향후 NICE-KIT 플랫폼에서 개발지원과 운영을 분리할 경우 해당 API는 운영 부분으로
	 *         이관
	 * @version :
	 * @param :
	 * @return : JSONObject
	 * @throws :
	 * @see : JSONSerializerTo.resVenderList()
	 * 
	 * @param domainName
	 * @return
	 */
	@RequestMapping(value = "/getVendor", method = RequestMethod.GET)
	public JSONObject getVenderList(@RequestParam String domainName) {
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		String reqDomain = domainName;
		JSONObject res = serializerTo.resVenderList(reqDomain);
		return res;
	}

	// request spec list
	@RequestMapping(value = "/getSpecLst", method = RequestMethod.GET)
	public JSONObject getSpecLst(@RequestParam String domainName) {

		JSONObject res = new JSONObject();
		JSONSerializerTo serializerTo = new JSONSerializerTo();

		res = serializerTo.resSpecList(domainName);

		return res;
	}
	
	@RequestMapping(value = "/getTemplate", method = RequestMethod.GET)
	public JSONObject getTemplateList(@RequestParam String domainName) {
		
		JSONObject res = new JSONObject();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		
		res = serializerTo.resTemplateList(domainName);
		
		return res;
		
	}
	
	@RequestMapping(value ="/getSpecInfo", method = RequestMethod.GET)
	public JSONObject getSpecInfo(@RequestParam String domainName, String specName) {
		
		JSONObject res = new JSONObject();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		
		res = serializerTo.resSpecInfo(domainName, specName);
		
		return res;
		
	}
	

//	// request spec info
//	@RequestMapping(value = "/getSpecInfo", method = RequestMethod.GET)
//	public JSONObject getSpecInfo(@RequestParam("specName") String specName) throws JSONException {
//
//		JSONObject res = new JSONObject();
//
//		JSONArray _dicListArr = new JSONArray();
//		JSONObject _dicListObj = new JSONObject();
//		_dicListObj.put("dicName", "실시간웹캠명");
//		String[] _word = { "A슬로프", "B슬로프", "H슬로프", "스키장전경" };
//		_dicListObj.put("wordList", _word);
//		_dicListArr.add(_dicListObj);
//
//		JSONArray _intentInfoArr = new JSONArray();
//		JSONObject _intentInfotObj = new JSONObject();
//		_intentInfotObj.put("id", "HotelWebCam");
//		_intentInfotObj.put("dicList", _dicListArr);
//		_intentInfoArr.add(_intentInfotObj);
//
//		JSONObject _svcListObj = new JSONObject();
//		_svcListObj.put("serviceName", "웹캠");
//		_svcListObj.put("serviceType", "voice");
//		_svcListObj.put("serviceDesc", "슬로프에 설치된 실시간웹캠 영상보기");
//		_svcListObj.put("serviceCode", "REWCAM0143");
//		_svcListObj.put("intentInfo", _intentInfoArr);
//
//		JSONArray _dicListArr2 = new JSONArray();
//		JSONObject _dicListObj2 = new JSONObject();
//		_dicListObj2.put("dicName", "식음업장명");
//		String[] _word2 = { "사우스가림", "오크카페", "셀프바베큐", "BHC치킨" };
//		_dicListObj2.put("wordList", _word2);
//
//		JSONObject _dicListObj22 = new JSONObject();
//		_dicListObj22.put("dicName", "레저스포츠명");
//		String[] _word22 = { "수영장", "사우나", "피트니스" };
//		_dicListObj22.put("wordList", _word22);
//
//		_dicListArr2.add(_dicListObj2);
//		_dicListArr2.add(_dicListObj22);
//
//		JSONArray _intentInfoArr2 = new JSONArray();
//		JSONObject _intentInfotObj2 = new JSONObject();
//		_intentInfotObj2.put("id", "HotelViewPage");
//		_intentInfotObj2.put("dicList", _dicListArr2);
//		_intentInfoArr2.add(_intentInfotObj2);
//
//		JSONObject _svcListObj2 = new JSONObject();
//		_svcListObj2.put("serviceName", "부대시설");
//		_svcListObj2.put("serviceType", "voice");
//		_svcListObj2.put("serviceDesc", "사업장 내 부대시설 정보 조회");
//		_svcListObj2.put("serviceCode", "REVIEW0145");
//		_svcListObj2.put("intentInfo", _intentInfoArr2);
//
//		JSONArray _svcListArr = new JSONArray();
//		_svcListArr.add(_svcListObj);
//		_svcListArr.add(_svcListObj2);
//
//		JSONObject _specDescObj = new JSONObject();
//		_specDescObj.put("svcList", _svcListArr);
//		JSONObject _resDataObj = new JSONObject();
//		_resDataObj.put("specDesc", _specDescObj);
//
//		res.put("resCode", "200");
//		res.put("resMsg", "성공");
//		res.put("resData", _resDataObj);
//		return res;
//	}

	// load index page
	// see also SpringDispatcher-servlet.xml
	@RequestMapping("/login")
	public ModelAndView login() {

		ModelAndView mv = new ModelAndView("/view/login");

		return mv;
		// return new ModelAndView("index");
	}
	
	@RequestMapping("/editor/login")
	public ModelAndView editorLogin() {

		ModelAndView mv = new ModelAndView("/editor/login");

		return mv;
		// return new ModelAndView("index");
	}
	
	
	@RequestMapping("/editor/reqLogin")
	public ModelAndView editorReqLogin() {

		ModelAndView mv = new ModelAndView("/editor/domain");

		return mv;
		// return new ModelAndView("index");
	}

	@RequestMapping("/step1")
	public ModelAndView step1() {

		ModelAndView mv = new ModelAndView("/view/step1");

		return mv;
		// return new ModelAndView("index");
	}

	@RequestMapping("/step2")
	public ModelAndView step2() {

		ModelAndView mv = new ModelAndView("/view/step2");

		return mv;
		// return new ModelAndView("index");
	}

	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public @ResponseBody List<ResFileUpload> uploadController(@RequestParam("uploadFile") MultipartFile uploadFile,
			MultipartHttpServletRequest request,
			@RequestParam String domainName,
			@RequestParam String domainId,
			@RequestParam String specName) {

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

		List<ResFileUpload> result = new ArrayList<ResFileUpload>();
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
			
			result.add(res);
		}
		return result;
	}

	
	@RequestMapping(value = "/getVendorPage", method = RequestMethod.GET)
	public JSONObject getVenderPage(@RequestParam String domainName, String vendorName) {
		
		SelectDataTo selectTo = new SelectDataTo();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		
		String templateUrl = null;
		
		List<Row> list = selectTo.selectUseTemplateofVendor(vendorName);
		
		if (list == null) {
			
			JSONObject res = serializerTo.resNotFoundTemplate("요청하신 사업자명을 통한 미리보기가 없습니다");
			return res;
			
		}
		
		for (Row row : list) {
			
			templateUrl = row.getString("templateuipath");
		}
		
		JSONObject server = parsingFrom.getServerInfo();
		
		String path = "http://" + server.get("serverIp") + ":" + server.get("port") + templateUrl;
		
		JSONObject res = serializerTo.resPreView(path);
						
		return res;
		
	}
	
	
	@RequestMapping(value ="/getTemplatePage", method = RequestMethod.GET)
	public JSONObject getTemplatePage(@RequestParam String domainName, String templateName) {
		
		SelectDataTo selectTo = new SelectDataTo();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		
		String templateUrl = null;
		
		List<Row> list = selectTo.selectTemplatePreView(templateName);
		
		if (list == null) {
			
			JSONObject res = serializerTo.resNotFoundTemplate("요청하신 템플릿 미리보기가 없습니다");
			return res;
			
		}
		
		for (Row row : list) {
			
			templateUrl = row.getString("dirpath");
						
		}
		
		JSONObject server = parsingFrom.getServerInfo();
		
		String path = "http://" + server.get("serverIp") + ":" + server.get("port") + templateUrl;
						
		JSONObject res = serializerTo.resPreView(path);
		
		return res;
		
	}
	
	
	
	
	// request vendor template url
//	@RequestMapping(value = "/getVendorPage", method = RequestMethod.GET)
//	public JSONObject getVendorPage(@RequestParam("vendorName") String vendorName, HttpServletRequest _request)
//			throws IOException {
//
//		// String _urlPath = "http://222.107.124.9:8080/NICEKIT/resources/" +
//		// vendorName;
//		String _urlPath = "http://222.107.124.9:8080/NICEKIT/resources/";
//
//		JSONObject res = new JSONObject();
//		JSONObject vendorObj = new JSONObject();
//
//		// 디렉토리 순회용
//		// JSONArray vendorObjArray = new JSONArray();
//
//		ServletContext context = _request.getSession().getServletContext();
//
//		String path = context.getRealPath("/");
//
//		// 디렉토리 순회
//		/*
//		 * String dirPath = path + "/resources/template/" + vendorName;
//		 * 
//		 * if( new File(dirPath).exists() ){ File[] fileList = new
//		 * File(dirPath).listFiles();
//		 * 
//		 * for( int i = 0 ; i < fileList.length ; i++){
//		 * 
//		 * File file = fileList[i];
//		 * 
//		 * vendorObjArray.add(_urlPath + file.getName()); }
//		 * 
//		 * res.put("resData", vendorObjArray); }
//		 */
//
//		// 특정 파일
//		File file = new File(path + File.separator + "resources" + File.separator + "template" + File.separator
//				+ vendorName + ".html");
//
//		DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
//		// Resource resource = defaultResourceLoader.getResource(name);
//
//		if (file.exists()) {
//
//			vendorObj.put("urlPath", _urlPath + vendorName + ".html");
//			res.put("resData", vendorObj);
//		}
//
//		res.put("resCode", "200");
//		res.put("resMsg", "성공");
//		// vendorObj.put("urlPath", _urlPath);
//		return res;
//	}

	
	
	@RequestMapping(value = "/VendorGen", method = RequestMethod.POST)
	public JSONObject venderGen (InputStream body) {
		
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		
		String bf = null;
		String response ="";
		
		JSONObject res = new JSONObject();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		
		try {
			while ((bf = in.readLine()) != null ) {
				
				response += bf;
				
			}
			
			
			res = parsingFrom.parsingCreateVenderTemplate(response);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return res;
		
		
		
	}
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 3. 19. 오후 2:33:25]
	 * desc	: 향후 템플릿 업로드 기능에 따라 POST로 변환 처리 필요
	 * @version	:
	 * @param	: 
	 * @return 	: JSONObject 
	 * @throws 	: 
	 * @see		: 
	
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/setTemplate", method = RequestMethod.GET)
	public JSONObject setTemplate (@RequestParam String templateName, String domainName) {
		
		JSONObject res = new JSONObject();

		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		
		parsingFrom.parsingTemplateInfo(templateName, domainName);
		
		return res;
		
	}
	
	
	
//	// request vendor generate
//	@RequestMapping(value = "/VendorGen", method = RequestMethod.POST)
//	public JSONObject VendorGen(InputStream body, HttpServletRequest _request,
//			@RequestParam HashMap<String, Object> _paramMap) {
//
//		JSONObject res = new JSONObject();
//		JSONObject vendorObj = new JSONObject();
//
//		ServletContext context = _request.getSession().getServletContext();
//		String path = context.getRealPath("/");
//
//		String dirName = _paramMap.get("vendorName").toString();
//		String standardDirName = _paramMap.get("urlPath").toString();
//
//		// 향후 return되는 경로
//		String _urlPath = "http://222.107.124.9:8080/NICEKIT/resources/venders";
//
//		// 기존 디렉토리 File 객체
//		File orgFile = new File(path + File.separator + "resources" + File.separator + "template" +File.separator + standardDirName);
//		
//		System.out.println(path + File.separator + "resources" + File.separator + "template" +File.separator + standardDirName);
//		
////		File orgFile = new File(standardDirName); // parsing필요 --> resources 부터 사용
//		// 신규 생성될 디렉토리 File 객체
//		File newDirFile = new File(path + File.separator + "resources" + File.separator + dirName);
//
//		System.out.println("#############################################");
//		System.out.println("orgFile : " + orgFile.getAbsolutePath());
//		System.out.println("#############################################");
//		System.out.println("newDirFile : " + newDirFile.getAbsolutePath());
//		System.out.println("#############################################");
//
//		if (!orgFile.exists()) {
//
//			res.put("resData", "원본 폴더가 존재하지 않습니다.");
//
//			// 생성하고자하는 directory가 있는 경우 (vendername 일치한 폴더 이미 존재)
//		} else if (newDirFile.exists()) {
//
//			res.put("resData", "기존에 폴더가 있습니다.");
//		}
//		// 기존 폴더 없을경우 신규 생성 후 파일 복사 진행.
//		else {
//
//			newDirFile.mkdir();
//			this.copyFile(orgFile, newDirFile); // 복사
//			res.put("resData", _urlPath + dirName);
//		}
//
//		res.put("resCode", "200");
//		res.put("resMsg", "성공");
//		// vendorObj.put("", "");
//
//		return res;
//	}

//	public void copyFile(File sourceF, File targetF) {
//
//		File[] ff = sourceF.listFiles();
//		for (File file : ff) {
//			File temp = new File(targetF.getAbsolutePath() + File.separator + file.getName());
//			if (file.isDirectory()) {
//				temp.mkdir();
//				this.copyFile(file, temp);
//			} else {
//				FileInputStream fis = null;
//				FileOutputStream fos = null;
//				try {
//					fis = new FileInputStream(file);
//					fos = new FileOutputStream(temp);
//					byte[] b = new byte[4096];
//					int cnt = 0;
//					while ((cnt = fis.read(b)) != -1) {
//						fos.write(b, 0, cnt);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					try {
//						fis.close();
//						fos.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//			}
//		}
//	}

}
