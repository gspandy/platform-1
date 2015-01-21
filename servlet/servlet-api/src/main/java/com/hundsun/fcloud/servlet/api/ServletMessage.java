package com.hundsun.fcloud.servlet.api;

import java.util.Map;

/**
 * 消息
 *
 * @author gavin
 * @create 14-6-26
 * @since 1.1.0
 */
public interface ServletMessage {

    String HEADER_CODEC = "Codec";

    String HEADER_CONTENT_TYPE = "ContentType";

    String HEADER_PARAMETER_MAP = "ParameterMap";

    Object getHeader(String name);

    <H> H getHeader(String name, Class<H> type);

    Object getHeader(String name, Object defaultValue);

    <H> H getHeader(String name, Object defaultValue, Class<H> type);

    Map<String, Object> getHeaders();

    void setHeader(String name, Object value);

    void removeHeader(String name);

    void removeHeaders(String... names);

    Object getBody();

    <B> B getBody(Class<B> type);

}
