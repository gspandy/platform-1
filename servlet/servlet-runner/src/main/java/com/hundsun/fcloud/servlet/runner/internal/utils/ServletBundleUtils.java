package com.hundsun.fcloud.servlet.runner.internal.utils;

import org.osgi.framework.Bundle;

/**
 * Created by Gavin Hu on 2015/1/1.
 */
public class ServletBundleUtils {

    private static final String HEADER_SERVLET_API = "Servlet-API";

    private static final String HEADER_SERVLET_CONTEXT = "Servlet-Context";

    public static boolean hasServletAPI(Bundle bundle) {
        String servletAPI = bundle.getHeaders().get(HEADER_SERVLET_API);
        return servletAPI!=null;
    }

    public static String getServletContext(Bundle bundle) {
        return bundle.getHeaders().get(HEADER_SERVLET_CONTEXT);
    }

}
