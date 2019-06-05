package com.gmail.derynem.service;

import com.gmail.derynem.repository.ReviewRepository;
import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.Review;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.impl.ReviewServiceImpl;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.review.ReviewDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private Converter<ReviewDTO, Review> reviewConverter;
    @Mock
    private PageService pageService;
    @Mock
    private UserRepository userRepository;
    private ReviewService reviewService;
    private int countOfPages = 4;
    private int count = 4;
    private int limit = 10;

    @Before
    public void setUp() {
        Mockito.when(pageService.getPages(count, limit)).thenReturn(countOfPages);
        reviewService = new ReviewServiceImpl(reviewRepository, reviewConverter, pageService, userRepository);
    }

    @Test
    public void shouldGetPagesOfNotHiddenReviews() {
        Mockito.when(reviewRepository.getCountOfReviews(false)).thenReturn(count);
        Mockito.when(pageService.getPages(count, limit)).thenReturn(countOfPages);
        PageDTO<ReviewDTO> reviewPageInfo = reviewService.getReviewsPageInfo(1, limit, false);
        Assert.assertNotNull(reviewPageInfo.getEntities());
    }

    @Test
    public void shouldGetPagesIfAllReviewsAreHidden() {
        count = 0;
        Mockito.when(reviewRepository.getCountOfReviews(false)).thenReturn(count);
        Mockito.when(pageService.getPages(count, limit)).thenReturn(countOfPages);
        PageDTO<ReviewDTO> reviewPageInfo = reviewService.getReviewsPageInfo(1, limit, false);
        Assert.assertNotNull(reviewPageInfo.getEntities());
    }
}