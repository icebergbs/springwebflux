package cn.bingshan.echonettyserver.preChannelHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 使用HTTPS
 * @author bingshan
 * @date 2023/1/5 22:13
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean isClient;

    public HttpsCodecInitializer(SslContext context, boolean isClient) {
        this.context = context;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine engine = context.newEngine(ch.alloc());
        pipeline.addLast("ssl", new SslHandler(engine)); // 将SslHandler添加到ChannelPipeline中以使用HTTPS

        if(isClient) {
            pipeline.addLast("codec", new HttpClientCodec()); // 如果是客户端，则添加 HttpClientCodec
        } else {
            pipeline.addLast("codec", new HttpServerCodec()); // 如果是服务器，则添加HttpServerCodec
        }
    }
}
