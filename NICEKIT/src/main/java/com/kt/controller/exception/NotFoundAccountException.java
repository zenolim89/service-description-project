package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class NotFoundAccountException extends XRestException{

	private static final long serialVersionUID = 1L;
	

    public NotFoundAccountException() {
        super(Constants.NOT_FOUND_ACCOUNT[0], Constants.NOT_FOUND_ACCOUNT[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}
}
