package com.gmail.derynem.service;

import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.exception.UserRepositoryException;
import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.UserConverter;
import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.impl.UserServiceImpl;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.role.RoleDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.util.Collections;

import static java.util.Arrays.asList;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private PageService pageService;
    @Mock
    private RandomService randomService;
    @Mock
    Connection connection;
    @Mock
    private EncoderService encoderService;
    private UserService userService;
    private UserDTO validUserDTO;
    private RoleDTO validRoleDTO;
    private User validUser;
    private Role validRole;
    private int countOfPages = 4;
    private int countOfUsers = 4;
    private PageDTO<UserDTO> usersPageDTO = new PageDTO<>();

    @Before
    public void setUp() {
        Mockito.when(userRepository.getConnection()).thenReturn(connection);
        userService = new UserServiceImpl(userRepository, userConverter, pageService, randomService, encoderService);
        validRoleDTO = new RoleDTO(1L, "CUSTOMER");
        validRole = new Role(1L, "CUSTOMER");
        validUser = new User(1L, "test", "test", "test", "mail#mail", "test", validRole);
        validUserDTO = new UserDTO(1L, "test", "test", "test", "mail#mail", "test", validRoleDTO);
        usersPageDTO.setCountOfPages(4);
        usersPageDTO.setObjects(asList(validUserDTO, validUserDTO));
    }

    @Test
    public void shouldGetUserByEmail() {
        String email = "test";
        Mockito.when(userRepository.getByEmail(email, connection)).thenReturn(validUser);
        Mockito.when(userConverter.toDTO(validUser)).thenReturn(validUserDTO);
        UserDTO expectedUser = userService.getUserByEmail(email);
        Assert.assertNotNull(expectedUser);
    }

    @Test
    public void shouldReturnNullIfUserDoesntExist() {
        String email = "test";
        Mockito.when(userRepository.getByEmail(email, connection)).thenReturn(null);
        UserDTO expectedUser = userService.getUserByEmail(email);
        Assert.assertNull(expectedUser);
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowUSerExceptionAtMethodGetUserByEmailIfSmthDoWrong() {
        String email = "test";
        Mockito.when(userRepository.getByEmail(email, connection)).thenThrow(UserRepositoryException.class);
        userService.getUserByEmail(email);
    }

    @Test
    public void shouldGetListOfUsers() {
        int offset = 1;
        Mockito.when(userRepository.getUsersWithOffset(connection, offset)).thenReturn(asList(validUser, validUser));
        Mockito.when(userConverter.toDTO(validUser)).thenReturn(validUserDTO);
        PageDTO<UserDTO> pageDTO = userService.getUsersPageInfo(offset);
        Assert.assertNotNull(pageDTO.getObjects());
    }

    @Test
    public void shouldGetEmptyList() {
        int offset = 1;
        Mockito.when(userRepository.getUsersWithOffset(connection, offset)).thenReturn(Collections.emptyList());
        PageDTO<UserDTO> pageDTO = userService.getUsersPageInfo(offset);
        Assert.assertEquals(Collections.emptyList(), pageDTO.getObjects());
    }

    @Test
    public void shouldDeleteUsers() {
        int[] ids = {3, 4};
        Mockito.when(userRepository.deleteUsers(connection, ids)).thenReturn(2);
        userService.deleteUsers(ids);
    }

    @Test
    public void shouldGetPages() {
        Mockito.when(userRepository.getCountOfUsers(connection)).thenReturn(countOfUsers);
        Mockito.when(pageService.getPages(countOfPages)).thenReturn(countOfPages);
        PageDTO pageDTO = userService.getUsersPageInfo(1);
        Assert.assertEquals(countOfPages, pageDTO.getCountOfPages());
    }

    @Test
    public void shouldGetPagesIfZeroUsersInDatabase() {
        int count = 0;
        Mockito.when(userRepository.getCountOfUsers(connection)).thenReturn(count);
        Mockito.when(pageService.getPages(count)).thenReturn(countOfPages);
        PageDTO<UserDTO> pageDTO = userService.getUsersPageInfo(1);
        Assert.assertEquals(countOfPages, pageDTO.getCountOfPages());
    }
}