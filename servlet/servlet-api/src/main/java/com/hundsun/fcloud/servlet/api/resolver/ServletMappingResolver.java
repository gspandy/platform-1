package com.hundsun.fcloud.servlet.api.resolver;

import com.hundsun.fcloud.servlet.api.ServletRequest;

/**
 * Servlet 映射解析器
 *
 * @author gavin
 * @create 14-7-8
 * @since 2.0.0
 */
public interface ServletMappingResolver {

    /**
     * 返回跟 @Mapping 对应的服务名
     *
     * @param request
     * @return
     */
    String resolve(ServletRequest request);

}
