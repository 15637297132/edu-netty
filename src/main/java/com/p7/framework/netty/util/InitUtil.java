package com.p7.framework.netty.util;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-04-01 20:07
 **/
public class InitUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitUtil.class);

    public static EventLoopGroup initBoosEventLoopGroup() {
        if (EnvUtil.useEpoll()) {
            return new EpollEventLoopGroup(1, new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyEPOLLBoss_%d", this.threadIndex.incrementAndGet()));
                }
            });
        } else {
            return new NioEventLoopGroup(1, new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyNIOBoss_%d", this.threadIndex.incrementAndGet()));
                }
            });
        }
    }

    public static EventLoopGroup initWorkerEventLoopGroup() {
        if (EnvUtil.useEpoll()) {
            return new EpollEventLoopGroup(EnvUtil.workerThreadTotal, new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerEPOLLSelector_%d_%d", EnvUtil.workerThreadTotal, this.threadIndex.incrementAndGet()));
                }
            });
        } else {
            return new NioEventLoopGroup(EnvUtil.workerThreadTotal, new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerNIOSelector_%d_%d", EnvUtil.workerThreadTotal, this.threadIndex.incrementAndGet()));
                }
            });
        }
    }

    public static ChannelFuture serverStart(ServerBootstrap bootstrap, int port) throws Exception {
        return bootstrap.bind(port).sync();
    }

    public static void sync(ChannelFuture channelFuture) throws Exception {
        channelFuture.channel().closeFuture().sync();
    }

    public static void setServerChannel(ServerBootstrap serverBootstrap) {
        serverBootstrap.channel(EnvUtil.useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
    }
}
