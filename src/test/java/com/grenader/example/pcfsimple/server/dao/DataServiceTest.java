package com.grenader.example.pcfsimple.server.dao;

import com.grenader.example.pcfsimple.server.model.CreditCard;
import com.grenader.example.pcfsimple.server.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsIterableContaining.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles("test")
class DataServiceTest {

    @Autowired
    private DataService dataService;

    @Test
    void testGetPlainCreditCardData() {

        // Create a random card first
        final String cardholderName = "John" + System.currentTimeMillis();
        final String ccNumber = "456" + System.currentTimeMillis();
        final String expiry = "11/2021";
        final String cvv = "123";

        final CreditCard expectedCreditCard = dataService.createCreditCard(Optional.of(cardholderName),
                Optional.of(ccNumber), Optional.of(expiry),
                Optional.of(cvv));

        final CreditCard creditCardNative = dataService.getCreditCardViaNativeQuery(expectedCreditCard.getId());

        System.out.println("creditCardNative = " + creditCardNative);
        assertEquals(expectedCreditCard, creditCardNative);
    }


    @Test
    void testGetEncryptedCreditCardData() {

        final CreditCard newCard = dataService.createCreditCard(Optional.of("First Name Last Name"),
                Optional.of("4123123123123"),
                Optional.of("09/23"), Optional.of("890"));

        final CreditCard loadedCard = dataService.getEncryptedCreditCardData(newCard.getId());

        System.out.println("creditCardDirectData = " + loadedCard);

        assertEquals(loadedCard.getId(), newCard.getId());
        assertEquals(loadedCard.getCvv(), newCard.getCvv());

        assertNotEquals(loadedCard.getName(), newCard.getName());
        //todo: add more validation
    }

    @Test
    void createUser() {
        final String userName = "name-" + System.currentTimeMillis();
        final String userEmail = "name" + System.currentTimeMillis() + "@test.com";

        final long expectedNumberOfUsers = dataService.getUsersCount()+1;

        final User user = dataService.createUser(Optional.of(userName),
                Optional.of(userEmail));

        assertEquals(expectedNumberOfUsers, dataService.getUsersCount());

        assertThat(dataService.getAllUsers(), hasItems(
                new User(userName, userEmail)));
    }

    @Test
    void createCreditCard() {
        final String cardholderName = "John" + System.currentTimeMillis();
        final String ccNumber = "456" + System.currentTimeMillis();
        final String expiry = "11/2021";
        final String cvv = "123";

        final long expectedNumberOfUsers = dataService.getCreditCardsCount()+1;

        final CreditCard creditCard = dataService.createCreditCard(Optional.of(cardholderName),
                Optional.of(ccNumber), Optional.of(expiry),
                Optional.of(cvv));

        assertEquals(expectedNumberOfUsers, dataService.getCreditCardsCount());

        assertThat(dataService.getAllCreditCards(), hasItems(
                new CreditCard(cardholderName, ccNumber, expiry, cvv)));
    }
}