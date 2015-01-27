package com.hundsun.fcloud.cluster.node;

import com.hundsun.fcloud.cluster.node.internal.ZkConnection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by Gavin Hu on 2015/1/25.
 */
public class ClusterSubscriberTest {

    private ZkConnection zkConnection;

    @Before
    public void setup() {
        //
        zkConnection = new ZkConnection();
        zkConnection.setRetryPolicy(new ExponentialBackoffRetry(1000, 3));
        zkConnection.setConnectionString("localhost:2181");
        zkConnection.setSessionTimeout(60*1000);
        zkConnection.setConnectionTimeout(15*1000);
        //
        zkConnection.connect();
    }

    @Test
    public void testWatch() throws Exception {
        //
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkConnection.getClient(), "/cluster/karaf", true);
        //
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                //
                if(event.getType()==PathChildrenCacheEvent.Type.CHILD_ADDED) {

                }
            }
        });
        //
        pathChildrenCache.start();
        //
        TimeUnit.SECONDS.sleep(6000);
    }

    public void tearDown() {
        //
        zkConnection.disconnect();
    }

}
