package cn.bingshan.echonettyserver.preChannelHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 添加HTTP支持
 * @author bingshan
 * @date 2023/1/3 21:44
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("decoder", new HttpResponseDecoder()); //如果是客户端，则添加 HttpResponseDecoder 以处理来自服务器的响应
            pipeline.addLast("encoder", new HttpRequestEncoder());  //如果是客户端，则添加 HttpRequestEncoder 以向服务器发送请求
        } else {
            pipeline.addLast("decoder", new HttpRequestDecoder());  //如果是服务器，则添加 HttpRequestDecoder 以接受来自客户端的请求
            pipeline.addLast("encoder", new HttpResponseEncoder()); //如果是服务器，则添加 HttpResponseEncoder 以向客户端发送响应
        }
    }
}
