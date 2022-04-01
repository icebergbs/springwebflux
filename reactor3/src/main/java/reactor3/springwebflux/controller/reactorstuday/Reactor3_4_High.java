package reactor3.springwebflux.controller.reactorstuday;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.Function;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/24 13:58
 */
public class Reactor3_4_High {

    public static void main(String[] args) {

        //8.1. 打包重用操作符
        //8.1.1. 使用 transform 操作符
        Function<Flux<String>, Flux<String>> filterAndMap =
                f -> f.filter(color -> !color.equals("orange"))
                      .map(String::toUpperCase);
        Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .transform(filterAndMap)
                .subscribe(b -> System.out.println("Subscriber to Transformed MapAndFilter: " + b));

        //8.1.2. 使用 compose 操作符



    }

}
