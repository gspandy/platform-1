package com.hundsun.fcloud.servlet.share;

import com.hundsun.fcloud.servlet.api.ServletMessage;
import com.hundsun.fcloud.servlet.api.ServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Created by Gavin Hu on 2014/12/29.
 */
public class DefaultServletResponse extends DefaultServletMessage implements ServletResponse {

    private OutputStream outputStream = new ByteArrayOutputStream();

    public DefaultServletResponse() {
    }

    public DefaultServletResponse(ServletMessage servletMessage) {
        super.getHeaders().putAll(servletMessage.getHeaders());
        super.setBody(servletMessage.getBody());
    }

    @Override
    public void setContentType(String contentType) {
        super.setHeader(HEADER_CONTENT_TYPE, contentType);
    }

    @Override
    public void setParameter(String name, Object value) {
        getParameterMap().put(name, value);
    }

    @Override
    public Object getParameter(String name) {
        return getParameterMap().get(name);
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

}
