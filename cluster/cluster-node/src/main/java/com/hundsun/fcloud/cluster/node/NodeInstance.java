package com.hundsun.fcloud.cluster.node;

import org.apache.karaf.instance.core.Instance;
import org.apache.karaf.instance.core.InstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Gavin Hu on 2015/1/23.
 */
public class NodeInstance {

    private static final Logger logger = LoggerFactory.getLogger(NodeInstance.class);

    private InstanceService instanceService;

    public void setInstanceService(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    private Instance getInstance() {
        String instanceName = System.getProperty("karaf.name");
        return this.instanceService.getInstance(instanceName);
    }

    public String getName() {
        return getInstance().getName();
    }

    public String getHome() {
        return getInstance().getLocation();
    }

    public int getPid() {
        return getInstance().getPid();
    }

    public String getState() {
        try {
            return getInstance().getState();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Instance.ERROR;
        }
    }

    public String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    public int getSshPort() {
        return getInstance().getSshPort();
    }

    public int getRmiServerPort() {
        return getInstance().getRmiServerPort();
    }

    public int getRmiRegistryPort() {
        return getInstance().getRmiRegistryPort();
    }
}
