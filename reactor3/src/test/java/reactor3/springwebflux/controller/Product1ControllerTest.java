package reactor3.springwebflux.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.entity.Product;
import reactor3.springwebflux.service.Product1Service;

import static org.junit.Assert.*;

/**
 *   默认情况下，@WebFlux注解会确保所有包含 @RestController注解的JavaBean生成一个Mock的Web环境，
 * 但我们也可以指定想要使用的具体Controller类。
 *   同时，@WebFluxTest注解会自动注入WebTestClient工具类。 WebTestClient工具类专门用来测试WebFlux Controller组件，
 * 在使用上无须启动完整的HTTP容器。
 *   WebTestClient工具类提供常见方法如下：
 *     1. Http请求方法：
 *     2. exchange()： 用于发起Http请求， 返回一个 EntityExchangeResult
 *     3. expectStatus(): 用于验证返回状态，一般可以使用isOk() 来验证是否返回200状态码
 *     4. expectBody(): 用于验证返回对象体是否为指定对象，并利用returnResult()获取对象
 *
 */
@RunWith(SpringRunner.class)
@WebFluxTest(controllers = Product1Controller.class)
public class Product1ControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    Product1Service service;

    @Test
    public void deleteProduct() {
        BDDMockito.given(service.getProductByCode("Book001"))
                .willReturn(Mono.empty());

        webTestClient.delete()
                .uri("http://localhost:8081/v1/products/{productId}", "001")
                .exchange().expectStatus()
                .isOk().expectBody(Void.class).returnResult();

        Mockito.verify(service).deleteProductById("001");
        Mockito.verifyNoMoreInteractions(service);

    }

    @Test
    public void getProduct() {
        Product mockProdect = new Product("001", "Book001", "Microservice Practices",
                "New Book For Microservice By Tianyalan", 100F);

        BDDMockito.given(service.getProductByCode("Book001"))
                .willReturn(Mono.just(mockProdect));

        EntityExchangeResult<Product> result = webTestClient.get()
                .uri("http://localhost:8081/v1/products/{productCode}", "Book001")
                .exchange().expectStatus()
                .isOk().expectBody(Product.class).returnResult();

        Mockito.verify(service).getProductByCode("Book001");
        Mockito.verifyNoMoreInteractions(service);

        Assertions.assertThat(result.getResponseBody().getProductCode())
                .isEqualTo("Book001");
    }


}