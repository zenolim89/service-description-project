package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class SpecAlreadyExistsException extends XRestException{
	
	private static final long serialVersionUID = 1L;
	

    public SpecAlreadyExistsException() {
        super(Constants.SPEC_ALREADY_EXISTS[0], Constants.SPEC_ALREADY_EXISTS[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}

}
