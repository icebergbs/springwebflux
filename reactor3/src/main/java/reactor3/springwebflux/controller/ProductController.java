package reactor3.springwebflux.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.entity.Product;
import reactor3.springwebflux.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public Flux<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<Product> getProductsById(@PathVariable("id") final String id) {
        return productService.getProductsById(id).defaultIfEmpty(null);
    }

    @PostMapping("")
    public Mono<Void> createProduct(@RequestBody final Mono<Product> productMono) {
        return productService.createOrUpdateProduct(productMono);
    }

    @DeleteMapping("/{id}")
    public Mono<Product> deleteProduct(@PathVariable("id") final String id) {
        return productService.deleteProduct(id);
    }

}
