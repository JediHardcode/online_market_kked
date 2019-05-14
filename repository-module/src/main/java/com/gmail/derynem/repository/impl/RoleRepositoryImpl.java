package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.repository.exception.RoleRepositoryException;
import com.gmail.derynem.repository.model.Role;
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
    public List<Role> getRoles(Connection connection) {
        String sqlQuery = "SELECT * FROM T_ROLE";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Role> roles = new ArrayList<>();
            while (resultSet.next()) {
                roles.add(getRole(resultSet));
            }
            logger.info("Roles founded, count of roles is:{}", roles.size());
            return roles;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RoleRepositoryException("error at Method getRoles at repository module" + e.getMessage(), e);
        }
    }

    private Role getRole(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("F_ID"));
        role.setName(resultSet.getString("F_NAME"));
        return role;
    }
}