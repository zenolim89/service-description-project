package com.kt.controller;

import java.lang.Void;

import com.kt.controller.model.ResponseData;


public abstract class BaseController {
	
	protected ResponseData<Void> successResponse(){
		ResponseData<Void> res = new ResponseData<>();
		
		res.setResCode(200);
		res.setResMsg("성공");
        
		return res;
	}
	
	
	protected <T> ResponseData<T> successResponse(T data){
		ResponseData<T> res = new ResponseData<>();
		
		res.setResCode(200);
		res.setResMsg("성공");
		res.setResData(data);
        
		return res;
	}
	

}
