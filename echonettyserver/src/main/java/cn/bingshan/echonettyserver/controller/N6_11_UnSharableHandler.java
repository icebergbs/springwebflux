package cn.bingshan.echonettyserver.controller;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *   @Sharable的错误用法
 * @author bingshan
 * @date 2022/12/13 21:50
 */
@ChannelHandler.Sharable  //使用注解标注
public class N6_11_UnSharableHandler extends ChannelInboundHandlerAdapter {
    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        count++;  //将count字段的值加1
        System.out.println("Channel read called the " + count + " time");
        ctx.fireChannelRead(msg);  //记录方法调用，并转发给下一个ChannelHandler
    }
}

