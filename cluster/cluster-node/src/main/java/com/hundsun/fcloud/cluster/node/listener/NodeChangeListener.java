package com.hundsun.fcloud.cluster.node.listener;

/**
 * Created by Gavin Hu on 2015/1/26.
 */
public interface NodeChangeListener {

    String nodePath();

    void nodeChanged(NodeChangeEvent event);

}
