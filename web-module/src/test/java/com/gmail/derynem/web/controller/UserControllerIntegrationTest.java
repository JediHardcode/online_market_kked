package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.RandomService;
import com.gmail.derynem.service.model.profile.ProfileDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import com.gmail.derynem.service.model.user.UserPrincipal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import static com.gmail.derynem.web.constants.PageNamesConstant.USER_PROFILE_PAGE;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_PRIVATE_USERS;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_USER_PROFILE;
import static com.gmail.derynem.web.constants.RoleNamesConstant.ADMINISTRATOR_ROLE;
import static com.gmail.derynem.web.constants.RoleNamesConstant.CUSTOMER_ROLE;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
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
    private BindingResult bindingResult;
    private UserDTO user = new UserDTO();
    @Mock
    private Authentication authentication;
    @Mock
    private UserPrincipal userPrincipal;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        user.setId(2L);
        user.setName("newname");
        user.setSurName("tets");
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setTelephone("434");
        profileDTO.setAddress("test");
        profileDTO.setId(2L);
        user.setProfile(profileDTO);
        user.setPassword("4343dsdsd");
        user.setEmail("root@root");
        Mockito.when(userPrincipal.getUser()).thenReturn(user);
        Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);
    }

    @Test
    @WithMockUser(authorities = {ADMINISTRATOR_ROLE})
    public void shouldGetUsersPageWithUsers() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("userRoleUpdate"));
    }

    @Test
    @WithMockUser(authorities = {ADMINISTRATOR_ROLE})
    public void shouldRedirectAtUsersPage() {
        Model model = new ExtendedModelMap();
        UpdateRoleDTO updateRoleDTO = new UpdateRoleDTO();
        String url = userController.showUsers(model, 1, 10, updateRoleDTO);
        Assert.assertEquals("user/users", url);
    }

    @Test
    @WithMockUser(authorities = {CUSTOMER_ROLE})
    public void shouldRedirectIfCustomerGoToUsersPage() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(authorities = {ADMINISTRATOR_ROLE})
    public void shouldUpdateRoleUser() {
        UpdateRoleDTO updateRoleDTO = new UpdateRoleDTO();
        updateRoleDTO.setId(2L);
        updateRoleDTO.setRoleId(1L);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        String url = userController.changeStatus(updateRoleDTO, bindingResult);
        Assert.assertEquals(REDIRECT_PRIVATE_USERS + "?message=role updated", url);
    }

    @Test
    @WithMockUser(authorities = {ADMINISTRATOR_ROLE})
    public void shouldDeleteUsers() {
        Long[] ids = {3L, 4L};
        String url = userController.deleteUsers(ids);
        Assert.assertEquals(REDIRECT_PRIVATE_USERS + "?message=Users deleted", url);
    }

    @Test
    @WithMockUser(authorities = {ADMINISTRATOR_ROLE})
    public void shouldHaveAtModelPaginationInfo() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pages"));
    }

    @Test
    @WithMockUser(authorities = {ADMINISTRATOR_ROLE})
    public void shouldAddValidUser() {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setEmail(randomService.generatePassword() + "@test.com");
        addUserDTO.setMiddleName("test");
        addUserDTO.setName("test");
        addUserDTO.setRoleId(1L);
        addUserDTO.setSurName("test");
        ProfileDTO profileDTO = new ProfileDTO();
        addUserDTO.setProfile(profileDTO);
        Model model = new ExtendedModelMap();
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        String url = userController.addUser(addUserDTO, bindingResult, model);
        Assert.assertEquals(REDIRECT_PRIVATE_USERS + "?message=user added", url);
    }

    @Test
    public void shouldChangePassword() {
        Long id = 2L;
        String url = userController.changePassword(id);
        Assert.assertEquals(REDIRECT_PRIVATE_USERS + "?message=password changed", url);
    }

    @Test
    public void shouldUpdateUserInformation() {
        String url = userController.updateProfile(user, bindingResult);
        Assert.assertEquals(REDIRECT_USER_PROFILE + "?message=profile updated", url);
    }

    @Test
    public void shouldReturnProfilePageIfUserNotValid() {
        user.setName("2233");
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        String url = userController.updateProfile(user, bindingResult);
        Assert.assertEquals(USER_PROFILE_PAGE, url);
    }
}