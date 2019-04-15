package com.kt.controller.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends XRestException{

	private static final long serialVersionUID = 1L;
	
	public static final String[] SERVICE_UNAVAILABLE = new String[]{"500", "서비스를 처리할수 없음"};

    public ServiceUnavailableException() {
        super(SERVICE_UNAVAILABLE[0], SERVICE_UNAVAILABLE[1]);
    }
	
	@Override
	public HttpStatus exceptionStatus() {
		return HttpStatus.SERVICE_UNAVAILABLE;
	}

}
