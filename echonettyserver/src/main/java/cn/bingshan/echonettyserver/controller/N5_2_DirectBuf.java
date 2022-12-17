package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 访问直接缓冲区的数据
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_2_DirectBuf {

    public void writeToChannel() {
        ByteBuf directBuf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));  // 创建持有要写数据的ByteBuf
        if (!directBuf.hasArray()) {  //检查ByteBuf是否由数组支撑。如果不是，则这是一个直接缓冲区
            int length = directBuf.readableBytes(); // 获取可读字节数
            byte[] array = new byte[length];  // 分配一个新的数组来保存具有该长度的字节数据
            directBuf.getBytes(directBuf.readerIndex(), array); // 将字复制到该数组
            //hadleArray(array, offset, length);  // 使用数组、偏移量和长度作为参数调用你的方法
        }
    }
}
