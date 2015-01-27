package com.hundsun.fcloud.cluster.node.internal;

import com.alibaba.fastjson.JSONObject;
import com.hundsun.fcloud.cluster.node.Node;
import com.hundsun.fcloud.cluster.node.NodeService;
import com.hundsun.fcloud.cluster.node.listener.NodeChangeEvent;
import com.hundsun.fcloud.cluster.node.listener.NodeChangeListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Gavin Hu on 2015/1/25.
 */
public class NodeServiceImpl implements NodeService {

    private static final Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);

    private ZkConnection zkConnection;

    private Set<PathChildrenCache> pathChildrenCaches = new LinkedHashSet<PathChildrenCache>();

    public NodeServiceImpl() {
        //
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                //
                for(PathChildrenCache pathChildrenCache : pathChildrenCaches) {
                    try {
                        pathChildrenCache.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        });
    }

    public void setZkConnection(ZkConnection zkConnection) {
        this.zkConnection = zkConnection;
    }

    @Override
    public void addListener(final NodeChangeListener listener) {
        //
        try {
            PathChildrenCache pathChildrenCache = new PathChildrenCache(zkConnection.getClient(), listener.nodePath(), true);
            pathChildrenCache.start();
            //
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    //
                    Node node = JSONObject.parseObject(event.getData().getData(), Node.class);
                    NodeChangeEvent nodeChangeEvent = null;
                    //
                    if(event.getType()== PathChildrenCacheEvent.Type.CHILD_ADDED) {
                        nodeChangeEvent = new NodeChangeEvent(node, NodeChangeEvent.Type.NODE_ADDED);
                    }
                    if(event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                         nodeChangeEvent = new NodeChangeEvent(node, NodeChangeEvent.Type.NODE_UPDATED);
                    }
                    if(event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                        nodeChangeEvent = new NodeChangeEvent(node, NodeChangeEvent.Type.NODE_REMOVED);
                    }
                    //
                    if(nodeChangeEvent!=null) {
                        listener.nodeChanged(nodeChangeEvent);
                    }
                }
            });
            //
            pathChildrenCaches.add(pathChildrenCache);
        } catch (Exception e) {
            logger.error("添加节点变更监听器失败！", e);
        }

    }

}
