package com.p7.framework.netty.websocketx;

import com.p7.framework.netty.service.WebSocketService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * @author Yangzhen
 * @Description
 * @date 2020-03-26 9:36
 **/
public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    @Resource
    private AuthHandler authHandler;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private WebSocketService webSocketService;

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    public void start() {
        this.boss = new NioEventLoopGroup(1);
        this.worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).option(ChannelOption.SO_KEEPALIVE, false).option(ChannelOption.SO_REUSEADDR, true).childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new HttpServerCodec(), new ChunkedWriteHandler(), new HttpObjectAggregator(65536), new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS), authHandler, messageHandler);
            }
        });
        try {
            ChannelFuture channelFuture = bootstrap.bind(7777).sync();
//            channelFuture.channel().closeFuture().sync();
            ThreadTaskUtil.getScheduledExecutorService().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    ServerUserManager.getInstance().invalidCheck();
                }
            }, 1, 10, TimeUnit.SECONDS);

            ThreadTaskUtil.getScheduledExecutorService().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    ServerUserManager.getInstance().broadcastPing();
                }
            }, 1, 3, TimeUnit.SECONDS);

            ThreadTaskUtil.getScheduledExecutorService().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    webSocketService.broadcast("广播数据啦");
                }
            }, 1, 15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void destory() {
        if (boss != null) {
            boss.shutdownGracefully();
        }

        if (worker != null) {
            worker.shutdownGracefully();
        }
    }
}
