package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.UserRepository;
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
import java.util.ArrayList;
import java.util.List;

import static com.gmail.derynem.repository.constants.DataBaseVariables.OFFSET_LIMIT;

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
    public User getUserByEmail(String email, Connection connection) {
        String sqlQuery = "SELECT * FROM T_USER AS U LEFT JOIN T_ROLE AS R ON U.F_ROLE_ID = R.F_ID" +
                "  WHERE F_EMAIL =?"; //TODO CHANGE LATER FOR LIST OF PERMISSIONS LATER
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
    public List<User> getUsersWithOffset(Connection connection, Integer page) {
        String sqlQuery = "SELECT * FROM T_USER AS U LEFT JOIN T_ROLE AS R ON U.F_ROLE_ID = R.F_ID " +
                " WHERE F_DELETED = FALSE ORDER BY F_EMAIL LIMIT ?,?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, page);
            preparedStatement.setLong(2, OFFSET_LIMIT);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    users.add(getUser(resultSet));
                }
                return users;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException("error at Method getUsers!" + e.getMessage(), e);
        }
    }

    @Override
    public int updateUserRole(Connection connection, Long roleId, Long id) {
        String sqlQuery = "UPDATE T_USER SET F_ROLE_ID=? WHERE F_ID=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, roleId);
            preparedStatement.setLong(2, id);
            int row = preparedStatement.executeUpdate();
            logger.info("Updated successfully, updated user id:{} , new role id:{}", roleId, id);
            return row;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException("error at Method updateUserRole!" + e.getMessage(), e);
        }
    }

    @Override
    public int deleteUsers(Connection connection, int[] ids) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE T_USER SET F_DELETED= TRUE WHERE ");
        for (int i = 0; i < ids.length; i++) {
            if (i == 0) {
                stringBuilder.append("F_ID = ").append(ids[i]);
            } else {
                stringBuilder.append(" OR F_ID = ").append(ids[i]);
            }
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(stringBuilder.toString())) {
            int rows = preparedStatement.executeUpdate();
            logger.info(" users deleted , count of deleted users is:{}", rows);
            return rows;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException("error at method delete users at repository module|" + e.getMessage(), e);
        }
    }

    @Override
    public int getCountOfUsers(Connection connection) {
        String sqlQuery = "SELECT COUNT(F_ID) FROM T_USER WHERE F_DELETED = FALSE";
        int countOfUsers = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                countOfUsers = resultSet.getInt(1);
                logger.info("Count of users in database is {}", countOfUsers);
                return countOfUsers;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException("error at method getCountOfUsers at repository module|" + e.getMessage(), e);
        }
        return countOfUsers;
    }

    @Override
    public int changePassword(Connection connection, String password, Long id) {
        String sqlQuery = "UPDATE T_USER SET F_PASSWORD =? WHERE F_ID =?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, password);
            preparedStatement.setLong(2, id);
            int row = preparedStatement.executeUpdate();
            logger.info(" users in database changed :{}", row);
            return row;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException("error at method changePassword at repository module" + e.getMessage(), e);
        }
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
        user.setEmail(resultSet.getString("F_EMAIL"));
        user.setDeleted(resultSet.getBoolean("F_DELETED"));
        Role role = new Role();
        role.setId(resultSet.getLong("F_ROLE_ID"));
        role.setName(resultSet.getString(10));
        user.setRole(role);
        return user;
    }
}
