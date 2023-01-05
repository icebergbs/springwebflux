package cn.bingshan.echonettyserver.preChannelHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 自动聚会HTTP的消息片段
 * @author bingshan
 * @date 2023/1/4 21:55
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {
    private final boolean isClient;

    public HttpAggregatorInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (isClient) {
            pipeline.addLast("codec", new HttpClientCodec()); // 如果是客户端，则添加 HttpClientCodec
        } else {
            pipeline.addLast("codec", new HttpServerCodec()); // 如果是服务器，则添加HttpServerCodec
        }
        pipeline.addLast("aggregator",
                new HttpObjectAggregator(521 * 1024)); // 将最大的消息大小为512KB的HttpObjectAggregator添加到ChannelPipeline
    }
}
