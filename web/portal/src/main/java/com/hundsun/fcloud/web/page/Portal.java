package com.hundsun.fcloud.web.page;

import com.hundsun.fcloud.web.page.config.PageConfig;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Gavin Hu on 2015/2/26.
 */
public class Portal {

    public static void setPageConfig(HttpServletRequest req, PageConfig pageConfig) {
        req.setAttribute(PageConfig.class.getName(), pageConfig);
    }

    public static PageConfig getPageConfig(HttpServletRequest req) {
        return (PageConfig) req.getAttribute(PageConfig.class.getName());
    }

}
