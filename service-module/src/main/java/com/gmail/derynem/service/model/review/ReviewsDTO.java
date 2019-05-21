package com.gmail.derynem.service.model.review;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ReviewsDTO {
    @NotNull
    private List<ReviewDTO> reviews;

    public ReviewsDTO(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public ReviewsDTO() {
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "ReviewsDTO{" +
                "reviews=" + reviews +
                '}';
    }
}