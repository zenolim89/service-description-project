package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class NotFoundDirectoryException extends XRestException{
	
	private static final long serialVersionUID = 1L;
	

    public NotFoundDirectoryException() {
        super(Constants.NOT_FOUND_DIRECTORY[0], Constants.NOT_FOUND_DIRECTORY[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}

}
