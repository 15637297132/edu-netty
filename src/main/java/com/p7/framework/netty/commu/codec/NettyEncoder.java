package com.p7.framework.netty.commu.codec;

import com.p7.framework.netty.commu.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-04-15 13:38
 **/
@ChannelHandler.Sharable
public class NettyEncoder extends MessageToByteEncoder<Command> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Command command, ByteBuf out) throws Exception {

        try {
            ByteBuffer encode = command.encode();
            out.writeBytes(encode);
        } catch (Exception e) {
            LOGGER.error("encode exception, " + e.getMessage());
            ctx.close();
        }
    }
}
