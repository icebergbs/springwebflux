package cn.bingshan.echonettyserver.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * ByteToCharDecoder 类
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N10_8_ByteToCharDecoder extends ByteToMessageDecoder { // 扩展 ByteToMessageDecoder


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        while (byteBuf.readableBytes() >= 2) {
            list.add(byteBuf.readChar()); //将一个或者多个Character对象添加到传出的List中。
        }
    }
}

