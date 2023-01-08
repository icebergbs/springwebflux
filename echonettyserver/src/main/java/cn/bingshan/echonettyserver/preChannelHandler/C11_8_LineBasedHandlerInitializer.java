package cn.bingshan.echonettyserver.preChannelHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * 处理由行尾符分隔的帧
 * @author bingshan
 * @date 2023/1/8 15:32
 */
public class C11_8_LineBasedHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LineBasedFrameDecoder(64 *  1024)); // 该LineBaseFrameDecoder将提取的帧转发给下一个ChannelInboundHandler
        pipeline.addLast(new FrameHandler()); //添加FrameHandler以接受帧
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception { // 传入了单个帧的内容
            // Do something with the data extracted from the frame
        }
    }
}
