package com.grenader.example.encrypteddata.server.dao;

import com.grenader.example.encrypteddata.server.model.CreditCard;
import org.springframework.data.repository.CrudRepository;

public interface CCRepository extends CrudRepository<CreditCard, Integer> {

}