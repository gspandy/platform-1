package com.hundsun.fcloud.web.page;

import com.hundsun.fcloud.web.extender.fork.Forkable;
import com.hundsun.fcloud.web.extender.fork.ForkedHttpServletRequest;
import com.hundsun.fcloud.web.extender.fork.ForkedHttpServletResponse;
import com.hundsun.fcloud.web.extender.fork.ForkedRequestAndResponse;
import com.hundsun.fcloud.web.page.config.PageConfig;
import com.hundsun.fcloud.web.page.config.PageletConfig;
import com.hundsun.fcloud.web.page.loader.PageLoader;
import com.hundsun.fcloud.web.page.loader.ServletContextPageLoader;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Gavin Hu on 2015/2/26.
 */
public class ForkablePage implements Forkable {

    private PageLoader pageLoader;

    @Override
    public void setServletContext(ServletContext context) {
        this.pageLoader = newPageLoader(context);
    }

    protected PageLoader newPageLoader(ServletContext context) {
        return new ServletContextPageLoader(context);
    }

    @Override
    public boolean canFork(HttpServletRequest req) {
        //
        String pagePath = getRequestPath(req);
        Map<String, String> params = getRequestParams(req);
        //
        PageConfig pageConfig = pageLoader.load(pagePath, params);
        Portal.setPageConfig(req, pageConfig);
        //
        return pageConfig!=null;
    }

    private String getRequestPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        int indexDot = pathInfo.indexOf(".");
        if(indexDot>0) {
            return pathInfo.substring(0, indexDot);
        }
        return pathInfo;
    }

    private Map<String, String> getRequestParams(HttpServletRequest req) {
        Map<String, String> requestParams = new HashMap<String, String>();
        for(Enumeration<String> e=req.getParameterNames(); e.hasMoreElements();) {
            String parameterName = e.nextElement();
            String parameterValue = req.getParameter(parameterName);
            requestParams.put(parameterName, parameterValue);
        }
        //
        return requestParams;
    }

    @Override
    public List<ForkedRequestAndResponse> fork(HttpServletRequest req, HttpServletResponse res) {
        //
        PageConfig pageConfig = Portal.getPageConfig(req);
        //
        List<ForkedRequestAndResponse> forkedList = new ArrayList<ForkedRequestAndResponse>();
        for(PageletConfig pageletConfig : pageConfig.getAllPageletConfig()) {
            String forkedName = pageletConfig.getId();
            String forkedPath = pageletConfig.getPath();
            //
            ForkedHttpServletRequest forkedReq = new ForkedHttpServletRequest(req, forkedPath);
            ForkedHttpServletResponse forkedRes = new ForkedHttpServletResponse(res);
            //
            forkedList.add(new ForkedRequestAndResponse(forkedName, forkedReq, forkedRes));
        }
        //
        return forkedList;
    }

}
