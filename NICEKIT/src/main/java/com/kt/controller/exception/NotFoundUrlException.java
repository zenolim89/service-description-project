package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class NotFoundUrlException extends XRestException{
	
	private static final long serialVersionUID = 1L;
	

    public NotFoundUrlException() {
        super(Constants.NOT_FOUND_URL[0], Constants.NOT_FOUND_URL[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}

}
