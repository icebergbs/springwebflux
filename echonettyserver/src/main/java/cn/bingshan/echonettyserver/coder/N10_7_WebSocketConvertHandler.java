package cn.bingshan.echonettyserver.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * ShortToByteEncoder 类
 * @author bingshan
 * @date 2022/11/5 23:45
 */
public class N10_7_WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame,
        N10_7_WebSocketConvertHandler.MyWebSocketFrame>{ // 扩展 MessageToMessageEncoder


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, N10_7_WebSocketConvertHandler.MyWebSocketFrame myWebSocketFrame,
                          List<Object> list) throws Exception { //将MywebSocketFrame编码为指定的WebSocketFrame子类型
        ByteBuf payload = myWebSocketFrame.getData().duplicate().retain();
        switch (myWebSocketFrame.getType()) { //实例化一个指定子类型的WebSocketFrame
            case BINARY:
                list.add(new BinaryWebSocketFrame(payload));
                break;
            case TEXT:
                list.add(new TextWebSocketFrame(payload));
                break;
            case CLOSE:
                list.add(new CloseWebSocketFrame(true, 0, payload));
                break;
            case CONTINUATION:
                list.add(new ContinuationWebSocketFrame(payload));
                break;
            case PONG:
                list.add(new PongWebSocketFrame(payload));
                break;
            case PING:
                list.add(new PingWebSocketFrame(payload));
                break;
            default:
                throw new IllegalStateException("Unsupported wobsocket msg " + myWebSocketFrame);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame,
                          List<Object> list) throws Exception { //将WebSocketFrame解码为MyWebSocketFram , 并设置FrameType
        ByteBuf payload = webSocketFrame.content().duplicate().retain();
        if (webSocketFrame instanceof BinaryWebSocketFrame) {
            list.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.BINARY, payload));
        }
        if (webSocketFrame instanceof CloseWebSocketFrame) {
            list.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CLOSE, payload));
        }
        if (webSocketFrame instanceof PingWebSocketFrame) {
            list.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PING, payload));
        }
        if (webSocketFrame instanceof PongWebSocketFrame) {
            list.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PONG, payload));
        }
        if (webSocketFrame instanceof TextWebSocketFrame) {
            list.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.TEXT, payload));
        }
        if (webSocketFrame instanceof CloseWebSocketFrame) {
            list.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CLOSE, payload));
        }
        if (webSocketFrame instanceof ContinuationWebSocketFrame) {
            list.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CONTINUATION, payload));
        } else {
            throw new IllegalStateException("Unsupported wobsocket msg " + webSocketFrame);
        }

    }

    public static class MyWebSocketFrame {  //声明WebSocketConvertHandler所使用的OUTBOUND_IN类型
        public enum FrameType {  //定义用于被包装的有效负载的WebScoketFrame的类型
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }

        private final FrameType type;
        private final ByteBuf data;

        public MyWebSocketFrame(FrameType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }

        public FrameType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }
    }
}



