package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class DbInsertFailedException extends XRestException{
	private static final long serialVersionUID = 1L;
	

    public DbInsertFailedException() {
        super(Constants.DB_INSERT_FAILED[0], Constants.DB_INSERT_FAILED[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}
}
