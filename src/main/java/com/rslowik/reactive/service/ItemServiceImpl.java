package com.rslowik.reactive.service;

import com.rslowik.reactive.document.Item;
import com.rslowik.reactive.repository.ItemReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemReactiveRepository itemReactiveRepository;

    @Override
    public Mono<Item> getItemById(String id) {
//        return Optional.ofNullable(itemReactiveRepository.findById(id)).orElseThrow(() -> new HttpClientErrorException.NotFound(""));
        return null;
    }
}
