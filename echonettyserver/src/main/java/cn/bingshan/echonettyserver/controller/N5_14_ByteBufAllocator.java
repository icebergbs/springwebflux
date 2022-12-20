package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 *  获取一个到ByteBufAllocator的引用
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_14_ByteBufAllocator {

    public static void main(String[] args) {
        byteBufAllocator();
    }

    public static void byteBufAllocator() {
        Channel channel = (Channel) new ChannelInboundHandlerAdapter();
        ByteBufAllocator allocator = channel.alloc();  // 从Channel获取一个到ByteBufAllocator的引用
        // ....
        //ChannelHandlerContext ctx = ...;
        //ByteBufAllocator allocator1 = ctx.alloc(); // 从ChannelHandlerContext获取一个到ByteBufAllocator的引用
    }
}
