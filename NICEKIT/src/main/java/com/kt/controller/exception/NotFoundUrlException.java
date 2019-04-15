package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

public class NotFoundUrlException extends XRestException{
	
	private static final long serialVersionUID = 1L;
	
	public static final String[] NOT_FOUND_URL = new String[]{"404", "유효하지 않은 URL"};

    public NotFoundUrlException() {
        super(NOT_FOUND_URL[0], NOT_FOUND_URL[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}

}
