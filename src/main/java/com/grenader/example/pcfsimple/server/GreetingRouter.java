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
                                                UserDataHandler userDataHandler,
                                                CreditCardDataHandler creditCardDataHandler) {

        return RouterFunctions
                .route(RequestPredicates.GET("/hello").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), greetingHandler::hello).
                andRoute(RequestPredicates.GET("/prop").

                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), displayPropHandler::properties).
                andRoute(RequestPredicates.GET("/data/user/add").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), userDataHandler::createUser).
                andRoute(RequestPredicates.GET("/data/user/list").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), userDataHandler::listUser).
                andRoute(RequestPredicates.GET("/data/user/count").

                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), userDataHandler::countOfUser).
                andRoute(RequestPredicates.GET("/data/creditcard/add").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), creditCardDataHandler::createCreditCard).
                andRoute(RequestPredicates.GET("/data/creditcard/list").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), creditCardDataHandler::listCreditCard).
                andRoute(RequestPredicates.GET("/data/creditcard/count").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), creditCardDataHandler::countOfCreditCard).

                andRoute(RequestPredicates.GET("/data/creditcard/db/{id}").
                        and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), creditCardDataHandler::getEncryptedCreditCard);
    }
}