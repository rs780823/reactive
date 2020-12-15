package com.rslowik.reactive.service;

import com.rslowik.reactive.document.Item;
import reactor.core.publisher.Mono;

public interface ItemService {
    Mono<Item> getItemById(String id);
}
