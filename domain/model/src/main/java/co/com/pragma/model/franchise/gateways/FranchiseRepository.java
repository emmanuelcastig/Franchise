package co.com.pragma.model.franchise.gateways;

import co.com.pragma.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

        Mono<Franchise> save(Franchise franchise);
        Mono<Franchise> findById(String id);
        Mono<Franchise> findByName(String name);

}
