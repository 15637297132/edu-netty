package com.p7.framework.netty.commu.test;

import com.p7.framework.netty.commu.Command;
import com.p7.framework.netty.commu.Header;
import com.p7.framework.netty.commu.codec.JsonSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-04-17 10:05
 **/
public class NettyTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyTest.class);

    public static void main(String[] args) {
        testCommand();
    }

    public static void testCommand() {
        String msg = "测试消息，发送了什么鬼";
        Header header = new Header();
        header.setFrom("张三");
        header.setTo("李四");
        header.setDate(new Date());
        Command command = Command.createCommand(header);
        command.setBody(msg.getBytes(JsonSerializable.CHARSET_UTF8));
        ByteBuffer buffer = command.encode();
        LOGGER.info(Arrays.toString(buffer.array()));
        // 过滤消息总长度的前四个字节
        buffer.getInt();

        byte[] bytes = new byte[buffer.limit() - 4];
        buffer.get(bytes, 0, buffer.limit() - 4);

        Command decodeCommand = Command.decode(bytes);
        LOGGER.info(decodeCommand.getHeader().toString());
        LOGGER.info(new String(decodeCommand.getBody(), JsonSerializable.CHARSET_UTF8));
    }
}
