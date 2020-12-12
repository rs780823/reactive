package com.rslowik.reactive.fluxmonoplayground;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by rslowik
 * 08.12.20
 */
public class FluxAndMonoFactoryTest {

    private List<String> names = new ArrayList<>();

    @BeforeEach
    public void before() {
        names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");
    }

    @Test
    public void fluxWithIterable() {
        Flux<String> nameFlux = Flux.fromIterable(names).log();
        StepVerifier.create(nameFlux)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void fluxWithArray() {
        String[] namesLocal = new String[]{"Adam", "Anna", "Jack", "Jenny"};
        Flux<String> nameFlux = Flux.fromArray(namesLocal);
        StepVerifier.create(nameFlux)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void fluxWithStream() {
        Flux<String> nameFlux = Flux.fromStream(names.stream());
        StepVerifier.create(nameFlux)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void monoJustOrEmpty() {
        Mono<String> mono = Mono.justOrEmpty(null);
        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void monoSuplier() {
        Supplier<String> supplier = () -> "adam";
        Mono<String> mono = Mono.fromSupplier(supplier);
        StepVerifier
                .create(
                        mono.log()
                )
                .expectNext("adam")
                .verifyComplete();
    }

    @Test
    public void fluxRange() {
        Flux<Integer> flux = Flux.range(1, 5);
        StepVerifier
                .create(
                        flux.log()
                )
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }
}
