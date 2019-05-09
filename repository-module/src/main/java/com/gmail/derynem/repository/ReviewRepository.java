package com.gmail.derynem.repository;

import com.gmail.derynem.repository.model.Review;

import java.sql.Connection;
import java.util.List;

public interface ReviewRepository extends GenericRepository {
    List<Review> getReviewsWithOffset(Connection connection, int offset);

    List<Review> getNotHiddenReviewsWithOffset(Connection connection, int offset);

    int getCountOfAllReviews(Connection connection);

    int deleteReview(Connection connection, Long id);

    int changeHiddenStatus(Connection connection, boolean b, List<Long> ids);

    int getCountOfNotHiddenReviews(Connection connection);
}
