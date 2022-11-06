package cn.bingshan.echonettyserver.controller;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.local.LocalChannel;

import java.net.InetSocketAddress;

/**
 * @author bingshan
 * @date 2022/11/6 15:56
 */
public class N1_3_ChannelFuture {

    /**
     * 1-3 异步地建立连接
     *   展示了一个ChannelFuture作为一个 I/O 操作的一部分返回的例子。
     *   这里，connect()方法将会直接返回，而不会阻塞，该调用将会在后台完成。这究竟什么时候会发生
     *   则取决于若干的因素，但这个关注点已经从代码中抽象出来了。因为线程不用阻塞以等待对应的
     *   操作完成， 所以它可以同时做其他的工作，从而更加有效地利用资源
     * @param args
     */

    public static void main(String[] args) {
        Channel channel = new LocalChannel();
        //Does not block
        ChannelFuture future = channel.connect(
                new InetSocketAddress("192.168.0.1", 25));  // | 异步地连接到远程节点
    }
}
