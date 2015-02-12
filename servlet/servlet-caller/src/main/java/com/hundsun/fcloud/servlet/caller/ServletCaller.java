package com.hundsun.fcloud.servlet.caller;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;

/**
 * Created by Gavin Hu on 2014/12/29.
 */
public interface ServletCaller {

    ServletResponse call(ServletRequest request) throws Exception;

    void close();

}
