package com.hundsun.jresplus.base.validator;

/**
 * 
 * @author Leo
 * 
 */
public interface ValidatorManager {

	public <T extends Validator> T getValidator(Class<T> clazz);
}
