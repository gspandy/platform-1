package com.hundsun.fcloud.cluster.node;

import com.alibaba.fastjson.JSONObject;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.CreateMode;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Gavin Hu on 2015/1/22.
 */
public class NodeActivator {

    private NodeInfo nodeInfo;

    private CuratorFramework client;

    private String connectionString;

    private int sessionTimeout;

    private int connectionTimeout;

    private RetryPolicy retryPolicy;

    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
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
        this.client = CuratorFrameworkFactory.newClient(connectionString, sessionTimeout, connectionTimeout, retryPolicy);
        this.client.start();
        //
        String nodePath = String.format("/cluster/karaf/%s", getNodeName());
        byte[] nodeData = getNodeData();
        //
        this.client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(nodePath, nodeData);
    }

    public void stop() {
        //
        if(this.client!=null) {
            this.client.close();
        }
    }

    private String getNodeName() throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        return hostAddress.replaceAll("\\.", "_");
    }

    public byte[] getNodeData() throws UnknownHostException, UnsupportedEncodingException {
        //
        JSONObject nodeData = new JSONObject();
        nodeData.put("nodeHome", nodeInfo.getNodeHome());
        nodeData.put("nodeName", nodeInfo.getNodeName());
        nodeData.put("nodeHost", nodeInfo.getNodeHost());
        nodeData.put("nodePid", nodeInfo.getNodePid());
        nodeData.put("sshPort", nodeInfo.getSshPort());
        nodeData.put("rmiServerPort", nodeInfo.getSshPort());
        nodeData.put("rmiRegistryPort", nodeInfo.getRmiRegistryPort());
        //
        return nodeData.toJSONString().getBytes("utf-8");
    }

}
