package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.service.RoleService;
import com.gmail.derynem.service.converter.RoleConverter;
import com.gmail.derynem.service.exception.RoleServiceException;
import com.gmail.derynem.service.model.role.RoleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;

    public RoleServiceImpl(RoleRepository roleRepository, RoleConverter roleConverter) {
        this.roleRepository = roleRepository;
        this.roleConverter = roleConverter;
    }

    @Override
    public List<RoleDTO> getRoles() {
        try (Connection connection = roleRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Role> roleNames = roleRepository.getRoles(connection);
                if (roleNames == null || roleNames.isEmpty()) {
                    logger.info("No roles in database, add some");
                    connection.commit();
                    return Collections.emptyList();
                }
                List<RoleDTO> roles = roleNames.stream()
                        .map(roleConverter::toDTO)
                        .collect(Collectors.toList());
                logger.info("List of roles received from database, list size is{}", roles.size());
                connection.commit();
                return roles;
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
