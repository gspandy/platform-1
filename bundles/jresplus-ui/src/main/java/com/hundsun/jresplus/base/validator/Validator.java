package com.hundsun.jresplus.base.validator;

/**
 * 校验接口
 * 
 * @author Leo
 * 
 */
public interface Validator {

	/**
	 * 是否通过校验
	 * 
	 * @param value
	 *            需要校验的对象
	 * @return
	 */
	public boolean isValid(Object value);
}
