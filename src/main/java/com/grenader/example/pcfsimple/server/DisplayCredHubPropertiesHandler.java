package com.grenader.example.pcfsimple.server;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Provider;
import java.security.Security;

@Component
@Configuration
public class DisplayCredHubPropertiesHandler {

    @Value("${username}")
    private String userName;

    @Value("${uservalue}")
    private String userValue;

    @Value("${dbsecret}")
    private String dbsecret;

    public Mono<ServerResponse> properties(ServerRequest request) {

        StringBuilder builder = new StringBuilder(System.getProperty("java.version")).append("\n");
        builder.append("Security Providers").append("\n");
        for (Provider provider : Security.getProviders())
            builder.append(provider).append("\n");

        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue("Configured properties: name="+userName+", value="+userValue+", dbsecret="+dbsecret+"\n"+builder));
    }
}
