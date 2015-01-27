package com.hundsun.jresplus.base.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 动态验证的注解
 * 
 * @author Leo
 * 
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DynamicValidator.class)
@Documented
public @interface Dynamic {

	/**
	 * 报错信息
	 * 
	 * @return
	 */
	String message() default "";

	/**
	 * 校验的分组
	 * 
	 * @return
	 */
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 指定校验的类
	 * 
	 * @return
	 */
	Class<? extends Validator> handle();
}
