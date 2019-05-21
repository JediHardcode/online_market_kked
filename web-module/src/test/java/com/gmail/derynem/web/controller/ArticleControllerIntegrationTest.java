package com.gmail.derynem.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    private final String USER_EMAIL = "customer@customer";

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    public void shouldShowArticlePageWithArticles() throws Exception {
        this.mockMvc.perform(get("/public/articles"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("page"));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    public void shouldShowSingleArticlesPage() throws Exception {
        this.mockMvc.perform(get("/public/article/2"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("userId"));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    public void shouldRedirectAtArticlesPageIfArticleDoesntExist() throws Exception {
        this.mockMvc.perform(get("/public/article/999"))
                .andExpect(redirectedUrl("/public/articles"));
    }
}