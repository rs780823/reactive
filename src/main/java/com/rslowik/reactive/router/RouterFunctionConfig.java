package com.rslowik.reactive.router;

import com.rslowik.reactive.handler.HandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Created by rslowik
 * 10.12.20
 */
@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> route(HandlerFunction handlerFunction) {
        return RouterFunctions.route(GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON)), handlerFunction::flux)
                .andRoute(GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON)), handlerFunction::mono);
    }

}

