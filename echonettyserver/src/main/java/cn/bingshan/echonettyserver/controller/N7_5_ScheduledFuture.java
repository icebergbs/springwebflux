package cn.bingshan.echonettyserver.controller;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *  使用ScheduledFuture 取消任务
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N7_5_ScheduledFuture {
    private void shcedule() {
        Channel channel = (Channel) new ChannelInboundHandlerAdapter();
        ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(
                new Runnable() {  //创建一个Runnbel以供调度稍后执行
                    @Override
                    public void run() {  //要执行的代码
                        System.out.println("Run every 60 seconds");
                    }
        }, 60, 60, TimeUnit.SECONDS); // 调度任务在60秒之后, 并且以后每间隔60秒运行

        // some other code that runs...
        boolean mayInterruptIfRunning = false;
        future.cancel(mayInterruptIfRunning); //取消该任务，防止它再次运行
    }
}

