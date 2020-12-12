package com.rslowik.reactive.fluxmonoplayground;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rslowik
 * 08.12.20
 */
public class FluxAndMonoTransformTest {

    private List<String> names = new ArrayList<>();

    @BeforeEach
    public void before() {
        names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");
    }

    @Test
    public void transformWithMap() {
        Flux<String> nameFlux = Flux.fromIterable(names)
                .map(String::toUpperCase)
                .log();
        StepVerifier.create(nameFlux)
                .expectNext(
                        "Adam".toUpperCase(),
                        "Anna".toUpperCase(),
                        "Jack".toUpperCase(),
                        "Jenny".toUpperCase()
                )
                .verifyComplete();
    }

    @Test
    public void transformWithMapLengthRepeat() {
        Flux<String> nameFlux = Flux.fromIterable(names)
                .map(String::toUpperCase)
                .repeat(1)
                .log();
        StepVerifier.create(nameFlux)
                .expectNext(
                        "Adam".toUpperCase(),
                        "Anna".toUpperCase(),
                        "Jack".toUpperCase(),
                        "Jenny".toUpperCase(),
                        "Adam".toUpperCase(),
                        "Anna".toUpperCase(),
                        "Jack".toUpperCase(),
                        "Jenny".toUpperCase()
                )
                .verifyComplete();
    }

    @Test
    public void transformWithMapLength() {
        Flux<String> nameFlux = Flux.fromIterable(names)
                .map(String::toUpperCase)
                .log();
        StepVerifier.create(nameFlux)
                .expectNext(
                        "Adam".toUpperCase(),
                        "Anna".toUpperCase(),
                        "Jack".toUpperCase(),
                        "Jenny".toUpperCase()
                )
                .verifyComplete();
    }

    @Test
    public void transformWithFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));
                })
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformWithFlatMapParallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2) /// waiting for Flux<Flux<String>> -> (A, B), (C, D), (E, F)
                .flatMap(s ->
                        s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
                .flatMap(Flux::fromIterable)
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformWithFlatMapParallelMaintainOrder() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2) /// waiting for Flux<Flux<String>> -> (A, B), (C, D), (E, F)
                .flatMapSequential(s ->
                        s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
                .flatMap(Flux::fromIterable)
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }
}
