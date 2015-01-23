package com.hundsun.fcloud.servlet.caller.impl;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;
import com.hundsun.fcloud.servlet.caller.ServletCaller;
import com.hundsun.fcloud.servlet.caller.ServletCallerException;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * Created by Gavin Hu on 2015/1/4.
 */
public class PoolableServletCaller implements ServletCaller {

    private static final int DEFAULT_CALL_TIMEOUT = 3;

    private ObjectPool<ServletCaller> servletCallerObjectPool;

    public PoolableServletCaller(String host, int port, int poolSize) {
        this(host, port, poolSize, DEFAULT_CALL_TIMEOUT);
    }

    public PoolableServletCaller(String host, int port, int poolSize, int timeout) {
        this.servletCallerObjectPool = new GenericObjectPool(new PoolableServletCallerFactory(host, port, timeout), poolSize);
    }

    @Override
    public ServletResponse call(ServletRequest request) {
        //
        ServletResponse response = null;
        try {
            ServletCaller servletCaller = this.servletCallerObjectPool.borrowObject();
            //
            response = servletCaller.call(request);
            //
            this.servletCallerObjectPool.returnObject(servletCaller);
            //
        } catch (Exception e) {
            throw new ServletCallerException(e.getMessage(), e);
        }
        return response;
    }

    @Override
    public void close() {
        try {
            this.servletCallerObjectPool.close();
        } catch (Exception e) {
            throw new ServletCallerException(e.getMessage(), e);
        }
    }

    private class PoolableServletCallerFactory extends BasePoolableObjectFactory<ServletCaller> {

        private String host;

        private int port;

        private int timeout;

        public PoolableServletCallerFactory(String host, int port, int timeout) {
            this.host = host;
            this.port = port;
            this.timeout = timeout;
        }

        @Override
        public ServletCaller makeObject() throws Exception {
            SimpleServletCaller servletCaller =  new SimpleServletCaller(host, port);
            servletCaller.setTimeout(timeout);
            //
            return servletCaller;
        }

        @Override
        public void destroyObject(ServletCaller servletCaller) throws Exception {
            servletCaller.close();
        }
    }

}
