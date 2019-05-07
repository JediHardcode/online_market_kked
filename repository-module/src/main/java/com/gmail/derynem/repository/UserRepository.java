package com.gmail.derynem.repository;

import com.gmail.derynem.repository.model.User;

import java.sql.Connection;
import java.util.List;

public interface UserRepository extends GenericRepository {
    User add(User user, Connection connection);

    User getUserByEmail(String email, Connection connection);

    List<User> getUsersWithOffset(Connection connection,Integer page);

    int updateUserRole(Connection connection, Long roleId, Long id);

    int deleteUsers(Connection connection, int[] ids);

    int getCountOfUsers(Connection connection);
}
