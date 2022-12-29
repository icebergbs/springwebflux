package cn.bingshan.echonettyserver.controller;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioDatagramChannel;

import java.net.InetSocketAddress;

/**
 *  使用Bootstrap和 DatagramChannel
 * @author bingshan
 * @date 2022/11/6 21:12
 */
public class N8_8_DatagramChannel {

    private static int port = 8010;

    public N8_8_DatagramChannel(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
         new N8_8_DatagramChannel(port).start();  //  调用服务器的 start()
    }

    public static void start() throws InterruptedException {

            Bootstrap b = new Bootstrap();    // 创建一个Bootstrap类的实例以创建客户端Channel并连接它们
            b.group(new OioEventLoopGroup())  //设置EventLoopGroup,其将提供用于处理Channel事件的EventLoop
                    .channel(OioDatagramChannel.class)    // 指定要使用的Channel实现
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() {  //设置用于处理已被接受的子Channel的I/O和数据的ChannelInboundHandler


                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                            // Do something with the packet
                        }
                    });
            ChannelFuture future = b.bind(new InetSocketAddress(port)); //调用bind()，因为该协议是无连接的
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
