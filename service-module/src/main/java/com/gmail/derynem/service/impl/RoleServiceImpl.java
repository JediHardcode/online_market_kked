package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.service.RoleService;
import com.gmail.derynem.service.exception.RoleServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<String> getListOfRoleNames() {
        try (Connection connection = roleRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<String> roleNames = roleRepository.getListOfRoleNames(connection);
                if (roleNames == null || roleNames.isEmpty()) {
                    logger.info("No roles in database, add some");
                    return Collections.emptyList();
                }
                logger.info("List of names received from database, list size is{}", roleNames.size());
                connection.commit();
                return roleNames;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new RoleServiceException("Error at method getListOfRoleName at service module," + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RoleServiceException("Error at method getListOfRoleName at service module," + e.getMessage(), e);
        }
    }
}
