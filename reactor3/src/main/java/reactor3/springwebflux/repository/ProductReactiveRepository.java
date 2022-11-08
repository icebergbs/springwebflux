package reactor3.springwebflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.entity.Product;

public interface ProductReactiveRepository extends ReactiveMongoRepository<Product, String> {

    Mono<Product> getByProductCode(String productCode);
}
