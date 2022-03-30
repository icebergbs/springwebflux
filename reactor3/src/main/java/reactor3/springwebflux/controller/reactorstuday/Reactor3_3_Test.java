package reactor3.springwebflux.controller.reactorstuday;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import java.time.Duration;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/24 13:58
 */
public class Reactor3_3_Test {

    public static void main(String[] args) {

        //7.2. 开启调试模式
        Hooks.onOperatorDebug();

        //7.3.1. 用 checkpoint() 方式替代
        //checkpoint() 操作符，它有两种调试技术可用
        // 1. 以把这个操作符加到链中
        // 2.  checkpoint(String) 的方法变体，你可以传入一个独特的字符串以方便在 assembly traceback 中识别信息

        //7.4. 记录流的日志
        Flux<Integer> flux = Flux.range(1, 10)
                .log()
                .take(3);
        flux.subscribe();

    }


    //6.1. 使用 StepVerifier 来测试
    @Test
    public void testAppendBoomError() {
        Flux<String> source = Flux.just("foo", "bar");
        StepVerifier.create(
                appendBoomError(source))
                .expectNext("foo")
                .expectNext("bar")
                .expectErrorMessage("boom")
                .verify();
    }

    public <T> Flux<T> appendBoomError(Flux<T> source) {
        return source.concatWith(Mono.error(new IllegalArgumentException("boom")));
    }

    //6.2. 操控时间
    @Test
    public void testVirtualTime() {
        StepVerifier.withVirtualTime(() -> Mono.delay(Duration.ofDays(1)))
                .expectSubscription()
                .expectNoEvent(Duration.ofDays(1))
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    public void testSplitPathIsUsed() {
        StepVerifier.create(processOrFallback(Mono.just("just a phrase with   tabs!"),
                Mono.just("EMPTY_PHRASE")))
                .expectNext("just", "a", "phrase", "with", "tabs!")
                .verifyComplete();
    }

    // 6.6. 用 PublisherProbe 检查执行路径
    @Test
    public void testEmptyPathIsUsed() {
        StepVerifier.create(processOrFallback(Mono.empty(), Mono.just("EMPTY_PHRASE")))
                .expectNext("EMPTY_PHRASE")
                .verifyComplete();
    }

    public Flux<String> processOrFallback(Mono<String> source, Publisher<String> fallback) {
        return source.flatMapMany(phrase -> Flux.fromArray(phrase.split("\\s+")))
                .switchIfEmpty(fallback);
    }


    @Test
    public void testCommandEmptyPathIsUsed() {
        PublisherProbe<Void> probe = PublisherProbe.empty();

        StepVerifier.create(processOrFallback(Mono.empty(), probe.mono()))
                .verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    private Mono<String> executeCommand(String command) {
        return Mono.just(command + " DONE");
    }
    public Mono<Void> processOrFallback(Mono<String> commandSource, Mono<Void> doWhenEmpty) {
        return commandSource.flatMap(command -> executeCommand(command).then())
                .switchIfEmpty(doWhenEmpty);
    }






}
