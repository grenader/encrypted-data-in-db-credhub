package com.grenader.example.encrypteddata.server.dao;

import com.grenader.example.encrypteddata.server.model.CreditCard;
import com.grenader.example.encrypteddata.server.model.User;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class DataService {

    private final UserRepository userRepository;

    private final CCRepository ccRepository;
    private final EntityManager entityManager;

    public DataService(UserRepository userRepository,
                       CCRepository ccRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.ccRepository = ccRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public CreditCard getCreditCardViaNativeQuery(long ccId) {
        // getting Hibernate session using JPA 2.0 approach. @Transactional is required.
        Session session = entityManager.unwrap(org.hibernate.Session.class);

        CreditCard ccFromDB = session.createNativeQuery(
                "select * from credit_card c where c.id = :id", CreditCard.class)
                .setParameter("id", ccId)
                .getSingleResult();
        return ccFromDB;
    }

    @Transactional
    public CreditCard getEncryptedCreditCardData(long ccId) {
        // getting Hibernate session using JPA 2.0 approach. @Transactional is required.
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        System.out.println("session = " + session);

        Query query = session.createNativeQuery(
                "select id, name, number, expiration, cvv from credit_card c where c.id = :id");
        query.setParameter("id", ccId);

        Object[] arrayResult = (Object[]) query.getSingleResult();

        final CreditCard creditCard = new CreditCard((String)arrayResult[1], (String)arrayResult[2], (String)arrayResult[3], (String)arrayResult[4]);
        creditCard.setId( ((BigInteger)arrayResult[0]).longValue());

        return creditCard;
    }


    public User createUser(Optional<String> name, Optional<String> email) {
        User user = new User();
        user.setName(name.orElse("userName"));
        user.setEmail(email.orElse("user@test.com"));
        userRepository.save(user);
        return user;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public long getUsersCount() {
        return userRepository.count();
    }


    public CreditCard createCreditCard(Optional<String> cardholderName, Optional<String> ccNumber,
                                       Optional<String> expiration, Optional<String> cvv) {
        CreditCard creditCard = new CreditCard();
        creditCard.setName(cardholderName.orElse("userName"));
        creditCard.setNumber(ccNumber.orElse("411111111111111"));
        creditCard.setExpiration(expiration.orElse("11/21"));
        creditCard.setCvv(cvv.orElse("123"));
        ccRepository.save(creditCard);
        return creditCard;
    }

    public Iterable<CreditCard> getAllCreditCards() {
        return ccRepository.findAll();
    }

    public long getCreditCardsCount() {
        return ccRepository.count();
    }

}
