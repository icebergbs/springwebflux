package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 访问 CompositeByteBuf  中的数据
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_5_CompositeByteBuf_Read {

    public void writeToChannel() {
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        int length = compBuf.readableBytes();  //获得可读字节数
        byte[] array = new byte[length]; // 分配一个具有可读字节数长度的新数组
        compBuf.getBytes(compBuf.readerIndex(), array); // 将字节读到该数组中
        //handleArray(array, 0, array.length);  // 使用偏移量和长度作为参数使用该数组

    }
}
