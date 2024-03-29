package reactor3.springwebflux.demo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Subscribe {

	public static void main(String[] args) {
        subscribe();
        onErrorReturn();
        switchOnError();
	}
	
	private static void subscribe() {
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);
    }
	
	private static void onErrorReturn() {
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);
    }

    private static void switchOnError() {
//        Flux.just(1, 2)
//                .concatWith(Mono.error(new IllegalStateException()))
//                .switchOnError(Mono.just(0))
//                .subscribe(System.out::println);
    }
}
