package com.rslowik.reactive.fluxmonoplayground;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rslowik
 * 08.12.20
 */
public class FluxAndMonoExceptionTest {

    private List<String> names = new ArrayList<>();

    @BeforeEach
    public void before() {
        names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");
    }

    @Test
    public void fluxErrorHandling() {
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Excep....")))
                .concatWith(Flux.just("D"));

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();

    }

    @Test
    public void fluxErrorHandlingOnErrorResume() {
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Excep....")))
                .concatWith(Flux.just("D"))
                .onErrorResume(
                        // if there is an error we return "default", "default1" and complete
                        (e) -> {
                            System.out.println("Exception is: " + e);
                            return Flux.just("default", "default1");
                        }
                );

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default", "default1")
                .verifyComplete();

    }

    @Test
    public void fluxErrorHandlingReturnOnError() {
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Excep....")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default");

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                .verifyComplete();

    }

    @Test
    public void fluxErrorHandlingOnErrorMap() {
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Excep....")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e -> new IllegalArgumentException("mapped Exc", e));

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(IllegalArgumentException.class)
                .verify();

    }

    @Test
    public void fluxErrorHandlingOnErrorMapRetry() {
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Excep....")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e -> new IllegalArgumentException("mapped Exc", e))
                .retry(2);

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalArgumentException.class)
                .verify();

    }

    // @Test
    public void fluxErrorHandlingOnErrorMapRetryBackoff() {
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Excep....")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e -> new IllegalArgumentException("mapped Exc", e))
                .retryBackoff(2, Duration.ofSeconds(2));

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalArgumentException.class)
                .verify();

    }
}
