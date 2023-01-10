package cn.bingshan.echonettyserver.websocket;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * HttpRequestHandler
 * @author bingshan
 * @date 2023/1/10 21:09
 */
public class C12_1_HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> { // 扩展SimpleChannelInboundHandler以处理FullHttpRequest消息

    private final String wsUri;
    private static File INDEX = null;

    static {
        URL location = C12_1_HttpRequestHandler.class
                .getProtectionDomain()
                .getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public C12_1_HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (wsUri.equalsIgnoreCase(msg.getUri())) { // 如果请求了WebSocket协议升级，则增加引用计数（调用retain()),并将它传递给下一个ChannelInboundHandler
            ctx.fireChannelRead(msg.retain());
        } else {
            if (HttpHeaders.is100ContinueExpected(msg)) { //处理100 Continue 请求以符合HTTP1.1规范
                send100Continue(ctx);
            }
            RandomAccessFile file = new RandomAccessFile(INDEX, "r"); // 读取 index.html
            HttpResponse response = new DefaultHttpResponse(
                    msg.getProtocolVersion(), HttpResponseStatus.OK);
            response.headers().set(
                    HttpHeaders.Names.CONTENT_TYPE,
                    "text/plain; charset=UTF-8");
            boolean keepAlive = HttpHeaders.isKeepAlive(msg);
            if (keepAlive) {  // 如果请求了keep-alive,则添加所需要的HTTP头信息
                response.headers().set(
                        HttpHeaders.Names.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaders.Names.CONNECTION,
                        HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);  // 将 HttpResponse写到客户端
            if (ctx.pipeline().get(SslHandler.class) == null) { // 将index.html写到客户端
                ctx.write(new DefaultFileRegion(
                        file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            ChannelFuture future = ctx.writeAndFlush(
                    LastHttpContent.EMPTY_LAST_CONTENT); // 写LastHttpContent并冲刷至客户端
            if (!keepAlive) {   // 如果没有请求keep-alive, 则在写操作完成后关闭Channel
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }
}
