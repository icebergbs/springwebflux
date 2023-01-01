package cn.bingshan.echonettyserver.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * ShortToByteEncoder 类
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N10_5_ShortToByteEncoder extends MessageToByteEncoder<Short> { // 扩展 MessageToByteEncoder


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Short aShort, ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(aShort); //将Short写入ByteBuf中
    }
}

