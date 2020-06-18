package com.grenader.example.pcfsimple.server;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Configuration
public class DisplayCredHubPropertiesHandler {

    @Value("${username}")
    private String userName;

    @Value("${uservalue}")
    private String userValue;

    public Mono<ServerResponse> properties(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue("Configured properties: name="+userName+", value="+userValue));
    }
}
