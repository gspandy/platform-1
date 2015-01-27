package com.hundsun.fcloud.cluster.test;

import com.hundsun.fcloud.cluster.node.NodeService;
import com.hundsun.fcloud.cluster.test.listener.MyNodeClusterListener;

/**
 * Created by Gavin Hu on 2015/1/26.
 */
public class ClusterActivator {

    private NodeService nodeService;

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void start() {
        //
        this.nodeService.addListener(new MyNodeClusterListener());
    }

}
