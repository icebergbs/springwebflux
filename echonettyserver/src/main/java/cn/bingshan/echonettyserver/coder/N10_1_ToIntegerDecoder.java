package cn.bingshan.echonettyserver.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * ToIntegerDecoder 类扩展了 ByteToMessageDecoder
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N10_1_ToIntegerDecoder extends ByteToMessageDecoder{ // 扩展ByteToMessageDecoder类， 已将字节解码为特定的格式

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= 4) { //检查是否至少有4字节可读（一个int的字节长度）
            list.add(byteBuf.readInt()); // 从入站ByteBuf中读取一个int, 并将其添加到解码消息的List中。
        }
    }
}
