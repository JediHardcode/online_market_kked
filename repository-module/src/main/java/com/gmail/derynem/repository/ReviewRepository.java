package com.gmail.derynem.repository;

import com.gmail.derynem.repository.model.Review;

import java.util.List;

public interface ReviewRepository extends GenericRepository<Long, Review> {
    List<Review> getReviews(int offset, int limit, Boolean isHidden);

    int getCountOfReviews(Boolean isHidden);

    int changeIsHiddenStatus(boolean isHidden, List<Long> ids);
}