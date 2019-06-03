package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Review;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.converter.user.UserConverterAssembler;
import com.gmail.derynem.service.model.review.ReviewDTO;
import com.gmail.derynem.service.model.user.UserCommonDTO;
import org.springframework.stereotype.Component;

@Component("reviewConverter")
public class ReviewConverterImpl implements Converter<ReviewDTO, Review> {
    private final UserConverterAssembler userConverterAssembler;

    public ReviewConverterImpl(UserConverterAssembler userConverterAssembler) {
        this.userConverterAssembler = userConverterAssembler;
    }

    @Override
    public ReviewDTO toDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        UserCommonDTO userCommonDTO = userConverterAssembler.getUserCommonConverter().toDTO(review.getUser());
        reviewDTO.setUser(userCommonDTO);
        reviewDTO.setCreated(review.getCreated());
        reviewDTO.setHidden(review.isHidden());
        reviewDTO.setDescription(review.getDescription());
        reviewDTO.setId(review.getId());
        return reviewDTO;
    }

    @Override
    public Review toEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setCreated(reviewDTO.getCreated());
        review.setDescription(reviewDTO.getDescription());
        return review;
    }
}