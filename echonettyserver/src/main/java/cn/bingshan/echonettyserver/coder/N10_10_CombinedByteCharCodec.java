package cn.bingshan.echonettyserver.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * CharToByteEncoder 类
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N10_10_CombinedByteCharCodec extends CombinedChannelDuplexHandler<N10_8_ByteToCharDecoder, N10_9_CharToByteEncoder> { //通过该解码器和编码器实现参数化CombinedByteCharCOdec


    public N10_10_CombinedByteCharCodec() {
        super(new N10_8_ByteToCharDecoder(), new N10_9_CharToByteEncoder()); // 将委托实例传递给父类
    }
}

