package com.hundsun.fcloud.cluster.node.internal;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Gavin Hu on 2015/1/24.
 */
public class ClusterSubscriber implements CuratorWatcher {

    private static final Logger logger = LoggerFactory.getLogger(ClusterSubscriber.class);

    private static final String CLUSTER_PATH = "/cluster/karaf";

    private ZkConnection zkConnection;

    public void setZkConnection(ZkConnection zkConnection) {
        this.zkConnection = zkConnection;
    }

    public void start() throws Exception {
        //
        zkConnection.getClient()
                .getChildren()
                .usingWatcher(this)
                .forPath(CLUSTER_PATH);
    }

    public void stop() {
        //
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
        //
        logger.info("Cluster node changed!" + event.getPath());
        //

    }
}
