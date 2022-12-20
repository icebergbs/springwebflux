package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 *  ByteBuf 上的read() 和write() 操作
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_13_ByteBuf_read_write {

    public static void main(String[] args) {
        getAndSetByteBuf();
    }

    public static void getAndSetByteBuf() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buffer = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);  //创建一个用于保存给定字符串的字节的ByteBuf
        System.out.println((char) buffer.readByte()); // 打印第一个字符‘N'
        int readerIndex = buffer.readerIndex();  // 存储当前的readerIndex  和 writerIndex
        int writerIndex = buffer.writerIndex();
        buffer.writeByte((byte)'?'); // 将索引0处的 字符’?'追加到缓冲区
        System.out.println(buffer.getByte(0)); // 打印第一个字符，现在是‘B'
        assert readerIndex == buffer.readerIndex();
        assert writerIndex == buffer.writerIndex(); //将会成功，因为 writeByte() 方法移动了writerIndex
    }
}
