package com.gmail.derynem.service;

import com.gmail.derynem.repository.ReviewRepository;
import com.gmail.derynem.service.converter.ReviewConverter;
import com.gmail.derynem.service.impl.ReviewServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;

@RunWith(SpringJUnit4ClassRunner.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewConverter reviewConverter;
    @Mock
    private PageService pageService;
    @Mock
    public Connection connection;
    private ReviewService reviewService;
    private int countOfPages = 4;
    private int count = 4;

    @Before
    public void setUp() {
        Mockito.when(pageService.getPages(count)).thenReturn(countOfPages);
        Mockito.when(reviewRepository.getConnection()).thenReturn(connection);
        reviewService = new ReviewServiceImpl(reviewRepository, reviewConverter, pageService);
    }

    @Test
    public void shouldGetPagesOfNotHiddenReviews() {
        Mockito.when(reviewRepository.getCountOfReviews(connection, false)).thenReturn(count);
        Mockito.when(pageService.getPages(count)).thenReturn(countOfPages);
        int resultCountOfPages = reviewService.getCountOfPagesOfReviews(false);
        Assert.assertEquals(countOfPages, resultCountOfPages);
    }

    @Test
    public void shouldGetPagesIfAllReviewsAreHidden() {
        count = 0;
        Mockito.when(reviewRepository.getCountOfReviews(connection, false)).thenReturn(count);
        Mockito.when(pageService.getPages(count)).thenReturn(countOfPages);
        int resultCountOfPages = reviewService.getCountOfPagesOfReviews(null);
        Assert.assertEquals(countOfPages, resultCountOfPages);
    }
}