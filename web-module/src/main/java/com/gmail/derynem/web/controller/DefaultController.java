package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.ReviewService;
import com.gmail.derynem.service.model.review.ReviewDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.gmail.derynem.web.constants.PageNamesConstant.*;

@Controller
public class DefaultController {
    private final ReviewService reviewService;
    private final PageService pageService;

    public DefaultController(ReviewService reviewService, PageService pageService) {
        this.reviewService = reviewService;
        this.pageService = pageService;
    }

    @GetMapping("/home")
    public String home(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                       Model model) {
        int countOfPages = reviewService.getCountOfPagesOfReviews(false);
        Integer validPage = pageService.getValidPage(page, countOfPages);
        List<ReviewDTO> reviews = reviewService.getListOfReviews(validPage, false);
        model.addAttribute("pages", countOfPages);
        model.addAttribute("reviews", reviews);
        return HOME_PAGE;
    }

    @GetMapping("/login")
    public String login() {
        return LOGIN_PAGE;
    }

    @GetMapping("/403")
    public String errorAccessDenied() {
        return ERROR_PAGE_FORBIDDEN;
    }

    @GetMapping("/404")
    public String errorNotFound() {
        return ERROR_PAGE_NOT_FOUND;
    }
}
