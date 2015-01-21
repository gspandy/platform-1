package com.hundsun.fcloud.servlet.api.resolver;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;

/**
 * Created by Gavin Hu on 2014/12/28.
 */
public interface ServletReturnValueResolver {

    void resolve(Object returnValue, ServletRequest request, ServletResponse response);

}
