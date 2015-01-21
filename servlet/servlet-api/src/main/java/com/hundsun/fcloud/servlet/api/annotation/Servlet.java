package com.hundsun.fcloud.servlet.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义 Servlet
 *
 * @author gavin
 * @create 13-7-29
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Servlet {

    // alias for name()
    String value() default "";

}
