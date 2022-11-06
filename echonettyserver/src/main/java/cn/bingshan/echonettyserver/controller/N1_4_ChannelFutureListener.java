package cn.bingshan.echonettyserver.controller;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.local.LocalChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author bingshan
 * @date 2022/11/6 16:04
 */
public class N1_4_ChannelFutureListener {

    /**
     * 代码清单 1-4 显示了如何利用ChannelFutureListener
     *   首先，要连接到远程节点上。然后，要注册一个新的ChannelFutureListener 到对connect()方法的调用所返回的
     *   ChannelFuture上。当该监听器被通知连接已经建立的时候，要检查对应的状态。 如果该操作时成功的，那么将数据写到该Channel.
     *   否则，要从ChannelFuture中检索对应的Throwable。
     * @param args
     */
    public static void main(String[] args) {
        Channel channel = new LocalChannel();
        //Does not block
        ChannelFuture future = channel.connect(
                new InetSocketAddress("192.168.0.1", 25));  //异步地连接到远程节点
        future.addListener(new ChannelFutureListener() {    // 注册一个ChannelFutureListener, 以便在操作完成时获得通知
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (future.isSuccess()) {   // 检查操作地状态
                    ByteBuf buffer = Unpooled.copiedBuffer(   // 如果操作时成功地，则创建一个ByteBuf以持有数据
                            "Hello", Charset.defaultCharset());
                    ChannelFuture wf = future.channel().writeAndFlush(buffer);  // 将数据异步地发送到远程节点。返回一个ChannelFuture
                    //...

                } else {
                    Throwable cause = future.cause();  // 如果发生错误，则访问描述原因地Throwable
                    cause.printStackTrace();
                }
            }
        });
    }
}
