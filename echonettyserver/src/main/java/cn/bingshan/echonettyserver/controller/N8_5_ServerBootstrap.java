package cn.bingshan.echonettyserver.controller;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 *  引导服务器
 * @author bingshan
 * @date 2022/11/6 21:12
 */
public class N8_5_ServerBootstrap {

    private static int port = 8010;

    public N8_5_ServerBootstrap(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
         new N8_5_ServerBootstrap(port).start();  //  调用服务器的 start()
    }

    public static void start() throws InterruptedException {
        final N2_1_EchoServerHandler serverHandler = new N2_1_EchoServerHandler();

            ServerBootstrap b = new ServerBootstrap();    // 创建ServerBootstrap 以创建ServerSocketChannel,并绑定它
            b.group(new NioEventLoopGroup(), new NioEventLoopGroup())  //设置EventLoopGroup,其将提供用于处理Channel事件的EventLoop
                    .channel(NioServerSocketChannel.class)    // 指定要使用的Channel实现
                    .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {  //设置用于处理已被接受的子Channel的I/O和数据的ChannelInboundHandler
                        ChannelFuture connectFuture;

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            Bootstrap bootstrap = new Bootstrap(); //创建一个Bootstrap类的实例以链接到远程主机
                            bootstrap.channel(NioSocketChannel.class).handler(  // 为入站I/O设置ChannelInboundHandler
                                    new SimpleChannelInboundHandler<ByteBuf>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                            System.out.println("Received data");
                                        }
                                    }
                            );
                            bootstrap.group(ctx.channel().eventLoop()); // 使用与分配给已被接受的子Channel相同的EventLoop
                            connectFuture = bootstrap.connect(
                                    new InetSocketAddress("www.manning.com", 80)  //连接到远程节点
                            );
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                            if (connectFuture.isDone()) {
                                // do something with the data  //当连接完成时，执行一些数据操作（如代理）
                            }
                        }
                    });
            ChannelFuture future = b.bind(new InetSocketAddress(port)); //通过配置好的ServerBootstrap绑定该ServerSocketChannel
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("Server bound");
                    } else {
                        System.out.println("Bing attempt failed");
                        channelFuture.cause().printStackTrace();
                    }
                }
            });


    }


}
