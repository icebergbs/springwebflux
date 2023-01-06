package cn.bingshan.echonettyserver.preChannelHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 自动压缩HTTP消息
 * @author bingshan
 * @date 2023/1/5 22:04
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {
    private final boolean isClient;

    public HttpCompressionInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (isClient) {
            pipeline.addLast("codec", new HttpClientCodec()); // 如果是客户端，则添加 HttpClientCodec
            pipeline.addLast("decompressor",
                    new HttpContentDecompressor()); // 如果是客户端，则添加 HttpContentDecompressor 以处理来自服务器的压缩内容
        } else {
            pipeline.addLast("codec", new HttpServerCodec()); // 如果是服务器，则添加HttpServerCodec
            pipeline.addLast("compressor",
                    new HttpContentCompressor()); // 如果是服务器，则添加  HttpContentCompressor 来压缩数据（如果客户端支持它）
        }
    }
}
