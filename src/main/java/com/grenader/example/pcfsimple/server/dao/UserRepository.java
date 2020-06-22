package com.grenader.example.pcfsimple.server.dao;

import com.grenader.example.pcfsimple.server.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}