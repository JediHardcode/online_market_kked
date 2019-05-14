package com.gmail.derynem.repository;

import com.gmail.derynem.repository.model.Review;

import java.sql.Connection;
import java.util.List;

public interface ReviewRepository extends GenericRepository {
    List<Review> getReviewsWithOffset(Connection connection, int offset, Boolean isHidden);

    int getCountOfReviews(Connection connection, Boolean isHidden);

    int deleteById(Connection connection, Long id);

    int changeIsHiddenStatus(Connection connection, boolean isHidden, List<Long> ids);
}
