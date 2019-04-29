package com.kt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.controller.model.ResReqService;
import com.kt.controller.model.ResponseData;
import com.kt.serviceManager.WebAppService;

@RestController
@RequestMapping("/webapp")
public class WebAppController extends BaseController{

	@Autowired
	private WebAppService webAppSvc;
	
	/**
	 * 3rd Party 연동 서비스
	 * @param intentName 인텐스명
	 * @param word 발화 어휘
	 * @param name 데이터 베이스 테이블 명
	 * @return
	 */
	@RequestMapping(value = "/reqService", method = RequestMethod.GET)
	public ResponseData<ResReqService> reqService(@RequestParam String intentName, @RequestParam String word,
			@RequestParam String name, @RequestParam String token) {
		
		ResReqService result = webAppSvc.executeService("", name, intentName, "", word, token);

		return successResponse(result);
	}
}
