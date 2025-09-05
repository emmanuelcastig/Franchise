package co.com.pragma.model.product.gateways;

import co.com.pragma.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Void> save(Product product);

    Mono<Product> findById(String id);

    Mono<Void> deleteById(String id);

    Mono<Product> findByName(String name);
}
