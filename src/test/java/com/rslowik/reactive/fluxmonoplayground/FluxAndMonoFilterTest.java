package com.rslowik.reactive.fluxmonoplayground;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rslowik
 * 08.12.20
 */
public class FluxAndMonoFilterTest {

    private List<String> names = new ArrayList<>();

    @BeforeEach
    public void before() {
        names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");
    }

    @Test
    public void filterTest() {
        Flux<String> nameFlux = Flux.fromIterable(names)
                .filter(name -> name.startsWith("A"))
                .log();
        StepVerifier.create(nameFlux)
                .expectNext("Adam", "Anna")
                .verifyComplete();
    }

    @Test
    public void filterLengthTest() {
        Flux<String> nameFlux = Flux.fromIterable(names)
                .filter(name -> name.length() > 4)
                .log();

        StepVerifier.create(nameFlux)
                .expectNext("Jenny")
                .verifyComplete();
    }
}
