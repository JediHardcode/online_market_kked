package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.model.review.ReviewDTO;
import com.gmail.derynem.service.model.review.ReviewsDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_PRIVATE_REVIEWS;
import static java.util.Arrays.asList;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ReviewController reviewController;
    @Mock
    BindingResult bindingResult;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void shouldDeleteReview() {
        Long id = 4L;
        String url = reviewController.deleteReview(id);
        Assert.assertEquals(REDIRECT_PRIVATE_REVIEWS, url);
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void shouldGetReviewPageWithAllReviews() throws Exception {
        this.mockMvc.perform(get("/private/reviews"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("reviews"))
                .andExpect(model().attributeExists("pages"));
    }

    @Test
    @WithMockUser(authorities = "SALE")
    public void shouldRedirectAtErrorPageIfAuthorityWrong() throws Exception {
        this.mockMvc.perform(get("/private/reviews"))
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    public void shouldUpdateHiddenStatus() {
        List<ReviewDTO> reviewsDTOS = asList(new ReviewDTO(3L, true),
                new ReviewDTO(2L, true));
        ReviewsDTO reviewsDTO = new ReviewsDTO(reviewsDTOS);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        String url = reviewController.changeHiddenStatus(reviewsDTO, bindingResult);
        Assert.assertEquals(REDIRECT_PRIVATE_REVIEWS, url);
    }
}