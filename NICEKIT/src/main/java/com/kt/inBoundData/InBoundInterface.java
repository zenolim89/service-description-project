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
import com.kt.commonUtils.Constants;
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

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("/view/index");
		return mv;
	}

	/** load index page */
	/** see also SpringDispatcher-servlet.xml */
	@RequestMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView("/view/login");
		return mv;
	}

	@RequestMapping("/editor/login")
	public ModelAndView editorLogin() {
		ModelAndView mv = new ModelAndView("/editor/login");
		return mv;
	}

	@RequestMapping("/editor/reqLogin")
	public ModelAndView editorReqLogin() {
		ModelAndView mv = new ModelAndView("/editor/domain");
		return mv;
	}

	@RequestMapping("/step1")
	public ModelAndView step1() {
		ModelAndView mv = new ModelAndView("/view/step1");
		return mv;
	}

	@RequestMapping("/step2")
	public ModelAndView step2() {
		ModelAndView mv = new ModelAndView("/view/step2");
		return mv;
	}

	/** create domain */
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

	/** request domain list */
	@RequestMapping(value = "/getDomain", method = RequestMethod.GET)
	public JSONObject getDomain() {
		SelectDataTo selectTo = new SelectDataTo();
		JSONObject res = new JSONObject();
		res = selectTo.selectDomainList();
		return res;
	}

	/** request intent name list */
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

	/** request spec list */
	@RequestMapping(value = "/getSpecList", method = RequestMethod.GET)
	public JSONObject getSpecList(@RequestParam String domainName) {
		JSONObject res = new JSONObject();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		res = serializerTo.resSpecList(domainName);
		return res;
	}

	/** request template list */
	@RequestMapping(value = "/getTemplate", method = RequestMethod.GET)
	public JSONObject getTemplateList(@RequestParam String domainName) {
		JSONObject res = new JSONObject();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		res = serializerTo.resTemplateList(domainName);
		return res;
	}

	/** request template preview */
	@RequestMapping(value = "/getTemplatePage", method = RequestMethod.GET)
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
		for (Row row : list)
			templateUrl = row.getString("dirpath");
		JSONObject server = parsingFrom.getServerInfo();
		String path = "http://" + server.get("serverIp") + ":" + server.get("port") + templateUrl;
		JSONObject res = serializerTo.resPreView(path);
		return res;
	}

	/** create vendor with template */
	@RequestMapping(value = "/CreateWithTemplate", method = RequestMethod.POST)
	public JSONObject CreateWithTemplate(InputStream body) {
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}
			res = parsingFrom.parsingCreateWithTemplate(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/** request vendor list */
	@RequestMapping(value = "/getVendor", method = RequestMethod.GET)
	public JSONObject getVendorList(@RequestParam String domainName) {
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		String reqDomain = domainName;
		JSONObject res = serializerTo.resVendorList(reqDomain);
		return res;
	}

	/** request vendor preview */
	@RequestMapping(value = "/getVendorPage", method = RequestMethod.GET)
	public JSONObject getVendorPage(@RequestParam String domainName, String vendorName) {
		SelectDataTo selectTo = new SelectDataTo();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String dirPath = null;
		List<Row> list = selectTo.selectUseTemplateofVendor(vendorName);
		if (list == null) {
			JSONObject res = serializerTo.resNotFoundTemplate("요청하신 사업자명을 통한 미리보기가 없습니다");
			return res;
		}
		for (Row row : list)
			dirPath = row.getString("dirpath");
		JSONObject server = parsingFrom.getServerInfo();
		String path = "http://" + server.get("serverIp") + ":" + server.get("port") + dirPath;
		JSONObject res = serializerTo.resPreView(path);
		return res;
	}

	/** create vendor with vendor */
	@RequestMapping(value = "/CreateWithVendor", method = RequestMethod.POST)
	public JSONObject CreateWithVendor(InputStream body) {
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}
			res = parsingFrom.parsingCreateWithVendor(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/** request file upload */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public @ResponseBody List<ResFileUpload> uploadController(@RequestParam("uploadFile") MultipartFile uploadFile,
			MultipartHttpServletRequest request, @RequestParam String domainName, @RequestParam String domainId,
			@RequestParam String specName) {
		System.out.println("RewardController reAddProCtrl uploadFile : " + uploadFile);
		// UtilFile 객체 생성
		UtilFile utilFile = new UtilFile();
		// 파일 업로드 결과값을 path로 받아온다(이미 fileUpload() 메소드에서 해당 경로에 업로드는 끝났음)
		String uploadPath = utilFile.fileUpload(request, uploadFile);
		System.out.println("RewardController reAddProCtrl uploadPath : " + uploadPath);
		/** 업로드 엑셀파일 파서 */
		ExcelService excelSvc = new ExcelService();
		List<ExcelUploadForm> list = excelSvc.excelUpload(uploadPath, domainName, domainId, specName);
		List<ResFileUpload> result = new ArrayList<ResFileUpload>();
		for (ExcelUploadForm e : list) {
			ResFileUpload res = new ResFileUpload();
			res.setServiceName(e.getServiceName());
			res.setServiceCode(e.getServiceCode());
			res.setServiceDesc(e.getServiceDesc());
			res.setServiceType(e.getServiceType());
			res.setInvokeType(e.getInvokeType());
			res.setIntentName(e.getIntentInfo());
			res.setIsRegistered(e.isRegistered());
			// dicNameList, wordList 매핑
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
			result.add(res);
		}
		return result;
	}

	/** request temp list */
	@RequestMapping(value = "/getTemp", method = RequestMethod.GET)
	public JSONObject getTempList(@RequestParam String domainName) {
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		String reqDomain = domainName;
		JSONObject res = serializerTo.resTempList(reqDomain);
		return res;
	}

	/** deploy vendor */
	@RequestMapping(value = "/tempSave", method = RequestMethod.POST)
	public JSONObject tempSave(InputStream body) {
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}
			res = parsingFrom.resTempSaveToVendor(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/** deploy vendor */
	@RequestMapping(value = "/deployVendor", method = RequestMethod.POST)
	public JSONObject deployVendor(InputStream body) {
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}
			res = parsingFrom.resTempMoveToVendor(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/** registration domain intent information */
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

	/** request dictionary info */
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

	/** set auth */
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
			res = parsingFrom.setAuth(response);
		} catch (Exception e) {
			response = e.getMessage().toString();
		}
		return res;
	}

	@RequestMapping(value = "/setSpec", method = RequestMethod.GET)
	public JSONObject setSpec(@RequestParam String domainName, @RequestParam String specName) {
		SelectDataTo selectTo = new SelectDataTo();
		InsertDataTo insertTo = new InsertDataTo();
		String ks = "commonks";
		JSONObject resObj = insertTo.insertSpecIndexTo(specName, domainName);
		return resObj;
	}

	@RequestMapping(value = "/reqService", method = RequestMethod.GET)
	public ModelAndView reqService(@RequestParam String intentName, @RequestParam String word,
			@RequestParam String name) {
		SelectDataTo selectTo = new SelectDataTo();
		JSONObject res = new JSONObject();
		ModelAndView mv = new ModelAndView("jsonView");
		String keySpace = "vendorsvcks";
		System.out.println("[DEBUG] 수신된 인텐트명: " + intentName + " 요청된 어휘: " + word + " 서비스 사업장 구분자:" + name);
		res = selectTo.selectMatchingService(intentName, word, name, keySpace);
		if (res.containsKey("serviceType")) {
			if ((res.get("serviceType").toString()).equals("RetriveATChangeView")) {
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
		String bf = null;
		String response = "";
		JSONObject res = new JSONObject();
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		try {
			while ((bf = in.readLine()) != null) {
				response += bf;
			}
			res = parsingFrom.setExcelForm(response,
					Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_SAMPLE,
					Constants.EXTERNAL_FOLDER_URLPATH_SAMPLE);
		} catch (Exception e) {
			res.put("code", "4000");
			res.put("message", e.getMessage());
			response = e.getMessage().toString();
		}
		return res;
	}

	@RequestMapping(value = "/getSpecInfo", method = RequestMethod.GET)
	public JSONObject getSpecInfo(@RequestParam String domainName, String specName) {
		JSONObject res = new JSONObject();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		res = serializerTo.resSpecInfo(domainName, specName);
		return res;

	}

	@RequestMapping(value = "/setTemplate", method = RequestMethod.GET)
	public JSONObject setTemplate(@RequestParam String templateName, String domainName) {
		JSONObject res = new JSONObject();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		parsingFrom.parsingTemplateInfo(templateName, domainName);
		return res;
	}

}
