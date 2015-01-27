package com.hundsun.jresplus.util.dispatcher;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * @author Leo
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Dispatcher {

	/**
	 * 用户标识该方法是做什么的,报错将返回该字符串
	 * 
	 * @return
	 */
	String remark() default "";
}
