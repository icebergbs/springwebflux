package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 *  通过ChannelHandlerContext 访问 ChannelPipeline
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N6_7_ChannelHandlerContext {

    public void channel() {
//        ChannelHandlerContext ctx = new ChannelHandlerContext() {};
////        ChannelPipeline pipeline = ctx.pipeline(); //获取与ChannelHandlerContext 相关联的ChannelPipeline的引用
////        pipeline.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));  //通过ChannelPipeline写入缓存区


    }

}

