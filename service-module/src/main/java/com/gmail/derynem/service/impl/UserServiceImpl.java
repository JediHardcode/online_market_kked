package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.converter.UserConverter;
import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = userRepository.getUserByEmail(email, connection);
                if (user == null) {
                    logger.info("not found user with this email");
                    return null;
                }
                UserDTO foundUser = userConverter.toDTO(user);
                logger.info("user found! User name is:{},role :{}", foundUser.getName(), foundUser.getRole().getName());
                connection.commit();
                return foundUser;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException("Error at method getUserByEmail at Service module:" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserServiceException("Error at method getUserByEmail at Service module:" + e.getMessage(), e);
        }
    }
}
