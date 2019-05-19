package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.ReviewRepository;
import com.gmail.derynem.repository.model.Review;
import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.ReviewService;
import com.gmail.derynem.service.converter.ReviewConverter;
import com.gmail.derynem.service.exception.ReviewServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.review.ReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.derynem.service.constants.PageConstant.OFFSET_LIMIT;

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
    @Transactional
    public PageDTO<ReviewDTO> getReviewsPageInfo(Integer page, Boolean isHidden) {
        PageDTO<ReviewDTO> reviewPageDTO = new PageDTO<>();
        int countOfReviews = reviewRepository.getCountOfReviews(isHidden);
        int countOfPages = pageService.getPages(countOfReviews, OFFSET_LIMIT);
        int offset = pageService.getOffset(page, countOfPages, OFFSET_LIMIT);
        reviewPageDTO.setCountOfPages(countOfPages);
        List<Review> reviewList = reviewRepository.getReviews(offset, OFFSET_LIMIT, isHidden);
        if (reviewList == null || reviewList.isEmpty()) {
            logger.info("no available reviews");
            reviewPageDTO.setEntities(Collections.emptyList());
            return reviewPageDTO;
        }
        List<ReviewDTO> reviews = reviewList.stream()
                .map(reviewConverter::toDTO)
                .collect(Collectors.toList());
        reviewPageDTO.setEntities(reviews);
        if (isHidden != null) {
            logger.info("List of reviews with isHidden status:{} received , list size is {}, count of pages of reviews is {}",
                    isHidden, reviewPageDTO.getEntities().size(), reviewPageDTO.getCountOfPages());
        } else {
            logger.info("List of reviews received , list size is {}, count of pages of reviews is {}",
                    reviewPageDTO.getEntities().size(), reviewPageDTO.getCountOfPages());
        }
        return reviewPageDTO;
    }

    @Override
    @Transactional
    public void deleteReview(Long id) throws ReviewServiceException {
        Review review = reviewRepository.getById(id);
        if (review != null) {
            reviewRepository.remove(review);
            logger.info("review id {} deleted from database", review.getId());
        } else {
            logger.info("review with id {} doesnt exist in database ", id);
            throw new ReviewServiceException(" review with id " + id + " doest exist in database");
        }
    }

    @Override
    @Transactional
    public void changeIsHiddenStatus(List<ReviewDTO> reviews) {
        List<Long> reviewsIdIsHidden = getReviewIdsIsHidden(reviews);
        List<Long> reviewsIdIsNotHidden = getReviewIdNotHidden(reviews);
        if (reviewsIdIsHidden == null || reviewsIdIsHidden.isEmpty()) {
            logger.info("No reviews is hidden");
        } else {
            int countOfIsHiddenReviews =
                    reviewRepository.changeIsHiddenStatus(true, reviewsIdIsHidden);
            logger.info(" hidden status  now have {} ids", countOfIsHiddenReviews);
        }
        if (reviewsIdIsNotHidden == null || reviewsIdIsNotHidden.isEmpty()) {
            logger.info("No reviews become visible ");
        } else {
            int countOfIsNotHiddenReviews =
                    reviewRepository.changeIsHiddenStatus(false, reviewsIdIsNotHidden);
            logger.info(" not hidden status now have {} ids", countOfIsNotHiddenReviews);
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