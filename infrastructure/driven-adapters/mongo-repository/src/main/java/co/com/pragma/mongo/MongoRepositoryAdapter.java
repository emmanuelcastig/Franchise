package co.com.pragma.mongo;

import co.com.pragma.model.franchise.Franchise;
import co.com.pragma.model.franchise.gateways.FranchiseRepository;
import co.com.pragma.mongo.document.FranchiseDocument;
import co.com.pragma.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public class MongoRepositoryAdapter extends AdapterOperations<Franchise, FranchiseDocument, String, MongoDBRepository>
implements FranchiseRepository
{

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper mapper) {

        super(repository, mapper, d -> mapper.map(d, Franchise.class));
    }

    @Override
    public Mono<Franchise> findByName(String name) {
        return repository.findByName(name)
                .map(this::toEntity);
    }
}
