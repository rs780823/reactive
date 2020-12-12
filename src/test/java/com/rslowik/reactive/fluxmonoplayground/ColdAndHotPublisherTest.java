package com.rslowik.reactive.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Created by rslowik
 * 08.12.20
 */
public class ColdAndHotPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {

        Flux<String> flux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1))
                .log();

        flux.subscribe(s -> System.out.println("Subscriber 1: " + s)); // emits the value from the beginning
        Thread.sleep(2000l);

        flux.subscribe(s -> System.out.println("Subscriber 2: " + s)); // emits the value from the beginning
        Thread.sleep(4000l);

    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        Flux<String> flux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));
           //     .log();

        ConnectableFlux<String> connectableFlux = flux.publish();
        connectableFlux.connect();
        connectableFlux.subscribe(s -> System.out.println("Subscriber 1: " + s));

        Thread.sleep(2000l);
        connectableFlux.subscribe(s -> System.out.println("Subscriber 2: " + s));
        Thread.sleep(4000l);
    }
}
