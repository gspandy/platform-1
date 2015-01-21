package com.hundsun.fcloud.servlet.api;

import java.io.InputStream;

/**
 * Servlet请求
 *
 * @author gavin
 * @create 13-7-28
 * @since 1.0.0
 */
public interface ServletRequest extends ServletMessage {

    String getContentType();

    Object getParameter(String name);

    void setParameter(String name, Object value);

    InputStream getInputStream();

}