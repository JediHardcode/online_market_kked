package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.model.article.ArticleDTO;
import com.gmail.derynem.service.model.comment.CommentDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private final String customerEmail = "customer@customer";
    private final String saleEmail = "sale@sale";
    private final String rootEmail = "root@root";
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
    @WithUserDetails(value = customerEmail)
    public void shouldShowArticlePageWithArticles() throws Exception {
        this.mockMvc.perform(get("/public/articles"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("page", "user"));
    }

    @Test
    @WithUserDetails(value = customerEmail)
    public void shouldShowSingleArticlesPage() throws Exception {
        this.mockMvc.perform(get("/public/article/2"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithUserDetails(value = customerEmail)
    public void shouldRedirectAtArticlesPageIfArticleDoesntExist() throws Exception {
        this.mockMvc.perform(get("/public/article/999"))
                .andExpect(redirectedUrl("/public/articles"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldDeleteArticle() throws Exception {
        mockMvc.perform(post("/private/article/delete")
                .param("id", "1"))
                .andExpect(redirectedUrl("/public/articles?message=article deleted"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldReturn404IfArticleNotFound() throws Exception {
        mockMvc.perform(post("/private/article/delete")
                .param("id", "99"))
                .andExpect(redirectedUrl("/404"));
    }

    @Test
    @WithUserDetails(value = customerEmail)
    public void shouldRedirectAt403ErrorPage() throws Exception {
        mockMvc.perform(post("/private/article/delete")
                .param("id", "1"))
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldDeleteComment() throws Exception {
        mockMvc.perform(post("/public/article/comment/delete")
                .param("id", "1")
                .param("articleId", "1"))
                .andExpect(redirectedUrl("/public/article/1?message=comment deleted"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldRedirectAt404PageIfCommentDoesntExist() throws Exception {
        mockMvc.perform(post("/public/article/comment/delete")
                .param("id", "777")
                .param("articleId", "1"))
                .andExpect(redirectedUrl("/404"));
    }

    @Test
    @WithUserDetails(value = rootEmail)
    public void shouldRedirectAt403IfDoesntHavePermissionForSeeArticles() throws Exception {
        mockMvc.perform(post("/public/articles"))
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithUserDetails(value = rootEmail)
    public void shouldRedirectAt403IfDoesntHavePermission() throws Exception {
        mockMvc.perform(post("/private/article/delete")
                .param("id", "1"))
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldUpdateArticle() throws Exception {
        mockMvc.perform(post("/private/article/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("article", validArticleDTO))
                .andExpect(redirectedUrl("/public/article/" + validArticleDTO.getId() + "?message=article updated"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldReturnArticlePageIfArticleNotValid() throws Exception {
        validArticleDTO.setName("3221");
        validArticleDTO.setContent("");
        mockMvc.perform(post("/private/article/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("article", validArticleDTO))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldSaveArticle() throws Exception {
        validArticleDTO.setName("validName");
        validArticleDTO.setContent(" ");
        mockMvc.perform(post("/private/article/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("article", validArticleDTO))
                .andExpect(redirectedUrl("/public/articles?message=article added"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldShowAddArticlePage() throws Exception {
        mockMvc.perform(get("/private/article/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("article"));
    }

    @Test
    @WithUserDetails(value = customerEmail)
    public void shouldAddCommentAtArticle() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(" ");
        commentDTO.getUser().setId(2L);
        mockMvc.perform(post("/public/article/1/comment")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("comment", commentDTO));
    }
}