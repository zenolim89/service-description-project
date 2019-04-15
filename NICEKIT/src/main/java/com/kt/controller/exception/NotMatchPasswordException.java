package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class NotMatchPasswordException extends XRestException{
	
	private static final long serialVersionUID = 1L;
	

    public NotMatchPasswordException() {
        super(Constants.NOT_MATCH_PASSWORD[0], Constants.NOT_MATCH_PASSWORD[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}

}
