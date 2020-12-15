package com.rslowik.reactive.repository;

import com.rslowik.reactive.document.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Created by rslowik
 * 11.12.20
 */
@Repository
public interface ItemCappedReactiveRepository extends ReactiveMongoRepository<ItemCapped, String> {
    @Tailable
    Flux<ItemCapped> findItemsBy();
}
