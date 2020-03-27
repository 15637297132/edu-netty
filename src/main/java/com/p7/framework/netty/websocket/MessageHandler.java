package com.p7.framework.netty.websocket;

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
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            String evtMsg = "";
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    evtMsg = "读空闲";
                    break;
                case WRITER_IDLE:
                    evtMsg = "写空闲";
                    break;
                case ALL_IDLE:
                    evtMsg = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + " 超时，超时类型：" + evtMsg);
            super.userEventTriggered(ctx, evt);
        }
    }
}
