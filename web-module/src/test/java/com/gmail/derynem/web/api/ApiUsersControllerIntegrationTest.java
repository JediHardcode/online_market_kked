package com.gmail.derynem.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.derynem.service.RandomService;
import com.gmail.derynem.service.model.profile.ProfileDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ApiUsersControllerIntegrationTest {
    @Autowired
    private RandomService randomService;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    private AddUserDTO userDTO = new AddUserDTO();
    private ProfileDTO profileDTO = new ProfileDTO();
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        userDTO.setEmail(randomService.generatePassword() + "@testssss.com");
        userDTO.setMiddleName("test");
        userDTO.setName("tets");
        userDTO.setRoleId(2L);
        userDTO.setSurName("tets");
        profileDTO.setTelephone("43435323");
        profileDTO.setAddress("address");
        userDTO.setProfile(profileDTO);
    }

    @Test
    public void shouldSaveUserIfUserWithCurrEmailDoesntExist() throws Exception {
        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestIfUserNotValid() throws Exception {
        userDTO.setSurName("23234343");
        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfUserWithCurrEmailAlreadyExistInDatabase() throws Exception {
        userDTO.setEmail("root@root");
        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }
}