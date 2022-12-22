package cn.bingshan.echonettyserver.controller;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 *  使用SimpleChannelInboundHandler
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N6_2_SimpleDiscardHander extends SimpleChannelInboundHandler<Object> {  //扩展了SimpleChannelInboundHandler

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //No need to do anyting special  // 不需要任何显式的资源释放
    }
}
