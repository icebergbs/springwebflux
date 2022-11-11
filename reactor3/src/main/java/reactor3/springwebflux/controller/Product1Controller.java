package reactor3.springwebflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.entity.Product;
import reactor3.springwebflux.service.Product1Service;
import reactor3.springwebflux.service.ProductService;

@RestController
public class Product1Controller {

    @Autowired
    Product1Service product1Service;

    @DeleteMapping("/v1/products/{productId}")
    public Mono<Void> deleteProduct(@PathVariable  String productId) {
        Mono<Void> result = product1Service.deleteProductById(productId);
        return result;
    }

    @GetMapping("/v1/products/{productCode}")
    public Mono<Product> getProduct(@PathVariable String productCode) {
        Mono<Product> product = product1Service.getProductByCode(productCode);
        return product;
    }
}
