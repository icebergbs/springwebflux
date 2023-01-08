package cn.bingshan.echonettyserver.preChannelHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * 使用ChannelInitalizer安装解码器
 * @author bingshan
 * @date 2023/1/8 16:02
 */
public class C11_9_CmdHandlerInitalizer extends ChannelInitializer<Channel> {
    static final byte SPACE = ' ';

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new CmdDecoder(64 * 1024)); //添加CmdDecoder提取Cmd对象，并将它转发给下一个ChannelInboundHandler
        pipeline.addLast(new CmdHandler()); //添加CmdHandler以接收和处理Cmd对象
    }

    public static final class Cmd {
        private final ByteBuf name;
        private final ByteBuf args;

        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        public ByteBuf name() {
            return name;
        }

        public ByteBuf args() {
            return args;
        }
    }

    public static final class CmdDecoder extends LineBasedFrameDecoder {
        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer); //从ByteBuf中提取由行尾符序列分隔的帧
            if (frame == null) {
                return null; // 如果输入中没有帧，则返回null
            }
            int index = frame.indexOf(frame.readerIndex(),
                    frame.writerIndex(), SPACE); // 查找第一个空格字符的索引，前面是命令名称，接着是参数
            return new Cmd(frame.slice(frame.readerIndex(), index), //使用包含有命令名称和参数的切片创建新的Cmd对象
                    frame.slice(index + 1, frame.writerIndex()));
        }
    }

    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
            // DO something with the command  //处理传经ChannelPipeline的Cmd对象
        }
    }
}
