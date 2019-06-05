package com.gmail.derynem.web.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ApiReviewIntegrationController {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    private final String secureEmail = "secure@secure";

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(secureEmail)
    public void shouldReturnReviewWithHiddenStatusWithoutRequestParam() throws Exception {
        mockMvc.perform(get("/api/v1/reviews"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(secureEmail)
    public void shouldReturnReviewWithHiddenStatusWithRequestParam() throws Exception {
        mockMvc.perform(get("/api/v1/reviews")
                .param("isHidden", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(secureEmail)
    public void shouldReturnReviewWithHiddenStatusWithNonMatchedRequestParam() throws Exception {
        mockMvc.perform(get("/api/v1/reviews")
                .param("isHidden", "test"))
                .andExpect(status().isOk());
    }
}