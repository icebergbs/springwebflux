package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 复制一个ByteBuf
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_11_ByteBuf_copy {

    public static void main(String[] args) {
        copyByteBuf();
    }

    public static void copyByteBuf() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buffer = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);  //创建一个用于保存给定字符串的字节的ByteBuf
        ByteBuf copy = buffer.copy(0, 15);  //创建该ByteBuf从索引0开始到索引15结束的分段的副本
        System.out.println(copy.toString(utf8));  // 将打印“Netty in Action"
        buffer.setByte(0, (byte)'J');   //更新索引0处的字节
        assert buffer.getByte(0) != copy.getByte(0);  // 将会成功，因为数据不是共享的
    }
}
