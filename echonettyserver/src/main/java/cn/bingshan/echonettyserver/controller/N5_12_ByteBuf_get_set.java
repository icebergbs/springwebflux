package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 *  get() 和 set() 方法的用法
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_12_ByteBuf_get_set {

    public static void main(String[] args) {
        getAndSetByteBuf();
    }

    public static void getAndSetByteBuf() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buffer = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);  //创建一个用于保存给定字符串的字节的ByteBuf
        System.out.println((char) buffer.getByte(0)); // 打印第一个字符‘N'
        int readerIndex = buffer.readerIndex();  // 存储当前的readerIndex  和 writerIndex
        int writerIndex = buffer.writerIndex();
        buffer.setByte(0, (byte)'B'); // 将索引0处的字节更新为字符’B'
        System.out.println(buffer.getByte(0)); // 打印第一个字符，现在是‘B'
        assert readerIndex == buffer.readerIndex(); //将会成功，因为这些操作并不会修改相应的索引
        assert writerIndex == buffer.writerIndex();
    }
}
