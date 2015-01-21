package com.hundsun.fcloud.servlet.runner.internal.resolver;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.resolver.ServletContextResolver;

/**
 * 支持直销 HS_WEB 上下文解析器
 */
public class DsServletContextResolver implements ServletContextResolver {

    @Override
    public String resolve(ServletRequest request) {
        return "HS_WEB";
    }

}
