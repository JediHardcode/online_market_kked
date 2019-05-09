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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.gmail.derynem.repository.constants.DataBaseVariables.OFFSET_LIMIT;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

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
    private UserService userService;
    private UserDTO validUserDTO;
    private RoleDTO validRoleDTO;
    private User validUser;
    private Role validRole;

    @Before
    public void setUp() {
        Mockito.when(userRepository.getConnection()).thenReturn(connection);
        userService = new UserServiceImpl(userRepository, userConverter, pageService, randomService);
        validRoleDTO = new RoleDTO(1L, "CUSTOMER");
        validRole = new Role(1L, "CUSTOMER");
        validUser = new User(1L, "test", "test", "test", "mail#mail", "test", validRole);
        validUserDTO = new UserDTO(1L, "test", "test", "test", "mail#mail", "test", validRoleDTO);
    }

    @Test
    public void shouldGetUserByEmail() {
        String email = "test";
        Mockito.when(userRepository.getUserByEmail(email, connection)).thenReturn(validUser);
        Mockito.when(userConverter.toDTO(validUser)).thenReturn(validUserDTO);
        UserDTO expectedUser = userService.getUserByEmail(email);
        Assert.assertNotNull(expectedUser);
    }

    @Test
    public void shouldReturnNullIfUserDoesntExist() {
        String email = "test";
        Mockito.when(userRepository.getUserByEmail(email, connection)).thenReturn(null);
        UserDTO expectedUser = userService.getUserByEmail(email);
        Assert.assertNull(expectedUser);
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowUSerExceptionAtMethodGetUserByEmailIfSmthDoWrong() {
        String email = "test";
        Mockito.when(userRepository.getUserByEmail(email, connection)).thenThrow(UserRepositoryException.class);
        UserDTO expectedUser = userService.getUserByEmail(email);
    }

    @Test
    public void shouldGetListOfUsers() {
        int offset = 1;
        Mockito.when(userRepository.getUsersWithOffset(connection, offset)).thenReturn(asList(validUser, validUser));
        Mockito.when(userConverter.toDTO(validUser)).thenReturn(validUserDTO);
        List<UserDTO> userDTOS = userService.getUsers(offset);
        Assert.assertNotNull(userDTOS);
    }

    @Test
    public void shouldGetEmptyList() {
        int offset = 1;
        Mockito.when(userRepository.getUsersWithOffset(connection, offset)).thenReturn(Collections.emptyList());
        List<UserDTO> userDTOS = userService.getUsers(offset);
        Assert.assertEquals(Collections.emptyList(), userDTOS);
    }

    @Test
    public void shouldDeleteUsers() {
        int[] ids = {3, 4};
        Mockito.when(userRepository.deleteUsers(connection, ids)).thenReturn(2);
        userService.deleteUsers(ids);
    }

    @Test
    public void shouldGetPagesDTO() {
        int count = 4;
        Mockito.when(userRepository.getCountOfUsers(connection)).thenReturn(count);
        int countOfPages = (count + OFFSET_LIMIT - 1) / OFFSET_LIMIT;
        Mockito.when(pageService.getPages(countOfPages)).thenReturn(new PageDTO());
        PageDTO pageDTO = userService.getPages();
        Assert.assertNotNull(pageDTO);
    }

    @Test
    public void shouldGetPageDTOifNoUsersInDatabase() {
        int count = 0;
        Mockito.when(userRepository.getCountOfUsers(connection)).thenReturn(count);
        int countOfPages = (count + OFFSET_LIMIT - 1) / OFFSET_LIMIT;
        Mockito.when(pageService.getPages(countOfPages)).thenReturn(new PageDTO());
        PageDTO pageDTO = userService.getPages();
        Assert.assertNotNull(pageDTO);
    }
}