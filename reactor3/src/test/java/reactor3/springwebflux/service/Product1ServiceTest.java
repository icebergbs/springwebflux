package reactor3.springwebflux.service;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor3.springwebflux.entity.Product;
import reactor3.springwebflux.repository.ProductReactiveRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Product1ServiceTest {


    @Autowired
    Product1Service service;

    @MockBean
    ProductReactiveRepository repository;

/*
  对ProductService测试的难点在于如何隔离 ProductReactiveRepository, 即我们希望在不
进行真实数据访问操作的前提下仍然能够验证 ProductService 中方法的正确性. 尽管ProductService
中的getProductByCode() 逻辑简单, 但从集成测试的角度讲, 确保组件之间的隔离性是一条基本的测试原则.
  使用Mock机制完成隔离. 首先通过@MockBean 注解注入 ProductReactiveRepository, 然后基于第三方
Mock框架mockito(http://site.mockito.org) 提供的given/willReturn 机制完成对ProductReactiveRepository中
getProductByCode()的Mock

  在集成测试中, Mock是一种常用策略. Mock的实现一般都会采用类似mockito的第三方框架,而具体Mock方法的行为则通过模拟的方式来实现.
与使用代码不同, 对于某一个或一些被测试对象所依赖的测试方法而言,编写Mock相对简单,只要模拟被使用的方法即可.
 */
    @Test
    public void getProductByCode() {
        Product mockProdect = new Product("001", "Book001", "Microservice Practices",
                "New Book For Microservice By Tianyalan", 100F);

        BDDMockito.given(repository.getByProductCode("Book001"))
                .willReturn(Mono.just(mockProdect));

        Mono<Product> product = service.getProductByCode("Book001");

        StepVerifier.create(product).expectNextMatches(results -> {
            Assertions.assertEquals(results.getProductCode(), "Book001");
            Assertions.assertEquals(results.getProductName(), "Microservice Practices");
            return true;
        }).verifyComplete();
    }

    @Test
    public void deleteProductById() {
    }
}