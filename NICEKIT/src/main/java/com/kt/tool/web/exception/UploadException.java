package com.kt.tool.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class UploadException extends RuntimeException {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = 5947935974742508170L;

	private int code = 0;
	private String message = null;
	
	public UploadException(HttpStatus status, String message) {
		this.code = Integer.parseInt(null != status ? status.toString() : "401");
		this.message = message;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
