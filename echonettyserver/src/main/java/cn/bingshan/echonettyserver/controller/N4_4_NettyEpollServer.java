package cn.bingshan.echonettyserver.controller;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 使用Netty的异步网络处理
 * @author bingshan
 * @date 2022/12/2 20:31
 */
public class N4_4_NettyEpollServer {
    public void server(int port) throws InterruptedException {
        final ByteBuf buf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));
        EventLoopGroup group = new EpollEventLoopGroup();  // 为非阻塞模式使用EpollEventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap();  // 创建ServerBootstrap
            b.group(group).channel(EpollServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {  // 指定ChannelInitializer, 对于每个已接受的连接都调用它
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ChannelInboundHandlerAdapter() {  // 添加ChannelInbound-HandlerAdapter以接收和处理事件
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            ctx.writeAndFlush(buf.duplicate())
                                                    .addListener(ChannelFutureListener.CLOSE);
                                        }
                                    }
                            );
                        }
                    });
            ChannelFuture f = b.bind().sync(); //绑定服务器以接受连接
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();  // 释放所有的资源
        }
    }
}
