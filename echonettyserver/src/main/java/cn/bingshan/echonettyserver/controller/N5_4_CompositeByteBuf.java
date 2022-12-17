package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 使用 CompositeByteBuf 的复合缓冲区模式
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_4_CompositeByteBuf {

    public void writeToChannel() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")); // can be backing or direct
        ByteBuf bodyBuf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")); // can be backing or direct
        messageBuf.addComponents(headerBuf, bodyBuf); // 将ByteBuf 实例追加到ComplsiteByteBuf
        // .......
        messageBuf.removeComponent(0); // remove the header   删除位于索引位置为0 （第一个组件）的ByteBuf
        for (ByteBuf buf : messageBuf) {    // 循环遍历所有的ByteBuf实例
            System.out.println(buf.toString());
        }
    }
}
