package reactor3.springwebflux.controller;

import java.util.List;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/25 21:01
 */
public interface MyEventListener<T> {
    void onDataChunk(List<T> chunk);
    void processComplete();
}
