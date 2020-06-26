package com.grenader.example.encrypteddata.server.dao;

import com.grenader.example.encrypteddata.server.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}