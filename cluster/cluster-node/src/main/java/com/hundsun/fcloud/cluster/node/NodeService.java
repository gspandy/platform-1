package com.hundsun.fcloud.cluster.node;

import com.hundsun.fcloud.cluster.node.listener.NodeChangeListener;

/**
 * Created by Gavin Hu on 2015/1/25.
 */
public interface NodeService {

    void addListener(NodeChangeListener listener);

}
