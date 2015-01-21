package com.hundsun.fcloud.servlet.api.resolver;

import com.hundsun.fcloud.servlet.api.ServletRequest;

/**
 * Created by Gavin Hu on 2014/12/31.
 */
public interface ServletContextResolver {

    String resolve(ServletRequest request);

}
