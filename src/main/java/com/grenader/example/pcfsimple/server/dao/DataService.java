package com.grenader.example.pcfsimple.server.dao;

import com.grenader.example.pcfsimple.server.model.CreditCard;
import com.grenader.example.pcfsimple.server.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DataService {

    private final UserRepository userRepository;

    private final CCRepository ccRepository;

    public DataService(UserRepository userRepository, CCRepository ccRepository) {
        this.userRepository = userRepository;
        this.ccRepository = ccRepository;
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

    public long getCreditCardsCount() {
        return ccRepository.count();
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

}
