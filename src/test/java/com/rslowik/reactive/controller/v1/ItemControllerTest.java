package com.rslowik.reactive.controller.v1;

import com.rslowik.reactive.document.Item;
import com.rslowik.reactive.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext
@Slf4j
@AutoConfigureWebTestClient
@Profile("test")
class ItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @BeforeEach
    public void setup() {
        itemReactiveRepository.deleteAll()
                .thenMany(
                        Flux.fromIterable(getData()))
                .flatMap(item -> itemReactiveRepository.save(item))
                .doOnNext(item -> log.info("Saved item: {}", item))
                .blockLast();
    }

    @Test
    void getAllItemsTestApproach1() {
        webTestClient.get().uri(ItemController.ITEM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4);
    }

    @Test
    void getAllItemsTestApproach2() {
        webTestClient.get().uri(ItemController.ITEM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4);
    }

    @Test
    void getAllItemsTestApproach3() {
        webTestClient.get().uri(ItemController.ITEM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4)
                .consumeWith(
                        resp -> Optional.ofNullable(resp.getResponseBody()).orElseThrow(
                                () -> new IllegalArgumentException("Error")
                        ).forEach(
                                item -> assertNotNull(item.getId())
                        )
                );
    }

    @Test
    void getAllItemsTestApproach4() {
        Flux<Item> flux = webTestClient.get().uri(ItemController.ITEM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    private List<Item> getData() {
        return Arrays.asList(
                new Item(null, "Samsung TV", 399.99),
                new Item(null, "LG TV", 329.99),
                new Item(null, "Apple Watch", 349.99),
                new Item("ABC", "Beats Headphones", 149.99)
        );
    }

    @Test
    void getItemOk() {
        webTestClient.get().uri(ItemController.ITEM_ENDPOINT_V1 + "/{id}", "ABC")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.price", 149.99);
    }

    @Test
    void getItemNotFound() {
        webTestClient.get().uri(ItemController.ITEM_ENDPOINT_V1 + "/{id}", "CCC")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createItem() {
        Item item = new Item(null, "Test Item", 222.99);
        webTestClient.post()
                .uri(ItemController.ITEM_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo(item.getDescription())
                .jsonPath("$.price").isEqualTo(item.getPrice());
    }

    @Test
    void deleteItem() {
        webTestClient.delete()
                .uri(ItemController.ITEM_ENDPOINT_V1 + "/{id}", "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
    }

    @Test
    void updateItem() {
        double newPrice = 333.99;
        Item item = new Item(null, "Beats Headphones", newPrice);
        webTestClient.put().uri(ItemController.ITEM_ENDPOINT_V1 + "/{id}", "ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("ABC")
                .jsonPath("$.description").isEqualTo(item.getDescription())
                .jsonPath("$.price").isEqualTo(item.getPrice());
    }
}