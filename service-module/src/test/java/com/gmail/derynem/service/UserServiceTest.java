package com.gmail.derynem.service;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.UserConverter;
import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.impl.UserServiceImpl;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.role.RoleDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
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

import static com.gmail.derynem.service.constants.PageConstant.OFFSET_LIMIT;
import static java.util.Arrays.asList;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    @Mock
    private RoleRepository roleRepository;
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
        userService = new UserServiceImpl(userRepository, userConverter, pageService, randomService, encoderService, roleRepository);
        validRoleDTO = new RoleDTO(1L, "CUSTOMER");
        validRole = new Role(1L, "CUSTOMER");
        validUser = new User(1L, "test", "test", "test", "mail#mail", "test", validRole);
        validUserDTO = new UserDTO(1L, "test", "test", "test", "mail#mail", "test", validRoleDTO);
        usersPageDTO.setCountOfPages(4);
        usersPageDTO.setEntities(asList(validUserDTO, validUserDTO));
    }

    @Test
    public void shouldGetUserByEmail() {
        String email = "test";
        Mockito.when(userRepository.getByEmail(email)).thenReturn(validUser);
        Mockito.when(userConverter.toDTO(validUser)).thenReturn(validUserDTO);
        UserDTO expectedUser = userService.getUserByEmail(email);
        Assert.assertNotNull(expectedUser);
    }

    @Test
    public void shouldReturnNullIfUserDoesntExist() {
        String email = "test";
        Mockito.when(userRepository.getByEmail(email)).thenReturn(null);
        UserDTO expectedUser = userService.getUserByEmail(email);
        Assert.assertNull(expectedUser);
    }

    @Test
    public void shouldGetListOfUsers() {
        int offset = 1;
        Mockito.when(userRepository.findAll(offset, 10)).thenReturn(asList(validUser, validUser));
        Mockito.when(userConverter.toDTO(validUser)).thenReturn(validUserDTO);
        PageDTO<UserDTO> pageDTO = userService.getUsersPageInfo(offset);
        Assert.assertNotNull(pageDTO.getEntities());
    }

    @Test
    public void shouldGetEmptyList() {
        int offset = 1;
        Mockito.when(userRepository.findAll(offset, 10)).thenReturn(Collections.emptyList());
        PageDTO<UserDTO> pageDTO = userService.getUsersPageInfo(offset);
        Assert.assertEquals(Collections.emptyList(), pageDTO.getEntities());
    }

    @Test
    public void shouldDeleteUsers() throws UserServiceException {
        Long[] ids = {3L, 4L};
        Mockito.when(userRepository.getById(3L)).thenReturn(validUser);
        Mockito.when(userRepository.getById(4L)).thenReturn(validUser);
        userService.deleteUsers(ids);
    }

    @Test
    public void shouldGetPages() {
        Mockito.when(userRepository.getCountOfEntities()).thenReturn(countOfUsers);
        Mockito.when(pageService.getPages(countOfPages, OFFSET_LIMIT)).thenReturn(countOfPages);
        PageDTO pageDTO = userService.getUsersPageInfo(1);
        Assert.assertEquals(countOfPages, pageDTO.getCountOfPages());
    }

    @Test
    public void shouldGetPagesIfZeroUsersInDatabase() {
        int count = 0;
        Mockito.when(userRepository.getCountOfEntities()).thenReturn(count);
        Mockito.when(pageService.getPages(count, OFFSET_LIMIT)).thenReturn(countOfPages);
        PageDTO<UserDTO> pageDTO = userService.getUsersPageInfo(1);
        Assert.assertEquals(countOfPages, pageDTO.getCountOfPages());
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowServiceExceptionIfTryDeleteNotExistUser() throws UserServiceException {
        Mockito.when(userRepository.getById(3L)).thenReturn(null);
        Long[] ids = {3L, 4L};
        userService.deleteUsers(ids);
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowServiceExceptionIfTryToUpdateNotExistUser() throws UserServiceException {
        UpdateRoleDTO updateRoleDTO = new UpdateRoleDTO();
        updateRoleDTO.setRoleId(1L);
        updateRoleDTO.setId(2L);
        Mockito.when(userRepository.getById(2L)).thenReturn(null);
        userService.updateUserRole(updateRoleDTO);
    }
}