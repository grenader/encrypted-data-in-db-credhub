package com.grenader.example.encrypteddata.server;


import com.grenader.example.encrypteddata.server.dao.DataService;
import com.grenader.example.encrypteddata.server.model.CreditCard;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component

public class CreditCardDataHandler {

    private final DataService dataService;

    public CreditCardDataHandler(DataService dataService) {
        this.dataService = dataService;
    }

    public Mono<ServerResponse> createCreditCard(ServerRequest request) {

        final Optional<String> name = request.queryParam("name");
        final Optional<String> number = request.queryParam("number");
        final Optional<String> expiration = request.queryParam("expiration");
        final Optional<String> cvv = request.queryParam("cvv");

        final CreditCard creditCard = dataService.createCreditCard(name, number, expiration, cvv);
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue("Hello, the credit card has been created. creditCardId=" + creditCard.getId()));

    }

    public Mono<ServerResponse> listCreditCard(ServerRequest request) {

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.fromCallable(() -> dataService.getAllCreditCards()), Iterable.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> countOfCreditCard(ServerRequest request) {

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.fromCallable(() -> dataService.getCreditCardsCount()), Long.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getEncryptedCreditCard(ServerRequest request) {

        final String idStr = request.pathVariable("id");
        final long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Id was not a number!");
        }

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.fromCallable(() -> dataService.getEncryptedCreditCardData(id)), CreditCard.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }


}
