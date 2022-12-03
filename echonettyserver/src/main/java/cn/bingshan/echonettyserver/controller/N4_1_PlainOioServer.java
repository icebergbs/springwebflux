package cn.bingshan.echonettyserver.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 为使用Netty 的阻塞网络编程
 * @author bingshan
 * @date 2022/12/1 21:10
 */
public class N4_1_PlainOioServer {
    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port); // 将服务器绑定到指定端口
        try {
            for (;;) {
                final Socket clientSocket = socket.accept();  // 接受连接
                System.out.println("Accepted connection form " + clientSocket);
                new Thread(new Runnable() {  // 创建一个新的线程来处理该连接
                    @Override
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            out.write("Hi! \r\n".getBytes(Charset.forName("UTF-8")));  //将消息写给已连接的客户端
                            out.flush();;
                            clientSocket.close();  // 关闭连接
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();  // 启动线程
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
