package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.RandomService;
import com.gmail.derynem.service.model.role.RoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_PRIVATE_USERS;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
    @Autowired
    private RandomService randomService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserController userController;
    @Mock
    BindingResult bindingResult;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void shouldGetUsersPageWithUsers() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("updateObj"));
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void shouldRedirectAtUsersPage() {
        Model model = new ExtendedModelMap();
        UpdateRoleDTO updateRoleDTO = new UpdateRoleDTO();
        String url = userController.showUsers(model, 1, updateRoleDTO);
        Assert.assertEquals("users", url);
    }

    @Test
    @WithMockUser(authorities = {"CUSTOMER"})
    public void shouldRedirectIfCustomerGoToUsersPage() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void shouldUpdateRoleUser() {
        UpdateRoleDTO updateRoleDTO = new UpdateRoleDTO();
        updateRoleDTO.setId(2L);
        updateRoleDTO.setRoleId(1L);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        String url = userController.changeStatus(updateRoleDTO, bindingResult);
        Assert.assertEquals(REDIRECT_PRIVATE_USERS, url);
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void shouldDeleteUsers() {
        int[] ids = {3, 4};
        String url = userController.deleteUsers(ids);
        Assert.assertEquals(REDIRECT_PRIVATE_USERS, url);
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void shouldHaveAtModelPaginationInfo() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pages"));
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void shouldAddValidUser() {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setEmail(randomService.generatePassword() + "@test.com");
        addUserDTO.setMiddleName("test");
        addUserDTO.setName("test");
        addUserDTO.setRoleId(1L);
        addUserDTO.setSurName("test");
        Model model = new ExtendedModelMap();
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        String url = userController.addUser(addUserDTO, bindingResult, model);
        Assert.assertEquals(REDIRECT_PRIVATE_USERS, url);
    }

    @Test
    public void shouldChangePassword() {
        Long id = 2L;
        String url = userController.changePassword(id);
        Assert.assertEquals(REDIRECT_PRIVATE_USERS, url);
    }
}
