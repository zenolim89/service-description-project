package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class NotFoundSpecException extends XRestException{
	
	private static final long serialVersionUID = 1L;
	

    public NotFoundSpecException() {
        super(Constants.NOT_FOUND_SPEC[0], Constants.NOT_FOUND_SPEC[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}

}
