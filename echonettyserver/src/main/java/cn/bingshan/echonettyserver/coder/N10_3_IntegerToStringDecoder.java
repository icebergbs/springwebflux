package cn.bingshan.echonettyserver.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * IntegerToStringDecoder 类
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N10_3_IntegerToStringDecoder extends MessageToMessageDecoder<Integer> { // 扩展 MessageToMessageDecoder<Integer>


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Integer integer, List<Object> list) throws Exception {
        list.add(String.valueOf(integer));  //将Integer消息转换为它的String表示，并将其添加到输出的List中
    }
}
