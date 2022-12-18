package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocatorMetricProvider;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 使用ByteBufProcessor来寻找 \r
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_9_ByteBufProcessor {

    public void findByteBuf() {
        ByteBuf buffer = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));
        int index = buffer.forEachByte(ByteBufProcessor.FIND_CR);
    }
}
