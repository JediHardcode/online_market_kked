package com.gmail.derynem.service.converter;

import com.gmail.derynem.repository.model.Review;
import com.gmail.derynem.service.model.review.ReviewDTO;

public interface ReviewConverter {
    ReviewDTO toDTO(Review review);
}