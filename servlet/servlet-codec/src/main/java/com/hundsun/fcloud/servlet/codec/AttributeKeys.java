package com.hundsun.fcloud.servlet.codec;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import io.netty.util.AttributeKey;

/**
 * Created by Gavin Hu on 2014/12/29.
 */
public class AttributeKeys {

    public static AttributeKey<ServletRequest> servletRequestKey = AttributeKey.valueOf("servletRequest");

}
