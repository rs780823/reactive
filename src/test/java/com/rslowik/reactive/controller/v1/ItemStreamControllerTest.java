package com.rslowik.reactive.controller.v1;

import com.rslowik.reactive.document.ItemCapped;
import com.rslowik.reactive.repository.ItemCappedReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static com.rslowik.reactive.controller.v1.ItemStreamController.ITEM_STREAM_V1_API_PATH;

@SpringBootTest
@DirtiesContext
@Slf4j
@AutoConfigureWebTestClient
@Profile("test")
class ItemStreamControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ItemCappedReactiveRepository itemCappedReactiveRepository;

    @BeforeEach
    public void setup() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(
                ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped()
        );
        Flux<ItemCapped> fluxCapped = Flux.interval(Duration.ofMillis(100))
                .map(i -> new ItemCapped(null, "Test Item " + i, 111.11 + i))
                .take(5);
        itemCappedReactiveRepository
                .insert(fluxCapped)
                .doOnNext(itemCapped -> log.info("Test insert {}", itemCapped))
                .blockLast();
    }

    @Test
    void getItemsStream() {
        Flux<ItemCapped> itemCappedFlux = webTestClient.get().uri(ITEM_STREAM_V1_API_PATH)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ItemCapped.class)
                .getResponseBody()
                .take(5);

        StepVerifier.create(itemCappedFlux.log("getItemsStream"))
                .expectNextCount(5)
                .thenCancel()
                .verify();
    }
}