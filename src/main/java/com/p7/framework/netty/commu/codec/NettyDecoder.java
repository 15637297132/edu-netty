package com.p7.framework.netty.commu.codec;

import com.p7.framework.netty.commu.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-04-17 10:49
 **/
public class NettyDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyDecoder.class);

    private static final int FRAME_MAX_LENGTH = 16777216;

    public NettyDecoder() {
        super(FRAME_MAX_LENGTH, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);

            if (frame == null) {
                return null;
            }

            return Command.decode(frame.nioBuffer());
        } catch (Exception e) {
            LOGGER.error("decode exception, " + e.getMessage());
        } finally {
            if (null != frame) {
                frame.release();
            }
        }

        return null;
    }

}
