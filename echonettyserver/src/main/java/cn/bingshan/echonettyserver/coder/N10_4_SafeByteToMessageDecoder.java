package cn.bingshan.echonettyserver.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * TooLongFrameException 类
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N10_4_SafeByteToMessageDecoder extends ByteToMessageDecoder { // 扩展 ByteToMessageDecoder 已将字节解码为消息

    private static final int MAX_FARME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readable = byteBuf.readableBytes();
        if (readable > MAX_FARME_SIZE) { //检查缓冲区中是否有超过MAX_FARME_SIZE个字节
            byteBuf.skipBytes(readable); //跳过所有的可读字节，抛出TooLongFrameException并通知ChannelHandler
            throw new TooLongFrameException("Frame too big！");
        }
        // do something
    }
}

