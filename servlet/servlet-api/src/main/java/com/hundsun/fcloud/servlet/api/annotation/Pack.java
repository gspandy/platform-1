package com.hundsun.fcloud.servlet.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义打包格式
 *
 * @author gavin
 * @since 1.0.0
 * @create 2013-07-31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pack {

    // alias for fields()
    Field[] value() default {};

    Field[] fields() default {};

    @interface Field {

        String name();
        //
        String column();

    }

}
