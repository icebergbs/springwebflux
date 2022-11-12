package cn.bingshan.echonettyserver.controller;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 *  代码清单 2-2
 * @author bingshan
 * @date 2022/11/6 21:12
 */
public class N2_2_EchoServer {

    private static int port = 8010;

    public N2_2_EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
         new N2_2_EchoServer(port).start();  //  调用服务器的 start()
    }

    public static void start() throws InterruptedException {
        final N2_1_EchoServerHandler serverHandler = new N2_1_EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();    // 1.创建 EventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap();    // 2.创建ServerBootstrap
            b.group(group)
                    .channel(NioServerSocketChannel.class)    // 3.指定所使用的NIO 传输Channel
                    .localAddress(new InetSocketAddress(port))    // 4.使用指定的端口设置套接字地址
                    .childHandler(new ChannelInitializer<SocketChannel>() {     // 5.添加一个 EchoServerHandler 到子Channel的ChannelPipeline
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(serverHandler);    // EchoServerHandler 被标注为@Shareable, 所以我们可以总是使用同样的实例
                        }
                    });
            ChannelFuture f = b.bind().sync();   // 6.异步地绑定服务器调用sync()方法阻塞等待直到绑定完成
            f.channel().closeFuture().sync();    // 7.获取Channel的CloseFuture, 并且阻塞当前线程直到它完成
        } finally {
            group.shutdownGracefully().sync();    // 8.关闭EventLoopGroup. 释放所以的资源
        }
    }

    /**
     *   在 2. 处创建了一个ServerBootstrap实例。因为你正在使用NIO传输，所以你指定了NioEventLoopGroup 1. 来接受和处理新的连接，
     * 并且将Channel的类型指定为NioServerSocketChannel 3.  在此之后设置端口，服务器将绑定到这个地址以监听新的连接请求。
     *   在 5. 处使用了一个特殊的类 -- ChannelInitializer. 这是关键。当一个新的连接被接受时，一个新的子Channel将会被创建，
     * 而ChannelInitializer将会把一个你的EchoServerHandler的实例添加到该Channel的Channel的ChannelPipeline中。
     * 这个ChannelHandler将会收到有关入站消息的通知。
     *   虽然NIO是可伸缩的， 但是其适当的尤其是关于多线程处理的配置并不简单。 Netty的设计封装了大部分的复杂性。
     *   在6. 绑定了服务器，并等待绑定完成。(对sync()的调用将导致当前Thread阻塞，一直到绑定操作完成为止)
     *   在7. 该应用程序将会阻塞等待直到服务器的Channel关闭（因为调用了 sync()）。
     *   在8. 然后你将可以关 EventLoopGroup ， 并释放所有的资源，包括所有被创建的线程。
     *
     */
}
