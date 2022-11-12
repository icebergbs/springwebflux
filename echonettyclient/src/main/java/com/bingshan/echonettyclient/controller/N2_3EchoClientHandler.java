package com.bingshan.echonettyclient.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 代码清单2-3 客户端的ChannelHandler
 * @author bingshan
 * @date 2022/11/12 16:22
 */
@ChannelHandler.Sharable       //标记该类的实例可以被多个Channel共享
public class N2_3EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));  //当被通知Channel是活跃的时候，发送一条消息
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client received: " +  byteBuf.toString(CharsetUtil.UTF_8));  // 记录已接受消息的转储
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
         cause.printStackTrace();  // 在发生异常时，记录错误并关闭Channel
         ctx.close();
    }

/*
  首先，重写channelActive(), 其将在一个连接建立时被调用。这确保了数据将会被尽可能地写入服务器。
  接下来， 重写channelRead0(). 每当接受数据时，都会调用这个方法。需要注意的是，由服务器发送的消息可能会被分块接收。
也就是说，如果服务器发送了5字节，那么不能保证这5个字节被一次性接收。即使对于这么少的数据，channelRead()也可能会被调用两次。
  作为一个面向流的协议， TCP保证了字节数组将会按照服务器发送它们的顺序被接收。
  重写exceptionCaught(), 记录Throwable,关闭Channel, 在这个场景下，终止到服务器的连接。
 */

/*
  SimpleChannelInboundHandler 与 ChannelInboundHandler
  why 在客户端是 SimpleChannelInboundHandler， 而不是在EchoServerHandler中使用的 ChannelInboundHandlerAdapter？
  这和两个因素的相互作用有关， 业务逻辑如何处理消息以及Netty如何管理资源。
  在客户端，当channelRead0()完成时，你已经有了传入消息，并且已经处理完成了。 当该方法返回时，SimpleChannelInboundHandler 负责释放指向保存该
消息的ByteBuf的内存引用。
  在EchoServerHandler中，你仍然需要将传入消息回送给发送者，而write()操作是异步的一直到channelRead()方法返回后可能仍然没有完成（代码清单2-1）。
为此，EchoServerHandler 扩展了ChannelInboundHandlerAdapter, 其在这个时间点上不会释放消息。
  消息他在EchoServerHandler 的channelReadComplete() 中，当writeAndFlush()被调用时被释放。
 */
}
