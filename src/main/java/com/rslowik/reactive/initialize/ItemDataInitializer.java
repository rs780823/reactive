package com.rslowik.reactive.initialize;

import com.rslowik.reactive.document.Item;
import com.rslowik.reactive.repository.ItemReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rslowik
 * 11.12.20
 */
@Component
@RequiredArgsConstructor
public class ItemDataInitializer implements CommandLineRunner {

    private final ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeDb();
    }

    private void initializeDb() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(System.out::println);
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
