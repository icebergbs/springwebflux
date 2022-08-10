package reactor3.springwebflux.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {


    @GetMapping("/")
    public Mono<String> index() {
        return Mono.just("Hello Spring Boot!");
    }

//    public Mono<String> testException() {
//        try {
//            float a = 1/0;
//        } catch (Exception e) {
//            throw new LinkerRuntimeException(200, "Test Exception!");
//        }
//        return Mono.just("Test ezception");
//    }
}
