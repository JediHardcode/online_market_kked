package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.model.article.ArticleDTO;
import com.gmail.derynem.service.model.user.UserCommonDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ArticleControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    private final String USER_EMAIL = "customer@customer";
    private final String SALE_EMAIL = "sale@sale";
    private final String ROOT_EMAIL = "root@root";
    private ArticleDTO validArticleDTO;
    private UserCommonDTO userCommonDTO;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        validArticleDTO = new ArticleDTO();
        validArticleDTO.setName("new name");
        validArticleDTO.setContent(" ");
        validArticleDTO.setId(1L);
        userCommonDTO = new UserCommonDTO();
        userCommonDTO.setId(3L);
        validArticleDTO.setUser(userCommonDTO);
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    public void shouldShowArticlePageWithArticles() throws Exception {
        this.mockMvc.perform(get("/public/articles"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("page", "user"));
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

    @Test
    @WithUserDetails(value = SALE_EMAIL)
    public void shouldDeleteArticle() throws Exception {
        mockMvc.perform(post("/private/article/delete")
                .param("id", "1"))
                .andExpect(redirectedUrlTemplate("/public/articles"));
    }

    @Test
    @WithUserDetails(value = SALE_EMAIL)
    public void shouldReturn404IfArticleNotFound() throws Exception {
        mockMvc.perform(post("/private/article/delete")
                .param("id", "99"))
                .andExpect(redirectedUrl("/404"));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    public void shouldRedirectAt403ErrorPage() throws Exception {
        mockMvc.perform(post("/private/article/delete")
                .param("id", "1"))
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithUserDetails(value = SALE_EMAIL)
    public void shouldDeleteComment() throws Exception {
        mockMvc.perform(post("/public/article/comment/delete")
                .param("id", "1")
                .param("articleId", "1"))
                .andExpect(redirectedUrl("/public/article/1"));
    }

    @Test
    @WithUserDetails(value = SALE_EMAIL)
    public void shouldRedirectAt404PageIfCommentDoesntExist() throws Exception {
        mockMvc.perform(post("/public/article/comment/delete")
                .param("id", "777")
                .param("articleId", "1"))
                .andExpect(redirectedUrl("/404"));
    }

    @Test
    @WithUserDetails(value = ROOT_EMAIL)
    public void shouldRedirectAt403IfDoesntHavePermissionForSeeArticles() throws Exception {
        mockMvc.perform(post("/public/articles"))
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithUserDetails(value = ROOT_EMAIL)
    public void shouldRedirectAt403IfDoesntHavePermission() throws Exception {
        mockMvc.perform(post("/private/article/delete")
                .param("id", "1"))
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithUserDetails(value = SALE_EMAIL)
    public void shouldUpdateArticle() throws Exception {
        mockMvc.perform(post("/private/article/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("article", validArticleDTO))
                .andExpect(redirectedUrl("/public/article/" + validArticleDTO.getId()));
    }

    @Test
    @WithUserDetails(value = SALE_EMAIL)
    public void shouldReturnArticlePageIfArticleNotValid() throws Exception {
        validArticleDTO.setName("3221");
        validArticleDTO.setContent("");
        mockMvc.perform(post("/private/article/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("article", validArticleDTO))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("userId"));
    }
}