package com.hundsun.fcloud.servlet.caller.simple;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;
import com.hundsun.fcloud.servlet.caller.ServletCaller;
import com.hundsun.fcloud.servlet.codec.fixlen.FixlenRequestEncoder;
import com.hundsun.fcloud.servlet.codec.fixlen.FixlenResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.ConnectException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gavin Hu on 2014/12/29.
 */
public class SimpleServletCaller extends ChannelInboundHandlerAdapter implements ServletCaller {

    private static final Log log = LogFactory.getLog(SimpleServletCaller.class);

    private int timeout = 3; // SECOND

    private Channel channel;

    private EventLoopGroup workerGroup;

    private ServletResponseCallback callback;

    public SimpleServletCaller(String host, int port) {
        //
        this.workerGroup = new NioEventLoopGroup();
        //
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new FixlenRequestEncoder());
                ch.pipeline().addLast(new FixlenResponseDecoder());
                ch.pipeline().addLast(SimpleServletCaller.this);
            }
        });
        //
        ChannelFuture f = b.connect(host, port).syncUninterruptibly();
        this.channel = f.channel();
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public ServletResponse call(ServletRequest request) throws Exception {
        //
        this.callback = new ServletResponseCallback();
        //
        this.channel.writeAndFlush(request);
        //
        return callback.get();
    }

    @Override
    public void close() {
        if(channel!=null) {
            this.channel.close();
        }
        if(workerGroup!=null) {
            this.workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //
        if(msg instanceof ServletResponse) {
            //
            callback.handle((ServletResponse) msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //
        log.error(cause.getMessage(), cause);
        //
        super.exceptionCaught(ctx, cause);
    }

    private class ServletResponseCallback {

        private ServletResponse response;

        private CountDownLatch latch = new CountDownLatch(1);

        public void handle(ServletResponse response) {
            this.response = response;
            latch.countDown();
        }

        public ServletResponse get() throws Exception {

            latch.await(timeout, TimeUnit.SECONDS);
            //latch.await();
            if(response==null) {
                throw new ConnectException("");
            }
            //
            return response;
        }
    }

}
