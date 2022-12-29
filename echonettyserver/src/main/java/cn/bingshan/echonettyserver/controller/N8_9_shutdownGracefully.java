package cn.bingshan.echonettyserver.controller;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

/**
 *  优雅关闭
 * @author bingshan
 * @date 2022/11/6 21:12
 */
public class N8_9_shutdownGracefully {

    private static int port = 8010;

    public N8_9_shutdownGracefully(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
         new N8_9_shutdownGracefully(port).start();  //  调用服务器的 start()
    }

    public static void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();    // 创建一个Bootstrap类的实例以创建客户端Channel并连接它们
        b.group(group)  //设置EventLoopGroup,其将提供用于处理Channel事件的EventLoop
                .channel(NioSocketChannel.class)    // 指定要使用的Channel实现
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {  //设置用于处理已被接受的子Channel的I/O和数据的ChannelInboundHandler


                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                        // Do something with the packet
                    }
                });
        ChannelFuture future = b.bind(new InetSocketAddress(port)); //
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

        Future<?> future1 = group.shutdownGracefully();  // 将释放所有的资源，并且关闭所有的当前正在使用中的Channel
        // block until the group has shutdown
        future1.syncUninterruptibly();


    }


}
