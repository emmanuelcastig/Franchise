package co.com.pragma.mongo;

import co.com.pragma.mongo.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface MongoDBRepository extends ReactiveMongoRepository<FranchiseDocument, String>
        , ReactiveQueryByExampleExecutor<FranchiseDocument> {
    Mono<FranchiseDocument> findByName(String name);
}
