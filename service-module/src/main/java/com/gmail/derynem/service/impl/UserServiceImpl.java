package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.EncoderService;
import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.RandomService;
import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.converter.UserConverter;
import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PageService pageService;
    private final RandomService randomService;
    private final EncoderService encoderService;

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter,
                           PageService pageService, RandomService randomService,
                           EncoderService encoderService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.pageService = pageService;
        this.randomService = randomService;
        this.encoderService = encoderService;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = userRepository.getByEmail(email, connection);
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
                throw new UserServiceException("Error at method getByEmail at Service module:" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserServiceException("Error at method getByEmail at Service module:" + e.getMessage(), e);
        }
    }

    @Override
    public PageDTO<UserDTO> getUsersPageInfo(Integer page) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                PageDTO<UserDTO> userPageDTO = new PageDTO<>();
                int countOfUsers = userRepository.getCountOfUsers(connection);
                int countOfPages = pageService.getPages(countOfUsers);
                userPageDTO.setCountOfPages(countOfPages);
                int offset = pageService.getOffset(page, countOfPages);
                List<User> userList = userRepository.getUsersWithOffset(connection, offset);
                if (userList == null || userList.isEmpty()) {
                    logger.info("no available users in database");
                    connection.commit();
                    userPageDTO.setObjects(Collections.emptyList());
                    return userPageDTO;
                }
                List<UserDTO> users = userList.stream()
                        .map(userConverter::toDTO)
                        .collect(Collectors.toList());
                userPageDTO.setObjects(users);
                logger.info("Users found, list size is {}, count of pages of users is {}",
                        userPageDTO.getObjects().size(), userPageDTO.getCountOfPages());
                connection.commit();
                return userPageDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException("Error at Method getUsersPageInfo at service module" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserServiceException("Error at Method getUsersPageInfo at service module" + e.getMessage(), e);
        }
    }

    @Override
    public void updateUserRole(UpdateRoleDTO updateRoleDTO) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int row = userRepository.updateUserRole(connection, updateRoleDTO.getRoleId(), updateRoleDTO.getId());
                if (row != 0) {
                    logger.info("User role successfully updated , user id{} , new role id {}",
                            updateRoleDTO.getId(), updateRoleDTO.getRoleId());
                } else {
                    logger.info("сan not find user with this id {} in database ", updateRoleDTO.getId());
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException("Error at Method updateUserRole at service module" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserServiceException("Error at Method updateUserRole at service module" + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUsers(int[] ids) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int row = userRepository.deleteUsers(connection, ids);
                if (row != 0) {
                    logger.info("Users with ids {} successfully deleted", ids);
                } else {
                    logger.info(" not found users with this ids {}", ids);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.info(e.getMessage(), e);
                throw new UserServiceException("error at method deleted Users at service module|" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            throw new UserServiceException("error at method deleted Users at service module|" + e.getMessage(), e);
        }
    }

    @Override
    public void addUser(AddUserDTO userDTO) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User userWithThisEmail = userRepository.getByEmail(userDTO.getEmail(), connection);
                if (userWithThisEmail != null) {
                    logger.info("user with this email {} already exist in database", userDTO.getEmail());
                    return;
                }
                User user = userConverter.fromAddUserToUser(userDTO);
                user.setPassword(encoderService.encodePassword(randomService.generatePassword()));
                User savedUser = userRepository.add(user, connection);
                logger.info("user saved , user name :{}, user id is {}", savedUser.getName(), savedUser.getId());
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException("error at method Add user at service module||" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserServiceException("error at method Add user at service module||" + e.getMessage(), e);
        }
    }

    @Override
    public void changePassword(Long id) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String password = randomService.generatePassword();
                int row = userRepository.changePassword(connection, encoderService.encodePassword(password), id);
                String userEmail = userRepository.getUserEmailById(id, connection);
                if (row == 0) {
                    logger.info(" no user with this id{} in database ", id);
                } else {
                    logger.info(" new password {} have user with id {} and email {}", password, id, userEmail);
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException("error at method changePassword at service module" + e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserServiceException("error at method changePassword at service module" + e.getMessage(), e);
        }
    }
}