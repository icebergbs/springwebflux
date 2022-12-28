package com.bingshan.echonettyclient.controller;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 引导一个客户端
 * @author bingshan
 * @date 2022/11/12 17:07
 */
public class N8_1_Bootstrap {

//    private final String host;
//    private final int port;

    public N8_1_Bootstrap() {
//        this.host = host;
//        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();  // 创建Bootstrap的实例以创建和连接新的客户端Channel
        b.group(group)                  // 指定EventLoopGroup, 提供用于处理Channel事件的EventLoop
                .channel(NioSocketChannel.class)  // 指定要使用的Channel实现
                .handler(new SimpleChannelInboundHandler<ByteBuf>() { //设置用于Channel事件和数据的ChanelInboundHandler
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                });
        ChannelFuture f = b.connect(new InetSocketAddress("127.0.0.1", 8010)); //连接到远程主机
        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    System.out.println("Connection established");
                } else {
                    System.out.println("Connection attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });

    }

    public static void main(String[] args) throws InterruptedException {
        new N8_1_Bootstrap().start();
    }
}
