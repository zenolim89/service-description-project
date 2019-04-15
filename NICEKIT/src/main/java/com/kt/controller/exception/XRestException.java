package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

public abstract class XRestException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	protected final HttpStatus status;

	protected String code;

	protected String message;

	protected String description;

	public XRestException() {
		super();
		this.status = exceptionStatus();
	}

	public XRestException(HttpStatus status, Error error) {
		super();

		this.code = error.getCode();
		this.message = error.getMessage();
		this.description = error.getDescription();
		this.status = status;
	}

	public XRestException(String code, String message) {

		this.status = exceptionStatus();
		this.code = code;
		this.message = message;
	}

	public XRestException(String... args) {

		this.status = exceptionStatus();
		this.code = args[0];
		this.message = args[1];
	}

	public XRestException(String[] defineError, String outMessage) {

		this.status = exceptionStatus();
		this.code = defineError[0];
		this.message = outMessage;
	}

	public HttpStatus getStatus() {

		return status;
	}

	public String getCode() {

		return code;
	}

	@Override
	public String getMessage() {

		return message;
	}

	public abstract HttpStatus exceptionStatus();

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public void setCode(String code) {

		this.code = code;
	}

	public void setMessage(String message) {

		this.message = message;
	}
}
