package reactor3.springwebflux.controller;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
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
    ConfigurableApplicationContext context;

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
        //rf.andOther(RouterFunctions.route(RequestPredicates.GET("/routeee").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::getProducts));
        RouterFunction<ServerResponse> newRf = rf.andRoute(RequestPredicates.GET("/cc").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::getProducts);
        //routeFunctionBeanPostProcessor.postProcessAfterInitialization(newRf, "routeProduct");



        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) context.getBeanFactory();
        BeanDefinition bd = bf.getBeanDefinition("routeProduct");
        bf.removeBeanDefinition("routeProduct");

        //RouterFunction<ServerResponse> rf1 = (RouterFunction<ServerResponse>) applicationContext.getBean("routeProduct");

        bd.setBeanClassName(RouterFunction.class.getName());
        bf.registerBeanDefinition("routeProduct", bd);
        bf.registerSingleton("routeProduct", newRf);

        RouterFunction<ServerResponse> rf2 = (RouterFunction<ServerResponse>) applicationContext.getBean("routeProduct");

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
