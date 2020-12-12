package com.rslowik.reactive.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * Created by rslowik
 * 08.12.20
 */
public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {

        Flux<Long> flux = Flux.interval(Duration.ofMillis(200))
                .log();
        flux.subscribe(element -> System.out.println("Value is: " + element));
        Thread.sleep(1000l);

    }

    @Test
    public void infiniteSequenceMapWithDelay() throws InterruptedException {

        Flux<Integer> flux = Flux.interval(Duration.ofMillis(200))
                //   .delayElements(Duration.ofSeconds(1))
                .take(5)
                .map(i -> Integer.valueOf(i.toString()))
                .log();
        flux.subscribe(element -> System.out.println("Value is: " + element));

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(0, 1, 2, 3, 4)
                .verifyComplete();

    }
}
