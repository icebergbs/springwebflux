package cn.bingshan.echonettyserver.controller;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *  可共享的 ChannelHandler
 * @author bingshan
 * @date 2022/12/13 21:50
 */
@ChannelHandler.Sharable  //使用注解标注
public class N6_10_SharableHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Channel read message: " + msg);
        ctx.fireChannelRead(msg);  //记录方法调用，并转发给下一个ChannelHandler
    }
}

