package cn.bingshan.echonettyserver.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * ToIntegerDecoder1 类扩展了 ReplayingDecoder
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N10_2_ToIntegerDecoder2 extends ReplayingDecoder<Void> { // 扩展ReplayingDecoder类， 已将字节解码为特定的格式

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception { // 传入的ByteBuf是ReplayingDecoderByteBuf
         list.add(byteBuf.readInt());  // 从入站ByteBuf中读取一个int, 并将其添加到解码消息的List中
    }
}
