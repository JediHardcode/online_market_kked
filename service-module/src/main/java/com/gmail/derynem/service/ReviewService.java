package com.gmail.derynem.service;

import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.review.ReviewDTO;

import java.util.List;

public interface ReviewService {
    PageDTO getNotHiddenReviewPages();

    PageDTO getAllReviewPages();

    List<ReviewDTO> getListOfAllReviews(Integer page);

    List<ReviewDTO> getListOfNotHiddenReviews(Integer page);

    void deleteReview(Long id);

    void changeHiddenStatus(List<ReviewDTO> reviews);
}
