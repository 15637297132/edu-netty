package com.p7.framework.netty.commu.handler;

import com.p7.framework.netty.commu.Command;
import com.p7.framework.netty.commu.Header;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-04-17 17:44
 **/
@ChannelHandler.Sharable
public class MessageServerHandler extends SimpleChannelInboundHandler<Command> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command msg) throws Exception {

        Header header = msg.getHeader();
        LOGGER.info(header.toString() + " : " + new String(msg.getBody()));

        ctx.writeAndFlush("服务器确认收到").addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                LOGGER.info("已返回确认消息");
            }
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        LOGGER.info(ctx.channel().remoteAddress().toString() + " 上线了");
    }
}
