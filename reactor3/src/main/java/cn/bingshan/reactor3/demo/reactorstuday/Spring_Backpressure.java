package cn.bingshan.reactor3.demo.reactorstuday;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

public class Spring_Backpressure {


    public static void main(String[] args) {

        /**
         * Reactor框架中的背压机制
         */
        /*
            在Reactor框架中，针对背压有以下4种处理策略：
              ERROR: 当下游跟不上节奏时发出一个错误信号
              DROP: 当下游没有准备好接受新的元素时抛弃这个元素
              LATEST: 让下游只得到上游最新的元素
              BUFFER: 缓存下游没有来得及处理的元素，如果缓存不限大小，则可能导致内存溢出。
            策略定义在枚举类型OverflowStrategy中，不过还有一个IGNORE类型，完全忽略下游背压请求。
         */

        //1. onBackpressureBuffer
        //2. onBackpressureDrop
        //3. onBackpressureLatest
        //4. onBackpressureError
    }
}
