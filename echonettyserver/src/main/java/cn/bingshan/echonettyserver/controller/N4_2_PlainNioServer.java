package cn.bingshan.echonettyserver.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 为使用Netty的异步网络编程
 *      与阻塞代码所做的事情完全相同，但是代码却截然不同
 * @author bingshan
 * @date 2022/12/1 21:17
 */
public class N4_2_PlainNioServer {
    public void serve(int port) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        ServerSocket ssocket = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        ssocket.bind(address);  // 将服务器绑定到选的的端口
        Selector selector = Selector.open();  // 打开Selector 来处理Channel
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);  //将ServerSocket注册到Selector以接受连接
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
        for (;;) {
            try {
                selector.select();  //等待需要处理的新事件；阻塞将一直持续到下一个传入事件
            }catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
            Set<SelectionKey> readKeys = selector.selectedKeys();  //获取所有接收事件的SelectionKey实例
            Iterator<SelectionKey> iterator = readKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();;
                try {
                    if (key.isAcceptable()) {   //检查事件是否是一个新的已经就绪可以被接收的连接
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate()); // 接收客户端，并将他注册到选择器
                        System.out.println("Accepted connection from " + client);
                        if (key.isWritable()) {           // 检查套件字是否已经准备好写数据
                            SocketChannel client1 = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            while (buffer.hasRemaining()) {
                                if (client1.write(buffer) == 0) {   // 将数据写到已连接的客户端
                                    break;
                                }
                            }
                            client1.close();  // 关闭连接
                        }
                    }
                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        //ignore on close
                    }
                }
            }
        }
    }
}
