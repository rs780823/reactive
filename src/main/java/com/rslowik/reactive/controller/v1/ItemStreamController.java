package com.rslowik.reactive.controller.v1;

import com.rslowik.reactive.document.ItemCapped;
import com.rslowik.reactive.repository.ItemCappedReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ItemStreamController {

    public static final String ITEM_STREAM_V1_API_PATH = "/v1/stream/items";

    @Autowired
    private ItemCappedReactiveRepository itemCappedReactiveRepository;

    @GetMapping(value = ITEM_STREAM_V1_API_PATH, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemsStream() {
        return itemCappedReactiveRepository.findItemsBy();
    }
}
