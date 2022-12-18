package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 对ByteBuf进行切片
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_10_ByteBuf_slice {

    public static void main(String[] args) {
        sliceByteBuf();
    }

    public static void sliceByteBuf() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buffer = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);  //创建一个用于保存给定字符串的字节的ByteBuf
        ByteBuf sliced = buffer.slice(0, 15);  //创建该ByteBuf从索引0开始到索引15结束的一个新切片
        System.out.println(sliced.toString(utf8));  // 将打印“Netty in Action"
        buffer.setByte(0, (byte)'J');   //更新索引0处的字节
        assert buffer.getByte(0) == sliced.getByte(0);  // 将会成功，因为数据是共享的，对其中一个所做的更改对另外一个也是可见的
    }
}
