package cn.bingshan.reactor3.demo;

import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;

public class MonoCreator {
	
	
    public static void main(String[] args) {
        Mono.fromSupplier(() -> "Hello").subscribe(System.out::println);
        
        Mono.justOrEmpty(Optional.of("Hello")).subscribe(System.out::println);
        
        Mono.create(sink -> sink.success("Hello")).subscribe(System.out::println);

        Mono.defer(() -> Mono.just("Hello")).subscribe(System.out::println);
    }

    /**
     * project reactor数据源大致可以分为两类：饿汉型和懒汉型
     *
     * mono defer方法创建数据源属于懒汉型，与Mono.just等创建数据源则是饿汉型，下面看一个例子：
     */
    @Test
    public void defer(){
        //声明阶段创建DeferClass对象
        Mono<Date> m1 = Mono.just(new Date());
        Mono<Date> m2 = Mono.defer(()->Mono.just(new Date()));
        m1.subscribe(System.out::println);
        m2.subscribe(System.out::println);

        //延迟5秒钟
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m1.subscribe(System.out::println);
        m2.subscribe(System.out::println);
    }
    
    
}
