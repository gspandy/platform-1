package com.hundsun.fcloud.web.extender.fork;

import com.hundsun.fcloud.web.extender.dispatch.Dispatcher;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Gavin Hu on 2015/2/7.
 */
public class ForkableDispatcher {

    private static final String FORK_CONFIG_LOCATION_FORMAT = "/WEB-INF/fork/%s.cfg";

    private Dispatcher dispatcher;

    private ServletContext context;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private Map<String, Properties> forkConfigMap = new HashMap<String, Properties>();

    public ForkableDispatcher(Dispatcher dispatcher, ServletContext context) {
        this.dispatcher = dispatcher;
        this.context = context;
    }

    public boolean canFork(final HttpServletRequest req, final HttpServletResponse res) {
        //
        String requestPath = getRequestPath(req);
        if(forkConfigMap.containsKey(requestPath)) {
            return true;
        }
        //
        String forkConfigPath = getForkConfigPath(requestPath);
        InputStream source = context.getResourceAsStream(forkConfigPath);
        if(source==null) {
            return false;
        }
        //
        try {
            Properties forkProps = new Properties();
            forkProps.load(source);
            forkConfigMap.put(requestPath, forkProps);
        } catch (IOException e) {
            return false;
        }
        //
        return true;
    }

    private String getForkConfigPath(String requestPath) {
        if(requestPath.startsWith("/")) {
            requestPath = requestPath.substring(1);
        }
        return String.format(FORK_CONFIG_LOCATION_FORMAT, requestPath.replaceAll("/", "_"));
    }

    public void forkAndDispatch(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        //
        final Map parameterMap = req.getParameterMap();
        Properties forkConfig = forkConfigMap.get(getRequestPath(req));
        //
        List<Future<ForkedRequestAndResponse>> futures = new ArrayList<Future<ForkedRequestAndResponse>>();
        for(final String propertyName : forkConfig.stringPropertyNames()) {
            final String propertyValue = forkConfig.getProperty(propertyName);
            Future<ForkedRequestAndResponse> future = executorService.submit(new Callable<ForkedRequestAndResponse>() {
                @Override
                public ForkedRequestAndResponse call() throws Exception {
                    ForkedHttpServletRequest forkedReq = new ForkedHttpServletRequest(req, propertyValue, parameterMap);
                    ForkedHttpServletResponse forkedRes = new ForkedHttpServletResponse(res);
                    //
                    dispatcher.dispatch(forkedReq, forkedRes);
                    //
                    return new ForkedRequestAndResponse(propertyName, forkedReq, forkedRes);
                }
            });
            //
            futures.add(future);
        }
        //
        for(Future<ForkedRequestAndResponse> future : futures) {
            //
            try {
                ForkedRequestAndResponse forkedRequestAndResponse = future.get();
                //
                req.setAttribute(forkedRequestAndResponse.getForkedName(), forkedRequestAndResponse.getForkedResponse().toString());
                //
            } catch (Exception e) {
                throw new ServletException(e.getMessage(), e);
            }
        }
        //
        dispatcher.dispatch(req, res);
    }

    private String getRequestPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        int indexDot = pathInfo.indexOf(".");
        if(indexDot>0) {
            return pathInfo.substring(0, indexDot);
        }
        return pathInfo;
    }


}
