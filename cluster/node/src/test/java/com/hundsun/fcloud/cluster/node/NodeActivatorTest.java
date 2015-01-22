package com.hundsun.fcloud.cluster.node;

import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by Gavin Hu on 2015/1/22.
 */
public class NodeActivatorTest {

    @Test
    public void testStart() throws Exception {
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
                //
                NodeActivator nodeActivator = new NodeActivator();
                nodeActivator.setConnectionString("localhost:2181");
                nodeActivator.setConnectionTimeout(15*1000);
                nodeActivator.setSessionTimeout(60*1000);
                nodeActivator.setRetryPolicy(retryPolicy);
                //
                try {
                    nodeActivator.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //
        TimeUnit.SECONDS.sleep(600);
    }

}
