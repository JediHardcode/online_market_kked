package com.gmail.derynem.web.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.gmail.derynem.web.constants.PageNamesConstant.ERROR_PAGE_FORBIDDEN;
import static com.gmail.derynem.web.constants.PageNamesConstant.ERROR_PAGE_NOT_FOUND;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class DefaultControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private DefaultController defaultController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    public void shouldShowHomePageWithReviews() throws Exception {
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("reviews"))
                .andExpect(model().attributeExists("pages"));
    }

    @Test
    public void shouldReturnForbiddenErrorRage() throws Exception {
        this.mockMvc.perform(get("/403"))
                .andExpect(status().isOk());
        String url = defaultController.errorAccessDenied();
        Assert.assertEquals(ERROR_PAGE_FORBIDDEN, url);
    }

    @Test
    public void shouldReturnNotFoundErrorRage() throws Exception {
        this.mockMvc.perform(get("/404"))
                .andExpect(status().isOk());
        String url = defaultController.errorNotFound();
        Assert.assertEquals(ERROR_PAGE_NOT_FOUND, url);
    }

}
