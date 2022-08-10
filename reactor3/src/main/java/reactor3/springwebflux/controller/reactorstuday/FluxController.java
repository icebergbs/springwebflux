package reactor3.springwebflux.controller.reactorstuday;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

public class FluxController {

    /**
     *创建Flux 方式可以分为两大类:
     *  1.Flux的静态方法, 2. 动态创建Flux
     */
    public static void main(String[] args) {
        //1
        Flux.just("Hello", "World").subscribe(System.out::println);
        System.out.println("------");

        Flux.fromArray(new Integer[] {1,2,3}).subscribe(System.out::println);
        System.out.println("------");

        Flux.empty().subscribe(System.out::println);
        System.out.println("------");

        Flux.range(1,5).subscribe(System.out::println);
        System.out.println("------");


        //2
        /**
         * 式来产生Flux序列, 序列的产生依赖Reactor所提供的SynchronousSink组件
         *   逐一: 含义是在具体的元素生成逻辑中,next()方法最多只能被调用一次
         */

        System.out.println("------Flux dynamic 2--------");
        Flux.generate(sink -> {
            sink.next("hello");
            sink.complete();
        }).subscribe(System.out::println);
        System.out.println("--------");

        /**
         * create()与generate()不同之处在于前者使用FluxSink组件.
         *   FluxSink支持同步和异步的消息产生方式,并且可以在一次调用中产生多个元素
         */
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);


        //创建 Mono
        System.out.println("------Mono static--------");
        Mono.just("Hello").subscribe(System.out::println);
        Mono.empty().subscribe(System.out::println);
        Mono.never().subscribe(System.out::println);

        //比较常用特殊的静态方法
        Mono.delay( Duration.ofSeconds(5)).subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("Hellow")).subscribe(System.out::println);

        //Mono dynamic
        Mono.create(sink -> {
            sink.success("Hello");
        }).subscribe(System.out::println);
    }
}
