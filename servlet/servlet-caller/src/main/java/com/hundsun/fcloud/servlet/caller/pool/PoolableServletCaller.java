package com.hundsun.fcloud.servlet.caller.pool;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;
import com.hundsun.fcloud.servlet.caller.ServletCaller;
import com.hundsun.fcloud.servlet.caller.ServletCallerException;
import com.hundsun.fcloud.servlet.caller.simple.SimpleServletCaller;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gavin Hu on 2015/1/4.
 */
public class PoolableServletCaller implements ServletCaller {

    private static final int DEFAULT_CALL_TIMEOUT = 3;

    private int index = 0;

    private List<ObjectPool<ServletCaller>> servletCallerObjectPools = new ArrayList<ObjectPool<ServletCaller>>();

    public PoolableServletCaller(String[] hosts, int[] ports, int poolSize) {
        this(hosts, ports, poolSize, DEFAULT_CALL_TIMEOUT);
    }

    public PoolableServletCaller(String[] hosts, int[] ports, int poolSize, int timeout) {
        if(hosts.length!=ports.length) {
            throw new IllegalArgumentException("Hosts and Ports length miss match！");
        }
        //
        for(int i=0; i<hosts.length; i++) {
            //
            String host = hosts[i];
            int port = ports[i];
            //
            ObjectPool<ServletCaller> servletCallerObjectPool = new GenericObjectPool(new PoolableServletCallerFactory(host, port, timeout), poolSize);
            this.servletCallerObjectPools.add(servletCallerObjectPool);
        }
    }

    private ThreadLocal<Integer> localCount = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    @Override
    public ServletResponse call(ServletRequest request) {
        //
        ServletResponse response = null;
        try {
            localCount.set(localCount.get() + 1);
            //
            if(localCount.get()>servletCallerObjectPools.size()) {
                throw new ServletCallerException("底层所有链接不可用！");
            }
            //
            if(index>=servletCallerObjectPools.size()) {
                index=0;
            }
            //
            ObjectPool<ServletCaller> servletCallerObjectPool = servletCallerObjectPools.get(index);
            ServletCaller servletCaller = servletCallerObjectPool.borrowObject();
            //
            response = servletCaller.call(request);
            //
            servletCallerObjectPool.returnObject(servletCaller);
            //
        } catch (SocketException e) {
            index++;
            call(request);
        } catch (Exception e) {
            throw new ServletCallerException(e.getMessage(), e);
        } finally {
            localCount.remove();
        }
        return response;
    }

    @Override
    public void close() {
        try {
            for(ObjectPool servletCallerObjectPool : servletCallerObjectPools) {
                servletCallerObjectPool.close();
            }
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
