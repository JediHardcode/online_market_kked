package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.converter.UserConverter;
import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.model.AddUserDTO;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.derynem.repository.constants.DataBaseVariables.OFFSET_LIMIT;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RoleRepository roleRepository;
    private final PageService pageService;

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, RoleRepository roleRepository, PageService pageService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.roleRepository = roleRepository;
        this.pageService = pageService;
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

    @Override
    public List<UserDTO> getUsers(Integer page) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int offset = (page * OFFSET_LIMIT) - OFFSET_LIMIT;
                List<User> userList = userRepository.getUsersWithOffset(connection, offset);
                if (userList == null || userList.isEmpty()) {
                    logger.info("no available users in database");
                    return Collections.emptyList();
                }
                List<UserDTO> users = userList.stream()
                        .map(userConverter::toDTO)
                        .collect(Collectors.toList());
                logger.info("List of users found, list size is {}", users.size());
                connection.commit();
                return users;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException("Error at Method getUsers at service module" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserServiceException("Error at Method getUsers at service module" + e.getMessage(), e);
        }
    }

    @Override
    public void updateUserRole(String role, Long id) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Long roleId = roleRepository.getRoleIdByRoleName(connection, role);
                int row = userRepository.updateUserRole(connection, roleId, id);
                if (row != 0) {
                    logger.info("User role successfully updated , user id{} , new role {}", id, role);
                    connection.commit();
                } else {
                    logger.info("сan not find user with this id in database ");
                    connection.commit();
                }
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
                    connection.commit();
                } else {
                    logger.info(" not found users with this ids {}", Arrays.toString(ids));
                    connection.commit();
                }
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
    public PageDTO getPages() {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int countOfUsers = userRepository.getCountOfUsers(connection);
                int countOfPages = (countOfUsers + OFFSET_LIMIT - 1) / OFFSET_LIMIT; // TODO  make a separate service
                connection.commit();
                logger.info("count of users in database:{}, count of pages:{}", countOfUsers, countOfPages);
                return pageService.getPages(countOfPages);
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException("error at method getPages at service module|" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserServiceException("error at method getPages at service module|" + e.getMessage(), e);
        }
    }

    @Override
    public void addUser(AddUserDTO userDTO) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User userWithThisEmail = userRepository.getUserByEmail(userDTO.getEmail(), connection);
                if (userWithThisEmail != null) {
                    logger.info("user with this email {} already exist in database", userDTO.getEmail());
                    return;
                }
                User user = userConverter.fromAddUserToUser(userDTO);
                user.getRole().setId(roleRepository.getRoleIdByRoleName(connection, userDTO.getRole()));
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
}

