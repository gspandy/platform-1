package com.hundsun.jresplus.ui.valid;

import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于后台校验和前台校验绑定的注解
 * 
 * @author Leo
 * 
 */
@Target(PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bind {

	public String[] groups() default {};

}
