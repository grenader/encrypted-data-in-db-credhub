package com.grenader.example.encrypteddata.server.dao;

import com.grenader.example.encrypteddata.server.model.CreditCard;
import com.grenader.example.encrypteddata.server.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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

    @Autowired
    private Environment env;

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
    void testGetEncryptedCreditCardData() throws NoSuchPaddingException, NoSuchAlgorithmException {
        final CreditCard newCard = dataService.createCreditCard(Optional.of("First Name Last Name"),
                Optional.of("4123123123123"),
                Optional.of("09/23"), Optional.of("890"));

        final CreditCard loadedCard = dataService.getEncryptedCreditCardData(newCard.getId());

        System.out.println("creditCardDirectData = " + loadedCard);

        assertEquals(newCard.getId(), loadedCard.getId());
        assertEquals(newCard.getCvv(), loadedCard.getCvv());

        assertNotEquals(loadedCard.getName(), newCard.getName());

        // decrypt DB values using the same way how AttributeEncryptor works
        assertEquals(newCard.getName(), decryptData(loadedCard.getName()));
        assertEquals(newCard.getNumber(), decryptData(loadedCard.getNumber()));
    }

    private String decryptData(String dbData) throws NoSuchAlgorithmException, NoSuchPaddingException {
        String secret = env.getProperty("dbsecret");
        if (StringUtils.isEmpty(secret))
            throw new IllegalStateException("dbsecret application parameter was not configured!");

        String AES = "AES";
        Key key = new SecretKeySpec(secret.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(AES);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        }
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