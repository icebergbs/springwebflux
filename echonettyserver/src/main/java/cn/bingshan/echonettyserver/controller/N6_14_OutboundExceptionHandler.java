package cn.bingshan.echonettyserver.controller;

import io.netty.channel.*;

/**
 *   添加ChannelFutureListener到  ChannelPromise
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N6_14_OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(!channelFuture.isSuccess()) {
                    channelFuture.cause().printStackTrace();
                    channelFuture.channel().close();
                }
            }
        });
    }
}

