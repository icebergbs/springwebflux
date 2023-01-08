package cn.bingshan.echonettyserver.preChannelHandler;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * 使用 ChunkedStream 传输文件内容
 * @author bingshan
 * @date 2023/1/8 17:56
 */
public class C11_12_ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

    private final File file;
    private final SslContext sslContext;

    public C11_12_ChunkedWriteHandlerInitializer(File file, SslContext sslContext) {
        this.file = file;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SslHandler(sslContext.newEngine(ch.alloc()))); //将SslHandler添加到ChannelPipeline中
        pipeline.addLast(new ChunkedWriteHandler()); // 添加 ChunkedWriteHandler 以处理作为ChunkedInput 传入的数据
        pipeline.addLast(new WriteStreamHandler()); // 一旦连接建立，WriteStreamHandler 就开始写文件数据
    }

    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception { // 当连接建立时， channelActive()将使用ChunkedInput写文件数据
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}
