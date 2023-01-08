package cn.bingshan.echonettyserver.preChannelHandler;

import io.netty.channel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 使用 FileRegion传输文件的内容
 * @author bingshan
 * @date 2023/1/8 17:36
 */
public class C11_11_FileRegion {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("/test.txt");
        FileInputStream in = new FileInputStream(file); // 创建一个FileInputStream
        FileRegion region = new DefaultFileRegion(
                in.getChannel(), 0, file.length());  //以该文件的完整长度创建一个新的DefaultFileRegion

        Channel channel = (Channel) new ChannelInboundHandlerAdapter();
        channel.writeAndFlush(region).addListener( // 发送该DefaultFileRegion, 并注册一个ChannelFutureListener
                new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            Throwable cause = future.cause(); // 处理失败
                            // Do something
                        }
                    }
                }
        );
    }
}
