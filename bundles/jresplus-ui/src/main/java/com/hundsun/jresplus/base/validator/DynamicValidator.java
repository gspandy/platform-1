package com.hundsun.jresplus.base.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.hundsun.jresplus.util.ResourceLoadUtils;

/**
 * 
 * @author Leo
 * 
 */
public class DynamicValidator implements ConstraintValidator<Dynamic, Object> {

	private Validator validator;
	@Autowired
	private ValidatorManager validatorManager;

	public void initialize(Dynamic constraintAnnotation) {
		if (validatorManager == null) {
			validatorManager = ResourceLoadUtils.getObjectFactory().getBean(
					ValidatorManager.class);
		}
		validator = validatorManager
				.getValidator(constraintAnnotation.handle());
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return validator.isValid(value);
	}
}
