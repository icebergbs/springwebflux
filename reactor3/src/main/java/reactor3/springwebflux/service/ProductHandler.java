package reactor3.springwebflux.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.entity.Product;

@Component
public class ProductHandler {

    @Autowired
    private ProductService productService;

    public Mono<ServerResponse> getProducts(ServerRequest request) {
        return ServerResponse.ok().body(this.productService.getProducts(), Product.class);
    }

    public Mono<ServerResponse> getProductById(ServerRequest request) {
        String id = request.pathVariable("id");

        return ServerResponse.ok().body(this.productService.getProductsById(id), Product.class);
    }

    public Mono<ServerResponse> createProduct(ServerRequest request) {
        Mono<Product> productMono= request.bodyToMono(Product.class);

        return ServerResponse.ok().body(this.productService.createOrUpdateProduct(productMono), Product.class);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String id = request.pathVariable("id");

        return ServerResponse.ok().body(this.productService.deleteProduct(id), Product.class);
    }
}
