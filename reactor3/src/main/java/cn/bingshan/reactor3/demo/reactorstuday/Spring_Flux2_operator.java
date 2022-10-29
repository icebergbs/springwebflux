package cn.bingshan.reactor3.demo.reactorstuday;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Spring_Flux2_operator {

    /**
     * Flux 和 Mono操作符
     */
    public static void main(String[] args) {
        /**
         * 转换操作符
         */

        //1. buffer 把当前流中的元素收集到集合中，并把集合对象作为流中的新元素。
        //    可以指定集合对象所包含的元素的最大数量
        Flux.range(1, 50).buffer(10).subscribe(System.out::println);
        System.out.println("---buffer---");
        /**
         * bufferTimeout()指定时间间隔进行收集
         * bufferUntil()会一直收集,直到断言条件返回true
         * bufferWhile() 只有当断言条件返回true时才会收集，一旦值为false,会立即开始下一次收集。
         */
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
        System.out.println("---bufferUntil---");
        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);
        System.out.println("---bufferWhile---");
        //2. map 映射操作，对流中的每个元素应用一个映射函数，从而到达变换效果。
        Flux.range(1,3).map(x -> x * x);
        System.out.println("---map---");

        //3. flatMap  把流中的每个元素转换成一个流 ,在把转换之后得到的所以流中的元素进行合并
        Flux.range(1,3).flatMap(x -> Mono.just(x * x)).subscribe(System.out::println);
        System.out.println("---flatMap---");
        // 在系统开发过程中，经常会碰到对从数据库中查询的数据项进行逐一处理的场景，这是可以充分利用flatMap的特性。
        // 例如使用该操作符对从数据库中获取的数据进行逐一删除的方法。
        //Mono<Void> deleteFiles = findReposotory.findByName(fileName).flatMap(fileRepository::delete);

        //4. window  类似于buffer ,所不同的是,window 操作符是把当前流中的元素收集到另外的Flux序列中,
        //   因此返回值是Flux<Flux<T>>
        Flux.range(1, 5).window(2).toIterable().forEach(w -> {
            w.subscribe(System.out::println);
            System.out.println("---window---");
        });


        /**
         * 过滤操作符: filter  first  last  skip
         */
        //1. filter 对流中包含的元素进行过滤，只留下满足指定过滤条件的元素。
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
        System.out.println("---filter---");

        //2. first   返回流中第一个元素
        //3. last

        //4. skip 忽略数据流的前n 个元素   skipLast 忽略流的最后n个元素。

        //5. take   按照指定的数量来提取元素,也可以按照指定的时间间隔  takeLast
        Flux.range(1, 100).take(10).subscribe(System.out::println);
        Flux.range(1, 100).takeLast(10).subscribe(System.out::println);
        System.out.println("---take---");



        /**
         * 组合操作符
         *
         */

        //1. then  等到上个操作完成在作下一个 when  等到多个操作完成在作下一个
        //如下代码很好的展示了when操作符的实际应用场景. 我们对用户上传的文件进行处理,
        //首先把图片复制到文件服务器的某一个路径,然后把路径信息保存到数据库,需要两个操作都完成之后方法才能返回.
        //Mono.when(copyFileToFileServer, saveFilePathToDatabase);

        //2. startWith  在数据元素序列的开头插入指定的元素项

        //3. merge  把多个流合并成一个Flux序列, 该操作按照所有流中元素的实际产生顺序
        Flux.merge(Flux.interval(Duration.of(100, ChronoUnit.MILLIS)).take(3),
                Flux.interval(Duration.of(50, ChronoUnit.MILLIS), Duration.of(100, ChronoUnit.MILLIS)).take(3)).toStream()
                .forEach(System.out::println);
        System.out.println("------");
        //不同于merge,  mergeSequential操作符按照所有流被订阅的顺序以流为单位进行合并
        Flux.mergeSequential(Flux.interval(Duration.ofMillis(100)).take(3),
                Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(3));

        //4. zipWith 把当前流中的元素与另外一个流中的元素按照一对一的方式进行合并
        Flux.just("a", "b").zipWith(Flux.just("c", "d")).subscribe(System.out::println);
        //也可通过一个BiFunction函数对合并的元素进行处理, 所得到的流的元素类型为该函数的返回值.
        Flux.just("a", "b").zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s + %s", s1, s2)).subscribe(System.out::println);


        /**
         * 条件操作符
         *   defaultIfEnpty   skipUntil   skipWhile   takeUntil  takeWhile
         */
        System.out.println("---   defaultIfEmpth ---");
        //1. defaultIfEmpth    返回来自原始数据流的元素, 如果原始数据流没有元素,则返回一个默认元素
        //在实际开发过程中应用广泛,通常在对方法返回值的处理上. 例如,controller返回一个空对象和404状态码
        Mono.empty().map(x -> x).defaultIfEmpty(0).subscribe(System.out::println);
        Mono.just(1).map(x -> x).defaultIfEmpty(-1).subscribe(System.out::println);

        System.out.println("---  takeUntil ---");
        //2. 其中Predicate代表一种断言条件, takeUntil将提取元素直到断言条件返回true
        Flux.range(1, 100).takeUntil(i -> i == 10).subscribe(System.out::println);
        System.out.println("--- takeWhile ---");
        //3. continuePredicate也是一种断言条件, takeWhile会在continuePredicate条件返回true时才进行元素的提取
        Flux.range(1, 100).takeWhile(i -> i <= 10).subscribe(System.out::println);
        //4. skipUntil丢弃原始数据流中的元素,直到Predicate返回true
        //5. skipWhile当continuePredicate返回true时才进行元素的丢弃
        System.out.println("------");


        /**
         * 数学操作符
         */
        //1. concat 合并来自不同Flux的数据,合并采用顺序的方式

        //2. count 统计Flux中元素的个数

        //3. reduce对流中包含的所以元素进行积累操作,得到一个包含计算结果的Mono序列
        //                通过一个BiFunction来实现
        //   reduceWith 用来在进行reduce操作时指定一个初始值
        Flux.range(1, 10).reduce((x, y) -> x + y).subscribe(System.out::println);
        Flux.range(1, 10).reduceWith(() -> 5, (x, y) -> x + y).subscribe(System.out::println);
        System.out.println("------///");


        /**
         *   Observable工具操作符
         *    delay   subscribe   timeout
         */
        //1. delay将事件的传递向后延迟一段时间

        //2. subscribe 添加响应的订阅逻辑
        //   Reactor消息类型有三种,正常消息\错误消息\完成消息,subscribe可以只处理其中包含的正常消息,也可同时处理错误消息和完成消息。
        //          subscribe操作符可以只处理其中包含的正常消息，也可以同时处理错误消息和完成消息。
        //  处理正常和错误消息
        Mono.just(100)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);
        System.out.println("------");

        //有时候不想直接抛出异常， 而是希望采用一种容错策略来返回一个默认值。
        Mono.just(100)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);
        System.out.println("------///");

        // 另一种容错策略是通过swaitchOnError()方法使用另外的流来产生元素


        //3. timeout  维持原始观察者的状态,在特定时间段内没有产生任何事件时,将生成一个异常

        //4. block  在接受到下一个元素之前一直阻塞
        //   常用来把响应式数据流转换为传统的数据流
        Mono.just(10).block(Duration.ofSeconds(5));



    }

    //场景: 用户上传文件,首先把图片复制到文件服务器,然后把路径信息保存到数据库
    public Flux<Void> updateFiles(Flux<FilePart> files) {
        return files.flatMap(file -> {
            Mono<Void> copyFileToFileServer = Mono.empty(); //....
            Mono<Void> saveFilePathToDatabase = Mono.empty(); //...
            return Mono.when(copyFileToFileServer, saveFilePathToDatabase);
        });
    }
}
