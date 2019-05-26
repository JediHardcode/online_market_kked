package com.gmail.derynem.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.derynem.service.model.item.ItemDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ApiItemControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    private static final String SECURE_EMAIL = "secure@secure";
    private ObjectMapper mapper = new ObjectMapper();
    private ItemDTO itemDTO = new ItemDTO();

    @Before
    public void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(SECURE_EMAIL)
    public void shouldShowListOfItems() throws Exception {
        mvc.perform(get("/api/v1.0/items"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(SECURE_EMAIL)
    public void shouldGetItem() throws Exception {
        mvc.perform(get("/api/v1.0/items/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(SECURE_EMAIL)
    public void shouldGet404IfItemDoesntExist() throws Exception {
        mvc.perform(get("/api/v1.0/items/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(SECURE_EMAIL)
    public void shouldAddItem() throws Exception {
        itemDTO.setName("valid name1");
        itemDTO.setDescription(" valid desc");
        itemDTO.setPrice("11.2");
        mvc.perform(post("/api/v1.0/items")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(itemDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(SECURE_EMAIL)
    public void shouldReturn400IfItemNotValid() throws Exception {
        itemDTO.setName("valid name1");
        itemDTO.setDescription(" valid desc");
        itemDTO.setPrice("11,lkjlj2");
        mvc.perform(post("/api/v1.0/items")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(itemDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(SECURE_EMAIL)
    public void shouldDeleteItem() throws Exception {
        mvc.perform(delete("/api/v1.0/items/2"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(SECURE_EMAIL)
    public void shouldReturn404IfOItemNotFound() throws Exception {
        mvc.perform(delete("/api/v1.0/items/9999"))
                .andExpect(status().isNotFound());
    }
}

