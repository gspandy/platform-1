package com.hundsun.fcloud.servlet.api.resolver;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;

import java.lang.reflect.Method;

/**
 * Created by Gavin Hu on 2014/12/28.
 */
public interface ServletArgumentResolver {

    Object[] resolve(Method servletMethod, ServletRequest request, ServletResponse response);

}
