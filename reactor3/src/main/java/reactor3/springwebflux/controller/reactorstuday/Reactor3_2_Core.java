package reactor3.springwebflux.controller.reactorstuday;

import org.junit.Test;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.UnicastProcessor;

import java.util.ArrayList;

/**
 *
 *
 * @author  bingshan
 * @date 2022/3/24 13:58
 */
public class Reactor3_2_Core {

    public static void main(String[] args) throws InterruptedException {
        //4.8. Processors



    }


    @Test
    public void testSink() {
        //4.8.2. 使用 Sink 门面对象来线程安全地生成流
        UnicastProcessor<Integer> processor = UnicastProcessor.create();
        FluxSink<Integer> sink = processor.sink();
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final int j = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sink.next(j);
                }
            });
            threads.add(thread);
        }
        threads.forEach(thread -> {
            thread.start();
        });
        processor.subscribe(System.out::println);

    }






}
