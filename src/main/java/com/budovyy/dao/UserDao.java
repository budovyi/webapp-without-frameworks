package com.budovyy.dao;

import com.budovyy.model.User;

import java.util.Optional;

public interface UserDao {

    User addUser(User user);

    User getByToken(String token);

    Optional<User> getByUsername(String username);
}
