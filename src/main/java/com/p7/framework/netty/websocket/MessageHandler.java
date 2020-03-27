package com.p7.framework.netty.websocket;

import com.p7.framework.netty.IdleStateEventUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-03-26 11:00
 **/
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        System.out.println(msg.getClass());
        System.out.println(((TextWebSocketFrame) msg).text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("我收到数据了"));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEventUtil.userEventTriggered(ctx, evt);
    }
}
