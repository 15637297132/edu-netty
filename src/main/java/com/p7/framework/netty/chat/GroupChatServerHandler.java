package com.p7.framework.netty.chat;

import com.p7.framework.netty.util.IdleStateEventUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {



    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 只要通道是连接的，一直会轮询到这
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEventUtil.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(simpleDateFormat.format(new Date()) + " 用户 " + channel.remoteAddress() + " 加入聊天");
        channelGroup.add(channel);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.remove(channel);
        channelGroup.writeAndFlush(simpleDateFormat.format(new Date()) + " 用户 " + channel.remoteAddress() + " 离开聊天");
        super.channelUnregistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, final String msg) throws Exception {
        final Channel channel = ctx.channel();

        Iterator<Channel> iterator = channelGroup.iterator();
        while (iterator.hasNext()) {
            Channel next = iterator.next();
            if (channel != next) {
                next.writeAndFlush(channel.remoteAddress() + "说：" + msg);
            } else {
                next.writeAndFlush("自己发送了消息：" + msg);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了~");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}

