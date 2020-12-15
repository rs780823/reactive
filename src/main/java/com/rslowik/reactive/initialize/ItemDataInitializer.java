package com.rslowik.reactive.initialize;

import com.rslowik.reactive.document.Item;
import com.rslowik.reactive.document.ItemCapped;
import com.rslowik.reactive.repository.ItemCappedReactiveRepository;
import com.rslowik.reactive.repository.ItemReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rslowik
 * 11.12.20
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

    private final ItemReactiveRepository itemReactiveRepository;
    private final ReactiveMongoOperations reactiveMongoOperations;
    private final ItemCappedReactiveRepository itemCappedReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeDb();
        createCappedCollection();
        dataSetupForCappedCollection();
    }

    private void createCappedCollection() {
        reactiveMongoOperations.dropCollection(ItemCapped.class);
        reactiveMongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());
    }

    private void initializeDb() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(System.out::println);
    }

    private void dataSetupForCappedCollection() {
        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ItemCapped(null, "Random Item" + i, (100.00 + i)));

        itemCappedReactiveRepository
                .insert(itemCappedFlux)
                .subscribe(itemCapped -> log.info("Inserted Item is: {}", itemCapped));
    }

    private List<Item> data() {
        return Arrays.asList(
                new Item(null, "Item1", 11.00),
                new Item(null, "Item2", 12.00),
                new Item(null, "Item3", 13.00),
                new Item(null, "Item4", 14.00),
                new Item(null, "Item5", 15.00),
                new Item(null, "Item6", 16.00),
                new Item(null, "Item7", 17.00),
                new Item(null, "Item8", 18.00),
                new Item(null, "Item9", 19.00),
                new Item(null, "Item10", 20.00)
        );
    }
}
