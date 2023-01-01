package cn.bingshan.echonettyserver.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * CharToByteEncoder 类
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N10_9_CharToByteEncoder extends MessageToByteEncoder<Character> { // 扩展 MessageToByteEncoder


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Character character, ByteBuf byteBuf) throws Exception {
        byteBuf.writeChar(character); // 将Characet解码为char,并将其写入到出站ByteBuf中
    }
}

