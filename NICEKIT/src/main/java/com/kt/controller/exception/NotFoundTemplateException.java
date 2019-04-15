package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class NotFoundTemplateException extends XRestException{
	
	private static final long serialVersionUID = 1L;
	

    public NotFoundTemplateException() {
        super(Constants.NOT_FOUND_TEMPLATE[0], Constants.NOT_FOUND_TEMPLATE[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}

}
