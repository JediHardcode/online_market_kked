package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.ReviewRepository;
import com.gmail.derynem.repository.model.Review;
import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.ReviewService;
import com.gmail.derynem.service.converter.ReviewConverter;
import com.gmail.derynem.service.exception.ReviewServiceException;
import com.gmail.derynem.service.model.review.ReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.derynem.repository.constants.DataBaseConstants.OFFSET_LIMIT;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final static Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final PageService pageService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewConverter reviewConverter, PageService pageService) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
        this.pageService = pageService;
    }

    @Override
    public int getCountOfPagesOfReviews(Boolean isHidden) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int countOfReviews = reviewRepository.getCountOfReviews(connection, isHidden);
                int countOfPages = pageService.getPages(countOfReviews);
                connection.commit();
                if (isHidden != null) {
                    logger.info("Count of available reviews is {} with isHidden status {}, count of pages with Offset {} is {}",
                            countOfReviews, isHidden, OFFSET_LIMIT, countOfPages);
                } else {
                    logger.info("Count of available reviews is {}, count of pages with Offset {} is {}",
                            countOfReviews, OFFSET_LIMIT, countOfPages);
                }
                return countOfPages;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException("error at method getCountOfPagesOfReviews at service module|" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ReviewServiceException("error at method getCountOfPagesOfReviews at service module|" + e.getMessage(), e);
        }
    }

    @Override
    public List<ReviewDTO> getListOfReviews(Integer page, Boolean isHidden) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int offset = pageService.getOffset(page);
                List<Review> reviewList = reviewRepository.getReviewsWithOffset(connection, offset, isHidden);
                if (reviewList == null || reviewList.isEmpty()) {
                    logger.info("no available reviews");
                    connection.commit();
                    return Collections.emptyList();
                }
                List<ReviewDTO> reviews = reviewList.stream()
                        .map(reviewConverter::toDTO)
                        .collect(Collectors.toList());
                if (isHidden != null) {
                    logger.info("List of reviews with isHidden status:{} received , list size is {}", isHidden, reviews.size());
                } else {
                    logger.info("List of reviews received , list size is {}", reviews.size());
                }
                connection.commit();
                return reviews;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException("error at method get list of all Reviews at service module|" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ReviewServiceException("error at method get list of all Reviews at service module|" + e.getMessage(), e);
        }
    }

    @Override
    public void deleteReview(Long id) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int row = reviewRepository.deleteReview(connection, id);
                if (row != 0) {
                    logger.info("review with id {} successfully deleted", id);
                } else {
                    logger.info(" not found review with this id {}", id);
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException("error at method deleteReview at service module" + e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewServiceException("error at method deleteReview at service module" + e.getMessage(), e);
        }
    }

    @Override
    public void changeIsHiddenStatus(List<ReviewDTO> reviews) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Long> reviewsIdIsHidden = getReviewIdsIsHidden(reviews);
                List<Long> reviewsIdIsNotHidden = getReviewIdNotHidden(reviews);
                if (reviewsIdIsHidden == null || reviewsIdIsHidden.isEmpty()) {
                    logger.info("No reviews is hidden");
                } else {
                    int countOfIsHiddenReviews =
                            reviewRepository.changeIsHiddenStatus(connection, true, reviewsIdIsHidden);
                    logger.info(" hidden status  now have {} ids", countOfIsHiddenReviews);
                }
                if (reviewsIdIsNotHidden == null || reviewsIdIsNotHidden.isEmpty()) {
                    logger.info("No reviews become visible ");
                } else {
                    int countOfIsNotHiddenReviews =
                            reviewRepository.changeIsHiddenStatus(connection, false, reviewsIdIsNotHidden);
                    logger.info(" not hidden status now have {} ids", countOfIsNotHiddenReviews);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException("error at method change hidden status at service module" + e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ReviewServiceException("error at method change hidden status at service module" + e.getMessage(), e);
        }
    }

    private List<Long> getReviewIdNotHidden(List<ReviewDTO> reviews) {
        return reviews.stream()
                .filter(reviewDTO -> !reviewDTO.isHidden())
                .map(ReviewDTO::getId)
                .collect(Collectors.toList());
    }

    private List<Long> getReviewIdsIsHidden(List<ReviewDTO> reviews) {
        return reviews.stream()
                .filter(ReviewDTO::isHidden)
                .map(ReviewDTO::getId)
                .collect(Collectors.toList());
    }
}