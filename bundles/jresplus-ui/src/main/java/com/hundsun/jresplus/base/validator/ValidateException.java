package com.hundsun.jresplus.base.validator;

import org.springframework.validation.Errors;

/**
 * 
 * @author Leo
 * 
 */
public class ValidateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6592436236650540266L;

	private Errors errors;

	public ValidateException(Errors errors) {
		this.errors = errors;
	}

	public Errors getErrors() {
		return errors;
	}

}
