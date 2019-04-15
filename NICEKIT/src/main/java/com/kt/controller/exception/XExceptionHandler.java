package com.kt.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class XExceptionHandler {

	@ExceptionHandler(NotFoundUrlException.class)
	@ResponseBody
	public ExceptionMessage NotFoundUrlException(NotFoundUrlException ex) {
		
		return new ExceptionMessage(ex.NOT_FOUND_URL[0], ex.NOT_FOUND_URL[1]);

	}
	
	
	@ExceptionHandler(ServiceUnavailableException.class)
	@ResponseBody
	public ExceptionMessage ServiceUnavailableException(ServiceUnavailableException ex) {
		
		return new ExceptionMessage(ex.SERVICE_UNAVAILABLE[0], ex.SERVICE_UNAVAILABLE[1]);

	}
	
	
	
	
}
