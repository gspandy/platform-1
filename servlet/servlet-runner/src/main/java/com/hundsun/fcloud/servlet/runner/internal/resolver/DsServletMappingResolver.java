package com.hundsun.fcloud.servlet.runner.internal.resolver;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.resolver.ServletMappingResolver;

/**
 * 支持直销 HS_WEB_xx 映射解析器
 */
public class DsServletMappingResolver implements ServletMappingResolver {

    @Override
    public String resolve(ServletRequest request) {
        return "HS_WEB_" + request.getHeader(ServletRequest.HEADER_CODEC, String.class);
    }

}
