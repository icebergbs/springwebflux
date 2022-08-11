package reactor3.springwebflux.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.config.RouteFunctionBeanPostProcessor;
import reactor3.springwebflux.config.RouterFunctionMapping;
import reactor3.springwebflux.service.ProductHandler;
import sun.reflect.misc.MethodUtil;

@RestController
public class HelloController {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ProductHandler productHandler;

    RouteFunctionBeanPostProcessor routeFunctionBeanPostProcessor = new RouteFunctionBeanPostProcessor();

    @GetMapping("/")
    public Mono<String> index() {
        return Mono.just("Hello Spring Boot!");
    }

    @GetMapping("/dynamicRoute")
    public Mono<String> dynamicRoute() {
        RouterFunction<ServerResponse> rf = (RouterFunction<ServerResponse>) applicationContext.getBean("routeProduct");
        RouterFunction<ServerResponse> newRf = rf.andRoute(RequestPredicates.GET("/routeee").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::getProducts);
        routeFunctionBeanPostProcessor.postProcessAfterInitialization(newRf, "routeProduct");

        RouterFunction<ServerResponse> rf1 = (RouterFunction<ServerResponse>) applicationContext.getBean("routeProduct");
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
