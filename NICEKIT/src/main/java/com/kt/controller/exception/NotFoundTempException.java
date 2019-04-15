package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class NotFoundTempException extends XRestException{

	private static final long serialVersionUID = 1L;
	

    public NotFoundTempException() {
        super(Constants.NOT_FOUND_TEMP[0], Constants.NOT_FOUND_TEMP[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}
}
