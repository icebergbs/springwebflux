package cn.bingshan.echonettyserver.controller;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 被回调触发的 ChannelHandler
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N1_2_ConnectHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当一个新的连接已经被建立时，channelActive(ChannelHandlerContext)将会被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client " + ctx.channel().remoteAddress() + " connected");  //打印一条信息
    }
}
