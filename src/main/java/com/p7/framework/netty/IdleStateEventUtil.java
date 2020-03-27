package com.p7.framework.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

public class IdleStateEventUtil {

    public static void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
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
            ctx.fireUserEventTriggered(evt);
        }
    }
}
