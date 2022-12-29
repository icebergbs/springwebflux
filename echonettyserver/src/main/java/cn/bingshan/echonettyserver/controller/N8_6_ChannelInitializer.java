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

import java.net.InetSocketAddress;

/**
 *  引导和使用ChannelInitializer
 * @author bingshan
 * @date 2022/11/6 21:12
 */
public class N8_6_ChannelInitializer {

    private static int port = 8010;

    public N8_6_ChannelInitializer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
         new N8_6_ChannelInitializer(port).start();  //  调用服务器的 start()
    }

    public static void start() throws InterruptedException {
        final N2_1_EchoServerHandler serverHandler = new N2_1_EchoServerHandler();

            ServerBootstrap b = new ServerBootstrap();    // 创建ServerBootstrap 以创建和绑定新的Channel
            b.group(new NioEventLoopGroup(), new NioEventLoopGroup())  //设置EventLoopGroup,其将提供用于处理Channel事件的EventLoop
                    .channel(NioServerSocketChannel.class)    // 指定要使用的Channel实现
                    .childHandler(new ChannelInitializerImpl()); //注册一个ChannelInitializerImpl 的实例来设置ChannelPipeline
            ChannelFuture future = b.bind(new InetSocketAddress(port)); // 绑定到地址
            future.sync();


    }


}

final class ChannelInitializerImpl extends ChannelInitializer<Channel> {  //用于设置ChannelPipeline的自定义ChannelInitializerImpl实现
    @Override
    protected void initChannel(Channel ch) throws Exception { // 将所需的ChannelHandler添加到ChannelPipeline
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
    }
}
