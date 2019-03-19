package com.kt.tool.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * 
 * The Class FieldValidationWideException.
 * @author nbware
 *
 */
public class MandatoryFieldValidationException extends RuntimeException {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = -8156071736703703734L;

	/** The code. */
	private int code;

	/** The message. */
	private String message = null;

	/**
	 * Instantiates a new mandatory field validation exception.
	 *
	 * @param messageAccessor the message accessor
	 * @param bindingResult the binding result
	 */
	public MandatoryFieldValidationException(MessageSourceAccessor messageAccessor, BindingResult bindingResult) {

		this.code = 0;
		if(null == bindingResult) {
			this.message = "";
			return;
		}

		for (FieldError error : bindingResult.getFieldErrors()) {
			this.message = messageAccessor.getMessage(error);
			log.debug(messageAccessor.getMessage(error));
			break;
		}
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return message;
	}
}
