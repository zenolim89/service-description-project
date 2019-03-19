package com.kt.tool.web.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.tool.web.util.Utils;

@ControllerAdvice(value="com.kt.tool.web")
@RestController
@RequestMapping(headers="Accept=application/json")
public class ControllerAdviceExceptionHandler extends RuntimeException {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = 8588136693111617570L;
	
	@Autowired
	Utils utils;
	
	/**
	 * Mandatory field validation exception.
	 *
	 * @param e the e
	 * @return the result data VO
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MandatoryFieldValidationException.class)
	public Object mandatoryFieldValidationException(MandatoryFieldValidationException exception) {
		exception.getStackTrace();
		log.error("exception.log : {}", exception);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 400);
		map.put("message", exception.getMessage());
		
		return map;
		
	}
	
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = UploadException.class)
	public Object uploadException(UploadException exception) {
		exception.getStackTrace();
		log.error("exception.log : {}", exception);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", exception.getCode());
		map.put("message", exception.getMessage());
		
		return map;
	}
	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ExceptionHandler(value = DataEmptyException.class)
	public Object dataEmptyException(DataEmptyException exception) {
		exception.getStackTrace();
		log.error("exception.log : " + exception);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", exception.getCode());
		map.put("message", utils.getResultMessage(String.valueOf(exception.getCode())));
		
		return map;
		
	}
	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public Object methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		exception.getStackTrace();
		log.error("exception.log : {}", exception);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 500);
		map.put("message", exception.toString());
		
		return map;
	}
	
	/**
	 * Default exception.
	 *
	 * @param exception the exception
	 * @return the object
	 */
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public Object defaultException(Exception exception) {
		exception.getStackTrace();
		log.error("exception.log : {}", exception);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 500);
		map.put("message", exception.toString());
		
		return map;
	}
	
}
