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