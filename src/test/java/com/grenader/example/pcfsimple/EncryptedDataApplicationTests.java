package com.grenader.example.pcfsimple;

import com.grenader.example.pcfsimple.server.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = EncryptedDataApplication.class)
public class EncryptedDataApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testTest() {
        final String userName = "sb-" + System.currentTimeMillis();
        final String userEmail = "sb" + System.currentTimeMillis() + "@test.com";

        // Add a new user
        webTestClient
                .get().uri("/data/add?name=" + userName + "&email=" + userEmail)
                .exchange()
                .expectStatus().isOk();

        // find the user in the list
        webTestClient
                .get().uri("/data/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class).contains(new User(userName, userEmail));
    }


}