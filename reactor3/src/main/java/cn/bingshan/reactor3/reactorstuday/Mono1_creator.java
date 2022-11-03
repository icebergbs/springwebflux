package cn.bingshan.reactor3.reactorstuday;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

public class Mono1_creator {

    /**
     * 创建 Mono
     *   很多创建Flux的方法同样适用，
     */
    public static void main(String[] args) {

        // 2.
        /**   创建 Mono  */
        System.out.println("------Mono static--------");
        Mono.just("Hello").subscribe(System.out::println);
        Mono.empty().subscribe(System.out::println);
        Mono.never().subscribe(System.out::println);

        //比较常用特殊的静态方法
        // 在指定的延迟时间之后会产生数字0作为唯一值。
        Mono.delay( Duration.ofSeconds(5)).subscribe(System.out::println);
        // 从一个Optional对象创建Mono,只有当Optional对象中包含值时，Mono序列才产生对应的元素
        Mono.justOrEmpty(Optional.of("Hellow")).subscribe(System.out::println);
        // 从一个可能为null的对象创建Mono,只有当对象不为null 时，Mono序列才产生对应的元素
        Mono.justOrEmpty(new String("Hellow Object")).subscribe(System.out::println);

        //Mono dynamic  通过create()并使用MonoSink组件
        Mono.create(sink -> {
            sink.success("Hello");
        }).subscribe(System.out::println);
    }
}
