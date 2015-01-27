package com.hundsun.fcloud.cluster.node;

/**
 * Created by Gavin Hu on 2015/1/26.
 */
public class Node {

    private String name;

    private String home;

    private String host;

    private int pid;

    private int sshPort;

    private int rmiServerPort;

    private int rmiRegistryPort;

    public Node() {
    }

    public Node(String name, String home, String host, int pid, int sshPort, int rmiServerPort, int rmiRegistryPort) {
        this.name = name;
        this.home = home;
        this.host = host;
        this.pid = pid;
        this.sshPort = sshPort;
        this.rmiServerPort = rmiServerPort;
        this.rmiRegistryPort = rmiRegistryPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getSshPort() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort = sshPort;
    }

    public int getRmiServerPort() {
        return rmiServerPort;
    }

    public void setRmiServerPort(int rmiServerPort) {
        this.rmiServerPort = rmiServerPort;
    }

    public int getRmiRegistryPort() {
        return rmiRegistryPort;
    }

    public void setRmiRegistryPort(int rmiRegistryPort) {
        this.rmiRegistryPort = rmiRegistryPort;
    }
}
