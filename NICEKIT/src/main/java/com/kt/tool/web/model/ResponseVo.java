package com.kt.tool.web.model;

import java.io.Serializable;

public class ResponseVo implements Serializable {
	
	private static final long serialVersionUID = -5817778678609092010L;

	private int code;
	
	private String message;
	
	private Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
