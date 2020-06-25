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

        // Add a new credit card
        webTestClient
                .get().uri("/data/creditcard/add?name=" + cardHolderName + "&number=" + cardNumber +
                "&expiration=" + cardExpiration + "&cvv=" + cvv)
                .exchange()
                .expectStatus().isOk();

        // find the credit card in the list
        final CreditCard expectedCard = new CreditCard(cardHolderName, cardNumber, cardExpiration, cvv);
        webTestClient
                .get().uri("/data/creditcard/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CreditCard.class).contains(expectedCard);

        //Load encrypted data:
        //todo: add more validation

        System.out.println("expectedCard = " + expectedCard);
    }


}