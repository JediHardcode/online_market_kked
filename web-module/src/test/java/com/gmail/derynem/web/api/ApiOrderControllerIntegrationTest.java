package com.gmail.derynem.web.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ApiOrderControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    private final String secureEmail = "secure@secure";

    @Before
    public void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(secureEmail)
    public void shouldGetOrders() throws Exception {
        mvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(secureEmail)
    public void shouldGetOrder() throws Exception {
        mvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(secureEmail)
    public void shouldReturn404IfOrderDoesntExist() throws Exception {
        mvc.perform(get("/api/v1/orders/787876"))
                .andExpect(status().isNotFound());
    }
}