package com.bingshan.echonettyclient.controller;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 代码清单2-4 客户端的主类
 * @author bingshan
 * @date 2022/11/12 17:07
 */
public class N2_4EchoClient {

//    private final String host;
//    private final int port;

    public N2_4EchoClient() {
//        this.host = host;
//        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();  // 创建Bootstrap
            b.group(group)                  // 指定EventLoopGroup以处理客户端事件； 需要适用于NIO的实现
                    .channel(NioSocketChannel.class)  // 使用于NIO传输的Channel类型
                    .remoteAddress(new InetSocketAddress("127.0.0.1", 8010))  //设置服务器的InetSocketAddress
                    .handler(new ChannelInitializer<SocketChannel>() {    // 在创建Channel时， 向ChannelPipeline中添加一个EchoClientHandler实例
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new N2_3EchoClientHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();  // 连接到远程节点，阻塞等待直到连接完成
            f.channel().closeFuture().sync();      // 阻塞，直到Channel关闭
        } finally {
            group.shutdownGracefully().sync();    // 关闭线程池并且释放所有的资源
        }
    }

/*
  和之前一样，使用了NIO传输。 注意，可以在客户端和服务器上分别使用不同的传输。 例如，在服务器使用NIO,在客户端使用OIO。 第四章将探讨影响你选择适用于
特定用例的特定传输的各种因素和场景。
 */
    public static void main(String[] args) throws InterruptedException {
        new N2_4EchoClient().start();
    }
}
