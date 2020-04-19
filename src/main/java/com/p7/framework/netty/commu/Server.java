package com.p7.framework.netty.commu;

import com.p7.framework.netty.commu.codec.NettyDecoder;
import com.p7.framework.netty.commu.handler.MessageServerHandler;
import com.p7.framework.netty.util.InitUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;


/**
 * @author Yangzhen
 * @Description
 * @date 2020-04-14 16:32
 **/
public class Server {

    public static void main(String[] args) {
        EventLoopGroup boos = InitUtil.initBoosEventLoopGroup();
        EventLoopGroup worker = InitUtil.initWorkerEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        final MessageServerHandler messageHandler = new MessageServerHandler();
        try {
            serverBootstrap.group(boos, worker);
            InitUtil.setServerChannel(serverBootstrap);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024).option(ChannelOption.SO_REUSEADDR, true).childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new StringEncoder(), new NettyDecoder(), messageHandler);
                }
            });

            InitUtil.sync(InitUtil.serverStart(serverBootstrap, 7777));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            boos.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
