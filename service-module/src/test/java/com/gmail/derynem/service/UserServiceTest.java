package com.gmail.derynem.service;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.converter.user.UserConverterAssembler;
import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.impl.UserServiceImpl;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.role.RoleDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static java.util.Arrays.asList;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverterAssembler userConverterAssembler;
    @Mock
    private Converter<AddUserDTO, User> addUserDTOUserConverter;
    @Mock
    private Converter<UserDTO, User> userDTOUserConverter;
    @Mock
    private PageService pageService;
    @Mock
    private RandomService randomService;
    @Mock
    private EncoderService encoderService;
    private UserService userService;
    private UserDTO validUserDTO;
    private RoleDTO validRoleDTO;
    private User validUser;
    private Role validRole;
    private int countOfPages = 4;
    private int countOfUsers = 4;
    private int limit = 10;
    private PageDTO<UserDTO> usersPageDTO = new PageDTO<>();

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userRepository, userConverterAssembler, pageService, randomService, encoderService, roleRepository);
        validRoleDTO = new RoleDTO(1L, "CUSTOMER");
        validRole = new Role(1L, "CUSTOMER");
        validUser = new User(1L, "test", "test", "test", "mail#mail", "test", validRole);
        validUserDTO = new UserDTO(1L, "test", "test", "test", "mail#mail", "test", validRoleDTO);
        usersPageDTO.setCountOfPages(4);
        usersPageDTO.setEntities(asList(validUserDTO, validUserDTO));
        Mockito.when(userConverterAssembler.getAddUserConverter()).thenReturn(addUserDTOUserConverter);
        Mockito.when(userConverterAssembler.getUserConverter()).thenReturn(userDTOUserConverter);
        Mockito.when(userDTOUserConverter.toDTO(validUser)).thenReturn(validUserDTO);
    }

    @Test
    public void shouldGetUserByEmail() {
        String email = "test";
        Mockito.when(userRepository.getByEmail(email, false)).thenReturn(validUser);
        UserDTO expectedUser = userService.getUserByEmail(email, false);
        Assert.assertNotNull(expectedUser);
    }

    @Test
    public void shouldReturnNullIfUserDoesntExist() {
        String email = "test";
        Mockito.when(userRepository.getByEmail(email, false)).thenReturn(null);
        UserDTO expectedUser = userService.getUserByEmail(email, false);
        Assert.assertNull(expectedUser);
    }

    @Test
    public void shouldGetListOfUsers() {
        int offset = 1;
        Mockito.when(userRepository.findAll(offset, 10)).thenReturn(asList(validUser, validUser));
        PageDTO<UserDTO> pageDTO = userService.getUsersPageInfo(offset, limit);
        Assert.assertNotNull(pageDTO.getEntities());
    }

    @Test
    public void shouldGetEmptyList() {
        int offset = 1;
        Mockito.when(userRepository.findAll(offset, 10)).thenReturn(Collections.emptyList());
        PageDTO<UserDTO> pageDTO = userService.getUsersPageInfo(offset, limit);
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
        Mockito.when(pageService.getPages(countOfPages, limit)).thenReturn(countOfPages);
        PageDTO pageDTO = userService.getUsersPageInfo(1, limit);
        Assert.assertEquals(countOfPages, pageDTO.getCountOfPages());
    }

    @Test
    public void shouldGetPagesIfZeroUsersInDatabase() {
        int count = 0;
        Mockito.when(userRepository.getCountOfEntities()).thenReturn(count);
        Mockito.when(pageService.getPages(count, limit)).thenReturn(countOfPages);
        PageDTO<UserDTO> pageDTO = userService.getUsersPageInfo(1, limit);
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