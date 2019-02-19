package com.kt.inBoundData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataManager.JSONParsingFrom;
import com.kt.dataManager.JSONSerializerTo;

@RestController
@RequestMapping("")
public class InBoundInterface {
	// Logger instance
	private static final Logger logger = Logger.getLogger(InBoundInterface.class);

	// load index page
	// see also SpringDispatcher-servlet.xml
	@RequestMapping("/")
	public ModelAndView index() {

		ModelAndView mv = new ModelAndView("/index/index");

		return mv;
		// return new ModelAndView("index");
	}
	
	@RequestMapping("/getPage")
	public ModelAndView getPage() {
		
		ModelAndView mv = new ModelAndView("/template/main");
		
		return mv;
	}
	


	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 12. 오후 12:21:07]
	 * desc	: 요청된 서비스를 처리하기 위한 인터페이스, 현재는 Auth부분을 별도의 parm형태로 받아 id 값을 확인하여 해당 서비스 벤터를 검색
	 * Demo 이후 수정 필요
	 * @version	:
	 * @param	: 
	 * @return 	: String 
	 * @throws 	: 
	 * @see		: 
	
	 * @param intentName
	 * @param word
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/reqService", method = RequestMethod.GET)
	public ModelAndView reqService(@RequestParam String intentName, @RequestParam String word, @RequestParam String name) {

		SelectDataTo selectTo = new SelectDataTo();
		JSONObject res = new JSONObject();
		ModelAndView mv = new ModelAndView();
		
		String keySpace = "vendersvcks";
		
		System.out.println("[DEBUG] 수신된 인텐트명: " + intentName + " 요청된 어휘: " + word + " 서비스 사업장 구분자:" + name);
		
		res = selectTo.selectMatchingService(intentName, word, name, keySpace);
		
		if (res.get("serviceType") == "RetriveATChangeView") {
			
			mv.setViewName("/template/" + res.get("toUrl").toString());
			mv.addObject("resCode", res.get("resCode"));
			mv.addObject("resMsg", res.get("resMsg"));
			
		}
		
		return mv;

	}

	//request domain list
	@RequestMapping(value = "/getDomain", method = RequestMethod.GET)
	public JSONObject getDomain() {

		JSONParsingFrom parsingFrom = new JSONParsingFrom();

		JSONObject res = new JSONObject();

		res = parsingFrom.getDomainList();

		return res;
	}
	
	//request intentNameList
	@RequestMapping(value ="/getIntentList", method = RequestMethod.GET)
	public JSONObject getIntentNameList () {
		
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

	//registration domain intent information
	@RequestMapping(value = "/setDictionary", method = RequestMethod.POST)
	public JSONObject setDicList (InputStream body) {

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
	@RequestMapping(value = "/getDictionary", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	public String getDic(InputStream body) {
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		String bf = null;
		String response = "";
		String res = null;
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
	public JSONObject regiForVanerderService (InputStream body) {
		
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		
		String bf = null;
		String response ="";
		JSONObject res = new JSONObject();
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		
		try {
			while ( (bf = in.readLine()) != null) {
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
	public JSONObject createVenderService (InputStream body ) {
		
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		
		String bf = null;
		String response ="";
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
}
