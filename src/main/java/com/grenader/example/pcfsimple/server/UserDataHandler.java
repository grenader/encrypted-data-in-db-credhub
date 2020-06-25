package com.grenader.example.pcfsimple.server;


import com.grenader.example.pcfsimple.server.dao.DataService;
import com.grenader.example.pcfsimple.server.model.User;
import com.grenader.example.pcfsimple.server.dao.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component

public class UserDataHandler {

    private final DataService dataService;

    public UserDataHandler(UserRepository userRepository, DataService dataService) {
        this.dataService = dataService;
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {

        final Optional<String> name = request.queryParam("name");
        final Optional<String> email = request.queryParam("email");

        User user = dataService.createUser(name, email);

        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue("Hello, the user has been created. userId=" + user.getId()));
    }

    public Mono<ServerResponse> listUser(ServerRequest serverRequest) {

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.<Iterable<User>>fromCallable(() -> dataService.getAllUsers()), Iterable.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> countOfUser(ServerRequest serverRequest) {

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.<Long>fromCallable(() -> dataService.getUsersCount()), Long.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }


}
