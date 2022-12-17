package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 使用ByteBuffer的复合缓冲区模式
 *    : 分配和复制操作，以及伴随着对数据管理的需要，使得这个版本的实现效率低下而且笨拙。
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_3_ByteBuffer {

    public void writeToChannel() {
         // User an array to hold the message parts
        ByteBuffer header = ByteBuffer.allocate(10);
        ByteBuffer body = ByteBuffer.allocate(10);

        ByteBuffer[] message = new ByteBuffer[] {header, body};
        // Create a new ByteBuffer and use copy to merge the header and body
        ByteBuffer message2 =
                ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }
}
