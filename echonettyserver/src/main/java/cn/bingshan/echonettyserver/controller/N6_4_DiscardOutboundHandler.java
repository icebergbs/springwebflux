package cn.bingshan.echonettyserver.controller;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 *  丢弃并释放出站消息
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N6_4_DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {  //扩展了ChannelOutboundHandlerAdapter


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ReferenceCountUtil.release(msg); // 通过使用 release() 释放资源
        promise.setSuccess(); //通知ChannelPromise 数据已经被处理了
    }
}

