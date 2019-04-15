package com.budovyy.service;

import com.budovyy.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> authorize(User user);

    Optional<User> addUser(User user);

    Optional<User> findByToken(String token);
}
