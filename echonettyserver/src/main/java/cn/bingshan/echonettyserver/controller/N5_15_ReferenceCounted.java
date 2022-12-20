package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *  引用计数
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_15_ReferenceCounted {

    public static void main(String[] args) {
        referenceCounted();
    }

    public static void referenceCounted() {
        Channel channel = (Channel) new ChannelInboundHandlerAdapter();
        ByteBufAllocator allocator = channel.alloc(); //从Channel获取ByteBufAllocator
        //....
        ByteBuf buf = allocator.directBuffer(); //从ByteBufAllocator分配一个ByteBuf
        assert buf.refCnt() == 1; //检查引用计数是否为预期的1
    }
}
