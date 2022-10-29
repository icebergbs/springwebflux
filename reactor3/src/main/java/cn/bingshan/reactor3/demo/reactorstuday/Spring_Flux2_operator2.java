package cn.bingshan.reactor3.demo.reactorstuday;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

public class Spring_Flux2_operator2 {

    public static void main(String[] args) {

        /**
         * 日志和调试操作符
         */

        //1. log() 用于观察所有的数据并使用日志工具进行跟踪
        System.out.println("--------- Log -----");

        Flux.range(1, 3).log().subscribe(System.out::println);

        //2. debug 由于响应式编程与传统编程方式的差异性，使用Reactor框架编写的代码在出现问题时比较难以调试。为了更好地帮助开发人员进行调试，
        //   Reactor框架提供了响应的工具。
        //   当需要获取更多与流相关的执行信息时，可以在程序开始的地方添加如下代码来启用调试模式。
        Hooks.onOperatorDebug();
        //   在调试模式启用之后，所有的操作符在执行时都会保存与执行链相关的额外信息。当出现错误时，这些信息会被作为异常堆栈信息的一部分输出。

        // z/0
        // 另一种做法是通过启用检查点（checkpoint)来对特定的流处理链启用调试模式。
        // 例如，以下代码演示了如何通过检查点来捕获0被作为除数的场景，我们在map操作符之后添加了一个名为“zero"的检查点。
        Mono.just(0).map(x -> 1 / x)
                .checkpoint("zero").subscribe(System.out::println);



        System.out.println("--------- Mono.defaultIfEmpty -----");
        Mono<Object> def = Mono.empty();
        def.defaultIfEmpty("defVal")
                .flatMap(obj ->  Mono.just(obj))
                .subscribe(System.out::println);
        System.out.println("--------- Mono.defaultIfEmpty111 -----");
        Mono<Object> def1 = Mono.just("defRes");
        def1.defaultIfEmpty("defVal1")
                .flatMap(obj ->  Mono.just(obj))
                .subscribe(System.out::println);

        System.out.println("--------- Mono.switchIfEmpty -----");
        Mono<Object> swit = Mono.empty();
        swit.switchIfEmpty(Mono.just("switDef"))
                .flatMap(obj ->  Mono.just(obj))
                .subscribe(System.out::println);
        System.out.println("--------- Mono.switchIfEmpty111-----");
        Mono<Object> swit1 = Mono.just("switRes");
        swit1.switchIfEmpty(Mono.just("switDef1"))
                .flatMap(obj ->  Mono.just(obj))
                .subscribe(System.out::println);

        System.out.println("--------- Mono.repeatWhenEmpty-----");
        Mono<Object> repeat = Mono.empty();
        repeat.repeatWhenEmpty(obj -> Mono.just("repeatDef"))
                .flatMap(obj ->  Mono.just(obj))
                .subscribe(System.out::println);
        System.out.println("--------- Mono.repeatWhenEmpty111-----");
        Mono<Object> repeat1 = Mono.just("repeatRes");
        repeat1.repeatWhenEmpty(obj -> Mono.just("repeatDef1"))
                .flatMap(obj ->  Mono.just(obj))
                .subscribe(System.out::println);



    }
}
