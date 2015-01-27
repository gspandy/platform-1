package com.hundsun.fcloud.cluster.node.internal;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;

/**
 * Created by Gavin Hu on 2015/1/24.
 */
public class ZkConnection {

    private CuratorFramework client;

    private String connectionString;

    private int sessionTimeout;

    private int connectionTimeout;

    private RetryPolicy retryPolicy;

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public void connect() {
        this.client = CuratorFrameworkFactory.newClient(connectionString, sessionTimeout, connectionTimeout, retryPolicy);
        this.client.start();
    }

    public CuratorFramework getClient() {
        return this.client;
    }

    public void disconnect() {
        if(this.client!=null) {
            this.client.close();
        }
    }

}
