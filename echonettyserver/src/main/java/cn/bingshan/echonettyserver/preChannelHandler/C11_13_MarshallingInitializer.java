package cn.bingshan.echonettyserver.preChannelHandler;

import io.netty.channel.*;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import java.io.Serializable;

/**
 * 使用 JBoss marshalling
 * @author bingshan
 * @date 2023/1/8 18:18
 */
public class C11_13_MarshallingInitializer extends ChannelInitializer<Channel> {

    private final MarshallerProvider marshallerProvider;
    private final UnmarshallerProvider unmarshallerProvider;

    public C11_13_MarshallingInitializer(MarshallerProvider marshallerProvider, UnmarshallerProvider unmarshallerProvider) {
        this.marshallerProvider = marshallerProvider;
        this.unmarshallerProvider = unmarshallerProvider;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MarshallingDecoder(unmarshallerProvider)); // 添加 MarshallingDecoder以将 ByteBuf转换为POJO
        pipeline.addLast(new MarshallingEncoder(marshallerProvider)); // 添加 MarshallingEncoder 以将 POJO 转换为 ByteBuf
        pipeline.addLast(new ObjectHandler()); // 添加ObjectHandler, 以处理普通的实现了Serializable接口的POJO
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Serializable> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) throws Exception {
            // Do something
        }
    }
}
