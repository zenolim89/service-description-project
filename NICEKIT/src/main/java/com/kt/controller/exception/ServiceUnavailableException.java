package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class ServiceUnavailableException extends XRestException{

	private static final long serialVersionUID = 1L;
	

    public ServiceUnavailableException() {
        super(Constants.SERVICE_UNAVAILABLE[0], Constants.SERVICE_UNAVAILABLE[1]);
    }
	
	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.SERVICE_UNAVAILABLE;
	}

}
