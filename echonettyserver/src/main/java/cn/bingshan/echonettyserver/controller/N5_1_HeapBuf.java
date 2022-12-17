package cn.bingshan.echonettyserver.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 支撑数组
 * @author bingshan
 * @date 2022/12/13 21:50
 */
public class N5_1_HeapBuf {

    public void writeToChannel() {
        ByteBuf heapBuf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));  // 创建持有要写数据的ByteBuf
        if (heapBuf.hasArray()) {  //检查ByteBuf是否有一个支撑数组
            byte[] array = heapBuf.array(); //如果有，则获取对该数组的引用
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex(); //计算第一个节点的偏移量
            int length = heapBuf.readableBytes();  //获得可读字节数
            //hadleArray(array, offset, length);  // 使用数组、偏移量和长度作为参数调用你的方法
        }
    }
}
