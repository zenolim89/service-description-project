package com.kt.inBoundData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kt.dataCreator_temp.DicCreator;
import com.kt.dataManager.JSONParsingFrom;

@RestController
@RequestMapping("")
public class InBoundInterface {

	// Logger instance
	private static final Logger logger = Logger.getLogger(InBoundInterface.class);

	//load index page
	//see also SpringDispatcher-servlet.xml
	@RequestMapping("/")
	public ModelAndView index() {

		ModelAndView mv = new ModelAndView("index");

		//테스트 소스: POC 이후 삭제 필요
		DicCreator creator = new DicCreator();
		creator.createResortDic();
		creator.createShopDic();
		creator.creatHospitalDic();
		//여기까지 

		return mv;
		//		return new ModelAndView("index");
	}

	@RequestMapping(value = "/<add method name here>", method = RequestMethod.GET)
	public String getSomething(@RequestParam(value = "request") String request,	@RequestParam(value = "version", required = false, defaultValue = "1") int version) {

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
				response = "Response from Spring RESTful Webservice : "+ request;

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

	//getDicInfo
	@RequestMapping(value = "/dictionary", method = RequestMethod.POST)
	public String getDic(InputStream body){

		JSONParsingFrom parsingFrom = new JSONParsingFrom();

		String bf = null;
		String response = "";
		String res = null;

		BufferedReader in = new BufferedReader(new InputStreamReader(body));

		try {

			while ((bf = in.readLine()) != null) {
				response += bf;
			}

			
			res = parsingFrom.getDicDictionaryList(response);

		} catch (Exception e) {
			response = e.getMessage().toString();
		}

		return res;
	}


	//setAuth
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public JSONObject reqAuth(InputStream body){

		JSONParsingFrom parsingFrom = new JSONParsingFrom();

		String bf = null;
		String response = "";
		JSONObject res = null;

		BufferedReader in = new BufferedReader(new InputStreamReader(body));

		try {

			while ((bf = in.readLine()) != null) {
				response += bf;
			}

			//send auth information to parser

			res = parsingFrom.setAuth(response);

		} catch (Exception e) {
			response = e.getMessage().toString();
		}

		System.out.print("[DEBUG: 인증처리 후 결과]: " + res + "\n");

		return res;
	}

	//service registration
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public JSONObject reqRegiForService(InputStream body){

		JSONParsingFrom parsingFrom = new JSONParsingFrom();

		String bf = null;
		String response = "";
		JSONObject res = null;

		BufferedReader in = new BufferedReader(new InputStreamReader(body));

		try {

			while ((bf = in.readLine()) != null) {
				response += bf;
			}

			//send registration to parser

			res = parsingFrom.regiService(response);

		} catch (Exception e) {
			response = e.getMessage().toString();
			//에러 정의 필요..보통 JSON 파일에 parm이 없을 경우 발생함...
		}

		return res;
	}

	@RequestMapping(value = "/<add method name here>", method = RequestMethod.PUT)
	public String putSomething(@RequestBody String request,@RequestParam(value = "version", required = false, defaultValue = "1") int version) {

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
				response = "Response from Spring RESTful Webservice : "+ request;

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

	@RequestMapping(value = "/<add method name here>", method = RequestMethod.DELETE)
	public void deleteSomething(@RequestBody String request,@RequestParam(value = "version", required = false, defaultValue = "1") int version) {

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
