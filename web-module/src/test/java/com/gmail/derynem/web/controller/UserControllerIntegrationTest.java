package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.model.AddUserDTO;
import com.gmail.derynem.service.model.RoleDTO;
import com.gmail.derynem.service.model.UpdateRoleDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
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
        updateRoleDTO.setRole("ADMINISTRATOR");
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        String url = userController.changeStatus(updateRoleDTO, bindingResult);
        Assert.assertEquals("redirect:/private/users", url);
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void shouldDeleteUsers() {
        int[] ids = {3, 4};
        String url = userController.deleteUsers(ids);
        Assert.assertEquals("redirect:/private/users", url);
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
        addUserDTO.setEmail("test@testo.com");
        addUserDTO.setMiddleName("test");
        addUserDTO.setName("test");
        addUserDTO.setRole("CUSTOMER");
        addUserDTO.setSurName("test");
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        String url = userController.addUser(addUserDTO, bindingResult);
        Assert.assertEquals("redirect:/private/users", url);
    }
}
