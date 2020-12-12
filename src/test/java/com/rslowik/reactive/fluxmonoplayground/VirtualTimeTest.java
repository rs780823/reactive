package com.rslowik.reactive.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

/**
 * Created by rslowik
 * 08.12.20
 */
public class VirtualTimeTest {

    @Test
    public void withoutVirtualTime() throws InterruptedException {

        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
                .take(3)
                .log();

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(0l, 1l, 2l)
                .verifyComplete();
    }

    @Test
    public void withVirtualTime() throws InterruptedException {

        VirtualTimeScheduler.getOrSet();

        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
                .take(5);

        StepVerifier.withVirtualTime(flux::log)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(10))
                .expectNext(0l, 1l, 2l, 3l, 4l)
             //   .thenAwait() /// without it VirtualTimeScheduler isn't going to work
                .verifyComplete();
    }
}
