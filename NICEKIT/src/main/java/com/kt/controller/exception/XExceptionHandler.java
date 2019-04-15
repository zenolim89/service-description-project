package com.kt.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class XExceptionHandler {

	@ExceptionHandler(XRestException.class)
	@ResponseBody
	public ExceptionMessage NotFoundAuthException(XRestException ex) {
		
		return new ExceptionMessage(ex.code, ex.message);

	}
	
	
	
	
}
