package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 读取所有数据
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_7_ByteBuf_Read {

    public void readByteBuf() {
        ByteBuf buffer = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));
        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
    }
}
