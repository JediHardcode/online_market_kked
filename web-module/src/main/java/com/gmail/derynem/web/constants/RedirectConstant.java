package com.gmail.derynem.web.constants;

public class RedirectConstant {
    public static final String REDIRECT_PRIVATE_USERS = "redirect:/private/users";
    public static final String REDIRECT_PRIVATE_REVIEWS = "redirect:/private/reviews";
    public static final String REDIRECT_USER_PROFILE = "redirect:/public/user/profile";
    public static final String REDIRECT_ARTICLES_PAGE = "redirect:/public/articles";
    public static final String REDIRECT_ARTICLE_PAGE = "redirect:/public/article/%s";
    public static final String REDIRECT_NOT_FOUND = "redirect:/404";

    private RedirectConstant() {
    }
}