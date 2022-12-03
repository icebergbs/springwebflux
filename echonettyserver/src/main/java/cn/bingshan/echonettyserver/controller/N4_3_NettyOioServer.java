package cn.bingshan.echonettyserver.controller;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioByteStreamChannel;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 使用Netty的阻塞网络处理
 * @author bingshan
 * @date 2022/12/1 21:38
 */
public class N4_3_NettyOioServer {
    public void server(int port) throws InterruptedException {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();  // 创建ServerBootstrap
            b.group(group)
                    .channel(OioServerSocketChannel.class) // 使用OioEventLoopGroup以 允许阻塞模式（旧I/O)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {  // 指定ChannelInitializer,对于每个已接受的连接都调用它

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ChannelInboundHandlerAdapter(){  //添加一个ChannelInboundHandlerAdapter以拦截和处理事件
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            ctx.writeAndFlush(buf.duplicate())
                                                    .addListener(ChannelFutureListener.CLOSE); //将消息写到客户端，并添加ChannelFutureListener，以便消息一被写完就关闭连接
                                        }
                                    });
                        }
                    });
            ChannelFuture f = b.bind().sync(); //绑定服务器以接收连接
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync(); // 释放所有的资源
        }
    }

}
