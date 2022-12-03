package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author bingshan
 * @date 2022/12/2 21:28
 */
public class N4_5_Channel {

    public void writeToChannel() {
        Channel channel = (Channel) new ChannelInboundHandlerAdapter();
        ByteBuf buf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));  // 创建持有要写数据的ByteBuf
        ChannelFuture cf = channel.writeAndFlush(buf);  // 写数据并冲刷它
        cf.addListener(new ChannelFutureListener() {  //添加ChannelFutureListener以便在写操作完成后接收通知
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {   // 写操作完成，并且没有错误发生
                    System.out.println("Write successful");
                } else {
                    System.out.println("Write error");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }

    public void mutilThreadUserOneChannel() {
        Channel channel = (Channel) new ChannelInboundHandlerAdapter();
        ByteBuf buf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")).retain();  // 创建持有要写数据的ByteBuf
        Runnable writer = new Runnable() {  //创建将数据写到Channel的Runnable
            @Override
            public void run() {
                channel.writeAndFlush(buf.duplicate());
            }
        };
        Executor executor = Executors.newCachedThreadPool();  // 获取到线程池Executor的引用
        //write in one thread
        executor.execute(writer);  //递交写任务给线程以便在某个线程中执行

        // write in another thread
        executor.execute(writer);  // 递交另一个写任务以便在另一个线程中执行

    }

}
