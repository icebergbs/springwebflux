package cn.bingshan.echonettyserver.controller;

import io.netty.channel.*;

/**
 *   添加ChannelFutureListener到ChannelFuture
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N6_13_ChannelFutureListener {

     public void failed() {
         Channel channel = (Channel) new ChannelOutboundHandlerAdapter();
         ChannelFuture future = channel.write("msg");
         future.addListener(new ChannelFutureListener() {
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

