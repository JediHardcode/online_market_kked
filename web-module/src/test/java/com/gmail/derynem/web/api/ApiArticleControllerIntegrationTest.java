package com.gmail.derynem.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.derynem.service.model.article.ArticleDTO;
import com.gmail.derynem.service.model.comment.CommentDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import com.gmail.derynem.web.controller.api.ApiArticleController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiArticleControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ApiArticleController apiArticleController;
    private MockMvc mvc;
    private ArticleDTO articleDTO = new ArticleDTO();
    private UserDTO userDTO = new UserDTO();
    private CommentDTO commentDTO = new CommentDTO();
    private List<CommentDTO> comments = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    @Mock
    private BindingResult bindingResult;

    @Before
    public void setup() {
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        articleDTO.setName("test");
        articleDTO.setContent(" ");
        userDTO.setId(2L);
        articleDTO.setUser(userDTO);
        commentDTO.setUser(userDTO);
        commentDTO.setContent(" ");
        comments = Collections.singletonList(commentDTO);
        articleDTO.setComments(comments);
        apiArticleController.addArticle(articleDTO, bindingResult);
        apiArticleController.addArticle(articleDTO, bindingResult);
    }

    @Test
    public void shouldSaveArticleAndReturn201StatusCode() throws Exception {
        mvc.perform(post("/api/v1.0/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(articleDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturn400StatusCodeIfArticleNotValid() throws Exception {
        articleDTO.setName("434342323");
        mvc.perform(post("/api/v1.0/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(articleDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetArticleAnd200IfIdNotNullAndArticleWithThisIdExistInDatabase() throws Exception {
        mvc.perform(get("/api/v1.0/articles/2"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGet404IfIdNotNullAndArticleWithThisIdNotExistInDatabase() throws Exception {
        mvc.perform(get("/api/v1.0/articles/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldSaveArticleAndReturn2001IfCommentsAreEmpty() throws Exception {
        articleDTO.setComments(new ArrayList<>());
        mvc.perform(post("/api/v1.0/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(articleDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldGetListOfArticleWithOffsetAndLimitAndReturn200() throws Exception {
        mvc.perform(get("/api/v1.0/articles")
                .param("page", "1")
                .param("limit", "8"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetListOfArticleIfRequestParamAreEmptyAndLimitAndReturn200() throws Exception {
        mvc.perform(get("/api/v1.0/articles"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequestIfAuthorDoesntExistInDatabase() throws Exception {
        articleDTO.getUser().setId(9999L);
        mvc.perform(post("/api/v1.0/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(articleDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnWhenDeleteDoestExistArticleDoesntExist() throws Exception {
        mvc.perform(delete("/api/v1.0/articles/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldSaveArticleIfCommentsAreEmpty() throws Exception {
        articleDTO.setComments(new ArrayList<>());
        mvc.perform(post("/api/v1.0/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(articleDTO)))
                .andExpect(status().isCreated());
    }
}