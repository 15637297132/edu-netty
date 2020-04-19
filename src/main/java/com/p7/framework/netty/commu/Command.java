package com.p7.framework.netty.commu;

import com.p7.framework.netty.commu.codec.JsonSerializable;

import java.nio.ByteBuffer;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-04-16 13:29
 **/
public class Command {

    private Header header;

    private byte[] body;

    public static Command createCommand(Header header) {
        Command command = new Command();
        command.header = header;
        return command;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public ByteBuffer encode() {
        // 4字节的byte = 1个int类型，计算字节数组的长度 8 = 4 + 4，两个字节的长度分别表示总长度和头部长度
        int length = 4;

        // 序列化头部信息
        byte[] headerData = JsonSerializable.encode(this.header);

        // 头部数据的长度
        length += headerData.length;

        // 数据长度
        if (this.body != null) {
            length += this.body.length;
        }

        ByteBuffer result = ByteBuffer.allocate(4 + length);

        result.putInt(length);

        result.putInt(headerData.length);

        result.put(headerData);

        if (this.body != null) {
            result.put(this.body);
        }

        result.flip();
        return result;
    }

    public static Command decode(final byte[] array) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        return decode(byteBuffer);
    }

    public static Command decode(ByteBuffer byteBuffer) {

        int length = byteBuffer.limit();
        int headerLength = byteBuffer.getInt() & 0xFFFFFF;

        byte[] headerBytes = new byte[headerLength];
        byteBuffer.get(headerBytes, 0, headerLength);
        Command command = new Command();
        Header header = JsonSerializable.decode(headerBytes, Header.class);
        command.setHeader(header);

        length = length - headerLength - 4;

        byte[] body = null;
        if (length > 0) {
            body = new byte[length];
            byteBuffer.get(body, 0, length);
        }
        command.setBody(body);
        return command;
    }
}
