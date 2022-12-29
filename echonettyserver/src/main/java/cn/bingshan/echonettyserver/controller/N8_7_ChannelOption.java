package cn.bingshan.echonettyserver.controller;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 *  使用属性值
 * @author bingshan
 * @date 2022/11/6 21:12
 */
public class N8_7_ChannelOption {

    private static int port = 8010;



    public N8_7_ChannelOption(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
         new N8_6_ChannelInitializer(port).start();  //  调用服务器的 start()
    }

    public static void start() throws InterruptedException {
        final AttributeKey<Integer> id = AttributeKey.valueOf("ID"); // 创建一个AttributeKey以标识该属性
        Bootstrap bootstrap = new Bootstrap();  //创建一个Bootstrap类的实例以创建客户端Channel并连接它们
        bootstrap.group(new NioEventLoopGroup())  //设置EventLoopGroup， 其提供了用以处理Channel事件的EventLoop
                .channel(NioSocketChannel.class)  // 指定Channel的实现
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {  // 设置用以处理Channel的I/O以及数据的ChannelInboundHandler
                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        Integer idVale = ctx.channel().attr(id).get(); //使用AttributeKey检索属性以及它的值
                        // do something with the idValue
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Received data");
                    }
                });

        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000); //设置ChannelIotion, 其将在connect()或者bind()被调用时被设置到已经创建的Channel上
        bootstrap.attr(id, 12345); //存储该id 属性
        ChannelFuture future = bootstrap.connect(
                new InetSocketAddress("www.manning.com", 80)  // 使用配置好的Bootstrap实例连接到远程主机
        );
        future.syncUninterruptibly();



    }


}


