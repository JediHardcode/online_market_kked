package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Review;
import com.gmail.derynem.service.converter.ReviewConverter;
import com.gmail.derynem.service.model.review.ReviewDTO;
import com.gmail.derynem.service.model.user.UserCommonDTO;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverterImpl implements ReviewConverter {

    @Override
    public ReviewDTO toDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        UserCommonDTO userCommonDTO = new UserCommonDTO();
        userCommonDTO.setId(review.getUser().getId());
        userCommonDTO.setMiddleName(review.getUser().getMiddleName());
        userCommonDTO.setName(review.getUser().getName());
        userCommonDTO.setSurName(review.getUser().getSurName());
        reviewDTO.setUser(userCommonDTO);
        reviewDTO.setCreated(review.getCreated());
        reviewDTO.setHidden(review.isHidden());
        reviewDTO.setDescription(review.getDescription());
        reviewDTO.setId(review.getId());
        return reviewDTO;
    }
}