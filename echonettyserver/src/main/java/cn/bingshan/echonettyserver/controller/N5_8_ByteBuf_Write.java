package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 写数据
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_8_ByteBuf_Write {

    public void writeByteBuf() {
        // Fills the writable bytes of a buffer with random integers.
        ByteBuf buffer = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));
        while (buffer.writableBytes() >= 4) {
           buffer.writeInt((int) Math.random());
        }
    }
}
