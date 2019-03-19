package com.kt.tool.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * 
 * The Class DataEmptyException.
 * @author nbware
 *
 */
public class DataEmptyException extends RuntimeException {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final long serialVersionUID = 639277493694472522L;
	
	private int code = 0;
	private String message = null;
	
	public DataEmptyException(HttpStatus status) {
		// String message = messageAccessor.getMessage("service.code."+String.valueOf(code));
		
		this.code = Integer.parseInt(null != status ? status.toString() : "204");
		this.message = "";
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
