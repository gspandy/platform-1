package com.hundsun.fcloud.servlet.runner.transport;

import com.hundsun.fcloud.servlet.runner.internal.transport.DefaultServer;
import com.hundsun.fcloud.servlet.runner.internal.transport.Server;

import java.util.concurrent.TimeUnit;

/**
 * Created by Gavin Hu on 2014/12/31.
 */
public class ServerTest {

    public static void main(String[] args) throws InterruptedException {
        //
        Server server = new DefaultServer(null);
        server.bind(6610);
        //
        TimeUnit.SECONDS.sleep(6000);
    }

}
