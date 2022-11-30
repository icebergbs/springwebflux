package cn.bingshan.reactor3.reactorstuday;

import java.util.List;

public interface SingleThreadEventListener {

    void onDataChunk(List<String> chunk);

    void processComplete();

    void processError(Throwable e);
}
