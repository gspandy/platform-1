package com.hundsun.fcloud.servlet.runner.internal.runner;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;
import org.osgi.framework.Bundle;

/**
 * Created by Gavin Hu on 2014/12/28.
 */
public interface ServletRunner {

    void init();

    boolean canRun(ServletRequest request, ServletResponse response);

    void run(ServletRequest request, ServletResponse response) throws Exception;

    void destroy();

}

