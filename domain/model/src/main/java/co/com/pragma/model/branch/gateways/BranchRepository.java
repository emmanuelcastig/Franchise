package co.com.pragma.model.branch.gateways;

import co.com.pragma.model.branch.Branch;
import reactor.core.publisher.Mono;

public interface BranchRepository {

    Mono<Void> save(Branch branch);

    Mono<Branch> findById(String id);

    Mono<Branch> findByName(String name);
}
