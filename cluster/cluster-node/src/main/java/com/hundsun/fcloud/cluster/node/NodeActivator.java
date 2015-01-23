package com.hundsun.fcloud.cluster.node;

import com.alibaba.fastjson.JSONObject;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * Created by Gavin Hu on 2015/1/22.
 */
public class NodeActivator {

    private static final Logger logger = LoggerFactory.getLogger(NodeActivator.class);

    private static final String CLUSTER_KARAF_PATH_FORMAT = "/cluster/karaf/%s";

    private NodeInstance nodeInstance;

    private CuratorFramework client;

    private String connectionString;

    private int sessionTimeout;

    private int connectionTimeout;

    private RetryPolicy retryPolicy;

    public void setNodeInstance(NodeInstance nodeInstance) {
        this.nodeInstance = nodeInstance;
    }

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

    public void start() throws Exception {
        //
        logger.info("Cluster node is starting!");
        //
        this.client = CuratorFrameworkFactory.newClient(connectionString, sessionTimeout, connectionTimeout, retryPolicy);
        this.client.start();
        //
        String nodePath = String.format(CLUSTER_KARAF_PATH_FORMAT, getNodeId());
        //
        this.client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(nodePath, getNodeData());
        //
        logger.info("Cluster node is started!");
    }

    public void stop() {
        //
        logger.info("Cluster node is stopping!");
        //
        if(this.client!=null) {
            this.client.close();
        }
        //
        logger.info("Cluster node is stopped!");
    }

    private String getNodeId() throws UnknownHostException {
        return nodeInstance.getHost().replaceAll("\\.", "_");
    }

    public byte[] getNodeData() {
        return JSONObject.toJSONBytes(nodeInstance);
    }

}
