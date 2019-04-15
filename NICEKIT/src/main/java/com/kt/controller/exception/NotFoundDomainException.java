package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class NotFoundDomainException extends XRestException{
	
	private static final long serialVersionUID = 1L;
	

    public NotFoundDomainException() {
        super(Constants.NOT_FOUND_DOMAIN[0], Constants.NOT_FOUND_DOMAIN[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}

}
