package com.hundsun.jresplus.ui.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * @author Leo
 * 
 */
public class ScriptValidator implements
		ConstraintValidator<ScriptValid, Object> {

	public void initialize(ScriptValid constraintAnnotation) {

	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return true;
	}

}
