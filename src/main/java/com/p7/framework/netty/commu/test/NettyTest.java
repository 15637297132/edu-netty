package com.p7.framework.netty.commu.test;

import com.p7.framework.netty.commu.Command;
import com.p7.framework.netty.commu.Header;
import com.p7.framework.netty.commu.codec.JsonSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-04-17 10:05
 **/
public class NettyTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyTest.class);

    public static void main(String[] args) throws IOException {
        testPut();
        testGet();
    }

    public static void testPut() throws IOException {
        File file = new File("D:/0");
        if (!file.exists()) {
            file.createNewFile();
        }

        FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();

        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 1024 * 2);

        for (int i = 0; i < 1; i++) {
            Command command = getCommand("测试发送数据" + i);
            ByteBuffer buffer = command.encode();
            mappedByteBuffer.put(buffer);
            byte[] array = buffer.array();
            System.out.println(Arrays.toString(array));
        }
        mappedByteBuffer.force();
    }

    public static void testGet() throws IOException {
        File file = new File("D:/0");
        FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 1024 * 2);
        ByteBuffer slice = mappedByteBuffer.slice();
        int length = slice.getInt();
        byte[] bytes = new byte[length - 4];
        slice.get(bytes, 0, length - 4);
        Command command = Command.decode(bytes);
        System.out.println(command.getHeader().toString());
        System.out.println(new String(command.getBody()));
    }

    public static void testCommand() {
        Command command = getCommand("testCommand");
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


    public static Command getCommand(String msg) {
        Header header = new Header();
        header.setFrom("张三");
        header.setTo("李四");
        header.setDate(new Date());
        Command command = Command.createCommand(header);
        command.setBody(msg.getBytes(JsonSerializable.CHARSET_UTF8));
        return command;
    }
}
