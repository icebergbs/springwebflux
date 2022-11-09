package reactor3.springwebflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ReactorTests {

    /**
     * Reactor 框架提供了专门的 reactor-test测试组件, 在pom中引入该组件的方式:
     *        <dependency>
     *             <groupId>io.projectreactor</groupId>
     *             <artifactId>reactor-test</artifactId>
     *             <scope>test</scope>
     *         </dependency>
     *
     * reactor-test测试组件中的核心类是 StepVerifier
     */

    Flux<String> helloWorld = Flux.just("Hello", "World");

    /**
     * StepVerifier示例: 正常场景
     */
    @Test
    public void testStepVerifier() {

        StepVerifier.create(helloWorld)
                .expectNext("Hello")
                .expectNext("World")
                .expectComplete()
                .verify();
    }

    /**
     * StepVerifier示例: 异常场景
     */
    @Test
    public void testStepVerifierWithError() {
       Flux<String> helloWorldWithException
               = helloWorld.concatWith(Mono.error(new IllegalArgumentException("exception")));
        StepVerifier.create(helloWorldWithException)
                .expectNext("Hello")
                .expectNext("World")
                .expectErrorMessage("expection")
                .verify();
    }

    /**
     * 上述步骤:
     *   1. 初始化: 将以有的Publisher对象(Mono 或 Flux) 传入 StepVerifier 的 create()方法
     *   2. 设置正常数据流断言: 多次调用expectNext()  expectNextMatches()设置断言,验证Publisher对象每一步产生的数据是否符合预期
     *   3. 设置完成数据流断言: 调用expectComplete() 设置断言,验证Publisher 是否满足正常结束的预期
     *   4. 设置异常数据流断言: 调用expectError() 设置断言, 验证 Publisher是否满足异常结束的预期
     *   5. 启动测试: 调用verify() 启动测试.
     */
}
