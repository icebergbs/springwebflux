package cn.bingshan.echonettyserver.controller;

import io.netty.channel.*;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *  使用EventLoop调度任务
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N7_3_EventLoop   {
    private void shcedule() {
        Channel channel = (Channel) new ChannelInboundHandlerAdapter();
        ScheduledFuture<?> future = channel.eventLoop().schedule(
                new Runnable() {  //创建一个Runnbel以供调度稍后执行
                    @Override
                    public void run() {  //要执行的代码
                        System.out.println("60 seconds later");
                    }
        }, 60, TimeUnit.SECONDS); // 调度任务在从现在开始的60秒之后执行
    }
}

