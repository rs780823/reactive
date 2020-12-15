package com.rslowik.reactive.controller.v1;

import com.rslowik.reactive.document.Item;
import com.rslowik.reactive.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by rslowik
 * 11.12.20
 */
@RestController
@Slf4j
public class ItemController {

    public static final String ITEM_ENDPOINT_V1 = "/v1/items";

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_ENDPOINT_V1)
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }

    @GetMapping(ITEM_ENDPOINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> getItem(@PathVariable("id") String id) {
        log.info("Enter '{}/{}'", ITEM_ENDPOINT_V1, id);
        return itemReactiveRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ITEM_ENDPOINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemReactiveRepository.save(item);
    }

    @DeleteMapping(ITEM_ENDPOINT_V1 + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteItem(@PathVariable("id") String id) {
        return itemReactiveRepository.deleteById(id);
    }

    @PutMapping(ITEM_ENDPOINT_V1 + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable("id") String id, @RequestBody Item body) {
        return itemReactiveRepository
                .findById(id)
                .flatMap(item -> {
                    item.setDescription(body.getDescription());
                    item.setPrice(body.getPrice());
                    return itemReactiveRepository.save(item);
                }).map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
