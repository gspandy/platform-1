package com.hundsun.fcloud.servlet.runner.internal.transport;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;
import com.hundsun.fcloud.servlet.codec.fixlen.FixlenRequestDecoder;
import com.hundsun.fcloud.servlet.codec.fixlen.FixlenResponseEncoder;
import com.hundsun.fcloud.servlet.runner.internal.runner.ServletRunner;
import com.hundsun.fcloud.servlet.runner.internal.tracker.ServletRunnerTracker;
import com.hundsun.fcloud.servlet.share.DefaultServletResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Gavin Hu on 2014/12/31.
 */
public class DefaultServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(DefaultServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ServletRunnerTracker servletRunnerTracker;

    public DefaultServer(ServletRunnerTracker servletRunnerTracker) {
        this.servletRunnerTracker = servletRunnerTracker;
    }

    @Override
    public void bind(int port) {
        //
        logger.info("Server is starting!");
        //
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        //
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FixlenRequestDecoder());
                        ch.pipeline().addLast(new FixlenResponseEncoder());
                        ch.pipeline().addLast(new ServletChannelHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        ChannelFuture f = b.bind(port).syncUninterruptibly();
        //
        logger.info("Server is started and listening on {}", port);
    }



    @Override
    public void close() {
        //
        logger.info("Server is stopping!");
        //
        if(bossGroup!=null) {
           this.bossGroup.shutdownGracefully();
        }
        //
        if(workerGroup!=null) {
            this.workerGroup.shutdownGracefully();
        }
        //
        logger.info("Server is stopped!");
    }

    private class ServletChannelHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //
            if(msg instanceof ServletRequest) {
                //
                ServletRequest servletRequest = (ServletRequest) msg;;
                ServletResponse servletResponse = new DefaultServletResponse();
                //
                for(ServletRunner servletRunner : servletRunnerTracker.getServletRunners()) {
                    if(!servletRunner.canRun(servletRequest, servletResponse)) {
                        continue;
                    }
                    //
                    servletRunner.run(servletRequest, servletResponse);
                    //
                    break;
                }
                //
                ctx.channel().writeAndFlush(servletResponse);
            }
        }

    }

}
