package com.kt.controller.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author user
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Error {
	
	private String code;

	private String message;

	private String description;

	private Object data;

	private String stackTrace;

	public Error(String[] errorResult, Exception e, boolean isDebug) {

		this.code = errorResult[0];
		this.message = errorResult[1];

		if(isDebug) {
			displayErrorStack(e);
		}
	}

	public Error(XRestException e, boolean isDebug) {

		this.code = e.getCode();
		this.message = e.getMessage();

		if(isDebug) {
			displayErrorStack(e);
		}
	}

	private void displayErrorStack(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
		this.stackTrace = errors.toString();
	}

	public Object getData() {

		return data;
	}

	public void setData(Object data) {

		this.data = data;
	}

	public String getCode() {

		return code;
	}

	public void setCode(String code) {

		this.code = code;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {

		this.message = message;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public String getStackTrace() {

		return stackTrace;
	}
}
