package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 *  释放消息资源
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N6_1_DiscardHandler extends ChannelInboundHandlerAdapter{  //扩展了ChannelInboundHandlerAdapter

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ReferenceCountUtil.release(msg); // 丢弃已接受的消息
    }
}
