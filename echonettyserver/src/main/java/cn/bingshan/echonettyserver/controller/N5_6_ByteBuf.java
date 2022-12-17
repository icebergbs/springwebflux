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
public class N5_6_ByteBuf {

    public void readByteBuf() {
        ByteBuf buffer = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }
        /**
         * 需要注意的是， 使用那些需要一个索引值参数的方法（的其中）之一来访问数据既不会改变readIndex 也不会改变 writeIndex。
         * 如果有需要，也可以通过调用readerIndex(index) 或者writerIndex(index)来手动移动这两者。
         */
    }
}
