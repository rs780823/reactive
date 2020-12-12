package com.rslowik.reactive.controller.v1;

import com.rslowik.reactive.document.Item;
import com.rslowik.reactive.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Created by rslowik
 * 11.12.20
 */
@RestController
@Slf4j
public class ItemController {

    private static final String ITEM_ENDPOINT_V1 = "/v1/items";

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_ENDPOINT_V1)
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }
}
