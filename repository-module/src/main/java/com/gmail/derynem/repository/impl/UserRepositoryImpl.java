package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.connection.impl.GenericRepositoryImpl;
import com.gmail.derynem.repository.exception.UserRepositoryException;
import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public User add(User user, Connection connection) {
        String addUserQuery = "INSERT INTO T_USER(F_EMAIL,F_NAME,F_SURNAME,F_MIDDLE_NAME,F_PASSWORD,F_ROLE_ID)" +
                " VALUES (?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurName());
            preparedStatement.setString(4, user.getMiddleName());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setLong(6, user.getRole().getId());
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    User saved = getUserWithId(resultSet, user);
                    logger.info("user with name [{}] and id [{}] added to database", saved.getName(), saved.getId());
                    return saved;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException("error at Method addUser at repository level: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public int changeRole(Long roleId, Connection connection) {
        return 0;
    }

    @Override
    public User getUserByEmail(String email, Connection connection) {
        String sqlQuery = "SELECT * FROM T_USER AS U LEFT JOIN T_ROLE AS R ON U.F_ROLE_ID = R.F_ID  WHERE F_EMAIL =?"; //TODO CHANGE LATER FOR LIST OF PERMISSIONS
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = getUser(resultSet);
                    logger.info("User found, name:{}, role:{}", user.getName(), user.getRole().getName());
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException("error at Method getUserByEmail at repository level" + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public int deleteUser(Long id, Connection connection) {
        return 0;
    }

    private User getUserWithId(ResultSet resultSet, User user) throws SQLException {
        User savedUser = new User();
        savedUser.setId(resultSet.getLong(1));
        savedUser.setRole(user.getRole());
        savedUser.setPassword(user.getPassword());
        savedUser.setEmail(user.getEmail());
        savedUser.setMiddleName(user.getMiddleName());
        savedUser.setSurName(user.getSurName());
        savedUser.setName(user.getName());
        savedUser.setDeleted(user.getDeleted());
        return savedUser;
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong(1));
        user.setName(resultSet.getString("F_NAME"));
        user.setSurName(resultSet.getString("F_SURNAME"));
        user.setMiddleName(resultSet.getString("F_MIDDLE_NAME"));
        user.setPassword(resultSet.getString("F_PASSWORD"));
        user.setEmail(resultSet.getString("F_PASSWORD"));
        user.setDeleted(resultSet.getBoolean("F_DELETED"));
        Role role = new Role();
        role.setId(resultSet.getLong("F_ROLE_ID"));
        role.setName(resultSet.getString(10));
        user.setRole(role);
        return user;
    }
}
