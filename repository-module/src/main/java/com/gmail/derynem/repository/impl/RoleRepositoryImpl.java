package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.repository.exception.RoleRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl implements RoleRepository {
    private final static Logger logger = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    @Override
    public List<String> getListOfRoleNames(Connection connection) {
        String sqlQuery = "SELECT F_NAME FROM T_ROLE";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<String> roleNames = new ArrayList<>();
            while (resultSet.next()) {
                roleNames.add(resultSet.getString(1));
            }
            logger.info("Role name founded, count of names is:{}", roleNames.size());
            return roleNames;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RoleRepositoryException("error at Method getListOfRoleName at repository module" + e.getMessage(), e);
        }
    }

    @Override
    public Long getRoleIdByRoleName(Connection connection, String role) {
        String sqlQuery = "SELECT F_ID FROM T_ROLE WHERE F_NAME =?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, role);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong(1);
                    logger.info("roleId found , role name was :{}, id this role is {}", role, id);
                    return id;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RoleRepositoryException("error at Method getRoleIdByRoleName at repository module" + e.getMessage(), e);
        }
        return null;
    }
}
