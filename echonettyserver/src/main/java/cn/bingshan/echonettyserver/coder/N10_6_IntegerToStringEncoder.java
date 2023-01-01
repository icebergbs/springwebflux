package cn.bingshan.echonettyserver.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * ShortToByteEncoder 类
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N10_6_IntegerToStringEncoder extends MessageToMessageEncoder<Integer> { // 扩展 MessageToMessageEncoder


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, List<Object> list) throws Exception {
        list.add(String.valueOf(integer)); //将Integer转换为String, 并将其添加到List中
    }
}

