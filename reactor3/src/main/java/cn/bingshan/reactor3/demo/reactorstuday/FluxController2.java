package cn.bingshan.reactor3.demo.reactorstuday;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

public class FluxController2 {

    public static void main(String[] args) {
        /**
         * debug
         */
        Hooks.onOperatorDebug();

        Flux.range(1, 3).log().subscribe(System.out::println);

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


        // z/0
        Mono.just(0).map(x -> 1 / x)
                .checkpoint("zero").subscribe(System.out::println);
    }
}
