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

@SpringBootTest
@ActiveProfiles("test")
class DataServiceTest {

    @Autowired
    private DataService dataService;

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