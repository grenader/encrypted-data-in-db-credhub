package com.grenader.example.pcfsimple;

import com.grenader.example.pcfsimple.server.model.CreditCard;
import com.grenader.example.pcfsimple.server.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = EncryptedDataApplication.class)
public class EncryptedDataApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testAddUser() {
        final String userName = "sb-" + System.currentTimeMillis();
        final String userEmail = "sb" + System.currentTimeMillis() + "@test.com";

        // Add a new user
        webTestClient
                .get().uri("/data/user/add?name=" + userName + "&email=" + userEmail)
                .exchange()
                .expectStatus().isOk();

        // find the user in the list
        final User expectedUser = new User(userName, userEmail);
        webTestClient
                .get().uri("/data/user/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class).contains(expectedUser);

        System.out.println("expectedUser = " + expectedUser);
    }

    @Test
    public void testAddCreditCard() {
        final String cardHolderName = "name-" + System.currentTimeMillis();
        final String cardNumber = "123" + System.currentTimeMillis();
        final String cardExpiration = "10/20";
        final String cvv = (new Random().nextInt(256) + 1) + "";

        AtomicReference<Long> ccId = new AtomicReference<>();
        // Add a new credit card
        webTestClient.get().uri("/data/creditcard/add?name=" + cardHolderName + "&number=" + cardNumber +
                "&expiration=" + cardExpiration + "&cvv=" + cvv)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result -> {
                    final String responseStr = result.getResponseBody();
                    assertThat(responseStr, containsString("Hello, the credit card has been created"));
                    assertThat(responseStr, containsString("creditCardId="));

                    // reading an Id of created credit card
                    final String[] split = responseStr.split("=");
                    ccId.set(Long.valueOf(split[split.length - 1]));
                });
        System.out.println("creditCard.getId() = " + ccId.get());

        // find the credit card in the list
        final CreditCard expectedCard = new CreditCard(cardHolderName, cardNumber, cardExpiration, cvv);
        webTestClient
                .get().uri("/data/creditcard/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CreditCard.class).contains(expectedCard);

        //Load encrypted credit card data
        webTestClient
                .get().uri(("/data/creditcard/db/" + ccId.get()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                // validate only non-encrypted fields.
                .jsonPath("$.id").isEqualTo(ccId.get())
                .jsonPath("$.cvv").isEqualTo(cvv);
    }
}