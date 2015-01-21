package com.hundsun.fcloud.servlet.share;

import com.hundsun.fcloud.servlet.api.ServletMessage;
import com.hundsun.fcloud.servlet.api.ServletRequest;

import java.io.InputStream;

/**
 * Created by Gavin Hu on 2014/12/29.
 */
public class DefaultServletRequest extends DefaultServletMessage implements ServletRequest {

    private ServletMessage servletMessage;

    public DefaultServletRequest() {
    }

    public DefaultServletRequest(ServletMessage servletMessage) {
        super.getHeaders().putAll(servletMessage.getHeaders());
        super.setBody(servletMessage.getBody());
    }

    @Override
    public String getContentType() {
        return getHeader(HEADER_CONTENT_TYPE, String.class);
    }

    @Override
    public Object getParameter(String name) {
        return getParameterMap().get(name);
    }

    public void setParameter(String name, Object value) {
        getParameterMap().put(name, value);
    }

    @Override
    public InputStream getInputStream() {
        return getBody(InputStream.class);
    }

}
