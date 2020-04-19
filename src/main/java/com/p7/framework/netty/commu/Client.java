package com.p7.framework.netty.commu;

import com.p7.framework.netty.commu.codec.JsonSerializable;
import com.p7.framework.netty.commu.codec.NettyEncoder;
import com.p7.framework.netty.commu.handler.MessageClientHandler;
import com.p7.framework.netty.util.InitUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Date;
import java.util.Scanner;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-04-17 17:49
 **/
public class Client {

    public static void main(String[] args) {
        EventLoopGroup group = InitUtil.initWorkerEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        final NettyEncoder nettyEncoder = new NettyEncoder();
        final MessageClientHandler messageHandler = new MessageClientHandler();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringDecoder(), nettyEncoder, messageHandler);
            }
        });
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7777);
        try {
            ChannelFuture sync = channelFuture.sync();

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                Channel channel = channelFuture.channel();
                Command command = Command.createCommand(new Header("from", "to", new Date()));
                command.setBody(msg.getBytes(JsonSerializable.CHARSET_UTF8));
                channel.writeAndFlush(command);
            }
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}
