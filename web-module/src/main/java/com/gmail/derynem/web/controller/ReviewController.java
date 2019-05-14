package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.ReviewService;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.review.ReviewDTO;
import com.gmail.derynem.service.model.review.ReviewsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

import static com.gmail.derynem.web.constants.PageNamesConstant.PRIVATE_HOME_PAGE;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_PRIVATE_REVIEWS;

@Controller
public class ReviewController {
    private final static Logger logger = LoggerFactory.getLogger(ReviewController.class);
    private final PageService pageService;
    private final ReviewService reviewService;

    public ReviewController(PageService pageService, ReviewService reviewService) {
        this.pageService = pageService;
        this.reviewService = reviewService;
    }

    @GetMapping("private/reviews")
    public String managementReviews(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                    Model model) {
        PageDTO<ReviewDTO> reviewsPageInfo = reviewService.getReviewsPageInfo(page, null);
        ReviewsDTO reviews = new ReviewsDTO(reviewsPageInfo.getObjects());
        model.addAttribute("reviews", reviews);
        model.addAttribute("pages", reviewsPageInfo.getCountOfPages());
        return PRIVATE_HOME_PAGE;
    }

    @PostMapping("/private/review")
    public String deleteReview(@RequestParam(value = "id") Long id) {
        if (id == null) {
            logger.info("redirect at reviews cause review id is null");
            return REDIRECT_PRIVATE_REVIEWS;
        }
        reviewService.deleteReview(id);
        return REDIRECT_PRIVATE_REVIEWS;
    }

    @PostMapping("/private/reviews")
    public String changeHiddenStatus(@ModelAttribute(value = "reviews")
                                     @Valid ReviewsDTO reviews,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return REDIRECT_PRIVATE_REVIEWS;
        }
        reviewService.changeIsHiddenStatus(reviews.getReviews());
        return REDIRECT_PRIVATE_REVIEWS;
    }
}
