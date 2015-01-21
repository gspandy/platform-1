package com.hundsun.fcloud.servlet.caller.impl;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;
import com.hundsun.fcloud.servlet.caller.ServletCaller;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Gavin Hu on 2015/1/4.
 */
public class PoolableServletCaller implements ServletCaller {

    private static final Logger logger = LoggerFactory.getLogger(PoolableServletCaller.class);

    private ObjectPool<ServletCaller> servletCallerObjectPool;

    public PoolableServletCaller(String host, int port, int poolSize) {
        this.servletCallerObjectPool = new GenericObjectPool(new PoolableServletCallerFactory(host, port), poolSize);
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
            logger.error(e.getMessage(), e);
        }
        return response;
    }

    @Override
    public void close() {
        try {
            this.servletCallerObjectPool.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private class PoolableServletCallerFactory extends BasePoolableObjectFactory<ServletCaller> {

        private String host;

        private int port;

        public PoolableServletCallerFactory(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public ServletCaller makeObject() throws Exception {
            return new SimpleServletCaller(host, port);
        }

        @Override
        public void destroyObject(ServletCaller servletCaller) throws Exception {
            servletCaller.close();
        }
    }

}
