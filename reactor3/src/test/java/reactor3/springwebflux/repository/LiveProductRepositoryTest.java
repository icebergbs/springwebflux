package reactor3.springwebflux.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor3.springwebflux.entity.Product;

import java.util.UUID;


/**
 * 测试真实的MongoDB
 */
@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedProductRepositoryTest.class)
public class LiveProductRepositoryTest {

    @Autowired
    ProductReactiveRepository repository;

    @Autowired
    MongoOperations operations;

    @Before
    public void setUp() {
        operations.dropCollection(Product.class);

        operations.insert(new Product("Product" + UUID.randomUUID().toString(),
                "Book001", "Microservie Practices",
                "New Book For Microservie By Tianyalan", 100F));
        operations.insert(new Product("Product" + UUID.randomUUID().toString(),
                "Book002", "Microservie Design",
                "Another Book For Microservie By Tianyalan", 200F));

        operations.findAll(Product.class).forEach(product -> {
            System.out.println(product.toString());
        });
    }

    @Test
    public void testGetByProductCode() {
        Mono<Product> product = repository.getByProductCode("Book001");

        StepVerifier.create(product)
                .expectNextMatches(results -> {
                    Assertions.assertEquals(results.getProductCode(), "Book001");
                    Assertions.assertEquals(results.getProductName(), "Microservie Practices");
                    return true;
                });
    }
}
