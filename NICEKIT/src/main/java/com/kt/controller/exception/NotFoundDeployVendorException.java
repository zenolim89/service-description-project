package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

import com.kt.commonUtils.Constants;

public class NotFoundDeployVendorException extends XRestException{

	private static final long serialVersionUID = 1L;
	

    public NotFoundDeployVendorException() {
        super(Constants.NOT_FOUND_DEPLOY_VENDOR[0], Constants.NOT_FOUND_DEPLOY_VENDOR[1]);
    }

	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.NOT_FOUND;
	}
}
