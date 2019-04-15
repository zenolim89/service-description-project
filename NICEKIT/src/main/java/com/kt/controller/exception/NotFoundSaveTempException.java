package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class NotFoundSaveTempException extends XRestException{

	private static final long serialVersionUID = 1L;
	

    public NotFoundSaveTempException() {
        super(Constants.NOT_FOUND_SAVE_TEMP[0], Constants.NOT_FOUND_SAVE_TEMP[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}
}
