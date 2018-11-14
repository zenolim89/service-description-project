package com.kt.inBoundData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
 
		return new ModelAndView("index");
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

//		if (logger.isDebugEnabled()) {
//			logger.debug("result: '" + response + "'");
//			logger.debug("End postSomething");
//		}
		
		System.out.print("[RESPONSE]: " + res);
		
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
			}

//			if (logger.isDebugEnabled()) {
//				logger.debug("result: '" + response + "'");
//				logger.debug("End postSomething");
//			}
			
//			System.out.print("[RESPONSE]: " + res);
			
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
