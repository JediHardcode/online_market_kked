package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.model.item.ItemDTO;
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
public class ItemControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    private final String USER_EMAIL = "customer@customer";
    private final String SALE_EMAIL = "sale@sale";

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldShowItemsPage() throws Exception {
        this.mockMvc.perform(get("/public/items"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("page"));
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldDeleteItem() throws Exception {
        this.mockMvc.perform(post("/private/items/delete")
                .param("id", "2"))
                .andExpect(redirectedUrl("/public/items"));
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    public void shouldRedirectAt403WhenCustomerTryDeleteItem() throws Exception {
        this.mockMvc.perform(post("/private/items/delete")
                .param("id", "2"))
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldRedirectAt404WhenDeleteNotExistItem() throws Exception {
        this.mockMvc.perform(post("/private/items/delete")
                .param("id", "99"))
                .andExpect(redirectedUrl("/404"));
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldShowItemPage() throws Exception {
        this.mockMvc.perform(get("/public/items/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("item"));
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldRedirectAt404WhenItemNotFound() throws Exception {
        this.mockMvc.perform(get("/public/items/8888"))
                .andExpect(redirectedUrl("/404"));
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldShowCopyItemPage() throws Exception {
        this.mockMvc.perform(get("/private/items/copy")
                .param("id", "2"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("item"));
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldAddCopyItem() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setPrice("12");
        itemDTO.setName("validName");
        itemDTO.setDescription("test");
        this.mockMvc.perform(post("/private/items/copy")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("item", itemDTO))
                .andExpect(redirectedUrl("/public/items"));
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldRedirectAt404WhenCopyItemNotFound() throws Exception {
        this.mockMvc.perform(get("/private/items/copy?id=999"))
                .andExpect(redirectedUrl("/404"));
    }
}