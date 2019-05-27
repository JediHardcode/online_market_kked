package com.gmail.derynem.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.derynem.service.model.article.ArticleDTO;
import com.gmail.derynem.service.model.comment.CommentDTO;
import com.gmail.derynem.service.model.user.UserCommonDTO;
import com.gmail.derynem.web.controller.api.ApiArticleController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ApiArticleControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    private ArticleDTO articleDTO = new ArticleDTO();
    private UserCommonDTO userDTO = new UserCommonDTO();
    private CommentDTO commentDTO = new CommentDTO();
    private List<CommentDTO> comments = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String SECURE_EMAIL = "secure@secure";
    @Mock
    private BindingResult bindingResult;

    @Before
    public void setup() {
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        articleDTO.setName("test");
        articleDTO.setContent(" ");
        userDTO.setId(2L);
        articleDTO.setUser(userDTO);
        commentDTO.setUser(userDTO);
        commentDTO.setContent(" ");
        comments = Collections.singletonList(commentDTO);
        articleDTO.setComments(comments);
    }

    @Test
    @WithUserDetails(value = SECURE_EMAIL)
    public void shouldSaveArticleAndReturn201StatusCode() throws Exception {
        mvc.perform(post("/api/v1/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(articleDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = SECURE_EMAIL)
    public void shouldReturn400StatusCodeIfArticleNotValid() throws Exception {
        articleDTO.setName("434342323");
        mvc.perform(post("/api/v1/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(articleDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = SECURE_EMAIL)
    public void shouldGetArticleAnd200IfIdNotNullAndArticleWithThisIdExistInDatabase() throws Exception {
        mvc.perform(get("/api/v1/articles/2"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = SECURE_EMAIL)
    public void shouldGet404IfIdNotNullAndArticleWithThisIdNotExistInDatabase() throws Exception {
        mvc.perform(get("/api/v1/articles/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = SECURE_EMAIL)
    public void shouldSaveArticleAndReturn2001IfCommentsAreEmpty() throws Exception {
        articleDTO.setComments(new ArrayList<>());
        mvc.perform(post("/api/v1/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(articleDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = SECURE_EMAIL)
    public void shouldGetListOfArticleWithOffsetAndLimitAndReturn200() throws Exception {
        mvc.perform(get("/api/v1/articles")
                .param("page", "1")
                .param("limit", "8"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = SECURE_EMAIL)
    public void shouldGetListOfArticleIfRequestParamAreEmptyAndLimitAndReturn200() throws Exception {
        mvc.perform(get("/api/v1/articles"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = SECURE_EMAIL)
    public void shouldReturnWhenDeleteDoestExistArticleDoesntExist() throws Exception {
        mvc.perform(delete("/api/v1/articles/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = SECURE_EMAIL)
    public void shouldSaveArticleIfCommentsAreEmpty() throws Exception {
        articleDTO.setComments(new ArrayList<>());
        mvc.perform(post("/api/v1/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(articleDTO)))
                .andExpect(status().isCreated());
    }
}