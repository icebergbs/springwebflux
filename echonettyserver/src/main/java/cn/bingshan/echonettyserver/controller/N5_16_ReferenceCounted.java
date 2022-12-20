package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *  释放引用计数的对象
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_16_ReferenceCounted {

    public static void main(String[] args) {
        referenceCounted();
    }

    public static void referenceCounted() {
        Channel channel = (Channel) new ChannelInboundHandlerAdapter();
        ByteBufAllocator allocator = channel.alloc(); //从Channel获取ByteBufAllocator
        //....
        ByteBuf buf = allocator.directBuffer(); //从ByteBufAllocator分配一个ByteBuf
        boolean released = buf.release(); // 减少到该对象的活动引用。当减少到0时， 该对象被释放，并且该方法返回true
    }
}
