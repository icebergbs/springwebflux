package cn.bingshan.echonettyserver.controller;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 *  缓存到 ChannelHandlerContext  的引用
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N6_9_WriteHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;  // 存储到ChannelHandlerContext的引用以供稍后使用
    }

    public void send(String msg) { //使用之前存储的到ChannelHandlerContext的引用来发送消息
        ctx.writeAndFlush(msg);
    }
}

