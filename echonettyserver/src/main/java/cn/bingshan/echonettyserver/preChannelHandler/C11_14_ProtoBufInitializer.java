package cn.bingshan.echonettyserver.preChannelHandler;

import com.google.protobuf.MessageLite;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * 使用 protobuf
 * @author bingshan
 * @date 2023/1/8 18:28
 */
public class C11_14_ProtoBufInitializer extends ChannelInitializer<Channel> {
    private final MessageLite lite;

    public C11_14_ProtoBufInitializer(MessageLite lite) {
        this.lite = lite;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ProtobufVarint32FrameDecoder()); // 添加 ProtobufVarint32FrameDecoder 以分隔帧
        pipeline.addLast(new ProtobufEncoder()); // 添加 ProtobufEncoder 以处理消息的编码
        pipeline.addLast(new ProtobufDecoder(lite)); // 添加  ProtobufDecoder 以解码消息
        pipeline.addLast(new ObjectHandler()); // 添加 ObjectHandler 以处理解码消息
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            // Do something with the object
        }
    }


}
