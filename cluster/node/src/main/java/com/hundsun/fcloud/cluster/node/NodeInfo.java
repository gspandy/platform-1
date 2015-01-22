package com.hundsun.fcloud.cluster.node;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by Gavin Hu on 2015/1/22.
 */
public class NodeInfo {

    private Properties props = new Properties();

    public void init() throws IOException {
        //
        for(String propertyName : System.getProperties().stringPropertyNames()) {
            if(propertyName.startsWith("karaf")) {
                props.setProperty(propertyName, System.getProperty(propertyName));
            }
        }
        //
        String nodeHome = System.getProperty("karaf.home");
        File instanceFile = new File(nodeHome, "/instances/instance.properties");
        props.load(new FileInputStream(instanceFile));
    }

    public String getNodeHome() {
        return props.getProperty("karaf.home");
    }

    public String getNodeHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return e.getMessage();
        }
    }

    public String getNodeName() {
        return props.getProperty("karaf.name");
    }

    public String getNodePid() {
        return props.getProperty("item.0.pid");
    }

    public String getSshPort() {
        return props.getProperty("ssh.port");
    }

    public String getRmiRegistryPort() {
        return props.getProperty("rmi.registry.port");
    }

    public String getRmiServerPort() {
        return props.getProperty("rmi.server.port");
    }

    public void destroy() {
        //
        props.clear();
    }

}
