package reactor3.springwebflux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.entity.Product;
import reactor3.springwebflux.repository.ProductReactiveRepository;

@Service
public class Product1Service {

    @Autowired
    private ProductReactiveRepository  repository;

    public Mono<Product> getProductByCode(String productCode) {
        return repository.getByProductCode(productCode);
    }

    public Mono<Void> deleteProductById(String id) {
        return repository.deleteById(id);
    }
}
