package com.gmail.derynem.service;

import com.gmail.derynem.service.exception.ReviewServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.review.ReviewDTO;

import java.util.List;

public interface ReviewService {
    PageDTO<ReviewDTO> getReviewsPageInfo(Integer page, Boolean isHidden);

    void deleteReview(Long id) throws ReviewServiceException;

    void changeIsHiddenStatus(List<ReviewDTO> reviews);

    void save(ReviewDTO reviewDTO);
}