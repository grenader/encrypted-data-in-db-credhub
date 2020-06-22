package com.grenader.example.pcfsimple.server;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GreetingRouter {

    @Bean
    public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler,
                                                DisplayCredHubPropertiesHandler displayPropHandler,
                                                DataHandler dataHandler) {

        return RouterFunctions
                .route(RequestPredicates.GET("/hello").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), greetingHandler::hello).
                andRoute(RequestPredicates.GET("/prop").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), displayPropHandler::properties).
                andRoute(RequestPredicates.GET("/data/add").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), dataHandler::createUser).
                andRoute(RequestPredicates.GET("/data/list").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), dataHandler::listUser).
                andRoute(RequestPredicates.GET("/data/count").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), dataHandler::countOfUser);

    }
}