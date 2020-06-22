package com.grenader.example.pcfsimple.server.dao;

import com.grenader.example.pcfsimple.server.model.CreditCard;
import org.springframework.data.repository.CrudRepository;

public interface CCRepository extends CrudRepository<CreditCard, Integer> {

}