package com.rslowik.reactive.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Created by rslowik
 * 08.12.20
 */
public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest() {

        Flux<Integer> flux = Flux.range(1, 10)
                .log();
        StepVerifier.create(flux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();

    }

    @Test
    public void backPressureCustomized() throws InterruptedException {

        Flux<Integer> flux = Flux.range(1, 10)
                .log();

        flux.subscribe(
                new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnNext(Integer value) {
                        request(1);
                        System.out.println("Value received is: " + value);
                        if (value == 4) {
                            cancel();
                        }
                    }
                }
        );
    }
}
