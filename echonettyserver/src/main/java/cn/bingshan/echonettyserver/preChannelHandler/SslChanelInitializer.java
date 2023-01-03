package cn.bingshan.echonettyserver.preChannelHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 添加SSL/TLS支持
 * @author bingshan
 * @date 2023/1/3 21:18
 */
public class SslChanelInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;

    public SslChanelInitializer(SslContext context, boolean startTls) { //传入要使用的SslContext
        this.context = context;
        this.startTls = startTls;  // 如果设置为true, 第一个写入的消息将不会被加密（客户端应该设置为true)
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine engine = context.newEngine(ch.alloc()); //对于每个SslHandler实例，都使用Channel的ByteBufAllocator从SslContext获取一个新的SSLEngine
        ch.pipeline().addFirst("ssl",  //将SslHandler作为第一个ChannelHandler添加到ChannelPipeline中
                new SslHandler(engine, startTls));
    }
}
