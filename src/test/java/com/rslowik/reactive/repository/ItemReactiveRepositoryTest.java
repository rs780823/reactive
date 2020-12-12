package com.rslowik.reactive.repository;

import com.rslowik.reactive.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by rslowik
 * 11.12.20
 */
@DataMongoTest
@DirtiesContext
public class ItemReactiveRepositoryTest {

    private String id = UUID.randomUUID().toString();
    private List<Item> items = Arrays.asList(
            new Item(null, "Samsung TV", 400.0),
            new Item(null, "JVC TV", 500.0),
            new Item(null, "Iphone 11 Pro", 1100.0),
            new Item(id, "Bose Headphones", 600.0)
    );

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @BeforeEach
    public void before() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> {
                    System.out.println("Inserted Item is:" + item);
                })
                .blockLast();

    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getItemById() {
        StepVerifier.create(itemReactiveRepository.findById(id))
                .expectSubscription()
                .expectNextMatches(item ->
                        "Bose Headphones".equals(item.getDescription())
                )
                .verifyComplete();
    }

    @Test
    public void getItemByDescription() {
        StepVerifier.create(itemReactiveRepository.findByDescription("Bose Headphones"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem() {
        String idLocal = UUID.randomUUID().toString();
        Item item = new Item(idLocal, "Some goods", 122.12);
        StepVerifier.create(itemReactiveRepository.save(item))
                .expectSubscription()
                .expectNextMatches(it -> idLocal.equals(it.getId()) && "Some goods".equals(it.getDescription()))
                .verifyComplete();
    }

    @Test
    public void updateItem() {
        double newPrice = 666.66;
        Flux<Item> updatedItem = itemReactiveRepository.findByDescription("Samsung TV")
                .map(item -> {
                    item.setPrice(newPrice);
                    return item;
                }).flatMap(
                        item -> itemReactiveRepository.save(item)
                );

        StepVerifier.create(updatedItem)
                .expectSubscription()
                .expectNextMatches(item -> newPrice == item.getPrice())
                .verifyComplete();
    }

    @Test
    public void deleteItemById() {
        Flux<Void> deletedItem = itemReactiveRepository.findByDescription("Samsung TV")
                .map(Item::getId)
                .flatMap(
                        id -> itemReactiveRepository.deleteById(id)
                );

        StepVerifier.create(deletedItem.log("deleteItemByIdTestCase"))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void deleteItem() {
        Flux<Void> deletedItem = itemReactiveRepository.findByDescription("Samsung TV")
                .flatMap(
                        itemReactiveRepository::delete
                );

        StepVerifier.create(deletedItem.log("deleteItemTestCase"))
                .expectSubscription()
                .verifyComplete();
    }
}