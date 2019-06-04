package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.ReviewService;
import com.gmail.derynem.service.exception.ReviewServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.review.ReviewDTO;
import com.gmail.derynem.service.model.review.ReviewsDTO;
import com.gmail.derynem.service.model.user.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Arrays;

import static com.gmail.derynem.web.constants.PageNamesConstant.NEW_REVIEW_PAGE;
import static com.gmail.derynem.web.constants.PageNamesConstant.PRIVATE_HOME_PAGE;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_LIMIT;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_PAGE;
import static com.gmail.derynem.web.constants.PageParamConstant.MESSAGE_PARAM;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_ITEMS_PAGE;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_PRIVATE_REVIEWS;

@Controller
public class ReviewController {
    private final static Logger logger = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("private/reviews")
    public String managementReviews(@RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                    @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
                                    Model model) {
        PageDTO<ReviewDTO> reviewsPageInfo = reviewService.getReviewsPageInfo(page, limit, null);
        ReviewsDTO reviews = new ReviewsDTO(reviewsPageInfo.getEntities());
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
        try {
            reviewService.deleteReview(id);
            return REDIRECT_PRIVATE_REVIEWS + String.format(MESSAGE_PARAM, "review deleted");
        } catch (ReviewServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_PRIVATE_REVIEWS + String.format(MESSAGE_PARAM, "delete review fail");
        }
    }

    @PostMapping("/private/reviews")
    public String changeHiddenStatus(@ModelAttribute(value = "reviews")
                                     @Valid ReviewsDTO reviews,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return REDIRECT_PRIVATE_REVIEWS + String.format(MESSAGE_PARAM, "update hidden status fail");
        }
        reviewService.changeIsHiddenStatus(reviews.getReviews());
        return REDIRECT_PRIVATE_REVIEWS + String.format(MESSAGE_PARAM, "hidden status changed");
    }

    @GetMapping("/home/review")
    public String showAddReviewPage(Model model,
                                    ReviewDTO reviewDTO) {
        model.addAttribute("review", reviewDTO);
        return NEW_REVIEW_PAGE;
    }

    @PostMapping("/home/review")
    public String saveReview(@ModelAttribute(value = "review") @Valid ReviewDTO reviewDTO,
                             BindingResult bindingResult,
                             Authentication authentication) {
        if (bindingResult.hasErrors()) {
            logger.info("review not valid, errors {}", Arrays.toString(bindingResult.getAllErrors().toArray()));
            return NEW_REVIEW_PAGE;
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        reviewDTO.getUser().setId(userPrincipal.getUser().getId());
        reviewService.save(reviewDTO);
        return REDIRECT_ITEMS_PAGE + String.format(MESSAGE_PARAM, "your review added, Thank you");
    }
}