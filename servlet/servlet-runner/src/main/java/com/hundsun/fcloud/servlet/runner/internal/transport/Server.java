package com.hundsun.fcloud.servlet.runner.internal.transport;

/**
 * Created by Gavin Hu on 2014/12/31.
 */
public interface Server {

    void bind(int port);

    void close();

}
