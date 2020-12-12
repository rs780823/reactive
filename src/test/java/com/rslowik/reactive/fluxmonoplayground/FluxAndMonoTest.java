package com.rslowik.reactive.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Created by rslowik
 * 08.12.20
 */
public class FluxAndMonoTest {

    @Test
    public void fluxTest() {
        Flux<String> fluxStrings = Flux.just("Spring", "Spring Boot", "Reactive Spring")
//                .concatWith(Flux.error(new RuntimeException("Errrrrrr")))
                .log();
        fluxStrings.subscribe(System.out::println, System.err::println, () -> System.out.println("completed"));

    }

    @Test
    public void fluxTestElementsNoErrors() {
        Flux<String> fluxStrings = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();
        fluxStrings.subscribe(System.out::println, System.err::println, () -> System.out.println("completed"));
        StepVerifier.create(fluxStrings)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }

    @Test
    public void fluxTestElementsWithErrors() {
        Flux<String> fluxStrings = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Errrrrrr")))
                .log();
        fluxStrings.subscribe(System.out::println, System.err::println, () -> System.out.println("completed"));
        StepVerifier.create(fluxStrings)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void fluxTestElementCount() {
        Flux<String> fluxStrings = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Errrrrrr")))
                .log();
        fluxStrings.subscribe(System.out::println, System.err::println, () -> System.out.println("completed"));
        StepVerifier.create(fluxStrings)
                .expectNextCount(3)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void monoNoErrorTest() {
        Mono<String> mono = Mono.just("Spring");
        mono.subscribe(System.out::println, System.err::println, () -> System.out.println("completed"));
        StepVerifier.create(mono.log())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void monoErrorTest() {
        StepVerifier.create(Mono.error(new RuntimeException("Err")).log())
                .expectError(RuntimeException.class);
    }
}
