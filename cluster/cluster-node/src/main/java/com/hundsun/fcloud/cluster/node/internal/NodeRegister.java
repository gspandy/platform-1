package com.hundsun.fcloud.cluster.node.internal;

import com.alibaba.fastjson.JSONObject;
import com.hundsun.fcloud.cluster.node.Node;
import org.apache.karaf.instance.core.Instance;
import org.apache.karaf.instance.core.InstanceService;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Gavin Hu on 2015/1/22.
 */
public class NodeRegister {

    private static final Logger logger = LoggerFactory.getLogger(NodeRegister.class);

    private static final String CLUSTER_KARAF_PATH_FORMAT = "/cluster/karaf/%s";

    private ZkConnection zkConnection;

    private InstanceService instanceService;

    public void setZkConnection(ZkConnection zkConnection) {
        this.zkConnection = zkConnection;
    }

    public void setInstanceService(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    public void start() throws Exception {
        //
        logger.info("Cluster node is registering!");
        //
        Node node = getNode();
        //
        String nodeId = getNodeId(node);
        String nodePath = String.format(CLUSTER_KARAF_PATH_FORMAT, nodeId);
        byte[] nodeData = JSONObject.toJSONBytes(node);
        //
        zkConnection.getClient()
                .create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(nodePath, nodeData);
        //
        logger.info("Cluster node is registered!");
    }

    public void stop() {
        //
    }

    private String getNodeId(Node node) throws UnknownHostException {
        return node.getHost().replaceAll("\\.", "_");
    }

    public Node getNode() throws UnknownHostException {
        //
        Instance instance = getInstance();
        String name = instance.getName();
        String home = instance.getLocation();
        String host = InetAddress.getLocalHost().getHostAddress();
        int pid = instance.getPid();
        int sshPort = instance.getSshPort();
        int rmiServerPort = instance.getRmiServerPort();
        int rmiRegisterPort = instance.getRmiRegistryPort();
        //
        return new Node(name, home, host, pid, sshPort, rmiServerPort, rmiRegisterPort);
    }

    private Instance getInstance() {
        String instanceName = System.getProperty("karaf.name");
        return this.instanceService.getInstance(instanceName);
    }

}
