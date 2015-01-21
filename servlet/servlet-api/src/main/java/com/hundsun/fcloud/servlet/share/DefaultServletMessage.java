package com.hundsun.fcloud.servlet.share;

import com.hundsun.fcloud.servlet.api.ServletMessage;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gavin Hu on 2014/12/29.
 */
public class DefaultServletMessage implements ServletMessage {

    private Map<String, Object> headers = new HashMap<String, Object>();

    private Object body = null;

    @Override
    public Object getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public <H> H getHeader(String name, Class<H> type) {
        Object header =  headers.get(name);
        return type.cast(header);
    }

    @Override
    public Object getHeader(String name, Object defaultValue) {
        Object header = headers.get(name);
        if(header==null) {
            return defaultValue;
        }
        return header;
    }

    @Override
    public <H> H getHeader(String name, Object defaultValue, Class<H> type) {
        Object header = headers.get(name);
        if(header==null) {
            return type.cast(defaultValue);
        }
        return type.cast(header);
    }

    @Override
    public Map<String, Object> getHeaders() {
        return headers;
    }

    @Override
    public void setHeader(String name, Object value) {
        this.headers.put(name, value);
    }

    @Override
    public void removeHeader(String name) {
        this.headers.remove(name);
    }

    @Override
    public void removeHeaders(String... names) {
        for(String name : names) {
            this.headers.remove(name);
        }
    }

    @Override
    public Object getBody() {
        return body;
    }

    @Override
    public <B> B getBody(Class<B> type) {
        return type.cast(body);
    }

    public void setBody(Object body) {
        this.body = body;
    }

    protected Map<String, Object> getParameterMap() {
        Map<String, Object> parameterMap = getHeader(HEADER_PARAMETER_MAP, Map.class);
        if(parameterMap==null) {
            parameterMap = new HashMap<String, Object>();
            setHeader(HEADER_PARAMETER_MAP, parameterMap);
        }
        //
        return parameterMap;
    }

}
