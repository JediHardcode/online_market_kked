package com.gmail.derynem.repository;

import com.gmail.derynem.repository.model.User;

import java.sql.Connection;
import java.util.List;

public interface UserRepository extends GenericRepository {
    User add(User user, Connection connection);

    User getByEmail(String email, Connection connection);

    List<User> getUsersWithOffset(Connection connection, Integer page);

    int updateUserRole(Connection connection, Long roleId, Long id);

    int deleteUsers(Connection connection, int[] ids);

    int getCountOfUsers(Connection connection);

    int changePassword(Connection connection, String password, Long id);

    String getUserEmailById(Long id, Connection connection);
}