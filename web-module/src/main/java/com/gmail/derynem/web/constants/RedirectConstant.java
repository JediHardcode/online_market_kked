package com.gmail.derynem.web.constants;

public class RedirectConstant {
    public static final String REDIRECT_PRIVATE_USERS = "redirect:/private/users";
    public static final String REDIRECT_PRIVATE_REVIEWS = "redirect:/private/reviews";
    public static final String REDIRECT_USER_PROFILE = "redirect:/public/user/profile";
    public static final String REDIRECT_ARTICLES_PAGE = "redirect:/public/articles";
    public static final String REDIRECT_ARTICLE_PAGE = "redirect:/public/article/%s";
    public static final String REDIRECT_NOT_FOUND = "redirect:/404";
    public static final String REDIRECT_CUSTOM_ERROR = "redirect:/500";
    public static final String REDIRECT_ITEMS_PAGE = "redirect:/public/items";
    public static final String REDIRECT_HOME = "redirect:/home";
    public static final String REDIRECT_ORDERS_PAGE = "redirect:/private/orders";
    public static final String REDIRECT_USER_ORDERS = "redirect:/user/orders";
    private RedirectConstant() {
    }
}