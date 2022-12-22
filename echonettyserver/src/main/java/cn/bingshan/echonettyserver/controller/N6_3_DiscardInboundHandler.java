package cn.bingshan.echonettyserver.controller;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 *  消费并释放入站消息
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N6_3_DiscardInboundHandler extends ChannelInboundHandlerAdapter {  //扩展了ChannelInboundHandlerAdapter


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ReferenceCountUtil.release(msg); //通过调用ReferenceCountUtil.release() 释放资源
    }
}
