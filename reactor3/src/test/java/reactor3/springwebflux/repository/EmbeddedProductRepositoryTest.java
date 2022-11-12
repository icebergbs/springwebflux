package reactor3.springwebflux.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor3.springwebflux.entity.Product;

import java.util.UUID;

import static de.flapdoodle.embed.mongo.Command.Mongo;


/**
 * 测试内嵌式MongoDB
 *   1. 引入 flapdoodle依赖
 *      flapdoodle是一个内嵌式MongoDB数据库，与传统的关系型数据库中使用的H2内嵌式类似
 *
 *   2. @DataMongoTest
 *      会使用测试配置自动创建与MongoDB的连接以及ReactiveMongoTemplate工具类
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class EmbeddedProductRepositoryTest {

    @Autowired
    ProductReactiveRepository repository;

    @Autowired
    MongoTemplate template;

    @Before
    public void setUp() {
        MongoOperations operations = template;
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
