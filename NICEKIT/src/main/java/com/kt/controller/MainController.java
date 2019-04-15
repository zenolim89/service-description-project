package com.kt.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.Void;
import java.util.ArrayList;
import java.util.List;

import com.kt.controller.exception.NotFoundUrlException;
import com.kt.controller.exception.ServiceUnavailableException;
import com.kt.controller.model.ResGetDomain;
import com.kt.controller.model.ResponseData;

@RestController
public class MainController extends BaseController{
	
	/**
	 * successResponse() 테스트
	 * @return
	 */
	@RequestMapping(value="/test01")
	public ResponseData<Void> test01(){
		return successResponse();
	}
	
	
	/**
	 * successResponse(T data) 테스트
	 * @return
	 */
	@RequestMapping(value="/test02")
	public ResponseData<ResGetDomain> test02(){
		
		ResGetDomain result = new ResGetDomain();
		
		List<String> domainList = new ArrayList<String>();
		domainList.add("resort");
		domainList.add("hotel");
		domainList.add("shop");

		result.setDomainList(domainList);
		
		return successResponse(result);
	}
	
	/**
	 * Exception 테스트 - Not Found URL
	 * @return
	 */
	@RequestMapping(value="/test03")
	public ResponseData<ResGetDomain> test03(){
		
		throw new NotFoundUrlException();
	}
	
	/**
	 * Exception 테스트 - Service Unavailable
	 * @return
	 */
	@RequestMapping(value="/test04")
	public ResponseData<ResGetDomain> test04(){
		
		throw new ServiceUnavailableException();
	}

	
	
	
	
}
