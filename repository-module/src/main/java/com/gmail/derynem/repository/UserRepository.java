package com.gmail.derynem.repository;

import com.gmail.derynem.repository.model.User;

import java.sql.Connection;

public interface UserRepository {
    User add(User user, Connection connection);

    int changeRole(Long roleId, Connection connection);

    User getUserByEmail(String email, Connection connection);

    int deleteUser(Long id, Connection connection);
}
