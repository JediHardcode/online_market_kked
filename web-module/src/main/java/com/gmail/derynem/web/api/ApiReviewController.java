package com.gmail.derynem.web.api;

import com.gmail.derynem.service.ReviewService;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.review.ReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_LIMIT;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_PAGE;

@RestController
@RequestMapping("/api/v1")
public class ApiReviewController {
    private final static Logger logger = LoggerFactory.getLogger(ApiReviewController.class);
    private final ReviewService reviewService;

    public ApiReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public ResponseEntity<PageDTO<ReviewDTO>> getReviews
            (@RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
             @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
             @RequestParam(value = "isHidden", required = false, defaultValue = "none") String isHidden) {
        PageDTO<ReviewDTO> reviewDTOPageDTO;
        switch (isHidden) {
            case "1":
                reviewDTOPageDTO = reviewService.getReviewsPageInfo(page, limit, true);
                logger.info("count of reviews :{} with isHidden status: true", reviewDTOPageDTO.getEntities().size());
                break;
            case "0": {
                reviewDTOPageDTO = reviewService.getReviewsPageInfo(page, limit, false);
                logger.info("count of reviews :{} with isHidden status: false", reviewDTOPageDTO.getEntities().size());
                break;
            }
            default: {
                reviewDTOPageDTO = reviewService.getReviewsPageInfo(page, limit, null);
                logger.info("count of reviews :{}", reviewDTOPageDTO.getEntities().size());
                break;
            }
        }
        return new ResponseEntity<>(reviewDTOPageDTO, HttpStatus.OK);
    }
}